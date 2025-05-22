package it.unibs.se.model;

public interface StorageUpdater {
    void storageWriter(GenericIngredient ingredient, int id) throws Exception;
    void updateIngredientQuantity(String type, int id, float increment) throws Exception;
}
