package com.example.isdfarmersmarket.dao.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "refresh_tokens")
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernate_sequence")
    private Long id;
    private String token;
    @OneToOne
    private User user;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RefreshToken refreshToken)) return false;
        return Objects.equals(getId(), refreshToken.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

}
