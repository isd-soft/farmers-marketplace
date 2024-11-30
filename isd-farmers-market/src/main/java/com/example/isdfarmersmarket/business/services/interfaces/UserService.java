package com.example.isdfarmersmarket.business.services.interfaces;

import com.example.isdfarmersmarket.web.commands.CustomerUpgradeCommand;
import com.example.isdfarmersmarket.web.commands.UpdateUserCommand;
import com.example.isdfarmersmarket.web.dto.UpdateUserDTO;
import com.example.isdfarmersmarket.web.dto.UserProfileDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    List<UserProfileDTO> getAllUsers(Pageable pageable);

    List<UserProfileDTO> searchUsersByFullName(String fullName, Pageable pageable);

    UserProfileDTO getUserById(Long id);

    UpdateUserDTO updateUser(UpdateUserCommand updateUserCommand);

    UserProfileDTO upgradeToFarmer(CustomerUpgradeCommand command);

}
