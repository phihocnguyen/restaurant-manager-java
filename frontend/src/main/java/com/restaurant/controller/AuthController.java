package com.restaurant.controller;

import com.restaurant.dto.TableDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
public class AuthController {

    @Autowired
    private RestTemplate restTemplate;

    private final String API_URL = "http://localhost:8080"; // Adjust if your backend is on a different port

    @GetMapping("/")
    public String indexPage(Model model) {
        model.addAttribute("title", "Trang chủ - Restaurant Manager");

        // Fetch tables for the map
        try {
            ResponseEntity<List<TableDto>> response = restTemplate.exchange(
                    API_URL + "/tables",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<TableDto>>() {}
            );
            model.addAttribute("tables", response.getBody());
        } catch (Exception e) {
            // Handle error fetching tables, maybe add an empty list or error message
            model.addAttribute("tables", List.of()); // Add empty list on error
            System.err.println("Error fetching tables: " + e.getMessage());
        }

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