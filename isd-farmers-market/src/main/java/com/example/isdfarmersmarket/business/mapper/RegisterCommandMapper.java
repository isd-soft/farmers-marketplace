package com.example.isdfarmersmarket.business.mapper;

import com.example.isdfarmersmarket.business.exception.custom_exceptions.RoleDoesntExistException;
import com.example.isdfarmersmarket.dao.enums.ERole;
import com.example.isdfarmersmarket.dao.models.Role;
import com.example.isdfarmersmarket.dao.models.User;
import com.example.isdfarmersmarket.dao.repositories.RoleRepository;
import com.example.isdfarmersmarket.web.commands.UserRegisterCommand;
import lombok.RequiredArgsConstructor;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
@Mapper(componentModel = "spring")
public abstract class RegisterCommandMapper {
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Mapping(target = "email", source = "userRegisterCommand.email")
    @Mapping(target = "firstName", source = "userRegisterCommand.firstName")
    @Mapping(target = "lastName", source = "userRegisterCommand.lastName")
    @Mapping(target = "phoneNumber", source = "userRegisterCommand.phoneNumber")
    @Mapping(target = "address", source = "userRegisterCommand.address")
    @Mapping(target = "description", source = "userRegisterCommand.description")
    @Mapping(target = "password", expression = "java(passwordEncoder.encode(userRegisterCommand.password()))") // Custom password encoding
    public abstract User map(UserRegisterCommand userRegisterCommand);

    @AfterMapping
    public void assignRoles(@MappingTarget User user, UserRegisterCommand userRegisterCommand) {
        Role customerRole = roleRepository.findByRole(ERole.CUSTOMER)
                .orElseThrow(() -> new RoleDoesntExistException("ROLE CUSTOMER DOESN'T EXIST"));
        user.addRole(customerRole);

        switch (userRegisterCommand.roleType()) {
            case FARMER:
                Role farmerRole = roleRepository.findByRole(ERole.FARMER)
                        .orElseThrow(() -> new RoleDoesntExistException("ROLE FARMER DOESN'T EXIST"));
                user.addRole(farmerRole);
                break;
            case ADMIN:
                Role adminRole = roleRepository.findByRole(ERole.ADMIN)
                        .orElseThrow(() -> new RoleDoesntExistException("ROLE ADMIN DOESN'T EXIST"));
                user.addRole(adminRole);
                break;
            default:
                break;
        }
    }
}
