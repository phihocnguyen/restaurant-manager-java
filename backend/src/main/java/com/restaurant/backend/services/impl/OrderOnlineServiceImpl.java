package com.restaurant.backend.services.impl;

import com.restaurant.backend.domains.dto.OrderOnline.OrderOnline;
import com.restaurant.backend.domains.dto.OrderOnline.OrderOnlineDetails;
import com.restaurant.backend.domains.dto.OrderOnline.dto.OrderOnlineDTO;
import com.restaurant.backend.domains.dto.OrderOnline.dto.OrderOnlineDetailsDTO;
import com.restaurant.backend.domains.dto.OrderOnline.enums.OrderStatus;
import com.restaurant.backend.domains.dto.OrderOnline.mapper.OrderOnlineMapper;
import com.restaurant.backend.domains.dto.OrderOnline.mapper.OrderOnlineDetailsMapper;
import com.restaurant.backend.domains.entities.Employee;
import com.restaurant.backend.domains.entities.MenuItem;
import com.restaurant.backend.domains.entities.Recipe;
import com.restaurant.backend.domains.entities.Ingredient;
import com.restaurant.backend.repositories.EmployeeRepository;
import com.restaurant.backend.repositories.OrderOnlineRepository;
import com.restaurant.backend.repositories.OrderOnlineDetailsRepository;
import com.restaurant.backend.repositories.MenuItemRepository;
import com.restaurant.backend.repositories.RecipeRepository;
import com.restaurant.backend.repositories.IngredientRepository;
import com.restaurant.backend.services.OrderOnlineService;
import com.restaurant.backend.other_services.EmailService;
import com.restaurant.backend.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.text.NumberFormat;
import java.util.Locale;

@Service
public class OrderOnlineServiceImpl implements OrderOnlineService {

    private final OrderOnlineRepository orderOnlineRepository;
    private final OrderOnlineDetailsRepository orderOnlineDetailsRepository;
    private final MenuItemRepository menuItemRepository;
    private final OrderOnlineMapper orderOnlineMapper;
    private final OrderOnlineDetailsMapper orderOnlineDetailsMapper;
    private final EmployeeRepository employeeRepository;
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final EmailService emailService;
    private final AccountRepository accountRepository;

    @Autowired
    public OrderOnlineServiceImpl(
            OrderOnlineRepository orderOnlineRepository,
            OrderOnlineDetailsRepository orderOnlineDetailsRepository,
            MenuItemRepository menuItemRepository,
            OrderOnlineMapper orderOnlineMapper,
            OrderOnlineDetailsMapper orderOnlineDetailsMapper,
            EmployeeRepository employeeRepository,
            RecipeRepository recipeRepository,
            IngredientRepository ingredientRepository,
            EmailService emailService,
            AccountRepository accountRepository) {
        this.orderOnlineRepository = orderOnlineRepository;
        this.orderOnlineDetailsRepository = orderOnlineDetailsRepository;
        this.menuItemRepository = menuItemRepository;
        this.orderOnlineMapper = orderOnlineMapper;
        this.orderOnlineDetailsMapper = orderOnlineDetailsMapper;
        this.employeeRepository = employeeRepository;
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.emailService = emailService;
        this.accountRepository = accountRepository;
    }

