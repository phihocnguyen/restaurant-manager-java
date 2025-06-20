package com.restaurant.backend.services.impl;

import com.restaurant.backend.domains.dto.Receipt.ReceiptDto;
import com.restaurant.backend.domains.dto.Receipt.dto.CreateReceiptDto;
import com.restaurant.backend.domains.dto.Receipt.dto.ReceiptHistoryDto;
import com.restaurant.backend.domains.entities.*;
import com.restaurant.backend.domains.dto.DiningTable.dto.CreateDiningTableDto;
import com.restaurant.backend.domains.dto.DiningTable.DiningTableDto;
import com.restaurant.backend.domains.dto.DiningTable.enums.TableStatus;
import com.restaurant.backend.mappers.impl.DiningTableMapper;
import com.restaurant.backend.mappers.impl.ReceiptMapper;
import com.restaurant.backend.repositories.ReceiptRepository;
import com.restaurant.backend.repositories.ReceiptDetailRepository;
import com.restaurant.backend.repositories.RecipeRepository;
import com.restaurant.backend.repositories.IngredientRepository;
import com.restaurant.backend.services.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.restaurant.backend.domains.dto.DiningTable.enums.TableStatus;
import com.restaurant.backend.domains.dto.DiningTable.dto.CreateDiningTableDto;
import com.restaurant.backend.repositories.ReceiptDetailRepository;
import com.restaurant.backend.domains.dto.DiningTable.DiningTableDto;
import com.restaurant.backend.domains.dto.Receipt.dto.ReceiptHistoryDto;
import com.restaurant.backend.other_services.EmailService;
import java.text.NumberFormat;
import java.util.Locale;

@Service
public class ReceiptServiceImpl implements ReceiptService {
    private final ReceiptRepository receiptRepository;
    private final ReceiptMapper receiptMapper;
    private final EmployeeService employeeService;
    private final CustomerService customerService;
    private final DiningTableService diningTableService;
    private final DiningTableMapper diningTableMapper;
    private final ReceiptDetailService receiptDetailService;
    private final ReceiptDetailRepository receiptDetailRepository;
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final EmailService emailService;

    public ReceiptServiceImpl(ReceiptRepository receiptRepository, ReceiptMapper receiptMapper,
                              EmployeeService employeeService, CustomerService customerService,
                              DiningTableService diningTableService, DiningTableMapper diningTableMapper,
                              ReceiptDetailService receiptDetailService, ReceiptDetailRepository receiptDetailRepository,
                              RecipeRepository recipeRepository, IngredientRepository ingredientRepository,
                              EmailService emailService) {
        this.receiptRepository = receiptRepository;
        this.receiptMapper = receiptMapper;
        this.employeeService = employeeService;
        this.customerService = customerService;
        this.diningTableService = diningTableService;
        this.diningTableMapper = diningTableMapper;
        this.receiptDetailService = receiptDetailService;
        this.receiptDetailRepository = receiptDetailRepository;
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.emailService = emailService;
    }

    @Override
    public Receipt save(Receipt receipt) {
        return receiptRepository.save(receipt);
    }

