package com.example.isdfarmersmarket.business.services;

import com.example.isdfarmersmarket.dao.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PerformanceService {
    private final OrderRepository orderRepository;
//    @Transactional(readOnly = true)
//    public Map<String, BigDecimal> getOrdersCountByMonth(int year) {
//       JwtPrincipal principal = (JwtPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//       List<Order> orders = orderRepository.findOrdersByFarmerAndYear(principal.getId(), year);
//
//       Map<String, BigDecimal> salesByMonth = new HashMap<>();
//        for (Order order : orders) {
//            String monthName = order.getCreatedDate().getMonth().toString();
//           salesByMonth.merge(monthName, order.getTotalPrice(), BigDecimal::add);
//           }
}
