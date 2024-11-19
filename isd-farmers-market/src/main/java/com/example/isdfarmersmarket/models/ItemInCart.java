package com.example.isdfarmersmarket.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.Optional;

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
        if (!(o instanceof User)) return false;
        ItemInCart product = (ItemInCart) o;
        return Objects.equals(getId(), product.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }


}
