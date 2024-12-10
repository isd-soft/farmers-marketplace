package com.example.isdfarmersmarket.dao.models;

import com.example.isdfarmersmarket.dao.enums.OrderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "farmer_id", nullable = false)
    private User farmer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "delivery_type_id", nullable = false)
    private DeliveryTypeFarmer deliveryTypeFarmer;

    @Column(name = "total_delivery_price", nullable = false)
    private BigDecimal totalDeliveryPrice;

    @Column(name = "total_items_price", nullable = false)
    private BigDecimal totalItemsPrice;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<ItemInOrder> itemsInOrder = new HashSet<>();

    @Column(name = "created_date", columnDefinition = "TimeStamp")
    private LocalDateTime createdDate = LocalDateTime.now();

    public Order(User customer, User farmer, OrderStatus orderStatus, DeliveryTypeFarmer deliveryTypeFarmer, BigDecimal totalDeliveryPrice, BigDecimal totalItemsPrice, BigDecimal totalPrice) {
        this.customer = customer;
        this.farmer = farmer;
        this.orderStatus = orderStatus;
        this.deliveryTypeFarmer = deliveryTypeFarmer;
        this.totalDeliveryPrice = totalDeliveryPrice;
        this.totalItemsPrice = totalItemsPrice;
        this.totalPrice = totalPrice;
    }

//    public Order(User customer, User farmer, OrderStatus orderStatus, BigDecimal totalPrice) {
//        this.customer = customer;
//        this.farmer = farmer;
//        this.orderStatus = orderStatus;
//        this.totalItemsPrice = totalPrice;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return Objects.equals(getId(), order.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
