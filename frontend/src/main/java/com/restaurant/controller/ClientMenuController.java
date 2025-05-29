package com.restaurant.controller;

import com.restaurant.dto.MenuItem;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/menu-client")
public class ClientMenuController {

    @GetMapping
    public String showMenu(Model model) {
        model.addAttribute("title", "Menu - G15 Kitchen");

        // Mock data
        List<MenuItem> menuItems = Arrays.asList(
                new MenuItem(1L, "Sliced Tomato With Green Vegetables", "Tomato, carrots, yellow pepper, brown sauce", 9.99, "Appetizer", "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8Zm9vZHxlbnwwfHwwfHx8MA%3D%3D", false),
                new MenuItem(2L, "Avocado Salad", "Egg, avocado, pepper, nuts", 5.99, "Salad", "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8Zm9vZHxlbnwwfHwwfHx8MA%3D%3D", false),
                new MenuItem(3L, "Caesar Salad", "Romaine lettuce, chicken, tomato, cucumber", 9.99, "Salad", "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8Zm9vZHxlbnwwfHwwfHx8MA%3D%3D", false),
                new MenuItem(4L, "Grilled Salmon", "Salmon fillet with vegetables", 18.50, "Main dish", "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8Zm9vZHxlbnwwfHwwfHx8MA%3D%3D", false),
                new MenuItem(5L, "Chocolate Lava Cake", "Warm chocolate cake with molten center", 7.00, "Dessert", "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8Zm9vZHxlbnwwfHwwfHx8MA%3D%3D", false),
                new MenuItem(6L, "Fresh Orange Juice", "Freshly squeezed orange juice", 4.00, "Drink", "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8Zm9vZHxlbnwwfHwwfHx8MA%3D%3D", false)
        );

        model.addAttribute("menuItems", menuItems);

        return "menu-client"; // Refers to menu-client.html
    }
} 