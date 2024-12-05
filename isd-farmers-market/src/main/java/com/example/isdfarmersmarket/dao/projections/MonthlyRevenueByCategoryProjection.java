package com.example.isdfarmersmarket.dao.projections;

import java.math.BigDecimal;

public interface MonthlyRevenueByCategoryProjection {
    String getCategory();

    int getMonth();

    BigDecimal getRevenue();
}
