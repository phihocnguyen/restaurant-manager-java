package com.restaurant.backend.domains.dto.Receipt;

import com.restaurant.backend.domains.dto.Customer.CustomerDto;
import com.restaurant.backend.domains.dto.DiningTable.DiningTableDto;
import com.restaurant.backend.domains.dto.Employee.EmployeeDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReceiptDto {
    private Integer id;
    private String tableId;
    private Double recPay;
    private String paymentMethod;
    private List<ReceiptItemDto> items;
    private Boolean isdeleted;
    private Instant recTime;
    private CustomerDto customer;
    private EmployeeDto employee;
    private DiningTableDto diningTable;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReceiptItemDto {
        private Integer id;
        private String itemId;
        private String itemName;
        private Integer quantity;
        private Double price;
        private Double totalPrice;
    }
}
