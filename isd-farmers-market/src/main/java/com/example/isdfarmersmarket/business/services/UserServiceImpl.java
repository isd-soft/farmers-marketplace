package com.example.isdfarmersmarket.business.services;

import com.example.isdfarmersmarket.business.exception.custom_exceptions.CustomUsernameNotFoundException;
import com.example.isdfarmersmarket.business.exception.custom_exceptions.RoleAlreadyExistsException;
import com.example.isdfarmersmarket.business.exception.custom_exceptions.RoleDoesntExistException;
import com.example.isdfarmersmarket.business.mapper.UserProfileMapper;
import com.example.isdfarmersmarket.business.security.JwtPrincipal;
import com.example.isdfarmersmarket.business.services.interfaces.CreateReviewsService;
import com.example.isdfarmersmarket.business.services.interfaces.UserService;
import com.example.isdfarmersmarket.business.utils.SecurityUtils;
import com.example.isdfarmersmarket.dao.enums.ERole;
import com.example.isdfarmersmarket.dao.enums.SearchUserByRoleParams;
import com.example.isdfarmersmarket.dao.models.Role;
import com.example.isdfarmersmarket.dao.models.User;
import com.example.isdfarmersmarket.dao.repositories.FarmerReviewRepository;
import com.example.isdfarmersmarket.dao.repositories.OrderRepository;
import com.example.isdfarmersmarket.dao.repositories.RoleRepository;
import com.example.isdfarmersmarket.dao.repositories.UserRepository;
import com.example.isdfarmersmarket.dao.specifications.UserSpecification;
import com.example.isdfarmersmarket.web.commands.CustomerUpgradeCommand;
import com.example.isdfarmersmarket.web.commands.UpdateUserCommand;
import com.example.isdfarmersmarket.web.dto.PageResponseDTO;
import com.example.isdfarmersmarket.web.dto.UpdateUserDTO;
import com.example.isdfarmersmarket.web.dto.UserProfileDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserDetailsService, UserService {

    UserRepository userRepository;
    UserProfileMapper userProfileMapper;
    RoleRepository roleRepository;
    OrderRepository orderRepository;
    FarmerReviewRepository farmerReviewRepository;
    CreateReviewsService createReviewsService;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Transactional(readOnly = true)
    public PageResponseDTO<UserProfileDTO> searchUsers(String fullName, SearchUserByRoleParams roleParams, Pageable pageable) {
        Specification<User> filters = UserSpecification.filterUsers(fullName, roleParams);
        Page<User> usersPage = userRepository.findAll(filters, pageable);
        var content = userProfileMapper.map(usersPage.getContent());
        return new PageResponseDTO<>(content, usersPage.getTotalElements());
    }
    public UserProfileDTO getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        return userProfileMapper.map(user);
    }
    @Transactional(readOnly = true)
    public UserProfileDTO getUserProfile(Long id) {
        JwtPrincipal principal = SecurityUtils.getPrincipal();
        User profileUser = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        UserProfileDTO userProfileDTO = userProfileMapper.map(profileUser);
        if (principal != null) {
            User authenticatedUser = userRepository.findById(principal.getId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));
            userProfileDTO.setCanReview(
                    !principal.getRoles().contains(ERole.FARMER) &&
                            createReviewsService.canReviewFarmer(authenticatedUser, profileUser));
            log.error(userProfileDTO.getCanReview().toString());
        }

        return userProfileDTO;
    }


    @Transactional
    public UpdateUserDTO updateUser(UpdateUserCommand updateUserCommand) {
        User user = userRepository.findById(updateUserCommand.getId())
                .orElseThrow(() -> new EntityNotFoundException("User with the specified ID not found")
                );
        user.setFirstName(updateUserCommand.getFirstName());
        user.setLastName(updateUserCommand.getLastName());
        user.setEmail(updateUserCommand.getEmail());
        user.setPhoneNumber(updateUserCommand.getPhoneNumber());
        user.setDescription(updateUserCommand.getDescription());
        user.setAddress(updateUserCommand.getAddress());
        userRepository.save(user);
        return userProfileMapper.mapToUpdateDTO(user);
    }

    @Transactional
    @Override
    public UserProfileDTO upgradeToFarmer(CustomerUpgradeCommand command) {
        JwtPrincipal principal = SecurityUtils.getPrincipal();
        User customer = userRepository.findById(principal.getId())
                .orElseThrow(CustomUsernameNotFoundException::new);

        Role farmerRole = roleRepository.findByRole(ERole.FARMER)
                .orElseThrow(RoleDoesntExistException::new);

        if (customer.getRoles().contains(farmerRole)) {
            throw new RoleAlreadyExistsException();
        }

        customer.setAddress(command.getAddress());
        customer.setDescription(command.getDescription());
        customer.addRole(farmerRole);

        userRepository.save(customer);
        return userProfileMapper.map(customer);
    }


}