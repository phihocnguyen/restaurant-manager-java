// NOTE: Controller now delegates all logic to the service layer

package com.restaurant.backend.services.impl;

import com.restaurant.backend.domains.dto.Receipt.dto.CreateReceiptDto;
import com.restaurant.backend.domains.dto.ReceiptDetail.ReceiptDetailDto;
import com.restaurant.backend.domains.dto.ReceiptDetail.dto.*;
import com.restaurant.backend.domains.entities.*;
import com.restaurant.backend.mappers.impl.ReceiptDetailMapper;
import com.restaurant.backend.mappers.impl.ReceiptMapper;
import com.restaurant.backend.repositories.ReceiptDetailRepository;
import com.restaurant.backend.repositories.RecipeRepository;
import com.restaurant.backend.repositories.IngredientRepository;
import com.restaurant.backend.services.*;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.context.annotation.Lazy;
import com.restaurant.backend.other_services.EmailService;
import java.text.NumberFormat;
import java.util.Locale;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReceiptDetailServiceImpl implements ReceiptDetailService {
    private final ReceiptDetailRepository receiptDetailRepository;
    private final @Lazy ReceiptService receiptService;
    private final ReceiptMapper receiptMapper;
    private final ReceiptDetailMapper receiptDetailMapper;
    private final EmployeeService employeeService;
    private final CustomerService customerService;
    private final DiningTableService diningTableService;
    private final MenuItemService menuItemService;
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final EmailService emailService;

    public ReceiptDetailServiceImpl(
            ReceiptDetailRepository receiptDetailRepository,
            @Lazy ReceiptService receiptService,
            ReceiptMapper receiptMapper,
            ReceiptDetailMapper receiptDetailMapper,
            EmployeeService employeeService,
            CustomerService customerService,
            DiningTableService diningTableService,
            MenuItemService menuItemService,
            RecipeRepository recipeRepository,
            IngredientRepository ingredientRepository,
            EmailService emailService) {
        this.receiptDetailRepository = receiptDetailRepository;
        this.receiptService = receiptService;
        this.receiptMapper = receiptMapper;
        this.receiptDetailMapper = receiptDetailMapper;
        this.employeeService = employeeService;
        this.customerService = customerService;
        this.diningTableService = diningTableService;
        this.menuItemService = menuItemService;
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.emailService = emailService;
    }

    private BigDecimal calculateTotalReceiptPay(Integer recId) {
        return receiptDetailRepository.findAllByRecId(recId).stream()
                .map(ReceiptDetail::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void updateMenuItemStock(MenuItem item, int delta) {
        item.setInstock(item.getInstock() + delta);
        menuItemService.saveEntity(item);
    }

    private void updateReceiptPayment(Receipt receipt) {
        receipt.setRecPay(calculateTotalReceiptPay(receipt.getId()));
        receiptService.save(receipt);
    }

    private Receipt buildAndSaveReceipt(CreateReceiptDto dto) {
        Receipt receipt = receiptMapper.mapTo(dto);
        Optional.ofNullable(dto.getEmpId())
                .map(employeeService::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .ifPresent(receipt::setEmp);

        Optional.ofNullable(dto.getCusId())
                .map(customerService::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .ifPresent(receipt::setCus);

        Optional.ofNullable(dto.getTabId())
                .map(diningTableService::findById)
                .map(Optional::ofNullable)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .ifPresent(receipt::setTab);

        Optional.ofNullable(dto.getIsdeleted()).ifPresent(receipt::setIsdeleted);
        Optional.ofNullable(dto.getRecTime()).ifPresent(receipt::setRecTime);
        Optional.ofNullable(dto.getPaymentMethod()).ifPresent(receipt::setPaymentMethod);

        return receiptService.save(receipt);
    }

    @Override
    public ReceiptDetail save(ReceiptDetail receiptDetail) {
        return this.receiptDetailRepository.save(receiptDetail);
    }

    @Override
    public ResponseEntity<ReceiptDetailDto> createReceiptWithOneDetail(CreateReceiptWithOneReceiptDetail body) {
        CreateReceiptDto receiptDto = body.getReceipt();
        CreateReceiptDetailDto detailDto = body.getDetail();
        Receipt receipt = buildAndSaveReceipt(receiptDto);

        MenuItem item = menuItemService.findById(detailDto.getItemId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        if (item.getInstock() < detailDto.getQuantity()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough instock!");

        updateMenuItemStock(item, -detailDto.getQuantity());

        ReceiptDetail detail = ReceiptDetail.builder()
                .id(new ReceiptDetailId(receipt.getId(), item.getId()))
                .item(item)
                .rec(receipt)
                .quantity(detailDto.getQuantity())
                .price(item.getItemSprice().multiply(BigDecimal.valueOf(detailDto.getQuantity())))
                .build();

        ReceiptDetail saved = receiptDetailRepository.save(detail);
        updateReceiptPayment(receipt);

        return item.getItemType() == ItemType.FOOD ?
                new ResponseEntity<>(receiptDetailMapper.mapFrom(saved), HttpStatus.CREATED) :
                new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<ReceiptDetailDto>> createReceiptWithManyDetails(CreateReceiptWithManyReceiptDetailsDto body) {
        CreateReceiptDto receiptDto = body.getReceipt();
        List<ReceiptDetailItem> detailsDto = body.getDetails();
        Receipt receipt = buildAndSaveReceipt(receiptDto);

        List<ReceiptDetail> notifyChef = new ArrayList<>();

        List<ReceiptDetail> details = detailsDto.stream().map(d -> {
            MenuItem item = menuItemService.findById(d.getItemId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
            if (item.getInstock() < d.getQuantity()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough instock!");
            updateMenuItemStock(item, -d.getQuantity());

            ReceiptDetail detail = ReceiptDetail.builder()
                    .id(new ReceiptDetailId(receipt.getId(), item.getId()))
                    .item(item)
                    .rec(receipt)
                    .quantity(d.getQuantity())
                    .price(item.getItemSprice().multiply(BigDecimal.valueOf(d.getQuantity())))
                    .build();
            if (item.getItemType() == ItemType.FOOD) notifyChef.add(detail);
            return detail;
        }).collect(Collectors.toList());

        receiptDetailRepository.saveAll(details);
        updateReceiptPayment(receipt);

        // Sau khi tạo hóa đơn và chi tiết, gửi email nếu có khách hàng và email
        if (receipt.getCus() != null && receipt.getCus().getEmail() != null && !receipt.getCus().getEmail().isEmpty()) {
            String email = receipt.getCus().getEmail();
            String customerName = receipt.getCus().getName();
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

        return notifyChef.isEmpty() ?
                new ResponseEntity<>(HttpStatus.CREATED) :
                new ResponseEntity<>(notifyChef.stream().map(receiptDetailMapper::mapFrom).toList(), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ReceiptDetailDto> addDetailToExistingReceipt(Integer recId, CreateReceiptDetailDto dto) {
        Receipt receipt = receiptService.findById(recId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        MenuItem item = menuItemService.findById(dto.getItemId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        if (item.getInstock() < dto.getQuantity()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough instock!");

        updateMenuItemStock(item, -dto.getQuantity());

        ReceiptDetailId id = new ReceiptDetailId(recId, item.getId());
        Optional<ReceiptDetail> existed = receiptDetailRepository.findById(id);
        int quantity = dto.getQuantity() + existed.map(ReceiptDetail::getQuantity).orElse(0);

        ReceiptDetail detail = ReceiptDetail.builder()
                .id(id)
                .item(item)
                .rec(receipt)
                .quantity(quantity)
                .price(item.getItemSprice().multiply(BigDecimal.valueOf(quantity)))
                .build();

        ReceiptDetail saved = receiptDetailRepository.save(detail);
        updateReceiptPayment(receipt);

        if (item.getItemType() == ItemType.FOOD) saved.setQuantity(dto.getQuantity());

        return new ResponseEntity<>(receiptDetailMapper.mapFrom(saved), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<ReceiptDetailDto>> addDetailsToExistingReceipt(Integer recId, CreateManyReceiptDetailsDto dtos) {
        Receipt receipt = receiptService.findById(recId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        List<ReceiptDetail> notifyChef = new ArrayList<>();

        List<ReceiptDetail> details = dtos.getDetails().stream().map(d -> {
            MenuItem item = menuItemService.findById(d.getItemId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
            if (item.getInstock() < d.getQuantity()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough instock!");

            updateMenuItemStock(item, -d.getQuantity());

            ReceiptDetailId id = new ReceiptDetailId(recId, item.getId());
            Optional<ReceiptDetail> existed = receiptDetailRepository.findById(id);
            int quantity = d.getQuantity() + existed.map(ReceiptDetail::getQuantity).orElse(0);

            ReceiptDetail detail = ReceiptDetail.builder()
                    .id(id)
                    .item(item)
                    .rec(receipt)
                    .quantity(quantity)
                    .price(item.getItemSprice().multiply(BigDecimal.valueOf(quantity)))
                    .build();
            if (item.getItemType() == ItemType.FOOD) notifyChef.add(detail);
            return detail;
        }).collect(Collectors.toList());

        receiptDetailRepository.saveAll(details);
        updateReceiptPayment(receipt);

        return notifyChef.isEmpty() ?
                new ResponseEntity<>(HttpStatus.CREATED) :
                new ResponseEntity<>(notifyChef.stream().map(receiptDetailMapper::mapFrom).toList(), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<ReceiptDetailDto>> getAllDetailsForReceipt(Integer recId) {
        Receipt receipt = receiptService.findById(recId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        return new ResponseEntity<>(receiptDetailRepository.findAllByRecId(recId).stream().map(receiptDetailMapper::mapFrom).toList(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ReceiptDetailDto> updateReceiptDetail(Integer recId, Integer itemId, CreateReceiptDetailDto dto) {
        Receipt receipt = receiptService.findById(recId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        MenuItem item = menuItemService.findById(itemId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        ReceiptDetailId id = new ReceiptDetailId(recId, itemId);
        ReceiptDetail detail = receiptDetailRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // plus back old quantity
        updateMenuItemStock(item, detail.getQuantity());
        if (item.getInstock() < dto.getQuantity()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough instock!");
        }

        detail.setQuantity(dto.getQuantity());
        detail.setPrice(item.getItemSprice().multiply(BigDecimal.valueOf(dto.getQuantity())));
        ReceiptDetail saved = receiptDetailRepository.save(detail);

        // minus new quantity
        updateMenuItemStock(item, -dto.getQuantity());
        updateReceiptPayment(receipt);

        // if food? notify chef
        if (item.getItemType() == ItemType.FOOD) {
            saved.setQuantity(dto.getQuantity());
        }

        return new ResponseEntity<>(receiptDetailMapper.mapFrom(saved), HttpStatus.OK);
    }


    @Override
    public ResponseEntity<List<ReceiptDetailDto>> updateAllReceiptDetails(Integer recId, CreateManyReceiptDetailsDto dtoList) {
        Receipt receipt = receiptService.findById(recId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        List<ReceiptDetail> oldDetails = receiptDetailRepository.findAllByRecId(recId);

        // restore stock before deleting
        for (ReceiptDetail old : oldDetails) {
            MenuItem item = menuItemService.findById(old.getItem().getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
            updateMenuItemStock(item, old.getQuantity());
        }
        receiptDetailRepository.deleteAll(oldDetails);

        List<ReceiptDetail> notifyChef = new ArrayList<>();

        List<ReceiptDetail> newDetails = dtoList.getDetails().stream().map(dto -> {
            MenuItem item = menuItemService.findById(dto.getItemId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
            if (item.getInstock() < dto.getQuantity()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough instock!");
            }
            updateMenuItemStock(item, -dto.getQuantity());

            ReceiptDetail detail = ReceiptDetail.builder()
                    .rec(receipt)
                    .item(item)
                    .price(item.getItemSprice().multiply(BigDecimal.valueOf(dto.getQuantity())))
                    .quantity(dto.getQuantity())
                    .id(new ReceiptDetailId(recId, item.getId()))
                    .build();

            if (item.getItemType() == ItemType.FOOD) notifyChef.add(detail);
            return detail;
        }).collect(Collectors.toList());

        List<ReceiptDetail> saved = receiptDetailRepository.saveAll(newDetails);
        updateReceiptPayment(receipt);

        return notifyChef.isEmpty() ?
                new ResponseEntity<>(HttpStatus.CREATED) :
                new ResponseEntity<>(notifyChef.stream().map(receiptDetailMapper::mapFrom).toList(), HttpStatus.CREATED);
    }

    @Override
    public Optional<ReceiptDetail> findById(ReceiptDetailId receiptDetailId) {
        return this.receiptDetailRepository.findById(receiptDetailId);
    }

    @Override
    public ResponseEntity<ReceiptDetailDto> deleteOneDetail(Integer recId, Integer itemId) {
        Receipt receipt = receiptService.findById(recId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        MenuItem item = menuItemService.findById(itemId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        ReceiptDetail detail = receiptDetailRepository.findById(new ReceiptDetailId(recId, itemId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        updateMenuItemStock(item, detail.getQuantity());
        receiptDetailRepository.deleteById(detail.getId());
        updateReceiptPayment(receipt);

        return item.getItemType() == ItemType.FOOD ?
                new ResponseEntity<>(receiptDetailMapper.mapFrom(detail), HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<ReceiptDetailDto>> deleteManyDetails(Integer recId) {
        Receipt receipt = receiptService.findById(recId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        List<ReceiptDetail> details = receiptDetailRepository.findAllByRecId(recId);

        List<ReceiptDetail> notifyChef = new ArrayList<>();
        for (ReceiptDetail detail : details) {
            MenuItem item = menuItemService.findById(detail.getItem().getId()).orElse(null);
            if (item == null || Boolean.TRUE.equals(item.getIsdeleted())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
            updateMenuItemStock(item, detail.getQuantity());
            if (item.getItemType() == ItemType.FOOD) notifyChef.add(detail);
        }
        receiptDetailRepository.deleteAll(details);
        updateReceiptPayment(receipt);

        return notifyChef.isEmpty() ?
                new ResponseEntity<>(HttpStatus.OK) :
                new ResponseEntity<>(notifyChef.stream().map(receiptDetailMapper::mapFrom).toList(), HttpStatus.OK);
    }

    // Helper method for formatting currency
    private String formatCurrency(Number amount) {
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        return formatter.format(amount) + " VND";
    }

}
