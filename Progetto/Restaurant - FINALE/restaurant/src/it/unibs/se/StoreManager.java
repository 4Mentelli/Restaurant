package it.unibs.se;

import it.unibs.fp.mylib.InputDati;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.unibs.se.Xml.*;

public class StoreManager {

    public static void initialize() {


        int option;

        do {
            List<Ingredient> stock_list = createStockList();
            List<Ingredient> storage = storageReader();

            System.out.println("\n" + LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)).toUpperCase());
            System.out.println("1 - Visualizza registro");
            System.out.println("2 - Aggiungi merci al registro");
            System.out.println("3 - Crea lista della spesa");
            System.out.println("0 - Menu principale");

            option = InputDati.leggiIntero("Scegli: ", 0, 3);
            System.out.println();

            switch (option) {
                //Visualizza registro
                case 1 -> {
                    for (Ingredient ingredient : storage)
                        ingredient.printStock();
                }
                //Aggiungi merci al registro
                case 2 -> {

                    for (Ingredient ingredient : storage)
                        ingredient.printStock();

                    int option2 = 0;
                    if (!storage.isEmpty()) {
                        System.out.println("0 - Aggiungi nuovo ingrediente");
                        option2 = InputDati.leggiIntero("\nScegli: ", 0, storage.get(storage.size() - 1).getId());
                    }

                    if (option2 == 0) {
                        int id = 1;
                        if (!storage.isEmpty()) {
                            id = storage.get(storage.size() - 1).getId() + 1;
                        }
                        try {
                            storageUpdater(id, storage);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        try {
                            attributeUpdater(option2);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                //Crea lista della spesa
                case 3 -> {
                    //Prende tutte le prenotazioni della settimana
                    List<Reservation> reservations = reservationReader();
                    List<Ingredient> ingredients = new ArrayList<>();
                    int total_people = 0;

                    for (Reservation reservation : reservations) {
                        //Per ogni prenotazione controlla se è della settimana corrente e aggiunge gli ingredienti alla lista
                        if (reservation.getDate().isInCurrentWeek()) {
                            ingredients.addAll(reservation.getIngredients());
                            total_people += reservation.getNumber_of_people();
                        }
                    }

                    //Per ogni ingrediente controlla se è presente nel registro in quantità sufficiente e crea la lista della spesa
                    List<Ingredient> shopping_list = createShoppingList(ingredients, storage);

                    //Per ogni cliente controlla se ci sono almeno sufficienti drinks per tipo e sufficienti extras per tipo a testa

                    int drinks_needed = total_people * Manager.getDrinks();
                    List<Ingredient> drinks = xmlReader(true);
                    for (Ingredient drink : drinks) {
                        if (drink.getQuantity_in_stock() < drinks_needed) {
                            shopping_list.add(new Ingredient(drink.getName(), drinks_needed - drink.getQuantity_in_stock(), drink.getUnit()));
                        }
                    }
                    int extras_needed = total_people * Manager.getExtras();
                    List<Ingredient> extras = xmlReader(false);
                    for (Ingredient extra : extras) {
                        if (extra.getQuantity_in_stock() < extras_needed) {
                            shopping_list.add(new Ingredient(extra.getName(), extras_needed - extra.getQuantity_in_stock(), extra.getUnit()));
                        }
                    }


                    //Stampa la lista della spesa
                    System.out.println("\n\nLista della spesa:");
                    for (int i = 0; i < shopping_list.size(); i++) {
                        System.out.println(i + 1 + " - " + shopping_list.get(i).toString());
                    }
                }
            }


        } while (option != 0);
    }

    private static List<Ingredient> createStockList() {
        List<Ingredient> stock_list = new ArrayList<>();
        for (Recipe recipe : recipeReader()) {
            for (Ingredient ingredient : recipe.getIngredients()) {
                if (!ingredient.isIn(stock_list)) {
                    stock_list.add(ingredient);
                }
            }
        }
        return stock_list;
    }

    private static List<Ingredient> createShoppingList(List<Ingredient> ingredients, List<Ingredient> storage) {

        // Unifica gli ingredienti uguali
        Map<String, Ingredient> ingredientMap = new HashMap<>();
        for (Ingredient ingredient : ingredients) {
            String ingredientName = ingredient.getName().toUpperCase();
            if (ingredientMap.containsKey(ingredientName)) {
                Ingredient existingIngredient = ingredientMap.get(ingredientName);
                existingIngredient.setQuantity(existingIngredient.getQuantity() + ingredient.getQuantity());
            } else {
                ingredientMap.put(ingredientName, ingredient);
            }
        }
        List<Ingredient> uniqueIngredients = new ArrayList<>(ingredientMap.values());


        // Popola la lista della spesa
        List<Ingredient> shopping_list = new ArrayList<>();

        for (Ingredient ingredient : uniqueIngredients) {
            boolean found = false;
            for (Ingredient stock : storage) {
                if (ingredient.getName().equals(stock.getName())) { // confronto il nome dell'ingrediente
                    found = true;
                    if (ingredient.getQuantity() > stock.getQuantity_in_stock()) { // se non c'è abbastanza quantità in magazzino
                        //System.out.println("Aggiunto " + (ingredient.getQuantity() - stock.getQuantity_in_stock()) + " " + ingredient.getUnit() + " di " + ingredient.getName());
                        shopping_list.add(new Ingredient(ingredient.getName(), ingredient.getQuantity() - stock.getQuantity_in_stock(), ingredient.getUnit()));
                    }
                    break;
                }
            }
            if (!found) { // se l'ingrediente non è stato trovato in magazzino
                shopping_list.add(ingredient);
            }
        }

        return shopping_list;
    }
}
