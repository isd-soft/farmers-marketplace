package com.example.isdfarmersmarket.dao.projections;

import java.math.BigDecimal;

public interface RevenueByCategoryProjection {

    String getCategory();

    BigDecimal getRevenue();
}
