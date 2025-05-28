package com.restaurant.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @GetMapping("/")
    public String indexPage(Model model) {
        model.addAttribute("title", "Trang chủ - Restaurant Manager");
        return "index";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("title", "Đăng nhập - Restaurant Manager");
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, 
                       @RequestParam String password,
                       @RequestParam(required = false) String rememberMe) {
        // TODO: Implement login logic
        return "redirect:/manager/menu";
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordPage(Model model) {
        model.addAttribute("title", "Quên mật khẩu - Restaurant Manager");
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam String email) {
        // TODO: Implement forgot password logic
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("title", "Đăng ký - Restaurant Manager");
        return "register";
    }
} 