    @Override
    public List<Receipt> findAll() {
        return receiptRepository.findAll().stream()
                .filter(receipt -> !Boolean.TRUE.equals(receipt.getIsdeleted()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Receipt> findById(int id) {
        return receiptRepository.findById(id);
    }

    @Override
    public ReceiptDto create(CreateReceiptDto dto) {
        // Check if table is available
        DiningTableDto tableDto = diningTableService.getTableById(dto.getTabId());
        if (tableDto.getTabStatus() == TableStatus.EMPTY) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bàn chưa được phục vụ");
        }
        DiningTable table = diningTableService.findById(dto.getTabId());
        Receipt receipt = receiptMapper.mapTo(dto);
        if (dto.getEmpId() != null) {
            Employee emp = employeeService.findById(dto.getEmpId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
            receipt.setEmp(emp);
        }
        if (dto.getCusId() != null) {
            Customer cus = customerService.findById(dto.getCusId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
            receipt.setCus(cus);
        }
        receipt.setTab(table);
        receipt.setRecTime(dto.getRecTime());
        receipt.setIsdeleted(dto.getIsdeleted() != null ? dto.getIsdeleted() : false);
        
        // Save receipt first to get ID
        receipt = receiptRepository.save(receipt);
        
        // Get all receipt details and process ingredient quantities
        List<ReceiptDetail> details = receiptDetailRepository.findAllByRecId(receipt.getId());
        for (ReceiptDetail detail : details) {
            MenuItem menuItem = detail.getItem();
            if (menuItem.getItemType() == ItemType.FOOD) {
                // Get recipes for this menu item
                List<Recipe> recipes = recipeRepository.findAllByItemId(menuItem.getId());
                
                // For each recipe, decrease the ingredient quantity
                for (Recipe recipe : recipes) {
                    double requiredQuantity = recipe.getIngreQuantityKg() * detail.getQuantity();
                    Ingredient ingredient = recipe.getIngre();
                    
                    if (ingredient.getInstockKg() < requiredQuantity) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                            "Not enough ingredients in stock for " + menuItem.getItemName());
                    }
                    
                    ingredient.setInstockKg(ingredient.getInstockKg() - requiredQuantity);
                    ingredientRepository.save(ingredient);
                }
            }
        }

        // Calculate total amount from receipt details
        java.math.BigDecimal totalAmount = details.stream()
                .map(ReceiptDetail::getPrice)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
        receipt.setRecPay(totalAmount);
        
        // Update table status to EMPTY
        CreateDiningTableDto updateDto = CreateDiningTableDto.builder()
            .tabNum(table.getTabNum())
            .tabStatus(TableStatus.EMPTY)
            .isdeleted(table.getIsdeleted())
            .build();
        diningTableService.updateTable(table.getId(), updateDto);
        
        if (receipt.getCus() != null && receipt.getCus().getEmail() != null && !receipt.getCus().getEmail().isEmpty()) {
            String email = receipt.getCus().getEmail();
            String customerName = receipt.getCus().getName();
            // Lấy danh sách chi tiết hóa đơn
            List<ReceiptDetail> receiptDetails = receiptDetailRepository.findAllByRecId(receipt.getId());
            StringBuilder content = new StringBuilder();
            content.append("<div style='font-family:Inter,sans-serif;max-width:600px;margin:auto;background:#fff;border-radius:12px;box-shadow:0 2px 16px #0001;padding:32px;'>");
            content.append("<div style='text-align:center;margin-bottom:24px;'><h2 style='color:#D4AF37;margin:8px 0 0 0;font-family:Playfair Display,serif;'>G15 Kitchen</h2></div>");
            content.append("<h3 style='color:#222;text-align:center;margin-bottom:24px;'>Cảm ơn bạn đã dùng bữa tại <span style='color:#D4AF37;'>G15 Kitchen</span>!</h3>");
            content.append("<div style='font-size:1.1em;margin-bottom:16px;'>Khách hàng: <b>" + customerName + "</b></div>");
            content.append("<div style='overflow-x:auto;'><table style='width:100%;border-collapse:collapse;margin-bottom:24px;'>");
            content.append("<thead><tr style='background:#F9F6EF;color:#D4AF37;font-weight:bold;'><th style='padding:12px 8px;border-bottom:2px solid #D4AF37;'>Tên món</th><th style='padding:12px 8px;border-bottom:2px solid #D4AF37;'>Số lượng</th><th style='padding:12px 8px;border-bottom:2px solid #D4AF37;'>Đơn giá</th></tr></thead><tbody>");
            for (ReceiptDetail detail : receiptDetails) {
                MenuItem menuItem = detail.getItem();
                content.append("<tr style='border-bottom:1px solid #eee;'>");
                content.append("<td style='padding:10px 8px;'>").append(menuItem.getItemName()).append("</td>");
                content.append("<td style='padding:10px 8px;text-align:center;'>").append(detail.getQuantity()).append("</td>");
                content.append("<td style='padding:10px 8px;text-align:right;color:#D4AF37;'>").append(formatCurrency(detail.getPrice())).append("</td>");
                content.append("</tr>");
            }
            content.append("</tbody></table></div>");
            content.append("<div style='text-align:right;font-size:1.1em;margin-bottom:24px;'><b>Tổng tiền: <span style='color:#D4AF37;'>").append(formatCurrency(receipt.getRecPay())).append("</span></b></div>");
            content.append("<div style='background:#F9F6EF;padding:16px 20px;border-radius:8px;text-align:center;color:#444;'>Chúng tôi hy vọng bạn đã có trải nghiệm tuyệt vời.<br>Cảm ơn bạn đã tin tưởng <b style='color:#D4AF37;'>G15 Kitchen</b>!</div>");
            content.append("<div style='margin-top:32px;text-align:center;color:#aaa;font-size:0.95em;'>Nếu có thắc mắc, vui lòng liên hệ: <a href='mailto:info@g15kitchen.com' style='color:#D4AF37;text-decoration:none;'>info@g15kitchen.com</a></div>");
            content.append("</div>");
            emailService.sendReceiptEmail(email, "Cảm ơn bạn đã dùng bữa tại G15 Kitchen", content.toString());
        }
        
        return receiptMapper.mapFrom(receiptRepository.save(receipt));
    }

    @Override
    public ReceiptDto update(int id, CreateReceiptDto dto) {
        Optional<Receipt> db = findById(id);
        if (db.isEmpty() || Boolean.TRUE.equals(db.get().getIsdeleted())) 
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        Receipt receipt = receiptMapper.mapTo(dto);
        receipt.setId(id);
        receipt.setEmp(employeeService.findById(dto.getEmpId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST)));
        receipt.setCus(customerService.findById(dto.getCusId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST)));
        receipt.setTab(diningTableService.findById(dto.getTabId()));
        // Calculate total amount
        List<ReceiptDetail> details = receiptDetailRepository.findAllByRecId(id);
        java.math.BigDecimal totalAmount = details.stream()
                .map(ReceiptDetail::getPrice)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
        receipt.setRecPay(totalAmount);
        return receiptMapper.mapFrom(receiptRepository.save(receipt));
    }

