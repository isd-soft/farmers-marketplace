package com.example.isdfarmersmarket.DTOs;

import com.example.isdfarmersmarket.DTOs.abstractions.UserRegisterRequestDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class FarmerRegisterRequestDTO extends UserRegisterRequestDTO {
    private String address;
    private String description;
}
