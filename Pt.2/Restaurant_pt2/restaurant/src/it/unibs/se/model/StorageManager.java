package it.unibs.se.model;

import it.unibs.se.exception.ConvertException;

import java.util.*;

public class StorageManager {

    private final StorageReader reader;
    private final StorageUpdater updater;

    public StorageManager(StorageReader reader, StorageUpdater updater) {
        this.reader = reader;
        this.updater = updater;
    }

    public List<GenericIngredient> getStorage() {
        return reader.storageReader();
    }

    public List<IdentifiableIngredient> getDrinks() {
        return reader.drinksReader();
    }

    public List<IdentifiableIngredient> getExtras() {
        return reader.extrasReader();
    }

    public <T extends Ingredient> int getLastId(List<T> list) {
        if (list.isEmpty()) {
            return 0;
        }
        return list.size();
    }

    public void addNewIngredient(GenericIngredient ingredient) {
        try {
            updater.storageWriter(ingredient, getLastId(getStorage()) + 1);
        } catch (Exception e) {
            throw new ConvertException("Errore nell'aggiunta del prodotto", e);
        }
    }

    public void updateProduct(String type, int id, float increment) {
        try {
            updater.updateIngredientQuantity(type, id, increment);
        } catch (Exception e) {
            throw new ConvertException("Errore nell'aggiornamento della quantità", e);
        }
    }

    public List<GenericIngredient> makeShoppingList() {
        ShoppingListCalculator calculator = new ShoppingListCalculator(reader);
        return calculator.calculateShoppingList();
    }

}
