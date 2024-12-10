package com.example.isdfarmersmarket.business.services.interfaces;

import com.example.isdfarmersmarket.dao.enums.SearchUserByRoleParams;
import com.example.isdfarmersmarket.web.commands.CustomerUpgradeCommand;
import com.example.isdfarmersmarket.web.commands.UpdateUserCommand;
import com.example.isdfarmersmarket.web.dto.PageResponseDTO;
import com.example.isdfarmersmarket.web.dto.UpdateUserDTO;
import com.example.isdfarmersmarket.web.dto.UserProfileDTO;
import org.springframework.data.domain.Pageable;

public interface UserService {

    PageResponseDTO<UserProfileDTO> searchUsers(String fullName, SearchUserByRoleParams roleParams, Pageable pageable);

    UserProfileDTO getUserProfile(Long id);

    UpdateUserDTO updateUser(UpdateUserCommand updateUserCommand);

    UserProfileDTO upgradeToFarmer(CustomerUpgradeCommand command);

}
