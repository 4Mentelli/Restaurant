package it.unibs.se;

import java.util.List;
import java.util.Locale;

public class Ingredient {

    private String name;
    private String unit_of_measure;
    private float quantity_in_stock;
    private float quantity;
    private int id;

    public Ingredient(String nome, float quantity, String unit_of_measure) {
        this.name = nome;
        this.quantity = quantity;
        this.unit_of_measure = unit_of_measure;
    }

    public Ingredient(String nome, float quantity, int unit_of_measure) {
        this.name = nome;
        this.quantity = quantity;
        this.unit_of_measure = unitIntToString(unit_of_measure);
    }

    //Drink, extras and storage constructor
    public Ingredient(String name, float quantity, String unit_of_measure, int id) {
        this.name = name;
        this.quantity_in_stock = quantity;
        this.unit_of_measure = unit_of_measure;
        this.id = id;
    }

    public Ingredient() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public float getQuantity_in_stock() { return quantity_in_stock; }

    public void setQuantity_in_stock(float quantity_in_stock) { this.quantity_in_stock = quantity_in_stock; }

    public String getUnit() {
        return unit_of_measure;
    }

    public String toString() {
        return this.name + " " + this.quantity + " " + this.unit_of_measure;
    }

    public String unitIntToString(int unit) {
        if (unit == 1) {
            return "g";
        } else if (unit == 2) {
            return "ml";
        } else return "pezzi";
    }

    public void printDrinksExtras() {
        System.out.println(this.id + " - " + this.getName() + " " + this.getQuantity_in_stock() + " pezzi");
    }

    public void printStock() {
        System.out.println(this.id + " - " + this.name + " " + this.quantity_in_stock + " " + this.unit_of_measure);
    }

    public boolean isIn(List<Ingredient> ingredients) {
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getName().toUpperCase(Locale.ROOT).equals(this.name.toUpperCase(Locale.ROOT))) {
                return true;
            }
        }
        return false;
    }
}

