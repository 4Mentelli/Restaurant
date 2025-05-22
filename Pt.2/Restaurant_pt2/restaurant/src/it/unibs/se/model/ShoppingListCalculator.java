package it.unibs.se.model;

import java.util.*;

public class ShoppingListCalculator {

    private final StorageReader reader;

    public ShoppingListCalculator(StorageReader reader) {
        this.reader = reader;
    }

    public List<GenericIngredient> calculateShoppingList() {

        List<GenericIngredient> all_ingredients = new ArrayList<>();
        int total_people = 0;
        for (Reservation reservation : reader.reservationsReader()) {
            if (reservation.getDate().isInCurrentWeek()) {
                all_ingredients.addAll(calculateReservationIngredients(reservation));
                total_people += reservation.getNumber_of_people();
            }
        }

        List<GenericIngredient> shopping_list = calculateUniqueIngredients(all_ingredients, reader.storageReader());
        shopping_list.addAll(calculateDrinks(total_people));
        shopping_list.addAll(calculateExtras(total_people));

        return shopping_list;
    }

    private List<GenericIngredient> calculateUniqueIngredients(List<GenericIngredient> ingredients, List<GenericIngredient> storage) {

        Map<String, GenericIngredient> ingredientMap = new HashMap<>();
        for (GenericIngredient ingredient : ingredients) {
            String ingredientName = ingredient.getName().toUpperCase(Locale.ROOT);
            if (ingredientMap.containsKey(ingredientName)) {
                GenericIngredient existingIngredient = ingredientMap.get(ingredientName);
                existingIngredient.setQuantity(existingIngredient.getQuantity() + ingredient.getQuantity());
            } else {
                ingredientMap.put(ingredientName, ingredient);
            }
        }
        List<GenericIngredient> uniqueIngredients = new ArrayList<>(ingredientMap.values());

        List<GenericIngredient> shopping_list = new ArrayList<>();
        for (GenericIngredient ingredient : uniqueIngredients) {
            boolean found = false;
            for (GenericIngredient stock : storage) {
                if (ingredient.getName().equalsIgnoreCase(stock.getName())) {
                    found = true;
                    if (ingredient.getQuantity() > stock.getQuantity()) {
                        shopping_list.add(new GenericIngredient(ingredient.getName(), ingredient.getQuantity() - stock.getQuantity(), ingredient.getUnitOfMeasure()));
                    }
                    break;
                }
            }
            if (!found)
                shopping_list.add(ingredient);
        }

        return shopping_list;
    }

    private List<GenericIngredient> calculateDrinks(int total_people) {
        List<GenericIngredient> shopping_list = new ArrayList<>();
        int drinks_needed = total_people * reader.parametersReader()[2];
        List<IdentifiableIngredient> drinks = reader.drinksReader();
        for (IdentifiableIngredient drink : drinks) {
            if (drink.getQuantity() < drinks_needed) {
                shopping_list.add(new GenericIngredient(drink.getName(), drinks_needed - drink.getQuantity(), drink.getUnitOfMeasure()));
            }
        }
        return shopping_list;
    }

    private List<GenericIngredient> calculateExtras(int total_people) {
        List<GenericIngredient> shopping_list = new ArrayList<>();
        int extras_needed = total_people * reader.parametersReader()[3];
        List<IdentifiableIngredient> extras = reader.extrasReader();
        for (IdentifiableIngredient extra : extras) {
            if (extra.getQuantity() < extras_needed) {
                shopping_list.add(new GenericIngredient(extra.getName(), extras_needed - extra.getQuantity(), extra.getUnitOfMeasure()));
            }
        }
        return shopping_list;
    }

    private List<GenericIngredient> calculateReservationIngredients(Reservation reservation) {

        List<GenericIngredient> ingredients = new ArrayList<>();

        if (reservation.getOrderedMenus() != null) {

            for (int i = 0; i < reservation.getOrderedMenus().size(); i++) {

                int menu_id = reservation.getOrderedMenus().get(i).menu_id();
                int menu_quantity = reservation.getOrderedMenus().get(i).quantity();

                for (Menu menu : reader.themedMenusReader())
                    if (menu.getId() == menu_id)
                        for (Recipe r : menu.getDishes())
                            for (GenericIngredient ingredient : r.getIngredients())
                                ingredients.add(new GenericIngredient(ingredient.getName(), ingredient.getQuantity() * menu_quantity, ingredient.getUnitOfMeasure()));

            }
        }


        if (reservation.getOrderedDishes() != null) {

            for (int i = 0; i < reservation.getOrderedDishes().size(); i++) {

                List<Integer> dish_ids = reservation.getOrderedDishes().get(i).getDish_ids();
                int quantity = reservation.getOrderedDishes().get(i).getQuantity();

                for (Integer dishId : dish_ids)
                    for (Recipe recipe : reader.recipesReader())
                        if (recipe.getId() == dishId)
                            for (GenericIngredient ingredient : recipe.getIngredients())
                                ingredients.add(new GenericIngredient(ingredient.getName(), ingredient.getQuantity() * quantity, ingredient.getUnitOfMeasure()));
            }
        }
        return ingredients;
    }

}
