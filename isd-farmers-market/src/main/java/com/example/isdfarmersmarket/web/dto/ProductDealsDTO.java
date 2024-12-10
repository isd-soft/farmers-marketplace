package com.example.isdfarmersmarket.web.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;


@Data
@NoArgsConstructor
public class ProductDealsDTO {
    Page<CompactProductDTO> discountedAbove50Percent;
    Page<CompactProductDTO> discountedAbove30Percent;
    Page<CompactProductDTO> discountedAbove15Percent;
    Page<CompactProductDTO> discountedAbove5Percent;
}
