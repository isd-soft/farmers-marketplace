package com.example.isdfarmersmarket.business.mapper;

import com.example.isdfarmersmarket.business.security.JwtPrincipal;
import com.example.isdfarmersmarket.business.utils.SecurityUtils;
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
    @Mapping(target = "canMessage", expression = "java(canMessage(user))")
    @Mapping(target = "canReview", expression = "java(canReview(user))")
    @Mapping(target = "isCurrentUser", expression = "java(isCurrentUser(user))")
    UserProfileDTO map(User user);

    List<UserProfileDTO> map(List<User> users);

    UpdateUserDTO mapToUpdateDTO(User user);

    default boolean isFarmer(User user) {
        return user.getRoles().stream()
                .anyMatch(role -> role.getAuthority().equals(ERole.FARMER.name()));
    }

    default boolean canMessage(User user) {
        JwtPrincipal currentUser = SecurityUtils.getPrincipal();
        return currentUser.getRoles().contains(ERole.CUSTOMER)
                && user.getRoles().stream()
                .anyMatch(role -> role.getAuthority().equals(ERole.FARMER.name()));
    }

    default boolean canReview(User user) {
        JwtPrincipal currentUser = SecurityUtils.getPrincipal();
        return currentUser.getRoles().contains(ERole.CUSTOMER)
                && user.getRoles().stream()
                .anyMatch(role -> role.getAuthority().equals(ERole.FARMER.name()))
                && !currentUser.getId().equals(user.getId());
    }
    default boolean isCurrentUser(User user) {
        JwtPrincipal currentUser = SecurityUtils.getPrincipal();
        return currentUser.getId().equals(user.getId());
    }

}