package com.example.isdfarmersmarket.business.services.order;

import com.example.isdfarmersmarket.business.mapper.OrderMapper;
import com.example.isdfarmersmarket.dao.models.Category;
import com.example.isdfarmersmarket.dao.models.ItemInOrder;
import com.example.isdfarmersmarket.dao.models.Product;
import com.example.isdfarmersmarket.dao.models.User;
import com.example.isdfarmersmarket.dao.repositories.OrderRepository;
import com.example.isdfarmersmarket.dao.repositories.UserRepository;
import com.example.isdfarmersmarket.web.commands.order.CreateOrderCommand;
import com.example.isdfarmersmarket.web.commands.CreateOrderCommandș
import com.example.isdfarmersmarket.web.commands.UpdateCategoryCommand;
import com.example.isdfarmersmarket.web.commands.order.UpdateOrderCommand;
import com.example.isdfarmersmarket.web.dto.CategoryDTO;
import com.example.isdfarmersmarket.web.dto.OrderDTO;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;
//    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderDTO createOrder(CreateOrderCommand createOrderCommand, String authenticatedUsername) {
        User user = userRepository.findByEmail(authenticatedUsername)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + authenticatedUsername));

        Set<ItemInOrder> items = new HashSet<>();
        createOrderCommand.getProductId().forEach(productItem -> {
            Product product = productRepository.findById(productItem.getProductById())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + productItem.getProductId()));

            ItemInOrder item = new ItemInOrder();
            item.setProduct(product);
            item.setQuantity(productItem.getQuantity());
            item.setOrder(null); // This will be set when the order is saved
            items.add(item);
        });

        // Calculate total price
        BigDecimal totalPrice = items.stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Create and populate the Order entity
        Order order = new Order();
        order.setUser(user);
        order.setProducts(items);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setTotalPrice(totalPrice);
        order.setCreatedDate(LocalDateTime.now());

        // Persist the order
        Order savedOrder = orderRepository.save(order);

        // Map to DTO and return
        return orderMapper.map(savedOrder);
    }
    @Override
    @Transactional
    public OrderDTO createOrder(CreateOrderCommand createOrderCommand) {
        User user = userRepository.findById(createOrderCommand.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User with the specified ID not found"));

        // Map products from the command
        Set<ItemInOrder> items = mapProducts(createOrderCommand);

        // Calculate total price
        BigDecimal totalPrice = calculateTotalPrice(items);

        // Create the order
        Order order = new Order();
        order.setUser(user);
        order.setProducts(items);
        order.setOrderStatus(OrderStatus.PENDING); // Default status for a new order
        order.setTotalPrice(totalPrice);
        order.setCreatedDate(LocalDateTime.now());

        // Save the order
        orderRepository.save(order);

        // Return mapped DTO
        return daoMapper.map(order);
    }

    @Override
    @Transactional
    public OrderDTO updateOrder(Long id, UpdateOrderCommand updateOrderCommand) {
        // Fetch the order to update
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order with the specified ID not found"));

        // Update order fields
        order.setOrderStatus(OrderStatus.valueOf(updateOrderCommand.getOrderStatus().toUpperCase()));
        order.setTotalPrice(updateOrderCommand.getTotalPrice());

        // Save updated order
        orderRepository.save(order);

        // Return updated DTO
        return daoMapper.map(order);
    }

    @Override
    @Transactional
    public OrderDTO deleteOrder(Long id) {
        // Fetch the order to delete
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order with the specified ID not found"));

        // Delete the order
        orderRepository.delete(order);

        // Return the deleted order's DTO
        return daoMapper.map(order);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDTO getOrderById(Long id) {
        // Fetch the order
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order with the specified ID not found"));

        // Return the DTO
        return daoMapper.map(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getAllOrders() {
        // Fetch all orders
        List<Order> orders = orderRepository.findAll();

        // Return mapped DTO list
        return daoMapper.mapOrders(orders);
    }

    // Utility to calculate total price
    private BigDecimal calculateTotalPrice(Set<ItemInOrder> items) {
        return items.stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Utility to map products from command
    private Set<ItemInOrder> mapProducts(CreateOrderCommand createOrderCommand) {
        Set<ItemInOrder> items = new HashSet<>();
        createOrderCommand.getProducts().forEach(productItem -> {
            ItemInOrder item = new ItemInOrder();
            item.setProductId(productItem.getProductId());
            item.setQuantity(productItem.getQuantity());
            items.add(item);
        });
        return items;
    }
//}
//    @Override
//    @Transactional
//    public OrderDTO createOrder(CreateOrderCommand createOrderCommand) {
//        String title = createOrderCommand.getTitle();
//        if (categoryRepository.existsByTitle(title))
//            throw new EntityExistsException("Category with the specified title already exists");
//        Category category = Category.builder()
//                .title(title)
//                .build();
//        categoryRepository.save(category);
//        return daoMapper.map(category);
//    }
//
//    @Override
//    public OrderDTO updateOrder(Long id, UpdateOrderCommand updateOrderCommand) {
//        return null;
//    }
//
//    @Override
//    public OrderDTO deleteOrder(Long id) {
//        return null;
//    }
//
//    @Override
//    public List<OrderDTO> getAllOrders() {
//        return List.of();
//    }
//
//
//    @Override
//    @Transactional
//    public CategoryDTO updateCategory(Long id, UpdateCategoryCommand updateCategoryCommand) {
//        Category category = categoryRepository.getCategoryById(id)
//                .orElseThrow(() -> new EntityNotFoundException("Category with the specified id not found")
//                );
//        String title = updateCategoryCommand.getTitle();
//        if (categoryRepository.existsByTitle(title))
//            throw new EntityExistsException("Category with the specified title already exists");
//        category.setTitle(title);
//        categoryRepository.save(category);
//        return daoMapper.map(category);
//    }
//
//    @Override
//    @Transactional
//    public CategoryDTO deleteCategory(Long id) {
//        Category category = categoryRepository.getCategoryById(id)
//                .orElseThrow(() -> new EntityNotFoundException("Category with the specified id not found")
//                );
//        categoryRepository.delete(category);
//        return daoMapper.map(category);
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<CategoryDTO> getAllCategories() {
//        List<Category> categories = categoryRepository.findAll();
//        return daoMapper.mapCategories(categories);
//    }

//
//    @Service
//    public class OrderService {
//
//        private final OrderRepository orderRepository;
//        private final ProductRepository productRepository; // Assume this repository exists
//
//        public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
//            this.orderRepository = orderRepository;
//            this.productRepository = productRepository;
//        }
//
//        public void updateOrder(Long id, UpdateOrderCommand command) {
//            Order order = orderRepository.findById(id)
//                    .orElseThrow(() -> new IllegalArgumentException("Order not found"));
//
//            order.setOrderStatus(OrderStatus.valueOf(command.getOrderStatus()));
//            order.setTotalPrice(command.getTotalPrice());
//
//            // Handle products
//            Set<ItemInOrder> items = new HashSet<>();
//            for (Long productId : command.getProductIds()) {
//                Product product = productRepository.findById(productId)
//                        .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));
//
//                ItemInOrder item = new ItemInOrder();
//                item.setOrder(order);
//                item.setProduct(product);
//                items.add(item);
//            }
//            order.setProducts(items);
//
//            orderRepository.save(order);
//        }
//    }

}
