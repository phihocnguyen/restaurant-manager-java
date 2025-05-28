package com.restaurant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/manager")
public class CustomerController {

    @Autowired
    private RestTemplate restTemplate;
    private final String API_URL = "http://localhost:8080";

    @GetMapping("/customers")
    public String customers(Model model) {
        model.addAttribute("title", "Quản lý Khách hàng - Restaurant Manager");
        model.addAttribute("activeTab", "customers");
        Object customers = restTemplate.getForObject(API_URL + "/customers", Object.class);
        model.addAttribute("customers", customers);
        return "manager/customers";
    }

    // API endpoints
    @GetMapping("/api/customers")
    @ResponseBody
    public Object getAllCustomers() {
        return restTemplate.getForObject(API_URL + "/customers", Object.class);
    }

    @GetMapping("/api/customers/{id}")
    @ResponseBody
    public Object getCustomer(@PathVariable Long id) {
        return restTemplate.getForObject(API_URL + "/customers/" + id, Object.class);
    }

    @PostMapping("/api/customers")
    @ResponseBody
    public Object createCustomer(@RequestBody Object customer) {
        return restTemplate.postForObject(API_URL + "/customers", customer, Object.class);
    }

    @PutMapping("/api/customers/{id}")
    @ResponseBody
    public Object updateCustomer(@PathVariable Long id, @RequestBody Object customer) {
        restTemplate.put(API_URL + "/customers/" + id, customer);
        return customer;
    }

    @DeleteMapping("/api/customers/{id}")
    @ResponseBody
    public void deleteCustomer(@PathVariable Long id) {
        restTemplate.delete(API_URL + "/customers/" + id);
    }
} 