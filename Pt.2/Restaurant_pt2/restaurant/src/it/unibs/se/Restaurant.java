package it.unibs.se;

import it.unibs.se.controller.RestaurantController;
import it.unibs.se.view.RestaurantView;

public class Restaurant {
    public static void main(String[] args) {

        RestaurantView view = new RestaurantView();
        RestaurantController controller = new RestaurantController(view);
        controller.start();
    }
}
