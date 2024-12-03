package com.example.isdfarmersmarket.business.services;

import com.example.isdfarmersmarket.business.mapper.UserProfileMapper;
import com.example.isdfarmersmarket.business.security.JwtPrincipal;
import com.example.isdfarmersmarket.dao.models.User;
import com.example.isdfarmersmarket.dao.repositories.UserRepository;
import com.example.isdfarmersmarket.web.dto.UserProfileDTO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.example.isdfarmersmarket.business.security.JwtPrincipal;
import org.webjars.NotFoundException;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CurrentUserServiceImpl implements CurrentUserService{
    UserRepository userRepository;
    UserProfileMapper userProfileMapper;

    @Override
    public UserProfileDTO getCurrentUserInfo() {
        JwtPrincipal principal = getPrincipal();
        User user = userRepository.findById(principal.getId()).orElseThrow(() -> new NotFoundException("User not found"));
        return userProfileMapper.map(user);
    }
    private JwtPrincipal getPrincipal() {
        return (JwtPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
