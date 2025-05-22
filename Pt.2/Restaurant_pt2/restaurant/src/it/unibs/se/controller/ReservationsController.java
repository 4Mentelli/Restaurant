package it.unibs.se.controller;

import it.unibs.se.model.*;
import it.unibs.se.view.ReservationsView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationsController {
    private final ReservationsManager reservationsManager;
    private final ReservationsView reservationsView;

    public ReservationsController(ReservationsManager reservationsManager, ReservationsView reservationsView) {
        this.reservationsManager = reservationsManager;
        this.reservationsView = reservationsView;
    }

    public void start() {
        int option;
        do {
            reservationsView.showCurrentDate();
            reservationsView.showMenu();
            option = reservationsView.readOption(0, 3);

            switch (option) {
                case 1 -> {
                    reservationsView.printAllReservations(reservationsManager.getReservations(), reservationsManager.getMenus(), reservationsManager.getRecipes());
                    reservationsView.showSize(reservationsManager.getReservations().size());
                    reservationsView.waitForEnter();
                }
                case 2 -> {
                    Reservation reservation = handleNewReservationRequest();
                    if (reservation != null) {
                        reservationsManager.addReservation(reservation);
                        reservationsView.showSuccessfulOperationMessage();
                    }
                    else {
                        reservationsView.showFailedReservationMessage();
                        reservationsView.waitForEnter();
                    }
                }
                case 3 -> {
                    if (reservationsManager.getReservations().isEmpty()) {
                        reservationsView.showSize(0);
                        reservationsView.waitForEnter();
                    } else {
                        reservationsView.printAllReservations(reservationsManager.getReservations(), reservationsManager.getMenus(), reservationsManager.getRecipes());
                        reservationsView.showCancelOption();
                        int delete_id = reservationsView.readIdToDelete();
                        if (reservationsManager.deleteReservation(delete_id))
                            reservationsView.showSuccessfulOperationMessage();
                        else if (delete_id != 0) {
                            reservationsView.showNoIDMatchMessage();
                            reservationsView.waitForEnter();
                        }
                    }
                }
            }
        } while (option != 0);
    }

    private Reservation handleNewReservationRequest() {

        if (reservationsManager.getTotalSeats() == 0) {
            reservationsView.showNoParametersMessage();
            return null;
        }
        if (reservationsManager.getRecipes().isEmpty()) {
            reservationsView.showNoRecipesMessage();
            return null;
        }

        reservationsView.newLine();
        LocalDate local_date = reservationsView.readLocalDate();
        String date_string = reservationsManager.convertReadDate(local_date);
        Date date = new Date(date_string);
        if (!handleDateAvailability(date)) {
            reservationsView.showInvalidDateMessage();
            return null;
        }

        int number_of_people = reservationsView.readNumberOfPeople(reservationsManager.getTotalSeats());
        if (!reservationsManager.isTableAvailable(number_of_people, date_string)) {
            reservationsView.showOverbookingMessage();
            return null;
        }

        int id = reservationsManager.calculateNewReservationId();

        List<Recipe> available_recipes = reservationsManager.getAvailableRecipes(date);
        if (available_recipes.isEmpty()) {
            reservationsView.showNoAvailableRecipesMessage();
            return null;
        }
        List<Menu> available_menus = reservationsManager.getAvailableMenus(date);

        Reservation reservation = new Reservation(id, number_of_people, reservationsManager.getWorkloadForClient() * number_of_people, date);
        return handleOrder(reservation, date_string, available_menus, available_recipes);
    }

    private boolean handleDateAvailability(Date date) {

        String option = reservationsManager.isDateAvailable(date);
        switch (option) {
            case "past" -> {
                reservationsView.showExpiredMessage();
                return false;
            }
            case "future" -> {
                reservationsView.showTooFarMessage();
                return false;
            }
            case "weekend" -> {
                reservationsView.showWeekendMessage();
                return false;
            }
            case "holiday" -> {
                reservationsView.showHolidayMessage();
                return false;
            }
        }
        return true;
    }

    private Reservation handleOrder(Reservation reservation, String date_string, List<Menu> available_menus, List<Recipe> available_recipes) {

        List<OrderedMenu> all_ordered_menus = new ArrayList<>();
        List<OrderedDishes> all_ordered_dishes = new ArrayList<>();
        int orders_left = reservation.getNumber_of_people();

        while (orders_left > 0) {

            int option;
            reservationsView.newLine();
            reservationsView.showMessage("1 - Ordinazione alla carta");
            if (!available_menus.isEmpty()) {
                reservationsView.showMessage("2 - Menu' tematico");
                option = reservationsView.readOption(1, 2);
            } else
                option = reservationsView.readOption(1, 1);

            int number_of_same_order = 1;

            if (option == 1) {

                List<Integer> dishes_id_list = handleALaCarteOrder(date_string, available_recipes);
                if (orders_left > 1)
                    number_of_same_order = reservationsView.readNumberOfSameOrder(orders_left);

                OrderedDishes ordered_dishes = new OrderedDishes(dishes_id_list, number_of_same_order);
                all_ordered_dishes.add(ordered_dishes);

            } else {

                Menu menu_order = handleThemedMenuOrder(date_string, available_menus);
                if (orders_left > 1)
                    number_of_same_order = reservationsView.readNumberOfSameOrder(orders_left);

                OrderedMenu ordered_menu = new OrderedMenu(menu_order.getId(), number_of_same_order);
                all_ordered_menus.add(ordered_menu);

            }
            orders_left -= number_of_same_order;
        }

        reservation.setOrderedDishes(all_ordered_dishes);
        reservation.setOrderedMenus(all_ordered_menus);
        return reservation;
    }

    private List<Integer> handleALaCarteOrder(String date_string, List<Recipe> available_recipes) {

        int expected_workload = 0;
        List<Recipe> ordered_dishes = new ArrayList<>();
        int recipe_id;

        do {

            reservationsView.printAvailableRecipes(date_string, available_recipes);
            reservationsView.showTemporaryOrder(ordered_dishes);
            boolean found = false;
            Recipe selected_recipe;

            do {

                recipe_id = reservationsView.readOption(0, available_recipes.get(available_recipes.size() - 1).getId());
                for (Recipe recipe : available_recipes) {
                    if (recipe.getId() == recipe_id || recipe_id == 0) {
                        selected_recipe = recipe;
                        expected_workload += selected_recipe.getWorkload();
                        found = true;

                        if (recipe_id != 0) {
                            if (expected_workload <= reservationsManager.getWorkloadForClient()) {
                                ordered_dishes.add(selected_recipe);
                                reservationsView.showAddedMessage();
                            } else {
                                reservationsView.showWorkloadLimitMessage(expected_workload, reservationsManager.getWorkloadForClient());
                                expected_workload -= selected_recipe.getWorkload();
                                reservationsView.waitForEnter();
                            }
                        }
                        break;
                    }
                }
            } while (!found);
        } while (recipe_id != 0);

        List<Integer> dishes_id_list = new ArrayList<>();
        for (Recipe recipe : ordered_dishes)
            dishes_id_list.add(recipe.getId());

        return dishes_id_list;
    }

    private Menu handleThemedMenuOrder(String date_string, List<Menu> available_menus) {
        reservationsView.printAvailableMenus(date_string, available_menus);

        Menu ordered_menu = null;
        do {
            int menu_id = reservationsView.readOption(1, available_menus.get(available_menus.size() - 1).getId());
            for (Menu menu : available_menus) {
                if (menu.getId() == menu_id)
                    ordered_menu = menu;
            }
        } while (ordered_menu == null);

        return ordered_menu;
    }

}
