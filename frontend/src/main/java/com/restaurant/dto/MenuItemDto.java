package com.restaurant.dto;

public class MenuItemDto {
    private Integer id;
    private String itemType;
    private String itemName;
    private String itemImg;
    private Double itemSprice;

    public MenuItemDto() {}

    public MenuItemDto(Integer id, String itemType, String itemName, String itemImg, Double itemSprice) {
        this.id = id;
        this.itemType = itemType;
        this.itemName = itemName;
        this.itemImg = itemImg;
        this.itemSprice = itemSprice;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getItemType() { return itemType; }
    public void setItemType(String itemType) { this.itemType = itemType; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public String getItemImg() { return itemImg; }
    public void setItemImg(String itemImg) { this.itemImg = itemImg; }

    public Double getItemSprice() { return itemSprice; }
    public void setItemSprice(Double itemSprice) { this.itemSprice = itemSprice; }
} 