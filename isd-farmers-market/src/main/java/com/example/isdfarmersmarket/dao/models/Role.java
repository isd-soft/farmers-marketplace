package com.example.isdfarmersmarket.dao.models;

import com.example.isdfarmersmarket.dao.enums.ERole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Objects;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private ERole role;
    public Role(ERole role) {
        this.role = role;
    }
    @Override
    public String getAuthority() {
        return role.name();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role secondRole)) return false;
        return Objects.equals(getId(), secondRole.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

}