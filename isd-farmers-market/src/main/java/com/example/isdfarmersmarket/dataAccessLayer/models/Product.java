package com.example.isdfarmersmarket.dataAccessLayer.models;

import com.example.isdfarmersmarket.dataAccessLayer.enums.UnitType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernate_sequence")
    private Long id;
    private String title;
    private String description;
    @Column(name="unit_type")
    private UnitType unitType;
    @Column(name="price_per_unit")
    private BigDecimal pricePerUnit;
    @Column(name="discount_percents")
    private int discountPercents;
    private int quantity;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(mappedBy = "wishlist")
    private Set<User> inWishlists = new HashSet<>();

    private float rating;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<ProductReview> reviews = new HashSet<>();
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Image> images = new HashSet<>();

    @Column(name="created_date", columnDefinition = "TimeStamp")
    private LocalDateTime createdDate = LocalDateTime.now();
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        Product product = (Product) o;
        return Objects.equals(getId(), product.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
