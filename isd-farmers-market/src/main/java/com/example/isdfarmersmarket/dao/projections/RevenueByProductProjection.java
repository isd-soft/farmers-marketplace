package com.example.isdfarmersmarket.dao.projections;

import java.math.BigDecimal;

public interface RevenueByProductProjection {
    String getProduct();

    BigDecimal getRevenue();
}
