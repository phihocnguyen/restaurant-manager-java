package model;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Ingredient {
    private int id;
    private StringProperty ingreName = new SimpleStringProperty();
    private double instockKg;
    private double ingrePrice;
    private boolean isdeleted;

    public Ingredient() {}

    public Ingredient(int id, String ingreName, double instockKg, double ingrePrice, boolean isdeleted) {
        this.id = id;
        this.ingreName.set(ingreName);
        this.instockKg = instockKg;
        this.ingrePrice = ingrePrice;
        this.isdeleted = isdeleted;
    }

    public String getIngreName() {
        return ingreName.get();
    }

    public void setIngreName(String ingreName) {
        this.ingreName.set(ingreName);
    }

    public StringProperty ingreNameProperty() {
        return ingreName;
    }

    // Các getter/setter khác như cũ
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public double getInstockKg() { return instockKg; }
    public void setInstockKg(double instockKg) { this.instockKg = instockKg; }

    public double getIngrePrice() { return ingrePrice; }
    public void setIngrePrice(double ingrePrice) { this.ingrePrice = ingrePrice; }

    public boolean isIsdeleted() { return isdeleted; }
    public void setIsdeleted(boolean isdeleted) { this.isdeleted = isdeleted; }

    @Override
    public String toString() {
        return getIngreName();
    }
}
