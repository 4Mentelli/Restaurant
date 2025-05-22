package it.unibs.se.model;

import java.util.List;

public class OrderedDishes {

    private final List<Integer> dishes_ids;
    private final int quantity;

    public OrderedDishes(List<Integer> dish_ids, int quantity) {
        this.dishes_ids = dish_ids;
        this.quantity = quantity;
    }

    public List<Integer> getDish_ids() {
        return dishes_ids;
    }

    public int getQuantity() {
        return quantity;
    }

}
