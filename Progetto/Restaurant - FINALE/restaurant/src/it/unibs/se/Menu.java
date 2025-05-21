package it.unibs.se;

import it.unibs.fp.mylib.InputDati;

import java.util.ArrayList;
import java.util.List;

public class Menu{

    private String name;
    private Iterable<Recipe> dishes;
    private int id;
    private int workload;
    private Date[] period = new Date[2];

    public Menu(String name, List<Recipe> dishes, int id, int workloads) {
        this.name = name;
        this.dishes = dishes;
        this.id = id;
        this.workload = workloads;
        if (dishes.isEmpty()) {
            this.period[0] = new Date(1, 1, 2024);
            this.period[1] = new Date(31, 12, 2025);
        } else {
            this.period[0] = dishes.get(0).getPeriod()[0];
            this.period[1] = dishes.get(0).getPeriod()[1];
            for (Recipe dish : dishes) {
                if (dish.getPeriod()[0].after(this.period[0], false)) this.period[0] = dish.getPeriod()[0];
                if (this.period[1].after(dish.getPeriod()[1], false)) this.period[1] = dish.getPeriod()[1];
            }
        }
    }

    public Iterable<Recipe> getDishes() { return dishes; }
    public int getWorkload() { return workload; }

    public void printMenu() {
        System.out.println("\nMENU " + this.getId());
        System.out.println("Nome: " + this.getName());
        System.out.println("Piatti:");
        for (Recipe piatto : this.getDishes()) {
            System.out.println("- " + piatto.getName());
        }
        if (this.getStart().after(this.getEnd(), false))
            System.out.println("Il menù non è disponibile");
        else
            System.out.println("Dal " + this.getStart().toString() + " al " + this.getEnd().toString());
        System.out.println();
    }

    public String getName() {
        return name;
    }

    public int getId() { return id; }

    public Date getStart() { return period[0]; }

    public Date getEnd() { return period[1]; }
}
