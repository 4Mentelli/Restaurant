package it.unibs.se.view;

import it.unibs.fp.mylib.InputDati;
import it.unibs.se.model.*;

import java.time.LocalDate;
import java.util.List;

public class ReservationsView implements View {

    @Override
    public void showMenu() {
        showMessage("1 - Visualizza prenotazioni");
        showMessage("2 - Aggiungi prenotazione");
        showMessage("3 - Cancella prenotazione");
        showMessage("0 - Menu principale");
    }

    public void showSize(int size) {
        newLine();
        showMessage("Ci sono " + size + " prenotazioni");
    }

    public void showNoParametersMessage() {
        showMessage("I parametri del ristorante non sono ancora stati inizializzati dal gestore");
    }

    public void showNoRecipesMessage() {
        showMessage("Non sono presenti ricette nel ricettario");
    }

    public void showInvalidDateMessage() {
        showMessage("Non e' possibile prenotare per la data scelta");
    }

    public void showHolidayMessage() {
        showMessage("La data scelta e' una festività");
    }

    public void showWeekendMessage() {
        showMessage("La data scelta è nel weekend");
    }

    public void showOverbookingMessage() {
        showMessage("I posti disponibili per la data scelta non sono sufficienti");
    }

    public void showExpiredMessage() {
        showMessage("La data scelta è passata");
    }

    public void showTooFarMessage() {
        showMessage("La data scelta è troppo lontana");
    }

    public void showFailedReservationMessage() {
        showMessage("Prenotazione non effettuata");
    }

    public void showNoAvailableRecipesMessage() {
        newLine();
        showMessage("Non ci sono ricette per la data scelta");
    }

    public void showWorkloadLimitMessage(int expected_workload, int max_workload_for_client) {
        showMessage("[ " + expected_workload + " / " + max_workload_for_client + " ]");
        showMessage("Il piatto non può essere inserito perché supera il carico di lavoro per una persona");
        newLine();
    }

    public void showAddedMessage() {
        showMessage("Aggiunto!");
    }

    public void showCancelOption() {
        newLine();
        showMessage("0 - ANNULLA");
    }

    public void showTemporaryOrder(List<Recipe> dishes) {
        if (!dishes.isEmpty()) {
            newLine();
            showMessage("[ Piatti aggiunti ]");
            for (Recipe dish : dishes)
                showMessage(dish.getId() + " - " + dish.getName());
            newLine();
        }
    }

    public void printAllReservations(List<Reservation> reservations, List<Menu> menus, List<Recipe> recipes) {
        for (Reservation reservation : reservations)
            printReservation(reservation, menus, recipes);
    }

    public void printReservation(Reservation reservation, List<Menu> menus, List<Recipe> recipes) {
        printSpecificReservation(reservation);
        if (reservation.getOrderedDishes() != null)
            printPersonalizedMenu(reservation, recipes);
        if (reservation.getOrderedMenus() != null)
            printThemedMenu(reservation, menus);
    }

    public void printSpecificReservation(Reservation reservation) {
        newLine();
        showMessage("PRENOTAZIONE " + reservation.getId());
        showMessage("giorno: " + reservation.getDate().getString());
        showMessage("persone al tavolo: " + reservation.getNumber_of_people());
        showMessage("carico di lavoro: " + reservation.getWorkload());
        showMessage("ORDINAZIONI:");
    }

    public void printThemedMenu(Reservation reservation, List<Menu> menus) {
        for (int i = 0; i < reservation.getOrderedMenus().size(); i++) {

            int menu_id = reservation.getOrderedMenus().get(i).menu_id();
            int quantity = reservation.getOrderedMenus().get(i).quantity();

                for (Menu menu : menus) {
                    if (menu.getId() == menu_id) {
                        System.out.println("Menu: " + menu.getName() + " per " + quantity + " persone:");
                        for (Recipe recipe : menu.getDishes())
                            System.out.println(" - " + recipe.getName());
                    }
                }

        }

    }

    public void printPersonalizedMenu(Reservation reservation, List<Recipe> recipes) {
        for (int i = 0; i < reservation.getOrderedDishes().size(); i++) {

            List<Integer> dish_ids = reservation.getOrderedDishes().get(i).getDish_ids();
            int quantity = reservation.getOrderedDishes().get(i).getQuantity();

            showMessage("Menù alla carta per " + quantity + " persone:");
            for (Integer dishId : dish_ids) {
                for (Recipe recipe : recipes)
                    if (recipe.getId() == dishId)
                        showMessage(" - " + recipe.getName());
            }
        }

    }

    public void printAvailableRecipes(String date_string, List<Recipe> available_recipes) {
        newLine();
        showMessage("Ricette disponibili il " + date_string + ":");
        for (Recipe r : available_recipes)
            showMessage(r.getId() + " - " + r.getName());
        showMessage("0 - Termina Menu");
    }

    public void printAvailableMenus(String date_string, List<Menu> available_menus) {
        newLine();
        showMessage("Menu' disponibili il " + date_string + ":");
        for (Menu menu : available_menus)
            printAllThemedMenuInfo(menu);
    }

    public LocalDate readLocalDate() {
        return InputDati.leggiData("Inserisci la data della prenotazione: ");
    }

    public int readNumberOfPeople(int max) {
        return InputDati.leggiIntero("Inserisci il numero di persone: ", 1, max);
    }

    public int readNumberOfSameOrder(int maximum) {
        return InputDati.leggiIntero("Inserisci il numero di persone che prenderanno questa ordinazione: ", 1, maximum);
    }

}
