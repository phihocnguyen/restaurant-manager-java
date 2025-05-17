package model;

import javafx.beans.property.*;

import java.util.List;

public class Recipe {
    private final ObjectProperty<Ingredient> ingredient = new SimpleObjectProperty<>();
    private final DoubleProperty quantityKg = new SimpleDoubleProperty();

    public Recipe(Ingredient ingredient, double quantityKg) {
        this.ingredient.set(ingredient);
        this.quantityKg.set(quantityKg);
    }

    public ObjectProperty<Ingredient> ingredientProperty() {
        return ingredient;
    }

    public Ingredient getIngredient() {
        return ingredient.get();
    }

    public DoubleProperty ingreQuantityKgProperty() {
        return quantityKg;
    }

    public double getQuantityKg() {
        return quantityKg.get();
    }

    // Nested classes for RecipeData and MenuItem (as referenced)
    public static class RecipeData {
        private List<IngredientData> ingredients;

        public List<IngredientData> getIngredients() {
            return ingredients;
        }

        public void setIngredients(List<IngredientData> ingredients) {
            this.ingredients = ingredients;
        }
    }

    public static class IngredientData {
        private int id;
        private double quantity;

        public IngredientData(int id, double quantity) {
            this.id = id;
            this.quantity = quantity;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public double getQuantity() {
            return quantity;
        }

        public void setQuantity(double quantity) {
            this.quantity = quantity;
        }
    }

    public static class MenuItem {
        private String type;
        private String name;
        private String image;
        private double costPrice;
        private double sellingPrice;
        private int stockQuantity;
        private boolean isDeleted;

        public MenuItem(String type, String name, String image, double costPrice,
                        double sellingPrice, int stockQuantity, boolean isDeleted) {
            this.type = type;
            this.name = name;
            this.image = image;
            this.costPrice = costPrice;
            this.sellingPrice = sellingPrice;
            this.stockQuantity = stockQuantity;
            this.isDeleted = isDeleted;
        }

        // Getters and setters
        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public double getCostPrice() {
            return costPrice;
        }

        public void setCostPrice(double costPrice) {
            this.costPrice = costPrice;
        }

        public double getSellingPrice() {
            return sellingPrice;
        }

        public void setSellingPrice(double sellingPrice) {
            this.sellingPrice = sellingPrice;
        }

        public int getStockQuantity() {
            return stockQuantity;
        }

        public void setStockQuantity(int stockQuantity) {
            this.stockQuantity = stockQuantity;
        }

        public boolean isDeleted() {
            return isDeleted;
        }

        public void setDeleted(boolean deleted) {
            isDeleted = deleted;
        }
    }
}