package com.example.isdfarmersmarket.business.utils;

import com.example.isdfarmersmarket.business.security.JwtPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    public static JwtPrincipal getPrincipal() {
        return (JwtPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
