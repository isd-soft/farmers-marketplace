package com.example.isdfarmersmarket.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.dialect.function.PostgreSQLTruncRoundFunction;

import java.util.Objects;

@Entity
@Table(name="product_reviews")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductReview {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernate_sequence")
    private Long id;
    private String content;
    private float rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private User creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        ProductReview review = (ProductReview) o;
        return Objects.equals(getId(), review.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
