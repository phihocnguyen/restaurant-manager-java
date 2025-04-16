package model;

public class Menu {
    private String name;
    private double price;
    private String type; // "FOOD", "DRINK", "OTHER"
    private String img;

    public Menu(String name, double price, String type, String img) {
        this.name = name;
        this.price = price;
        this.type = type;
        this.img = img;
    }

    // Getters
    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }

    public String getImg() {
        return img;
    }

    // Setters
    public void setTen(String name) {
        this.name = name;
    }

    public void setGia(double price) {
        this.price = price;
    }

    public void setLoai(String type) {
        this.type = type;
    }

    public void setHinhAnh(String img) {
        this.img = img;
    }
}


