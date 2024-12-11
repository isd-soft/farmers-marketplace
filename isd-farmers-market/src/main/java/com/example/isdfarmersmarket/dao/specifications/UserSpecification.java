package com.example.isdfarmersmarket.dao.specifications;

import com.example.isdfarmersmarket.dao.enums.ERole;
import com.example.isdfarmersmarket.dao.enums.SearchUserByRoleParams;
import com.example.isdfarmersmarket.dao.models.User;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;


@Slf4j
@NoArgsConstructor
public class UserSpecification {
    public static Specification<User> hasFullName(String fullName) {
        return (root, query, builder) -> {
            String lowerFullName = fullName.toLowerCase() + "%";

            return builder.or(
                    builder.like(builder.lower(root.get("firstName")), lowerFullName),
                    builder.like(builder.lower(root.get("lastName")), lowerFullName)
            );
        };
    }

    public static Specification<User> hasRole(SearchUserByRoleParams roleFilter) {
        return (root, query, builder) -> {
            var roles = root.get("roles");
            var rolePath = roles.get("role");

            return switch (roleFilter) {
                case FARMERS -> builder.equal(rolePath, ERole.FARMER);
                case CUSTOMERS -> {
                    // Making a subquery
                    var subquery = query.subquery(Long.class);
                    var subRoot = subquery.from(User.class);
                    var subRoles = subRoot.get("roles");
                    var subRolePath = subRoles.get("role");
                    subquery.select(builder.count(subRolePath))
                            .where(builder.equal(subRoot.get("id"), root.get("id")),
                                    builder.equal(subRolePath, ERole.FARMER));
                    // Returning resulting customers
                    yield builder.and(builder.equal(rolePath, ERole.CUSTOMER), builder.equal(subquery, 0L));
                }
                case ADMINS -> builder.equal(rolePath, ERole.ADMIN);
                case ALL -> null;
            };
        };
    }

    public static Specification<User> filterUsers(String fullName, SearchUserByRoleParams searchParams) {
        Specification<User> spec = Specification.where(null);

        if (fullName != null && !fullName.isBlank()) {
            spec = spec.and(hasFullName(fullName));
        }
        if (searchParams != null) {
            spec = spec.and(hasRole(searchParams));
        }

        return spec;
    }
}
