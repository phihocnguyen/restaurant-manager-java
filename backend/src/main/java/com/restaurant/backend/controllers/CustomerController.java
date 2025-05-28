package com.restaurant.backend.controllers;

import com.restaurant.backend.domains.dto.Customer.CustomerDto;
import com.restaurant.backend.domains.dto.Customer.CreateCustomerDto;
import com.restaurant.backend.services.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        List<CustomerDto> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable Integer id) {
        CustomerDto customer = customerService.getCustomerById(id);
        if (customer != null) {
            return ResponseEntity.ok(customer);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<CustomerDto> createCustomer(@Valid @RequestBody CreateCustomerDto createCustomerDto) {
        // Check if CCCD, email, or phone already exists
        if (customerService.existsByCccd(createCustomerDto.getCccd())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(null);
        }
        if (customerService.existsByEmail(createCustomerDto.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(null);
        }
        if (customerService.existsByPhone(createCustomerDto.getPhone())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(null);
        }
        // Đảm bảo luôn set isdeleted = false khi tạo mới (handled in service)
        CustomerDto createdCustomer = customerService.createCustomer(createCustomerDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> updateCustomer(
            @PathVariable Integer id,
            @Valid @RequestBody CustomerDto customerDto) {
        // Kiểm tra customer có tồn tại không
        CustomerDto existingCustomer = customerService.getCustomerById(id);
        if (existingCustomer == null) {
            return ResponseEntity.notFound().build();
        }

        // Giữ nguyên giá trị isdeleted cũ nếu không được truyền lên (handled in service)
        CustomerDto updatedCustomer = customerService.updateCustomer(id, customerDto);
        if (updatedCustomer != null) {
            return ResponseEntity.ok(updatedCustomer);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Integer id) {
        // Soft delete: set isdeleted = true
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}
