package it.unibs.se.model;

public class IdentifiableIngredient implements Ingredient, Identifiable {
    private final GenericIngredient ingredient;
    private int id;

    public IdentifiableIngredient(GenericIngredient ingredient, int id) {
        this.ingredient = ingredient;
        this.id = id;
    }

    public String getName() {
        return ingredient.getName();
    }

    public String getUnitOfMeasure() {
        return ingredient.getUnitOfMeasure();
    }

    public float getQuantity() {
        return ingredient.getQuantity();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