    @Override
    @Transactional
    public OrderOnlineDTO createOrder(OrderOnlineDTO orderOnlineDTO) {
        OrderOnline orderOnline = orderOnlineMapper.toEntity(orderOnlineDTO);
        // Set fields that might not be fully handled by default mapping or need explicit setting
        orderOnline.setUserId(orderOnlineDTO.getUserId());
        orderOnline.setPaymentMethod(orderOnlineDTO.getPaymentMethod());
        orderOnline.setOrderTime(Instant.now()); // Ensure order time is set
        orderOnline.setStatus(OrderStatus.PENDING); // Ensure status is set
        // Ensure order details are linked to the order entity
        if (orderOnlineDTO.getOrderDetails() != null) {
            orderOnlineDTO.getOrderDetails().forEach(detailDTO -> {
                OrderOnlineDetails detail = orderOnlineDetailsMapper.toEntity(detailDTO);
                orderOnline.addOrderDetail(detail); // Use the helper method to link both sides
            });
        }
        // Recalculate and set total amount based on entity details (safer after details are linked)
        BigDecimal totalAmount = orderOnline.getOrderDetails().stream()
                .map(detail -> detail.getPrice().multiply(BigDecimal.valueOf(detail.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        orderOnline.setTotalAmount(totalAmount);
        // Save the order and flush changes immediately
        OrderOnline savedOrder = orderOnlineRepository.saveAndFlush(orderOnline);
        // Gửi email xác nhận đơn hàng cho khách ngay khi đặt hàng
        if (orderOnline.getUserId() != null) {
            String userId = orderOnline.getUserId();
            // Tìm account theo username trước, nếu không có thì thử theo email
            var accOpt = accountRepository.findById(userId);
            if (!accOpt.isPresent()) {
                accOpt = accountRepository.findOneByAccEmail(userId);
            }
            if (accOpt.isPresent()) {
                String email = accOpt.get().getAccEmail();
                // Tạo nội dung email HTML đẹp
                StringBuilder content = new StringBuilder();
                content.append("<div style='font-family:Inter,sans-serif;max-width:600px;margin:auto;background:#fff;border-radius:12px;box-shadow:0 2px 16px #0001;padding:32px;'>");
                content.append("<div style='text-align:center;margin-bottom:24px;'><h2 style='color:#D4AF37;margin:8px 0 0 0;font-family:Playfair Display,serif;'>G15 Kitchen</h2></div>");
                content.append("<h3 style='color:#222;text-align:center;margin-bottom:24px;'>Cảm ơn bạn đã đặt hàng tại <span style='color:#D4AF37;'>G15 Kitchen</span>!</h3>");
                content.append("<div style='overflow-x:auto;'><table style='width:100%;border-collapse:collapse;margin-bottom:24px;'>");
                content.append("<thead><tr style='background:#F9F6EF;color:#D4AF37;font-weight:bold;'><th style='padding:12px 8px;border-bottom:2px solid #D4AF37;'>Tên món</th><th style='padding:12px 8px;border-bottom:2px solid #D4AF37;'>Số lượng</th><th style='padding:12px 8px;border-bottom:2px solid #D4AF37;'>Đơn giá</th></tr></thead><tbody>");
                for (OrderOnlineDetails detail : orderOnline.getOrderDetails()) {
                    MenuItem menuItem = menuItemRepository.findById(detail.getItemId().intValue()).orElse(null);
                    if (menuItem != null) {
                        content.append("<tr style='border-bottom:1px solid #eee;'>");
                        content.append("<td style='padding:10px 8px;'>").append(menuItem.getItemName()).append("</td>");
                        content.append("<td style='padding:10px 8px;text-align:center;'>").append(detail.getQuantity()).append("</td>");
                        content.append("<td style='padding:10px 8px;text-align:right;color:#D4AF37;'>").append(formatCurrency(detail.getPrice())).append("</td>");
                        content.append("</tr>");
                    }
                }
                content.append("</tbody></table></div>");
                content.append("<div style='text-align:right;font-size:1.1em;margin-bottom:24px;'><b>Tổng tiền: <span style='color:#D4AF37;'>").append(formatCurrency(orderOnline.getTotalAmount())).append("</span></b></div>");
                content.append("<div style='background:#F9F6EF;padding:16px 20px;border-radius:8px;text-align:center;color:#444;'>Chúng tôi sẽ liên hệ với bạn để xác nhận và giao hàng sớm nhất.<br>Cảm ơn bạn đã tin tưởng <b style='color:#D4AF37;'>G15 Kitchen</b>!</div>");
                content.append("<div style='margin-top:32px;text-align:center;color:#aaa;font-size:0.95em;'>Nếu có thắc mắc, vui lòng liên hệ: <a href='mailto:info@g15kitchen.com' style='color:#D4AF37;text-decoration:none;'>info@g15kitchen.com</a></div>");
                content.append("</div>");
                emailService.sendReceiptEmail(email, "Xác nhận đơn hàng G15 Kitchen", content.toString());
            }
        }
        // Log the savedOrder entity state after saving and flushing
        System.out.println("Saved OrderOnline entity after saving and flushing: " + savedOrder);
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
    public OrderOnlineDTO updateOrderStatus(Long orderId, String newStatus, Integer employeeId) {
        System.out.println("Updating order status for order ID: " + orderId + " to status: " + newStatus);
        System.out.println("Received employee ID: " + employeeId);

        OrderOnline order = orderOnlineRepository.findById(orderId)
                .orElseThrow(() -> {
                    System.err.println("Order not found for ID: " + orderId);
                    return new RuntimeException("Order not found");
                });

        // If the order is being completed (status changed to COMPLETED), decrease ingredient quantities
        if (OrderStatus.valueOf(newStatus) == OrderStatus.COMPLETED) {
            // Get all menu items in the order
            List<OrderOnlineDetails> orderDetails = orderOnlineDetailsRepository.findByOrderOnlineId(orderId);
            
            for (OrderOnlineDetails detail : orderDetails) {
                MenuItem menuItem = menuItemRepository.findById(detail.getItemId().intValue())
                        .orElseThrow(() -> new RuntimeException("Menu item not found"));
                
                // Get recipes for this menu item
                List<Recipe> recipes = recipeRepository.findAllByItemId(menuItem.getId());
                
                // For each recipe, decrease the ingredient quantity
                for (Recipe recipe : recipes) {
                    double requiredQuantity = recipe.getIngreQuantityKg() * detail.getQuantity();
                    Ingredient ingredient = recipe.getIngre();
                    
                    if (ingredient.getInstockKg() < requiredQuantity) {
                        throw new RuntimeException("Not enough ingredients in stock for " + menuItem.getItemName());
                    }
                    
                    ingredient.setInstockKg(ingredient.getInstockKg() - requiredQuantity);
                    ingredientRepository.save(ingredient);
                }
            }
            // Gửi email xác nhận đơn hàng cho khách
            // Lấy email từ userId (accEmail)
            OrderOnline orderForEmail = orderOnlineRepository.findById(orderId).orElse(null);
            if (orderForEmail != null && orderForEmail.getUserId() != null) {
                String accEmail = orderForEmail.getUserId();
                var accOpt = accountRepository.findOneByAccEmail(accEmail);
                if (accOpt.isPresent()) {
                    String email = accOpt.get().getAccEmail();
                    // Tạo nội dung danh sách item đã order
                    StringBuilder content = new StringBuilder();
                    content.append("Cảm ơn bạn đã đặt hàng tại G15 Kitchen!\n\nDanh sách món đã đặt:\n");
                    for (OrderOnlineDetails detail : orderDetails) {
                        MenuItem menuItem = menuItemRepository.findById(detail.getItemId().intValue()).orElse(null);
                        if (menuItem != null) {
                            content.append("- ")
                                   .append(menuItem.getItemName())
                                   .append(" x ")
                                   .append(detail.getQuantity())
                                   .append(" (Đơn giá: ")
                                   .append(formatCurrency(detail.getPrice())).append(")\n");
                        }
                    }
                    content.append("\nTổng tiền: ").append(formatCurrency(orderForEmail.getTotalAmount())).append("\n");
                    content.append("\nChúng tôi sẽ liên hệ với bạn để xác nhận và giao hàng sớm nhất!");
                    emailService.sendReceiptEmail(email, "Xác nhận đơn hàng G15 Kitchen", content.toString());
                }
            }
        }

        order.setStatus(OrderStatus.valueOf(newStatus));
        if (OrderStatus.valueOf(newStatus) == OrderStatus.DELIVERING) {
            order.setDeliveryTime(Instant.now());
        }

        if (employeeId != null) {
            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> {
                        System.err.println("Employee not found for ID: " + employeeId);
                        return new RuntimeException("Employee not found");
                    });
            order.setEmployee(employee);
            System.out.println("Set employee for order. Employee ID to be saved: " + employee.getId() + ", Name: " + employee.getName());
        } else {
            System.out.println("Employee ID is null, not setting employee for order.");
            order.setEmployee(null);
        }

        OrderOnline updatedOrder = orderOnlineRepository.save(order);
        System.out.println("Order saved. Employee ID in saved order: " + (updatedOrder.getEmployee() != null ? updatedOrder.getEmployee().getId() : "null"));
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

    @Override
    public List<OrderOnlineDetailsDTO> getOrderDetailsByOrderId(Long orderId) {
        return orderOnlineDetailsRepository.findByOrderOnlineId(orderId)
                .stream()
                .map(orderOnlineDetailsMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderOnlineDTO updateOrder(Long orderId, OrderOnlineDTO updatedOrderDTO) {
        OrderOnline existingOrder = orderOnlineRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Use MapStruct mapper to update fields from DTO to entity
        orderOnlineMapper.updateEntityFromDTO(updatedOrderDTO, existingOrder);

        // If status is updated to DELIVERING, set delivery time
         if (updatedOrderDTO.getStatus() == OrderStatus.DELIVERING && existingOrder.getDeliveryTime() == null) {
             existingOrder.setDeliveryTime(Instant.now());
         }

        OrderOnline updatedOrder = orderOnlineRepository.save(existingOrder);
        // Optionally load details if needed in the response DTO
        OrderOnlineDTO resultDTO = orderOnlineMapper.toDTO(updatedOrder);
        resultDTO.setOrderDetails(getOrderDetailsByOrderId(updatedOrder.getId()));

        return resultDTO;
    }

    @Override
    public OrderOnlineDTO updatePaymentProof(Long id, String paymentImageUrl) {
        OrderOnline order = orderOnlineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        
        order.setPaymentImage(paymentImageUrl);
        OrderOnline savedOrder = orderOnlineRepository.save(order);
        return orderOnlineMapper.toDTO(savedOrder);
    }

    // Helper method for formatting currency
    private String formatCurrency(Number amount) {
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        return formatter.format(amount) + " VND";
    }
} 