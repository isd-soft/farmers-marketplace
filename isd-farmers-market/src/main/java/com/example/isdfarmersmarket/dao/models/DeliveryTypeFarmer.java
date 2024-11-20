package com.example.isdfarmersmarket.dao.models;

import com.example.isdfarmersmarket.dao.enums.DeliveryTypes;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name="delivery_type_farmer")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryTypeFarmer {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernate_sequence")
    private Long id;
    private BigDecimal price;
    private DeliveryTypes type;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farmer_id", nullable = false)
    private User farmer;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        DeliveryTypeFarmer deliveryType = (DeliveryTypeFarmer) o;
        return Objects.equals(getId(), deliveryType.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

}
