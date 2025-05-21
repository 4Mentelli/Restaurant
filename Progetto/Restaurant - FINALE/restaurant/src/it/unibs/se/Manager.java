package it.unibs.se;

import it.unibs.fp.mylib.InputDati;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

import static it.unibs.se.Xml.*;

public class Manager {

    public static int[] parameters = parametersReader();
    public static int n_client = parameters[0];
    public static int workload_for_client = parameters[1];
    public static int workload_for_restaurant = workload_for_client * (n_client + ((n_client * 20)/100));
    public static int drinks = parameters[2];
    public static int extras = parameters[3];
    public static int getN_client() {return n_client;}
    public static int getWorkload_for_client() {return workload_for_client;}
    public static int getWorkload_for_restaurant() {return workload_for_restaurant;}
    public static int getDrinks() {return drinks;}
    public static int getExtras() {return extras;}

    public static void initialize() throws Exception {
        if (n_client == 0) {
            System.out.println("\nBenvenuto! Prima di iniziare, inserisci i seguenti parametri del ristorante");
            n_client = InputDati.leggiInteroPositivo("Numero di posti a sedere: ");
            workload_for_client = InputDati.leggiInteroPositivo("Carico di lavoro per persona (un valore di circa 40 indica un carico di lavoro normale): ");
            drinks = InputDati.leggiInteroNonNegativo("Consumo pro capite di bevande: ");
            extras = InputDati.leggiInteroNonNegativo("Consumo pro capite di generi alimentari extra: ");
            workload_for_restaurant = workload_for_client * (n_client + ((n_client * 20)/100));

            parametersWriter(n_client, workload_for_client, drinks, extras);
        }

        int option;
        do {
            List<Recipe> recipes = recipeReader();
            List<Menu> tematic_menu_list = menuReader();
            List<Ingredient> drinks = xmlReader(true);
            List<Ingredient> extras = xmlReader(false);

            System.out.println("\n" + LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)).toUpperCase());
            System.out.println("1 - Gestisci Ricette");
            System.out.println("2 - Gestisci Bevande");
            System.out.println("3 - Gestisci Extra");
            System.out.println("4 - Gestisci Menù Tematici");
            System.out.println("5 - Visualizza proprietà Ristorante");
            System.out.println("0 - Menu principale");

            option = InputDati.leggiIntero("Scegli: ", 0, 5);
            System.out.print("\n");

            switch (option) {
                //Gestisci ricette
                case 1 -> {

                    System.out.println("1 - Visualizza ricette");
                    System.out.println("2 - Aggiungi ricetta");
                    System.out.println("3 - Rimuovi ricetta");
                    int option_1 = InputDati.leggiIntero("Scegli: ", 1, 3);
                    System.out.print("\n");

                    switch (option_1) {
                        //Visualizza ricette
                        case 1 -> {
                            for (Recipe recipe : recipes)
                                recipe.printRecipe();
                            Restaurant.waitComand();
                        }
                        //Aggiungi ricetta
                        case 2 -> {
                            try {
                                recipeWriter();
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                        //Rimuovi ricetta
                        case 3 -> {
                            try {
                                for (Recipe recipe : recipes)
                                    System.out.println(recipe.getId() + " - " + recipe.getName());
                                System.out.println("0 - ANNULLA");
                                int id = InputDati.leggiInteroConMinimo("Scegli la ricetta da rimuovere: ", 0);
                                elementDeleter(1, id);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
                //Gestisci bevande
                case 2 -> {

                    System.out.println("1 - Visualizza bevande");
                    System.out.println("2 - Aggiungi bevanda");
                    System.out.println("3 - Rimuovi bevanda");
                    int option_2 = InputDati.leggiIntero("Scegli: ", 1, 3);
                    System.out.print("\n");

                    switch (option_2) {
                        //Visualizza bevande
                        case 1 -> {
                            for (Ingredient drink : drinks)
                                drink.printDrinksExtras();
                            Restaurant.waitComand();
                        }
                        //Aggiungi bevanda
                        case 2 -> {
                            try {
                                xmlWriter(true);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                        //Rimuovi bevanda
                        case 3 -> {
                            try {
                                for (Ingredient drink : drinks)
                                    System.out.println(drink.getId() + " - " + drink.getName());
                                System.out.println("0 - ANNULLA");
                                int id = InputDati.leggiInteroConMinimo("Scegli la bevanda da rimuovere: ", 0);
                                elementDeleter(2, id);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
                //Gestisci extra
                case 3 -> {

                    System.out.println("1 - Visualizza extra");
                    System.out.println("2 - Aggiungi extra");
                    System.out.println("3 - Rimuovi extra");
                    int option_3 = InputDati.leggiIntero("Scegli: ", 1, 3);
                    System.out.print("\n");

                    switch (option_3) {
                        //Visualizza extra
                        case 1 -> {
                            for (Ingredient extra : extras)
                                extra.printDrinksExtras();
                            Restaurant.waitComand();
                        }
                        //Aggiungi extra
                        case 2 -> {
                            try {
                                xmlWriter(false);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                        //Rimuovi extra
                        case 3 -> {
                            try {
                                for (Ingredient extra : extras)
                                    System.out.println(extra.getId() + " - " + extra.getName());
                                System.out.println("0 - ANNULLA");
                                int id = InputDati.leggiInteroConMinimo("Scegli l'extra da rimuovere: ", 0);
                                elementDeleter(3, id);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
                //Gestisci menù tematici
                case 4 -> {

                    System.out.println("1 - Visualizza menù tematici");
                    System.out.println("2 - Aggiungi menù tematico");
                    System.out.println("3 - Rimuovi menù tematico");
                    int option_4 = InputDati.leggiIntero("Scegli: ", 1, 3);
                    System.out.print("\n");

                    switch (option_4) {
                        //Visualizza menù tematici
                        case 1 -> {
                            for (Menu menu : tematic_menu_list)
                                menu.printMenu();
                            Restaurant.waitComand();
                        }
                        //Crea menù tematico
                        case 2 -> {
                            try {
                                menuWriter();
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                        //Rimuovi menù tematico
                        case 3 -> {
                            try {
                                for (Menu menu : tematic_menu_list)
                                    System.out.println(menu.getId() + " - " + menu.getName());
                                System.out.println("0 - ANNULLA");
                                int id = InputDati.leggiInteroConMinimo("Scegli il menù tematico da rimuovere: ", 0);
                                elementDeleter(5, id);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
                //Visualizza proprietà Ristorante
                case 5 -> {
                    System.out.println("Numero di posti a sedere: " + getN_client());
                    System.out.println("Carico di lavoro per persona: " + getWorkload_for_client());
                    System.out.println("Carico di lavoro del ristorante: " + getWorkload_for_restaurant());
                    System.out.println("Consumo pro capite di bevande: " + getDrinks());
                    System.out.println("Consumo pro capite di generi alimentari extra: " + getExtras());
                    Restaurant.waitComand();
                }
            }
        } while (option != 0);
    }


}
