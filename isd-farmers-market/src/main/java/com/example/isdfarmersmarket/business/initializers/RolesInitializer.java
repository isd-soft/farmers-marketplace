package com.example.isdfarmersmarket.business.initializers;

import com.example.isdfarmersmarket.dao.enums.ERole;
import com.example.isdfarmersmarket.dao.models.Role;
import com.example.isdfarmersmarket.dao.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
@RequiredArgsConstructor
public class RolesInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;
    @Override
    public void run(String... args) throws Exception {
        if(roleRepository.count() == 0) {
            roleRepository.save(new Role(ERole.CUSTOMER));
            roleRepository.save(new Role(ERole.FARMER));
            roleRepository.save(new Role(ERole.ADMIN));

        }
    }
}
