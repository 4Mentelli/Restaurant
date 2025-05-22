package it.unibs.se.model;

public class GenericIngredient implements Ingredient {

    private final String name;
    private final String unit_of_measure;
    private float quantity;

    public GenericIngredient(String name, float quantity, String unit_of_measure) {
        this.name = name;
        this.quantity = quantity;
        this.unit_of_measure = unit_of_measure;
    }

    public String getName() {
        return name;
    }

    public String getUnitOfMeasure() {
        return unit_of_measure;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

}
