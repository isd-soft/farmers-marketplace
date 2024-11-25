package com.example.isdfarmersmarket.business.mapper;

import com.example.isdfarmersmarket.dao.enums.ERole;
import com.example.isdfarmersmarket.dao.models.User;
import com.example.isdfarmersmarket.web.dto.UserProfileDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface UserProfileMapper {

    @Mapping(target = "isFarmer", expression = "java(isFarmer(user))")
    UserProfileDTO map(User user);

    default boolean isFarmer(User user) {
        return user.getRoles().stream()
                .anyMatch(role -> role.getAuthority().equals(ERole.FARMER.name()));
    }
}
