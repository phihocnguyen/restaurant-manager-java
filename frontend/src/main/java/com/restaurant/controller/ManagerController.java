package com.restaurant.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/manager")
public class ManagerController {

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("title", "Dashboard - Restaurant Manager");
        model.addAttribute("activeTab", "dashboard");
        return "manager/dashboard";
    }

    @GetMapping("/menu")
    public String menu(Model model) {
        model.addAttribute("title", "Quản lý Menu - Restaurant Manager");
        model.addAttribute("activeTab", "menu");
        return "manager/menu";
    }

    @GetMapping("/tables")
    public String tables(Model model) {
        model.addAttribute("title", "Quản lý Bàn - Restaurant Manager");
        model.addAttribute("activeTab", "tables");
        return "manager/tables";
    }

    @GetMapping("/customers")
    public String customers(Model model) {
        model.addAttribute("title", "Quản lý Khách hàng - Restaurant Manager");
        model.addAttribute("activeTab", "customers");
        return "manager/customers";
    }

    @GetMapping("/reports")
    public String reports(Model model) {
        model.addAttribute("title", "Báo cáo - Restaurant Manager");
        model.addAttribute("activeTab", "reports");
        return "manager/reports";
    }

    @GetMapping("/orders")
    public String orders(Model model) {
        model.addAttribute("title", "Quản lý Đơn hàng - Restaurant Manager");
        model.addAttribute("activeTab", "orders");
        return "manager/orders";
    }

    @GetMapping("/settings")
    public String settings(Model model) {
        model.addAttribute("title", "Cài đặt - Restaurant Manager");
        model.addAttribute("activeTab", "settings");
        return "manager/settings";
    }
} 