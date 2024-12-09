package com.example.isdfarmersmarket.business.mapper;

import com.example.isdfarmersmarket.dao.models.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CompactFarmerMapper {
    CompactFarmerDTO map(User user);
    List<CompactFarmerDTO> map(List<User> users);
}
