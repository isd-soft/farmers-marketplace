package com.example.isdfarmersmarket.business.security;

import com.example.isdfarmersmarket.dao.enums.ERole;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtPrincipal {
    Long id;
    String email;
    List<ERole> roles;
}
