package it.unibs.se.model;

import java.util.List;

public class Menu implements Identifiable {

    private final String name;
    private final Iterable<Recipe> dishes;
    private int id;
    private final int workload;
    private Date[] period = new Date[2];

    public Menu(String name, List<Recipe> dishes, int id, int workload) {
        this.name = name;
        this.dishes = dishes;
        this.id = id;
        this.workload = workload;
        this.period = calculatePeriod(dishes);
    }

    private Date[] calculatePeriod(List<Recipe> dishes) {
        Date[] period = Manager.RESTAURANT_OPENING_PERIOD;
        if (!dishes.isEmpty()) {
            period = new Date[]{dishes.get(0).getStartDate(), dishes.get(0).getEndDate()};
            for (Recipe dish : dishes) {
                if (dish.getStartDate().isAfter(period[0]))
                    period[0] = dish.getStartDate();
                if (dish.getEndDate().isBefore(period[1]))
                    period[1] = dish.getEndDate();
            }
        }
        return period;
    }

    public String getName() {
        return name;
    }

    public Iterable<Recipe> getDishes() {
        return dishes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWorkload() {
        return workload;
    }

    public Date getStartDate() {
        return period[0];
    }

    public Date getEndDate() {
        return period[1];
    }

}
