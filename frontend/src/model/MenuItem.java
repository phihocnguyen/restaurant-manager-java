package model;

public class MenuItem {
    private int id;
    private String itemType;
    private String itemName;
    private String itemImg;
    private double itemCprice;
    private double itemSprice;
    private double instock;
    private boolean isdeleted;

    // Constructor, getter, setter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemImg() {
        return itemImg;
    }

    public void setItemImg(String itemImg) {
        this.itemImg = itemImg;
    }

    public double getItemCprice() {
        return itemCprice;
    }

    public void setItemCprice(double itemCprice) {
        this.itemCprice = itemCprice;
    }

    public double getItemSprice() {
        return itemSprice;
    }

    public void setItemSprice(double itemSprice) {
        this.itemSprice = itemSprice;
    }

    public double getInstock() {
        return instock;
    }

    public void setInstock(double instock) {
        this.instock = instock;
    }

    public boolean isIsdeleted() {
        return isdeleted;
    }

    public void setIsdeleted(boolean isdeleted) {
        this.isdeleted = isdeleted;
    }
    
}
