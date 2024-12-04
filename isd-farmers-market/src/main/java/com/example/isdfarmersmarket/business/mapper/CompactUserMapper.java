package com.example.isdfarmersmarket.business.mapper;

import com.example.isdfarmersmarket.business.security.JwtPrincipal;
import com.example.isdfarmersmarket.business.utils.SecurityUtils;
import com.example.isdfarmersmarket.dao.enums.ERole;
import com.example.isdfarmersmarket.dao.models.User;
import com.example.isdfarmersmarket.web.dto.CompactUserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CompactUserMapper {
    @Mapping(target = "isFarmer", expression = "java(isFarmer(user))")
    CompactUserDTO map(User user);

    List<CompactUserDTO> map(List<User> users);

    default boolean isFarmer(User user) {
        return user.getRoles().stream()
                .anyMatch(role -> role.getAuthority().equals(ERole.FARMER.name()));
    }


}
