package com.example.isdfarmersmarket.dao.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Table(name="items_in_cart")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemInCart {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernate_sequence")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    private float quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemInCart itemInCart)) return false;
        return Objects.equals(getId(), itemInCart.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }


}
