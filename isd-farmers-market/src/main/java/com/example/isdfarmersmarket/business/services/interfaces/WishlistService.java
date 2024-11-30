package com.example.isdfarmersmarket.business.services.interfaces;

import com.example.isdfarmersmarket.web.dto.CompactProductDTO;

import java.util.List;

public interface WishlistService {

    List<CompactProductDTO> getWishlistProducts();

    CompactProductDTO addProductToWishlist(Long productId);

    CompactProductDTO deleteProductFromWishlist(Long productId);
}
