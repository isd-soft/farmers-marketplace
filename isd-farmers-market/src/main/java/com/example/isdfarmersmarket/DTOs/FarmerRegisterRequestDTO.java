package com.example.isdfarmersmarket.DTOs;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class FarmerRegisterRequestDTO extends CustomerRegisterRequestDTO {
    private String address;
    private String description;
}
