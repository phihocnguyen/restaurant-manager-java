package com.restaurant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

@Controller
@RequestMapping("/menu-client")
public class ClientMenuController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping
    public String showMenu(Model model) {
        model.addAttribute("title", "Menu - G15 Kitchen");

        try {
            // Fetch menu items from backend API
            String url = "http://localhost:8080/items";
            ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);
            List<Map<String, Object>> menuItems = response.getBody();
            model.addAttribute("menuItems", menuItems);
        } catch (Exception e) {
            // Handle error by setting empty list
            model.addAttribute("menuItems", new ArrayList<>());
            model.addAttribute("error", "Failed to load menu items. Please try again later.");
            System.err.println("Error fetching menu items: " + e.getMessage());
        }

        return "menu-client";
    }
} 