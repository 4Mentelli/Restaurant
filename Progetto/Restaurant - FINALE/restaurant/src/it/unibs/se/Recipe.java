package it.unibs.se;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class Recipe {

    private String name;
    private List<Ingredient> ingredients;
    private int recipe_id;
    private int workload;
    private Date[] period = new Date[2];

    public Recipe(String name) {
        this.name = name;
        this.ingredients = new ArrayList<>();
    }

    public Recipe(String name, int recipe_id, int workload,List<Ingredient> ingredients, Date start, Date end) {
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

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public int getId() {
        return recipe_id;
    }

    public void setId(int id) { this.recipe_id = id; }

    public int getWorkload() {return workload;}

    public void setWorkload(int workload) {this.workload = workload;}

    public void setPeriod(Date start, Date end) {
        this.period[0] = start;
        this.period[1] = end;
    }

    public Date[] getPeriod() {
        return period;
    }

    public void addIngredients(Ingredient ingredient) {
        this.ingredients.add(ingredient);
    }

    public void printRecipe() {
        System.out.println("\nRICETTA " + this.getId());
        System.out.println("Nome: " + this.getName());
        System.out.println("Ingredienti:");
        for (Ingredient ingrediente : this.getIngredients())
            System.out.println("- " + ingrediente.getName() + " " + ingrediente.getQuantity() + " " + ingrediente.getUnit());
        System.out.println("Carico di lavoro: " + this.getWorkload());
        System.out.println("Dal " + this.getPeriod()[0] + " al " + this.getPeriod()[1]);
        System.out.println();
    }
}
