package com.example.isdfarmersmarket.dao.projections;

import java.math.BigDecimal;

public interface MonthlyRevenueProjection {
    String getMonth();

    BigDecimal getRevenue();
}
