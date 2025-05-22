package it.unibs.se.model;

public class IngredientFactory {

    public static IdentifiableIngredient createDrink(String name, float quantity_in_stock, int id) {
        return new IdentifiableIngredient(new GenericIngredient(name, quantity_in_stock, "bottiglie"), id);
    }

    public static IdentifiableIngredient createExtra(String name, float quantity_in_stock, int id) {
        return new IdentifiableIngredient(new GenericIngredient(name, quantity_in_stock, "unità"), id);
    }
}