    @Override
    public ReceiptDto partialUpdate(int id, CreateReceiptDto dto) {
        Receipt receipt = findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        if (receipt.getIsdeleted()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        if (dto.getEmpId() != null) {
            Employee emp = employeeService.findById(dto.getEmpId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
            receipt.setEmp(emp);
        }
        if (dto.getCusId() != null) {
            Customer cus = customerService.findById(dto.getCusId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
            receipt.setCus(cus);
        }
        if (dto.getTabId() != null) {
            DiningTable tab = diningTableService.findById(dto.getTabId());
            receipt.setTab(tab);
        }
        if (dto.getRecTime() != null) {
            receipt.setRecTime(dto.getRecTime());
        }
        if (dto.getIsdeleted() != null) {
            receipt.setIsdeleted(dto.getIsdeleted());
        }
        // Calculate total amount
        List<ReceiptDetail> details = receiptDetailRepository.findAllByRecId(id);
        java.math.BigDecimal totalAmount = details.stream()
                .map(ReceiptDetail::getPrice)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
        receipt.setRecPay(totalAmount);
        return receiptMapper.mapFrom(receiptRepository.save(receipt));
    }

    @Override
    public boolean softDelete(int id) {
        Optional<Receipt> db = receiptRepository.findById(id);
        if (db.isEmpty() || Boolean.TRUE.equals(db.get().getIsdeleted())) return false;

        db.get().setIsdeleted(true);
        receiptRepository.save(db.get());
        return true;
    }

    @Override
    public List<ReceiptHistoryDto> getAllReceipts() {
        return receiptRepository.findAll().stream()
                .filter(receipt -> !Boolean.TRUE.equals(receipt.getIsdeleted()))
                .map(receiptMapper::mapToHistoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public ReceiptDto getReceiptById(int id) {
        return this.receiptMapper.mapFrom(receiptRepository.findById(id).orElse(null));
    }

    // Helper method for formatting currency
    private String formatCurrency(Number amount) {
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        return formatter.format(amount) + " VND";
    }
}