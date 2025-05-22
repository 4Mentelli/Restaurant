package it.unibs.se.model;

import java.util.List;

public interface StorageReader {
    int[] parametersReader();
    List<Recipe> recipesReader();
    List<IdentifiableIngredient> drinksReader();
    List<IdentifiableIngredient> extrasReader();
    List<Menu> themedMenusReader();
    List<Reservation> reservationsReader();
    List<GenericIngredient> storageReader();
}
