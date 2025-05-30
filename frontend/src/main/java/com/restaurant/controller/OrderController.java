package com.restaurant.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/order")
public class OrderController {

    @GetMapping("/success")
    public String orderSuccessPage(Model model) {
        model.addAttribute("title", "Order Successful - G15 Kitchen");
        return "order-success"; // Refers to order-success.html
    }

    // You might want to add a POST endpoint here later to handle the order submission logic
    // For now, we'll update the payment.html form to redirect to /order/success
} 