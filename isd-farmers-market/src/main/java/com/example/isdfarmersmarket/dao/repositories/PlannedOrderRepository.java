package com.example.isdfarmersmarket.dao.repositories;

import com.example.isdfarmersmarket.dao.models.PlannedOrder;
import com.example.isdfarmersmarket.dao.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

public interface PlannedOrderRepository extends JpaRepository<PlannedOrder, Long> {
    @Query("SELECT po FROM PlannedOrder po WHERE po.dayOfWeek = :dayOfWeek AND po.time = :time AND po.active = :active")
    List<PlannedOrder> findByDayOfWeekAndTime(DayOfWeek dayOfWeek, LocalTime time, boolean active);

    List<PlannedOrder> findPlannedOrdersByCustomer(User customer);
}
