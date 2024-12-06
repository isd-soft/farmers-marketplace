package com.example.isdfarmersmarket.dao.repositories;

import com.example.isdfarmersmarket.dao.models.Order;
import com.example.isdfarmersmarket.dao.projections.MonthlyRevenueByCategoryProjection;
import com.example.isdfarmersmarket.dao.projections.MonthlyRevenueProjection;
import com.example.isdfarmersmarket.dao.projections.RevenueByCategoryProjection;
import com.example.isdfarmersmarket.dao.projections.RevenueByProductProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PerformanceRepository extends JpaRepository<Order, Long> {

    @Query(value = "SELECT c.title AS category, SUM(io.price_per_unit * io.quantity) AS revenue " +
            "FROM orders o " +
            "JOIN items_in_order io ON o.id = io.order_id " +
            "JOIN products p ON io.product_id = p.id " +
            "JOIN categories c ON p.category_id = c.id " +
            "WHERE EXTRACT(YEAR FROM o.created_date) = :year " +
            "AND o.farmer_id = :farmerId " +
            "GROUP BY c.title", nativeQuery = true)
    List<RevenueByCategoryProjection> getRevenueByCategoryAndFarmer(
            @Param("year") Long year,
            @Param("farmerId") Long farmerId);

    @Query(value = "SELECT p.title AS product, SUM(io.price_per_unit * io.quantity) AS revenue " +
            "FROM orders o " +
            "JOIN items_in_order io ON o.id = io.order_id " +
            "JOIN products p ON io.product_id = p.id " +
            "WHERE EXTRACT(YEAR FROM o.created_date) = :year " +
            "AND o.farmer_id = :farmerId " +
            "GROUP BY p.title", nativeQuery = true)
    List<RevenueByProductProjection> getRevenueByProductAndFarmer(
            @Param("year") Long year,
            @Param("farmerId") Long farmerId);

    @Query(value = "SELECT EXTRACT(MONTH FROM o.created_date) AS month, " +
            "SUM(io.price_per_unit * io.quantity) AS revenue " +
            "FROM orders o " +
            "JOIN items_in_order io ON o.id = io.order_id " +
            "WHERE EXTRACT(YEAR FROM o.created_date) = :year " +
            "AND o.farmer_id = :farmerId " +
            "GROUP BY EXTRACT(MONTH FROM o.created_date) " +
            "ORDER BY month", nativeQuery = true)
    List<MonthlyRevenueProjection> getMonthlyRevenueByYearAndFarmer(
            @Param("year") Long year,
            @Param("farmerId") Long farmerId);

    @Query(value = "SELECT c.title AS category, " +
            "EXTRACT(MONTH FROM o.created_date) AS month, " +
            "SUM(io.price_per_unit * io.quantity) AS revenue " +
            "FROM orders o " +
            "JOIN items_in_order io ON o.id = io.order_id " +
            "JOIN products p ON io.product_id = p.id " +
            "JOIN categories c ON p.category_id = c.id " +
            "WHERE EXTRACT(YEAR FROM o.created_date) = :year " +
            "AND o.farmer_id = :farmerId " +
            "GROUP BY c.title, EXTRACT(MONTH FROM o.created_date) " +
            "ORDER BY c.title, month",
            nativeQuery = true)
    List<MonthlyRevenueByCategoryProjection> getMonthlyRevenueByCategoryAndFarmer(
            @Param("year") Long year,
            @Param("farmerId") Long farmerId
    );

    @Query(value = "SELECT SUM(io.price_per_unit * io.quantity) AS total_revenue " +
            "FROM orders o " +
            "JOIN items_in_order io ON o.id = io.order_id " +
            "JOIN products p ON io.product_id = p.id " +
            "WHERE EXTRACT(YEAR FROM o.created_date) = :year " +
            "AND o.farmer_id = :farmerId", nativeQuery = true)
    Long getTotalRevenueByFarmerAndYear(
            @Param("year") Long year,
            @Param("farmerId") Long farmerId);
}
