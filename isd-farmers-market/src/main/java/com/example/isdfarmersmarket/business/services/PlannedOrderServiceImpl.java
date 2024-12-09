package com.example.isdfarmersmarket.business.services;

import com.example.isdfarmersmarket.business.exception.custom_exceptions.EntityNotFoundException;
import com.example.isdfarmersmarket.business.mapper.PlannedOrderMapper;
import com.example.isdfarmersmarket.business.security.JwtPrincipal;
import com.example.isdfarmersmarket.business.services.interfaces.PlannedOrderService;
import com.example.isdfarmersmarket.business.utils.SecurityUtils;
import com.example.isdfarmersmarket.dao.enums.DeliveryTypes;
import com.example.isdfarmersmarket.dao.enums.OrderStatus;
import com.example.isdfarmersmarket.dao.models.*;
import com.example.isdfarmersmarket.dao.repositories.*;
import com.example.isdfarmersmarket.web.commands.ScheduleOrderCommand;
import com.example.isdfarmersmarket.web.commands.UpdateScheduledOrderCommand;
import com.example.isdfarmersmarket.web.dto.PlannedOrderDTO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PlannedOrderServiceImpl implements PlannedOrderService {
    ItemInCartRepository itemInCartRepository;
    PlannedOrderRepository plannedOrderRepository;
    OrderRepository orderRepository;
    UserRepository userRepository;
    PlannedOrderMapper plannedOrderMapper;
    ProductRepository productRepository;
    private final DeliveryTypeRepository deliveryTypeRepository;
    private final DeliveryTypeService deliveryTypeService;

    @Override
    @Transactional
    public PlannedOrderDTO scheduleOrder(ScheduleOrderCommand scheduleOrderCommand) {
        JwtPrincipal principal = (JwtPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User customer = userRepository.findById(principal.getId()).orElse(null);
        Product product = productRepository.findById(scheduleOrderCommand.getProductId()).orElse(null);
        Long farmerId = product.getFarmer().getId();

        PlannedOrder plannedOrder = new PlannedOrder();
        plannedOrder.setCustomer(customer);
        plannedOrder.setProduct(product);
        plannedOrder.setQuantity(scheduleOrderCommand.getQuantity());
        plannedOrder.setDayOfWeek(scheduleOrderCommand.getDayOfWeek());
        plannedOrder.setTime(scheduleOrderCommand.getTime());
        plannedOrder.setDeliveryTypeFarmer(deliveryTypeRepository
                .findByFarmerIdAndType(farmerId, scheduleOrderCommand.getDeliveryType())
                .orElseThrow(() -> new EntityNotFoundException(farmerId, DeliveryTypes.class)));

        plannedOrderRepository.save(plannedOrder);

        return plannedOrderMapper.map(plannedOrder);
    }
    @Transactional
    @Override
    public PlannedOrderDTO updateScheduledOrder(Long id, UpdateScheduledOrderCommand updateScheduledOrderCommand) {
        if (!isOrderCreator(id)) {
            throw new AccessDeniedException("You are not the owner of this order");
        }
        PlannedOrder plannedOrder = plannedOrderRepository.findById(id).orElse(null);
        assert plannedOrder != null;
        Long farmerId = plannedOrder.getProduct().getFarmer().getId();
        plannedOrder.setQuantity(updateScheduledOrderCommand.getQuantity());
        plannedOrder.setDayOfWeek(updateScheduledOrderCommand.getDayOfWeek());
        plannedOrder.setTime(updateScheduledOrderCommand.getTime());
        plannedOrder.setDeliveryTypeFarmer(deliveryTypeRepository
                .findByFarmerIdAndType(farmerId, updateScheduledOrderCommand.getDeliveryType())
                .orElseThrow(() -> new EntityNotFoundException(farmerId, DeliveryTypes.class)));

        plannedOrderRepository.save(plannedOrder);

        return plannedOrderMapper.map(plannedOrder);
    }

    @Transactional
    @Override
    public PlannedOrderDTO changeActivityState(Long id, boolean state) {
        if (!isOrderCreator(id)) {
            throw new AccessDeniedException("You are not the owner of this order");
        }
        PlannedOrder plannedOrder = plannedOrderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, PlannedOrder.class));
        plannedOrder.setActive(state);
        plannedOrderRepository.save(plannedOrder);
        return plannedOrderMapper.map(plannedOrder);
    }
    @Transactional
    @Override
    public PlannedOrderDTO deletePlannedOrder(Long id) {
        if (!isOrderCreator(id)) {
            throw new AccessDeniedException("You are not the owner of this order");
        }
        PlannedOrder plannedOrder = plannedOrderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, PlannedOrder.class));
        plannedOrderRepository.delete(plannedOrder);
        return plannedOrderMapper.map(plannedOrder);
    }

    @Transactional
    @Override
    public List<PlannedOrderDTO> getCurrentUserPlannedOrders() {
        JwtPrincipal principal = SecurityUtils.getPrincipal();
        User currentUser = userRepository.findById( principal.getId()).orElse(null);
        List<PlannedOrder> plannedOrders = plannedOrderRepository.findPlannedOrdersByCustomer(currentUser);
        List<PlannedOrderDTO> plannedOrderDTOS = plannedOrderMapper.mapToPlannedOrders(plannedOrders);
        plannedOrderDTOS.forEach(plannedOrderDTO -> {
            plannedOrderDTO.setDeliveryPrice(
                    deliveryTypeService.getDeliveryTypePrice(plannedOrderDTO.getProduct().getFarmer().getEmail(), plannedOrderDTO.getDeliveryTypeFarmer().getType()));
        });
        return plannedOrderDTOS;
    }
    @Transactional
    @Override
    public PlannedOrderDTO getPlannedOrderById(Long id) {
        if (!isOrderCreator(id)) {
            throw new AccessDeniedException("You are not the owner of this order");
        }
        PlannedOrder plannedOrder = plannedOrderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, PlannedOrder.class));
        return plannedOrderMapper.map(plannedOrder);
    }

    @Override
    @Transactional
    @Scheduled(cron = "0 */10 * * * *")
    public void processPlannedOrders() {
        LocalDateTime now = LocalDateTime.now();
        DayOfWeek currentDay = now.getDayOfWeek();
        LocalTime currentTime = now.toLocalTime().truncatedTo(ChronoUnit.MINUTES);

        List<PlannedOrder> plannedOrders = plannedOrderRepository.findByDayOfWeekAndTime(currentDay, currentTime, true);

        for (PlannedOrder plannedOrder : plannedOrders) {
            Product product = plannedOrder.getProduct();
            if(product.getQuantity()>=plannedOrder.getQuantity()) {
                ItemInOrder newItem = new ItemInOrder();
                newItem.setProduct(product);
                newItem.setPricePerUnit(product.getPricePerUnit().subtract(product.getPricePerUnit().multiply(BigDecimal.valueOf((float) product.getDiscountPercents() / 100))));
                newItem.setQuantity(plannedOrder.getQuantity());
                product.setQuantity(product.getQuantity() - plannedOrder.getQuantity());
                productRepository.save(product);

                Order newOrder = new Order();
                newOrder.setCustomer(plannedOrder.getCustomer());
                newOrder.setFarmer(plannedOrder.getProduct().getFarmer());
                newOrder.setOrderStatus(OrderStatus.PENDING);
                newOrder.setDeliveryTypeFarmer(plannedOrder.getDeliveryTypeFarmer());
                newOrder.setTotalItemsPrice(newItem.getPricePerUnit().multiply(BigDecimal.valueOf(plannedOrder.getQuantity())));
                newOrder.setTotalDeliveryPrice(plannedOrder.getDeliveryTypeFarmer().getPrice());
                newOrder.setTotalPrice(newOrder.getTotalDeliveryPrice().add(newOrder.getTotalItemsPrice()));
                newOrder.getItemsInOrder().add(newItem);

                newItem.setOrder(newOrder);

                orderRepository.save(newOrder);
            } else {
                System.out.println("We apologize, your order was not created because the seller does not have the item in stock at the moment.");
            }
        }
    }

    private boolean isOrderCreator(Long orderId) {
        JwtPrincipal principal = SecurityUtils.getPrincipal();
        User currentUser = null;
        if (principal != null) {
            currentUser = userRepository.findById(principal.getId()).orElseThrow();
        }
        PlannedOrder plannedOrder = plannedOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        return plannedOrder.getCustomer().equals(currentUser);
    }
}
