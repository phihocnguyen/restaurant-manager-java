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
import org.springframework.http.HttpStatus;

// Import frontend DTOs
import com.restaurant.dto.LoginRequest;
import com.restaurant.dto.AccountResponse;

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
        return "auth/login";
    }

    @GetMapping("/profile")
    public String profilePage(Model model) {
        model.addAttribute("title", "Hồ Sơ Của Tôi - G15 Kitchen");
        return "auth/profile";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                       @RequestParam String password,
                       @RequestParam(required = false) String rememberMe,
                       Model model) { // Add Model to pass errors

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setAccUsername(username);
        loginRequest.setAccPassword(password);

        try {
            // Expect frontend AccountResponse DTO
            ResponseEntity<AccountResponse> response = restTemplate.postForEntity(
                    API_URL + "/login",
                    loginRequest,
                    AccountResponse.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                AccountResponse accountResponse = response.getBody();
                if (accountResponse != null && accountResponse.getRole() != null && accountResponse.getRole().getRoleName() != null) {
                    // Redirect based on role name from AccountRoleResponse
                    switch (accountResponse.getRole().getRoleName()) {
                        case "admin":
                            return "redirect:/manager/dashboard";
                        case "customer":
                            return "redirect:/";
                        default:
                            // Default redirect for other roles
                            return "redirect:/sales/items";
                    }
                } else {
                    // Handle unexpected successful response body
                    model.addAttribute("loginError", "Đăng nhập thất bại: Dữ liệu tài khoản không hợp lệ.");
                    return "auth/login";
                }
            } else if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                model.addAttribute("loginError", "Sai tài khoản hoặc mật khẩu.");
                return "auth/login";
            } else {
                 model.addAttribute("loginError", "Đăng nhập thất bại: Lỗi từ server.");
                 return "auth/login";
            }

        } catch (Exception e) {
            // Handle connection errors or other exceptions
            System.err.println("Login API call error: " + e.getMessage());
            model.addAttribute("loginError", "Đăng nhập thất bại: Không thể kết nối đến server.");
            return "auth/login";
        }
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordPage(Model model) {
        model.addAttribute("title", "Quên mật khẩu - Restaurant Manager");
        return "auth/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam String email) {
        // TODO: Implement forgot password logic
        return "redirect:auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("title", "Đăng ký - Restaurant Manager");
        return "auth/register";
    }
} 