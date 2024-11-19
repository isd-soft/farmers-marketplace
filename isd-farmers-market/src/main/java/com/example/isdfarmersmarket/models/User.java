package com.example.isdfarmersmarket.models;

import com.example.isdfarmersmarket.enums.DeliveryTypes;
import com.example.isdfarmersmarket.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.usertype.UserType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernate_sequence")
    private Long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    private Role role;
    private String email;
    @Column(name = "phone_number")
    private String phoneNumber;
    private String description;
    private String address;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<ItemInCart> cart = new HashSet<>();

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "wishlist",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "product_id") }
    )
    private Set<Product> wishlist = new HashSet<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Product> myProducts = new HashSet<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Order> myOrders = new HashSet<>();

    @Column(length = 1000)
    private String password;
    @Column(name = "free_delivery_from")
    private  int freeDeliveryFrom;
    @OneToMany(mappedBy = "farmer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<DeliveryTypeFarmer> deliveryTypes = new HashSet<>();

    private float rating;
    @OneToMany(mappedBy = "farmer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<FarmerReview> reviews = new HashSet<>();

    @Column(name="created_date", columnDefinition = "TimeStamp")
    private LocalDateTime createdDate = LocalDateTime.now();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(()-> role.name());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
