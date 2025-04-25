package com.restaurant.backend.controllers;

import com.restaurant.backend.domains.dto.Receipt.dto.CreateReceiptDto;
import com.restaurant.backend.domains.dto.ReceiptDetail.ReceiptDetailDto;
import com.restaurant.backend.domains.dto.ReceiptDetail.dto.CreateManyReceiptDetailsDto;
import com.restaurant.backend.domains.dto.ReceiptDetail.dto.CreateReceiptDetailDto;
import com.restaurant.backend.domains.dto.ReceiptDetail.dto.CreateReceiptWithManyReceiptDetailsDto;
import com.restaurant.backend.domains.dto.ReceiptDetail.dto.CreateReceiptWithOneReceiptDetail;
import com.restaurant.backend.domains.entities.*;
import com.restaurant.backend.mappers.impl.DiningTableMapper;
import com.restaurant.backend.mappers.impl.ReceiptDetailMapper;
import com.restaurant.backend.mappers.impl.ReceiptMapper;
import com.restaurant.backend.services.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class ReceiptDetailController {
    private final ReceiptDetailService receiptDetailService;
    private final ReceiptDetailMapper receiptDetailMapper;
    private final ReceiptService receiptService;
    private final ReceiptMapper receiptMapper;
    private final CustomerService customerService;
    private final EmployeeService employeeService;
    private final DiningTableService diningTableService;
    private final MenuItemService menuItemService;
    private final DiningTableMapper diningTableMapper;
    public ReceiptDetailController(ReceiptDetailService receiptDetailService, ReceiptMapper receiptMapper,
                                   ReceiptDetailMapper receiptDetailMapper, ReceiptService receiptService,
                                   CustomerService customerService, EmployeeService employeeService,
                                   DiningTableMapper diningTableMapper,
                                   DiningTableService diningTableService, MenuItemService menuItemService) {
        this.receiptDetailService = receiptDetailService;
        this.receiptDetailMapper = receiptDetailMapper;
        this.receiptService = receiptService;
        this.receiptMapper = receiptMapper;
        this.customerService = customerService;
        this.employeeService = employeeService;
        this.diningTableService = diningTableService;
        this.menuItemService = menuItemService;
        this.diningTableMapper = diningTableMapper;
    }

    private BigDecimal getRecPay(Integer recId){
        List<ReceiptDetail> details = this.receiptDetailService.findAllByRecId(recId);
        BigDecimal total = BigDecimal.ZERO;
        for (ReceiptDetail detail : details) {
            total = total.add(detail.getPrice());
        }
        return total;
    }

    @PostMapping(path="/receipt-details/one")
    public ResponseEntity<ReceiptDetailDto> addOneReceiptDetail(@RequestBody CreateReceiptWithOneReceiptDetail body){
        CreateReceiptDetailDto detail = body.getDetail();
        CreateReceiptDto receipt = body.getReceipt();

        Optional<MenuItem> dbMenuItem = this.menuItemService.findById(detail.getItemId());
        if(!dbMenuItem.isPresent() || dbMenuItem.get().getIsdeleted()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // create a brand new receipt
        Receipt newReceipt = this.receiptMapper.mapTo(receipt);

        if(receipt.getEmpId() != null){
            Optional<Employee> foundEmp = this.employeeService.findById(receipt.getEmpId());
            if(!foundEmp.isPresent()){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            newReceipt.setEmp(foundEmp.get());
        }
        if(receipt.getIsdeleted() != null){
            newReceipt.setIsdeleted(receipt.getIsdeleted());
        }
        if(receipt.getCusId() != null){
            Optional<Customer> foundCus = this.customerService.findById(receipt.getCusId());
            if(!foundCus.isPresent()){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            newReceipt.setCus(foundCus.get());
        }
        if(receipt.getTabId() != null){
            Optional<DiningTable> foundTab = Optional.of(this.diningTableMapper.mapTo(this.diningTableService.findById(receipt.getTabId())));
            if(!foundTab.isPresent()){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            newReceipt.setTab(foundTab.get());
        }
        if(receipt.getRecTime() != null){
            newReceipt.setRecTime(receipt.getRecTime());
        }
        Receipt savedReceipt = this.receiptService.save(newReceipt);


        if(dbMenuItem.get().getInstock() < detail.getQuantity()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough instock!");
        }

        // minus instock for menu
        dbMenuItem.get().setInstock(dbMenuItem.get().getInstock() - detail.getQuantity());
        this.menuItemService.save(dbMenuItem.get());


        ReceiptDetail receiptDetail = ReceiptDetail.builder()
                .id(new ReceiptDetailId(newReceipt.getId(), detail.getItemId()))
                .price(dbMenuItem.get().getItemSprice().multiply(BigDecimal.valueOf(detail.getQuantity()))) // !
                .quantity(detail.getQuantity())
                .item(dbMenuItem.get())
                .rec(savedReceipt)
                .build();

        ReceiptDetail savedReceiptDetail = this.receiptDetailService.save(receiptDetail);

        // update rec pay
        savedReceipt.setRecPay(getRecPay(savedReceipt.getId()));
        this.receiptService.save(savedReceipt);

        // if food returns ReceiptDetailDto for chefs
        if(receiptDetail.getItem().getItemType() == ItemType.FOOD){
            return new ResponseEntity<>(this.receiptDetailMapper.mapFrom(receiptDetail), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(path="/receipt-details/many")
    public ResponseEntity<List<ReceiptDetailDto>> addManyReceiptDetails(@RequestBody CreateReceiptWithManyReceiptDetailsDto body){
        CreateManyReceiptDetailsDto details = body.getDetails();
        CreateReceiptDto receipt = body.getReceipt();

        // create a brand new receipt
        Receipt newReceipt = this.receiptMapper.mapTo(receipt);

        if(receipt.getEmpId() != null){
            Optional<Employee> foundEmp = this.employeeService.findById(receipt.getEmpId());
            if(!foundEmp.isPresent()){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            newReceipt.setEmp(foundEmp.get());
        }
        if(receipt.getIsdeleted() != null){
            newReceipt.setIsdeleted(receipt.getIsdeleted());
        }
        if(receipt.getCusId() != null){
            Optional<Customer> foundCus = this.customerService.findById(receipt.getCusId());
            if(!foundCus.isPresent()){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            newReceipt.setCus(foundCus.get());
        }
        if(receipt.getTabId() != null){
            Optional<DiningTable> foundTab = Optional.of(this.diningTableMapper.mapTo(this.diningTableService.findById(receipt.getTabId())));

            if(!foundTab.isPresent()){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            newReceipt.setTab(foundTab.get());
        }
        if(receipt.getRecTime() != null){
            newReceipt.setRecTime(receipt.getRecTime());
        }
        Receipt savedReceipt = this.receiptService.save(newReceipt);

        List<ReceiptDetail> sendToChefList = new ArrayList<>();

        List<ReceiptDetail> receiptDetails = details.getDetails().stream().map(detailDto -> {
            Optional<MenuItem> dbMenuItem = this.menuItemService.findById(detailDto.getItemId());
            if(!dbMenuItem.isPresent() || dbMenuItem.get().getIsdeleted()){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
            if(dbMenuItem.get().getInstock() < detailDto.getQuantity()){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough instock!");
            }

            // minus instock for menu
            dbMenuItem.get().setInstock(dbMenuItem.get().getInstock() - detailDto.getQuantity());
            this.menuItemService.save(dbMenuItem.get());
             ReceiptDetail returnReceiptDetail = ReceiptDetail.builder()
                    .rec(savedReceipt)
                    .item(dbMenuItem.get())
                    .price(dbMenuItem.get().getItemSprice().multiply(BigDecimal.valueOf(detailDto.getQuantity())))
                    .quantity(detailDto.getQuantity())
                    .id(new ReceiptDetailId(savedReceipt.getId(), detailDto.getItemId()))
                    .build();

             // add to notify chefs list
            if(dbMenuItem.get().getItemType() == ItemType.FOOD){
                sendToChefList.add(returnReceiptDetail);
            }

            return returnReceiptDetail;
        }).toList();

        List<ReceiptDetail> savedReceiptDetails = this.receiptDetailService.saveAll(receiptDetails);
        // update rec pay
        savedReceipt.setRecPay(getRecPay(savedReceipt.getId()));
        this.receiptService.save(savedReceipt);

        if (sendToChefList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(sendToChefList.stream().map(this.receiptDetailMapper::mapFrom).collect(Collectors.toList()), HttpStatus.CREATED);
    }

    @PostMapping(path="/receipt-details/one/{recId}")
    public ResponseEntity<ReceiptDetailDto> addOneReceiptDetailToReceipt(@PathVariable Integer recId, @RequestBody CreateReceiptDetailDto createReceiptDetailDto){
        Optional<Receipt> dbReceipt = this.receiptService.findById(recId);
        if(!dbReceipt.isPresent() || dbReceipt.get().getIsdeleted()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<MenuItem> dbMenuItem = this.menuItemService.findById(createReceiptDetailDto.getItemId());
        if(!dbMenuItem.isPresent() || dbMenuItem.get().getIsdeleted()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<ReceiptDetail> dbReceiptDetail = this.receiptDetailService.findById(new ReceiptDetailId(dbReceipt.get().getId(), dbMenuItem.get().getId()));

        if(dbMenuItem.get().getInstock() < createReceiptDetailDto.getQuantity()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough instock!");
        }

        // minus instock for menu
        dbMenuItem.get().setInstock(dbMenuItem.get().getInstock() - createReceiptDetailDto.getQuantity());
        this.menuItemService.save(dbMenuItem.get());



        // temp receipt detail
        ReceiptDetail receiptDetail = null;
        // if existed plus in
        if(dbReceiptDetail.isPresent()){
            receiptDetail = ReceiptDetail.builder()
                    .rec(dbReceipt.get())
                    .item(dbMenuItem.get())
                    .id(new ReceiptDetailId(dbReceipt.get().getId(), dbMenuItem.get().getId()))
                    .price(dbMenuItem.get().getItemSprice().multiply(BigDecimal.valueOf(createReceiptDetailDto.getQuantity() + dbReceiptDetail.get().getQuantity())))
                    .quantity(createReceiptDetailDto.getQuantity() + dbReceiptDetail.get().getQuantity())
                    .build();
        }
        else receiptDetail = ReceiptDetail.builder()
                .rec(dbReceipt.get())
                .item(dbMenuItem.get())
                .id(new ReceiptDetailId(dbReceipt.get().getId(), dbMenuItem.get().getId()))
                .price(dbMenuItem.get().getItemSprice().multiply(BigDecimal.valueOf(createReceiptDetailDto.getQuantity())))
                .quantity(createReceiptDetailDto.getQuantity())
                .build();
        ReceiptDetail savedReceiptDetail = this.receiptDetailService.save(receiptDetail);

        // update rec pay
        dbReceipt.get().setRecPay(getRecPay(dbReceipt.get().getId()));
        this.receiptService.save(dbReceipt.get());

        // check if add one is food?
        if (savedReceiptDetail.getItem().getItemType() == ItemType.FOOD) {
            savedReceiptDetail.setQuantity(createReceiptDetailDto.getQuantity());
        }

        return new ResponseEntity<>(this.receiptDetailMapper.mapFrom(savedReceiptDetail), HttpStatus.CREATED);
    }

    @PostMapping(path="/receipt-details/many/{recId}")
    public ResponseEntity<List<ReceiptDetailDto>> addManyReceiptDetailsToReceipt(@PathVariable Integer recId, @RequestBody CreateManyReceiptDetailsDto createReceiptDetailDtos){
        Optional<Receipt> dbReceipt = this.receiptService.findById(recId);
        if(!dbReceipt.isPresent() || dbReceipt.get().getIsdeleted()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<ReceiptDetail> sendToChefList = new ArrayList<>();

        List<ReceiptDetail> receiptDetails = createReceiptDetailDtos.getDetails().stream().map(detailDto -> {
            Optional<MenuItem> dbMenuItem = this.menuItemService.findById(detailDto.getItemId());
            if(!dbMenuItem.isPresent() || dbMenuItem.get().getIsdeleted()){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }

            if(dbMenuItem.get().getInstock() < detailDto.getQuantity()){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough instock!");
            }

            // minus instock for menu
            dbMenuItem.get().setInstock(dbMenuItem.get().getInstock() - detailDto.getQuantity());
            this.menuItemService.save(dbMenuItem.get());

            Optional<ReceiptDetail> dbReceiptDetail = this.receiptDetailService.findById(new ReceiptDetailId(dbReceipt.get().getId(), dbMenuItem.get().getId()));

            ReceiptDetail returnedReceiptDetail = null;
            // if existed plus in
            if(dbReceiptDetail.isPresent()){
                returnedReceiptDetail = ReceiptDetail.builder()
                        .item(dbMenuItem.get())
                        .rec(dbReceipt.get())
                        .id(new ReceiptDetailId(dbReceipt.get().getId(), dbMenuItem.get().getId()))
                        .price(dbMenuItem.get().getItemSprice().multiply(BigDecimal.valueOf(detailDto.getQuantity() + dbReceiptDetail.get().getQuantity())))
                        .quantity(detailDto.getQuantity() + dbReceiptDetail.get().getQuantity())
                        .build();
            }
            else returnedReceiptDetail = ReceiptDetail.builder()
                    .item(dbMenuItem.get())
                    .rec(dbReceipt.get())
                    .id(new ReceiptDetailId(dbReceipt.get().getId(), dbMenuItem.get().getId()))
                    .price(dbMenuItem.get().getItemSprice().multiply(BigDecimal.valueOf(detailDto.getQuantity())))
                    .quantity(detailDto.getQuantity())
                    .build();

            if(returnedReceiptDetail.getItem().getItemType() == ItemType.FOOD){
                sendToChefList.add(returnedReceiptDetail);
            }

            return returnedReceiptDetail;
        }).toList();
        List<ReceiptDetail> savedReceiptDetails = this.receiptDetailService.saveAll(receiptDetails);

        // update rec pay
        dbReceipt.get().setRecPay(getRecPay(dbReceipt.get().getId()));
        this.receiptService.save(dbReceipt.get());

        if(sendToChefList.isEmpty()){
            return new ResponseEntity<>(HttpStatus.CREATED);
        }

        return new ResponseEntity<>(sendToChefList.stream().map(receiptDetailMapper::mapFrom).collect(Collectors.toList()), HttpStatus.CREATED);
    }

    @GetMapping(path="/receipt-details/{recId}")
    public ResponseEntity<List<ReceiptDetailDto>> getAllReceiptDetails(@PathVariable Integer recId){
        Optional<Receipt> dbReceipt = this.receiptService.findById(recId);
        if(!dbReceipt.isPresent() || dbReceipt.get().getIsdeleted()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<ReceiptDetail> dbReceiptDetails = this.receiptDetailService.findAllByRecId(recId);
        return new ResponseEntity<>(dbReceiptDetails.stream().map(receiptDetailMapper::mapFrom).collect(Collectors.toList()), HttpStatus.OK);
    }

    @PutMapping(path="/receipt-details/{recId}")
    public ResponseEntity<List<ReceiptDetailDto>> updateAllReceiptDetails(@PathVariable Integer recId, @RequestBody CreateManyReceiptDetailsDto createReceiptDetailDtos){
        Optional<Receipt> dbReceipt = this.receiptService.findById(recId);
        if(!dbReceipt.isPresent() || dbReceipt.get().getIsdeleted()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        // delete the old ones first
        List<ReceiptDetail> oldReceiptDetails = this.receiptDetailService.findAllByRecId(recId);
        for(ReceiptDetail oldReceiptDetail : oldReceiptDetails){
            Optional<MenuItem> oldMenuItem = this.menuItemService.findById(oldReceiptDetail.getItem().getId());
            if(!oldMenuItem.isPresent() || oldMenuItem.get().getIsdeleted()){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            oldMenuItem.get().setInstock(oldMenuItem.get().getInstock() + oldReceiptDetail.getQuantity());
            this.menuItemService.save(oldMenuItem.get());
        }
        this.receiptDetailService.deleteAll(oldReceiptDetails);

        List<ReceiptDetail> sendToChefList = new ArrayList<>();

        List<ReceiptDetail> receiptDetails = createReceiptDetailDtos.getDetails().stream().map(detailDto -> {
            Optional<MenuItem> dbMenuItem = this.menuItemService.findById(detailDto.getItemId());
            if(!dbMenuItem.isPresent() || dbMenuItem.get().getIsdeleted()){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
            if(dbMenuItem.get().getInstock() < detailDto.getQuantity()){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough instock!");
            }

            // minus instock for menu
            dbMenuItem.get().setInstock(dbMenuItem.get().getInstock() - detailDto.getQuantity());
            this.menuItemService.save(dbMenuItem.get());
            ReceiptDetail returnedReceiptDetail = ReceiptDetail.builder()
                    .rec(dbReceipt.get())
                    .item(dbMenuItem.get())
                    .price(dbMenuItem.get().getItemSprice().multiply(BigDecimal.valueOf(detailDto.getQuantity())))
                    .quantity(detailDto.getQuantity())
                    .id(new ReceiptDetailId(dbReceipt.get().getId(), detailDto.getItemId()))
                    .build();

            // add to send to chef
            if(returnedReceiptDetail.getItem().getItemType() == ItemType.FOOD){
                sendToChefList.add(returnedReceiptDetail);
            }

            return returnedReceiptDetail;
        }).toList();

        List<ReceiptDetail> savedReceiptDetails = this.receiptDetailService.saveAll(receiptDetails);

        // update rec pay
        dbReceipt.get().setRecPay(getRecPay(dbReceipt.get().getId()));
        this.receiptService.save(dbReceipt.get());

        if(sendToChefList.isEmpty()){
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(sendToChefList.stream().map(this.receiptDetailMapper::mapFrom).collect(Collectors.toList()), HttpStatus.CREATED);
    }

    @PatchMapping(path="receipt-details/{recId}/{itemId}")
    public ResponseEntity<ReceiptDetailDto> updateOneReceiptDetail(@PathVariable Integer recId, @PathVariable Integer itemId, @RequestBody CreateReceiptDetailDto createReceiptDetailDtos){
        Optional<Receipt> dbReceipt = this.receiptService.findById(recId);
        if(!dbReceipt.isPresent() || dbReceipt.get().getIsdeleted()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<MenuItem> dbMenuItem = this.menuItemService.findById(itemId);
        if(!dbMenuItem.isPresent() || dbMenuItem.get().getIsdeleted()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }



        Optional<ReceiptDetail> dbReceiptDetail = this.receiptDetailService.findById(new ReceiptDetailId(dbReceipt.get().getId(), dbMenuItem.get().getId()));
        if(!dbReceiptDetail.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // plus back
        dbMenuItem.get().setInstock(dbMenuItem.get().getInstock() + dbReceiptDetail.get().getQuantity());
        // check
        if(dbMenuItem.get().getInstock() < createReceiptDetailDtos.getQuantity()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough instock!");
        }

        if(createReceiptDetailDtos.getQuantity()!=null){
            // update receipt details
            dbReceiptDetail.get().setQuantity(createReceiptDetailDtos.getQuantity());
            dbReceiptDetail.get().setPrice(dbMenuItem.get().getItemSprice().multiply(BigDecimal.valueOf(createReceiptDetailDtos.getQuantity())));

        }
        ReceiptDetail savedReceiptDetail = this.receiptDetailService.save(dbReceiptDetail.get());
        // update rec pay
        dbReceipt.get().setRecPay(getRecPay(dbReceipt.get().getId()));
        this.receiptService.save(dbReceipt.get());
        // update menu item (minus)
        dbMenuItem.get().setInstock(dbMenuItem.get().getInstock() - createReceiptDetailDtos.getQuantity());
        this.menuItemService.save(dbMenuItem.get());

        // if food? notify chef
        if(savedReceiptDetail.getItem().getItemType() == ItemType.FOOD){
            savedReceiptDetail.setQuantity(createReceiptDetailDtos.getQuantity());
        }

        return new ResponseEntity<>(this.receiptDetailMapper.mapFrom(savedReceiptDetail), HttpStatus.OK);
    }

    @DeleteMapping(path="receipt-details/{recId}/{itemId}")
    public ResponseEntity<ReceiptDetailDto> deleteOneReceiptDetail(@PathVariable Integer recId, @PathVariable Integer itemId){
        Optional<Receipt> dbReceipt = this.receiptService.findById(recId);
        if(!dbReceipt.isPresent() || dbReceipt.get().getIsdeleted()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<MenuItem> dbMenuItem = this.menuItemService.findById(itemId);
        if(!dbMenuItem.isPresent() || dbMenuItem.get().getIsdeleted()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<ReceiptDetail> foundReceiptDetail = this.receiptDetailService.findById(new ReceiptDetailId(dbReceipt.get().getId(), dbMenuItem.get().getId()));
        if (!foundReceiptDetail.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        this.receiptDetailService.deleteById(foundReceiptDetail.get().getId());
        // plus back
        dbMenuItem.get().setInstock(dbMenuItem.get().getInstock() + foundReceiptDetail.get().getQuantity());
        this.menuItemService.save(dbMenuItem.get());

        // update rec pay
        dbReceipt.get().setRecPay(getRecPay(dbReceipt.get().getId()));
        this.receiptService.save(dbReceipt.get());
        if (foundReceiptDetail.get().getItem().getItemType() == ItemType.FOOD) {
            return new ResponseEntity<>(receiptDetailMapper.mapFrom(foundReceiptDetail.get()) ,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path="receipt-details/{recId}")
    public ResponseEntity<List<ReceiptDetailDto>> deleteManyReceiptDetails(@PathVariable Integer recId){
        Optional<Receipt> dbReceipt = this.receiptService.findById(recId);
        if(!dbReceipt.isPresent() || dbReceipt.get().getIsdeleted()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<ReceiptDetail> sendToChefList = new ArrayList<>();

        List<ReceiptDetail> allReceiptDetails = this.receiptDetailService.findAllByRecId(recId);
        for(ReceiptDetail receiptDetail : allReceiptDetails){
            Optional<MenuItem> dbMenuItem = this.menuItemService.findById(receiptDetail.getItem().getId());
            MenuItem item = dbMenuItem.orElse(null);
            if(item == null|| Boolean.TRUE.equals(item.getIsdeleted())){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            dbMenuItem.get().setInstock(dbMenuItem.get().getInstock() + receiptDetail.getQuantity());
            this.menuItemService.save(dbMenuItem.get());

            if(receiptDetail.getItem().getItemType() == ItemType.FOOD){
                sendToChefList.add(receiptDetail);
            }
        }
        this.receiptDetailService.deleteAll(allReceiptDetails);


        // update rec pay
        dbReceipt.get().setRecPay(getRecPay(dbReceipt.get().getId()));
        this.receiptService.save(dbReceipt.get());

        if(sendToChefList.isEmpty()){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else return new ResponseEntity<>(sendToChefList.stream().map(this.receiptDetailMapper::mapFrom).collect(Collectors.toList()), HttpStatus.OK);
    }
}


// get money for each item from menu item then multiply it to get price for receipt detail
// if existed plus in