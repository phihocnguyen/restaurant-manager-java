package com.restaurant.controller;

import com.restaurant.dto.TableDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Controller
@RequestMapping("/manager")
public class TableController {

    @Autowired
    private RestTemplate restTemplate;

    private final String API_URL = "http://localhost:8080";

    @GetMapping("/tables")
    public String tables(Model model) {
        model.addAttribute("title", "Quản lý Bàn - Restaurant Manager");
        model.addAttribute("activeTab", "tables");
        // Fetch tables from backend API
        ResponseEntity<List<TableDto>> response = restTemplate.exchange(
                API_URL + "/tables",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<TableDto>>() {}
        );
        model.addAttribute("tables", response.getBody());
        return "manager/tables"; // Tên file html trong thư mục templates/manager
    }

    // API endpoint to get all tables (for JS)
    @GetMapping("/api/tables")
    @ResponseBody
    public ResponseEntity<List<TableDto>> getAllTablesApi() {
        ResponseEntity<List<TableDto>> response = restTemplate.exchange(
                API_URL + "/tables",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<TableDto>>() {}
        );
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

    // API endpoint to get a table by ID (for JS)
    @GetMapping("/api/tables/{id}")
    @ResponseBody
    public ResponseEntity<TableDto> getTableByIdApi(@PathVariable Integer id) {
        ResponseEntity<TableDto> response = restTemplate.getForEntity(
                API_URL + "/tables/" + id,
                TableDto.class
        );
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

    // API endpoint to create a new table (for JS)
    @PostMapping("/api/tables")
    @ResponseBody
    public ResponseEntity<TableDto> createTableApi(@RequestBody TableDto tableDto) {
        ResponseEntity<TableDto> response = restTemplate.postForEntity(
                API_URL + "/tables",
                tableDto,
                TableDto.class
        );
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

    // API endpoint to update a table (for JS)
    @PutMapping("/api/tables/{id}")
    @ResponseBody
    public ResponseEntity<TableDto> updateTableApi(@PathVariable Integer id, @RequestBody TableDto tableDto) {
        String url = UriComponentsBuilder.fromUriString(API_URL + "/tables/{id}")
                .buildAndExpand(id)
                .toUriString();

        ResponseEntity<TableDto> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                new org.springframework.http.HttpEntity<>(tableDto),
                TableDto.class
        );
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

    // API endpoint to delete a table (for JS)
    @DeleteMapping("/api/tables/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteTableApi(@PathVariable Integer id) {
        restTemplate.delete(API_URL + "/tables/{id}", id);
        return ResponseEntity.noContent().build();
    }
} 