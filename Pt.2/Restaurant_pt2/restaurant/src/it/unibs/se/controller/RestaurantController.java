package it.unibs.se.controller;

import it.unibs.se.model.*;
import it.unibs.se.view.*;

public class RestaurantController {
    private final RestaurantView view;

    public RestaurantController(RestaurantView view) {
        this.view = view;
    }

    public void start() {
        int option;
        do {
            view.showCurrentDate();
            view.showMenu();
            option = view.readOption(0, 3);

            switch (option) {
                case 1 -> handleManager();
                case 2 -> handleReservations();
                case 3 -> handleStore();
                case 0 -> view.showGoodbyeMessage();
            }
        } while (option != 0);
    }

    private void handleManager() {
        ManagerReader reader = new DatabaseReader();
        ManagerUpdater updater = new DatabaseUpdater();
        Manager manager = new Manager(reader, updater);
        ManagerView managerView = new ManagerView();
        ManagerController managerController = new ManagerController(manager, managerView);
        managerController.start();
    }

    private void handleReservations() {
        ReservationReader reader = new DatabaseReader();
        ReservationUpdater updater = new DatabaseUpdater();
        ReservationsManager reservationManager =
                new ReservationsManager(reader, updater);
        ReservationsView reservationView = new ReservationsView();
        ReservationsController reservationController = new ReservationsController(reservationManager, reservationView);
        reservationController.start();
    }

    private void handleStore() {
        StorageReader reader = new DatabaseReader();
        StorageUpdater updater = new DatabaseUpdater();
        StorageManager storeManager = new StorageManager(reader, updater);
        StorageView storeView = new StorageView();
        StorageController storeController = new StorageController(storeManager, storeView);
        storeController.start();
    }
}

