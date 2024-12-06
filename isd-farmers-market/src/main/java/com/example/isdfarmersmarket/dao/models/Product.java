package com.example.isdfarmersmarket.dao.models;

import com.example.isdfarmersmarket.dao.enums.UnitType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name="products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_sequence")
    private Long id;

    @Column(length = 80)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(name="unit_type")
    private UnitType unitType;

    @Column(name="price_per_unit")
    private BigDecimal pricePerUnit;

    @Column(name="discount_percents")
    private int discountPercents;

    private int quantity;

    private boolean visible;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User farmer;

    @ManyToMany(mappedBy = "wishlist")
    private Set<User> inWishlists = new HashSet<>();

    private Float rating;
    private Integer reviewCount;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<ProductReview> reviews = new HashSet<>();
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Image> images = new HashSet<>();

    @Column(name="created_date", columnDefinition = "TimeStamp")
    private LocalDateTime createdDate = LocalDateTime.now();
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;
        return Objects.equals(getId(), product.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
