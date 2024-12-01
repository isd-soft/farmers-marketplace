package com.example.isdfarmersmarket.business.mapper;

import com.example.isdfarmersmarket.dao.enums.ERole;
import com.example.isdfarmersmarket.dao.models.User;
import com.example.isdfarmersmarket.web.dto.UpdateUserDTO;
import com.example.isdfarmersmarket.web.dto.UserProfileDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "spring")
public interface UserProfileMapper {

    @Mapping(target = "isFarmer", expression = "java(isFarmer(user))")
    UserProfileDTO map(User user);
    List<UserProfileDTO> map(List<User> user);

    UpdateUserDTO mapToUpdateDTO(User user);

    default boolean isFarmer(User user) {
        return user.getRoles().stream()
                .anyMatch(role -> role.getAuthority().equals(ERole.FARMER.name()));
    }
}
