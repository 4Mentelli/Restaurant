package it.unibs.se.model;

import it.unibs.se.exception.ConvertException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationsManager {

    private final ReservationReader reader;
    private final ReservationUpdater updater;

    public ReservationsManager(ReservationReader reader, ReservationUpdater updater) {
        this.reader = reader;
        this.updater = updater;
    }

    public List<Reservation> getReservations() { return reader.reservationsReader(); }
    public List<Recipe> getRecipes() { return reader.recipesReader(); }
    public List<Menu> getMenus() { return reader.themedMenusReader(); }
    public List<Date> getHolidays() { return reader.holidaysReader(); }
    public int getTotalSeats() { return reader.parametersReader()[0]; }
    public int getWorkloadForClient() { return reader.parametersReader()[1]; }

    public void addReservation(Reservation reservation) {
        try {
            updater.reservationWriter(reservation);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteReservation(int delete_id) {
        List<Reservation> reservations = getReservations();
        for (Reservation reservation : reservations) {
            if (reservation.getId() == delete_id) {
                reservations.remove(reservation);
                try {
                    updater.deleteElement("reservation", delete_id);
                } catch (Exception e) {
                    throw new ConvertException("Errore nella rimozione della prenotazione", e);
                }
                return true;
            }
        }
        return false;
    }

    public List<Recipe> getAvailableRecipes(Date date) {
        List<Recipe> recipes = getRecipes();
        List<Recipe> available_recipes = new ArrayList<>();
        for (Recipe recipe: recipes) {
            if (date.isBetween(recipe.getStartDate(), recipe.getEndDate()))
                available_recipes.add(recipe);
        }
        return available_recipes;
    }

    public List<Menu> getAvailableMenus(Date date) {
        List<Menu> menus = getMenus();
        List<Menu> available_menus = new ArrayList<>();
        for (Menu menu: menus) {
            if (date.isBetween(menu.getStartDate(), menu.getEndDate()))
                available_menus.add(menu);
        }
        return available_menus;
    }

    public int calculateNewReservationId() {
        int id = 1;
        if (!getReservations().isEmpty())
            id = getReservations().get(getReservations().size() - 1).getId() + 1;
        return id;
    }

    public String convertReadDate(LocalDate local_date) {
        return local_date.toString().split("-")[2] + "/" + local_date.toString().split("-")[1] + "/" + local_date.toString().split("-")[0];
    }

    public boolean isTableAvailable(int number_of_people, String date_string) {
        int total_people = 0;
        for (Reservation reservation : getReservations()) {
            if (reservation.getDate().getString().equals(date_string)) {
                total_people += reservation.getNumber_of_people();
            }
        }
        return total_people + number_of_people <= getTotalSeats();
    }

    public String isDateAvailable(Date date) {

        Date now = new Date(convertReadDate(LocalDate.now()));
        if (date.isExpired(now)) {
            return "past";
        }
        if (date.isTooFar()) {
            return "future";
        }
        if (date.isHoliday(getHolidays())) {
            return "holiday";
        }
        if (date.isInWeekend()) {
            return "weekend";
        }
        return "available";
    }

}
