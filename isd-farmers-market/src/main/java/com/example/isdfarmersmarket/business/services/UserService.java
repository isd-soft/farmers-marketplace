package com.example.isdfarmersmarket.business.services;

import com.example.isdfarmersmarket.business.mapper.UserProfileMapper;
import com.example.isdfarmersmarket.dao.models.User;
import com.example.isdfarmersmarket.dao.repositories.UserRepository;
import com.example.isdfarmersmarket.web.dto.UserProfileDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
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

    public List<UserProfileDTO> getAllUsers(Integer page, Integer pageSize) {
        return userRepository.findAll(PageRequest.of(page, pageSize))
                .stream()
                .map(userProfileMapper::map)
                .toList();
    }

    public List<UserProfileDTO> searchUsersByFullName(String fullName, Integer page, Integer pageSize) {
        return userRepository.findByFullNameContaining(fullName, PageRequest.of(page, pageSize))
                .stream()
                .map(userProfileMapper::map)
                .toList();
    }

    public UserProfileDTO getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        return userProfileMapper.map(user);
    }


}