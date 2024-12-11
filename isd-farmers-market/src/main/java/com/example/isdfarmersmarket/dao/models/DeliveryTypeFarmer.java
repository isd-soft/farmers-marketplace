package com.example.isdfarmersmarket.dao.models;

import com.example.isdfarmersmarket.dao.enums.DeliveryTypes;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name="delivery_type_farmer")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryTypeFarmer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal price;
    private DeliveryTypes type;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "farmer_id", nullable = false)
    private User farmer;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeliveryTypeFarmer deliveryType)) return false;
        return Objects.equals(getId(), deliveryType.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

}
