package com.example.isdfarmersmarket.business.mapper;

import com.example.isdfarmersmarket.dao.models.Image;
import com.example.isdfarmersmarket.web.dto.ImageDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ImageMapper {
    ImageDTO map(Image image);
    List<ImageDTO> mapImages(List<Image> images);
}
