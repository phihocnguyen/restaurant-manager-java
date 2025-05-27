package com.restaurant.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sales")
public class SalesController {

    @GetMapping
    public String salesPage(Model model) {
        model.addAttribute("title", "Bán hàng - Restaurant Manager");
        return "sales";
    }

    // Tables routes
    @GetMapping("/tables")
    public String tablesPage(Model model) {
        model.addAttribute("title", "Quản lý bàn - Restaurant Manager");
        model.addAttribute("activeTab", "active");
        return "sales/tables";
    }

    @GetMapping("/tables/active")
    public String activeTablesPage(Model model) {
        model.addAttribute("title", "Bàn đang hoạt động - Restaurant Manager");
        model.addAttribute("activeTab", "active");
        return "sales/tables";
    }

    @GetMapping("/tables/inactive")
    public String inactiveTablesPage(Model model) {
        model.addAttribute("title", "Bàn trống - Restaurant Manager");
        model.addAttribute("activeTab", "inactive");
        return "sales/tables";
    }

    @GetMapping("/tables/booking")
    public String bookingTablesPage(Model model) {
        model.addAttribute("title", "Đặt bàn - Restaurant Manager");
        model.addAttribute("activeTab", "booking");
        return "sales/tables";
    }

    // Items routes
    @GetMapping("/items")
    public String itemsPage(Model model) {
        model.addAttribute("title", "Thực đơn - Restaurant Manager");
        model.addAttribute("activeTab", "food");
        return "sales/items";
    }

    @GetMapping("/items/food")
    public String foodItemsPage(Model model) {
        model.addAttribute("title", "Món ăn - Restaurant Manager");
        model.addAttribute("activeTab", "food");
        return "sales/items";
    }

    @GetMapping("/items/drink")
    public String drinkItemsPage(Model model) {
        model.addAttribute("title", "Nước uống - Restaurant Manager");
        model.addAttribute("activeTab", "drink");
        return "sales/items";
    }

    @GetMapping("/items/other")
    public String otherItemsPage(Model model) {
        model.addAttribute("title", "Món khác - Restaurant Manager");
        model.addAttribute("activeTab", "other");
        return "sales/items";
    }
} 