package it.unibs.se.model;

import java.util.List;

public class Reservation implements Identifiable {

    private int id;
    private final int number_of_people;
    private final float workload;
    private final Date dateObj;
    private List<OrderedMenu> ordered_menus;
    private List<OrderedDishes> ordered_dishes;

    public Reservation(int id, int number_of_people, float workload, Date dateObj) {
        this.id = id;
        this.number_of_people = number_of_people;
        this.workload = workload;
        this.dateObj = dateObj;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return dateObj;
    }

    public int getNumber_of_people() {
        return number_of_people;
    }

    public float getWorkload() {
        return workload;
    }

    public List<OrderedMenu> getOrderedMenus() {
        return ordered_menus;
    }

    public List<OrderedDishes> getOrderedDishes() {
        return ordered_dishes;
    }

    public void setOrderedMenus(List<OrderedMenu> ordered_menus) {
        this.ordered_menus = ordered_menus;
    }

    public void setOrderedDishes(List<OrderedDishes> ordered_dishes) {
        this.ordered_dishes = ordered_dishes;
    }

}
