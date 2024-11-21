package com.example.isdfarmersmarket.web.dto;

import com.example.isdfarmersmarket.dao.enums.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class UserDTO {
        private Long id;
        private String firstName;
        private String lastName;
        private Role role;
        private String email;
        private String phoneNumber;
        private String description;
        private String address;
        private Set<ProductDTO> wishlist = new HashSet<>();
        private Set<ProductDTO> myProducts = new HashSet<>();
 //       private Set<Order> myOrders = new HashSet<>();
        private  int freeDeliveryFrom;
 //       private Set<DeliveryTypeFarmer> deliveryTypes = new HashSet<>();

        private float rating;
  //      private Set<FarmerReview> reviews = new HashSet<>();
        private LocalDateTime createdDate = LocalDateTime.now();
}
