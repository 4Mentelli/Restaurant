package it.unibs.se.model;

import java.util.List;

public class Recipe implements Identifiable {

    private final String name;
    private final List<GenericIngredient> ingredients;
    private int recipe_id;
    private final int workload;
    private final Date[] period = new Date[2];

    public Recipe(String name, int recipe_id, int workload, List<GenericIngredient> ingredients, Date start, Date end) {
        this.name = name;
        this.recipe_id = recipe_id;
        this.workload = workload;
        this.ingredients = ingredients;
        this.period[0] = start;
        this.period[1] = end;
    }

    public String getName() {
        return name;
    }

    public List<GenericIngredient> getIngredients() {
        return ingredients;
    }

    public int getId() {
        return recipe_id;
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

    public void setId(int id) {
        this.recipe_id = id;
    }

    public void addIngredients(GenericIngredient ingredient) {
        this.ingredients.add(ingredient);
    }

}
