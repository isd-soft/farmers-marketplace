package com.example.isdfarmersmarket.business.services;

import com.example.isdfarmersmarket.business.mapper.UserProfileMapper;
import com.example.isdfarmersmarket.dao.models.User;
import com.example.isdfarmersmarket.dao.repositories.UserRepository;
import com.example.isdfarmersmarket.web.commands.UpdateUserCommand;
import com.example.isdfarmersmarket.web.dto.UpdateUserDTO;
import com.example.isdfarmersmarket.web.dto.UserProfileDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserProfileMapper userProfileMapper;


    public void save(User user) {
        userRepository.save(user);
    }

    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public List<UserProfileDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .stream()
                .map(userProfileMapper::map)
                .toList();
    }

    public List<UserProfileDTO> searchUsersByFullName(String fullName, Pageable pageable) {
        return userRepository.findByFullNameContaining(fullName, pageable)
                .stream()
                .map(userProfileMapper::map)
                .toList();
    }

    public UserProfileDTO getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        return userProfileMapper.map(user);
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


}