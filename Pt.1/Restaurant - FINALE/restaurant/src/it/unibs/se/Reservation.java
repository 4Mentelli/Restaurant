package it.unibs.se;

import it.unibs.fp.mylib.InputDati;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.unibs.se.Xml.*;

public class Reservation {

    private int reservation_id;
    private int number_of_people;
    private float workload;
    private Date dateObj;
    private List<Map<Integer, Integer>> menusMap;
    private List<Map<Integer, List<Integer>>> dishesMap;

    public Reservation(int reservation_id, int number_of_people, float workload, Date dateObj) {
        this.reservation_id = reservation_id;
        this.number_of_people = number_of_people;
        this.workload = workload;
        this.dateObj = dateObj;
    }

    public int getReservation_id() {
        return reservation_id;
    }

    public void setReservation_id(int reservation_id) {
        this.reservation_id = reservation_id;
    }

    public Date getDate() {
        return dateObj;
    }

    public int getNumber_of_people() {
        return number_of_people;
    }

    public void setNumber_of_people(int number_of_people) {
        this.number_of_people = number_of_people;
    }

    public float getWorkload() {
        return workload;
    }

    public void setWorkload(float workload) {
        this.workload = workload;
    }

    public List<Map<Integer, Integer>> getMenusMap() {
        return menusMap;
    }

    public void setMenusMap(List<Map<Integer, Integer>> menusMap) {
        this.menusMap = menusMap;
    }

    public List<Map<Integer, List<Integer>>> getDishesMap() {
        return dishesMap;
    }

    public void setDishesMap(List<Map<Integer, List<Integer>>> dishesMap) {
        this.dishesMap = dishesMap;
    }

    public void printReservation() {
        List<Menu> menus = menuReader();
        List<Recipe> recipes = recipeReader();

        System.out.println("\nPRENOTAZIONE " + this.getReservation_id());
        System.out.println("giorno: " + this.getDate().toString());
        System.out.println("persone al tavolo: " + this.getNumber_of_people());
        System.out.println("carico di lavoro: " + this.getWorkload());
        System.out.println("ORDINAZIONI:");

        if (this.getMenusMap() != null)
            for (int i = 0; i < this.getMenusMap().size(); i++)
                for (Map.Entry<Integer, Integer> menu : this.getMenusMap().get(i).entrySet())
                    for (Menu m : menus)
                        if (m.getId() == menu.getValue()) {
                            System.out.println("Menu: " + m.getName() + " per " + menu.getKey() + " persone:");
                            for (Recipe r : m.getDishes())
                                System.out.println(" - " + r.getName());
                        }



        if (this.getDishesMap() != null)
            for (int i = 0; i < this.getDishesMap().size(); i++)
                for (Map.Entry<Integer, List<Integer>> dish : this.getDishesMap().get(i).entrySet()) {
                    System.out.println("Menù alla carta per " + dish.getKey() + " persone:");
                    for (int j = 0; j < dish.getValue().size(); j++) {
                        for (Recipe r : recipes)
                            if (r.getId() == dish.getValue().get(j))
                                System.out.println(" - " + r.getName());
                    }
            }
    }

    public static void deleteReservation(List<Reservation> reservations) {
        reservations = reservationReader();
        for (Reservation reservation : reservations)
            reservation.printReservation();
        int delete_id;
        delete_id = InputDati.leggiInteroConMinimo("\nInserisci l'id della prenotazione da eliminare: ", 0);
        for (Reservation reservation : reservations) {
            if (reservation.getReservation_id() == delete_id) {
                reservations.remove(reservation);
                elementDeleter(4, delete_id);
                System.out.println("Prenotazione eliminata con successo!");
                break;
            }
        }
    }

    public List<Ingredient> getIngredients() {

        List<Menu> menus = menuReader();
        List<Recipe> recipes = recipeReader();
        List<Ingredient> ingredients = new ArrayList<>();

        // Legge ogni menu di menusMap e aggiunge la ricetta di ogni piatto al vettore di ingredienti
        if (this.getMenusMap() != null)
            for (int i = 0; i < this.getMenusMap().size(); i++)
                for (Map.Entry<Integer, Integer> menu : this.getMenusMap().get(i).entrySet())
                    for (Menu m : menus)
                        if (m.getId() == menu.getValue()) {
                            for (Recipe r : m.getDishes())
                                for (Ingredient ingredient : r.getIngredients())
                                    ingredients.add(new Ingredient(ingredient.getName(), ingredient.getQuantity() * menu.getKey(), ingredient.getUnit()));
                        }

        // Legge ogni piatto di dishesMap e aggiunge la ricetta di ogni piatto al vettore di ingredienti
        if (this.getDishesMap() != null)
            for (int i = 0; i < this.getDishesMap().size(); i++)
                for (Map.Entry<Integer, List<Integer>> dish : this.getDishesMap().get(i).entrySet()) {
                    for (int j = 0; j < dish.getValue().size(); j++) {
                        for (Recipe r : recipes)
                            if (r.getId() == dish.getValue().get(j))
                                for (Ingredient ingredient : r.getIngredients())
                                    ingredients.add(new Ingredient(ingredient.getName(), ingredient.getQuantity() * dish.getKey(), ingredient.getUnit()));
                    }
                }
        return ingredients;
    }
}
