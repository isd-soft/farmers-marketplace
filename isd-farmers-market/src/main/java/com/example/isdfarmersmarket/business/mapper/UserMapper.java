package com.example.isdfarmersmarket.business.mapper;

import com.example.isdfarmersmarket.dao.models.Product;
import com.example.isdfarmersmarket.web.dto.ProductDTO;
import com.example.isdfarmersmarket.web.dto.UserDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO map(Product product);
    List<UserDTO> mapUsers(List<Product> products);
}
