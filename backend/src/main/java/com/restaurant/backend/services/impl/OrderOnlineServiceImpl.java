package com.restaurant.backend.services.impl;

import com.restaurant.backend.domains.dto.OrderOnline.OrderOnline;
import com.restaurant.backend.domains.dto.OrderOnline.OrderOnlineDetails;
import com.restaurant.backend.domains.dto.OrderOnline.dto.OrderOnlineDTO;
import com.restaurant.backend.domains.dto.OrderOnline.dto.OrderOnlineDetailsDTO;
import com.restaurant.backend.domains.dto.OrderOnline.enums.OrderStatus;
import com.restaurant.backend.domains.dto.OrderOnline.mapper.OrderOnlineMapper;
import com.restaurant.backend.domains.dto.OrderOnline.mapper.OrderOnlineDetailsMapper;
import com.restaurant.backend.repositories.OrderOnlineRepository;
import com.restaurant.backend.repositories.MenuItemRepository;
import com.restaurant.backend.services.OrderOnlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderOnlineServiceImpl implements OrderOnlineService {

    private final OrderOnlineRepository orderOnlineRepository;
    private final MenuItemRepository menuItemRepository;
    private final OrderOnlineMapper orderOnlineMapper;
    private final OrderOnlineDetailsMapper detailsMapper;

    @Autowired
    public OrderOnlineServiceImpl(
            OrderOnlineRepository orderOnlineRepository,
            MenuItemRepository menuItemRepository,
            OrderOnlineMapper orderOnlineMapper,
            OrderOnlineDetailsMapper detailsMapper) {
        this.orderOnlineRepository = orderOnlineRepository;
        this.menuItemRepository = menuItemRepository;
        this.orderOnlineMapper = orderOnlineMapper;
        this.detailsMapper = detailsMapper;
    }

    @Override
    @Transactional
    public OrderOnlineDTO createOrder(OrderOnlineDTO orderOnlineDTO) {
        OrderOnline orderOnline = orderOnlineMapper.toEntity(orderOnlineDTO);
        orderOnline.setOrderTime(LocalDateTime.now());
        orderOnline.setStatus(OrderStatus.PENDING);

        // Calculate total amount
        BigDecimal totalAmount = orderOnlineDTO.getOrderDetails().stream()
                .map(detail -> detail.getPrice().multiply(new BigDecimal(detail.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        orderOnline.setTotalAmount(totalAmount);

        // Map and add order details
        orderOnlineDTO.getOrderDetails().forEach(dto -> {
            OrderOnlineDetails detail = detailsMapper.toEntity(dto);
            orderOnline.addOrderDetail(detail);
        });

        OrderOnline savedOrder = orderOnlineRepository.save(orderOnline);
        return orderOnlineMapper.toDTO(savedOrder);
    }

    @Override
    public List<OrderOnlineDTO> getAllOrders() {
        return orderOnlineRepository.findAll().stream()
                .map(orderOnlineMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<OrderOnlineDTO> getOrderById(Long id) {
        return orderOnlineRepository.findById(id)
                .map(orderOnlineMapper::toDTO);
    }

    @Override
    public List<OrderOnlineDTO> getOrdersByUserId(String userId) {
        return orderOnlineRepository.findByUserId(userId).stream()
                .map(orderOnlineMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderOnlineDTO> getOrdersByStatus(String status) {
        return orderOnlineRepository.findByStatus(OrderStatus.valueOf(status)).stream()
                .map(orderOnlineMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderOnlineDTO updateOrderStatus(Long orderId, String newStatus) {
        OrderOnline order = orderOnlineRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(OrderStatus.valueOf(newStatus));
        if (OrderStatus.valueOf(newStatus) == OrderStatus.DELIVERING) {
            order.setDeliveryTime(LocalDateTime.now());
        }

        OrderOnline updatedOrder = orderOnlineRepository.save(order);
        return orderOnlineMapper.toDTO(updatedOrder);
    }

    @Override
    @Transactional
    public void cancelOrder(Long orderId) {
        OrderOnline order = orderOnlineRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Can only cancel pending orders");
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderOnlineRepository.save(order);
    }
} 