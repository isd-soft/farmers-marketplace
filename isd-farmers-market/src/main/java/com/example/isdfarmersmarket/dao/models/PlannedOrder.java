package com.example.isdfarmersmarket.dao.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="planned_orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlannedOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "planned_order_sequence")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private int quantity;

    @Enumerated(EnumType.STRING)
    @Column(name="day_of_week")
    private DayOfWeek dayOfWeek;

    private LocalTime time;

    private boolean active = true;

    @Column(name="created_date", columnDefinition = "TimeStamp")
    private LocalDateTime createdDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlannedOrder plannedOrder)) return false;
        return Objects.equals(getId(), plannedOrder.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}

