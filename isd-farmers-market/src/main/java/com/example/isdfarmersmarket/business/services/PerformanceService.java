package com.example.isdfarmersmarket.business.services;

import com.example.isdfarmersmarket.business.security.JwtPrincipal;
import com.example.isdfarmersmarket.dao.projections.MonthlyRevenueByCategoryProjection;
import com.example.isdfarmersmarket.dao.projections.MonthlyRevenueProjection;
import com.example.isdfarmersmarket.dao.projections.RevenueByCategoryProjection;
import com.example.isdfarmersmarket.dao.projections.RevenueByProductProjection;
import com.example.isdfarmersmarket.dao.repositories.PerformanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PerformanceService {
    private final PerformanceRepository performanceRepository;

    @Transactional(readOnly = true)
    public List<RevenueByCategoryProjection> getRevenueByCategory(Long year) {
        JwtPrincipal principal = (JwtPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return performanceRepository.getRevenueByCategoryAndFarmer(year, principal.getId());
    }

    @Transactional(readOnly = true)
    public List<RevenueByProductProjection> getRevenueByProduct(Long year) {
        JwtPrincipal principal = (JwtPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return performanceRepository.getRevenueByProductAndFarmer(year, principal.getId());
    }

    @Transactional(readOnly = true)
    public List<MonthlyRevenueProjection> getMonthlyRevenue(Long year) {
        JwtPrincipal principal = (JwtPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return performanceRepository.getMonthlyRevenueByYearAndFarmer(year, principal.getId());
    }

    @Transactional(readOnly = true)
    public List<MonthlyRevenueByCategoryProjection> getMonthlyRevenueByCategory(Long year) {
        JwtPrincipal principal = (JwtPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return performanceRepository.getMonthlyRevenueByCategoryAndFarmer(year, principal.getId());
    }

    @Transactional(readOnly = true)
    public Long getTotalRevenue(Long year) {
        JwtPrincipal principal = (JwtPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return performanceRepository.getTotalRevenueByFarmerAndYear(year, principal.getId());
    }
}
