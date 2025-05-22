package it.unibs.se.model;

import it.unibs.se.exception.ConvertException;

import java.util.List;

public class Manager {

    public static final Date[] RESTAURANT_OPENING_PERIOD = {new Date("01/01/2025"), new Date("31/12/2025")};
    private final ManagerReader reader;
    private final ManagerUpdater updater;
    private int[] parameters;

    public Manager(ManagerReader reader, ManagerUpdater updater) {
        this.reader = reader;
        this.updater = updater;
        this.parameters = reader.parametersReader();
    }

    public Date[] getRestaurantOpeningPeriod() { return RESTAURANT_OPENING_PERIOD; }

    public int[] getParameters() { return parameters; }

    public int getTotalSeats() { return parameters[0]; }

    public int getWorkloadForClient() { return parameters[1]; }

    public int getWorkloadForRestaurant() { return getWorkloadForClient() * (getTotalSeats() * 12 / 10); }

    public List<Recipe> getRecipes() { return reader.recipesReader(); }

    public List<IdentifiableIngredient> getDrinks() { return reader.drinksReader(); }

    public List<IdentifiableIngredient> getExtras() { return reader.extrasReader(); }

    public List<Menu> getThemedMenus() { return reader.themedMenusReader(); }

    public void setParameters(int[] parameters) {
        this.parameters = parameters;
    }

    public void writeParameters(int[] parameters) {
        setParameters(parameters);
        try {
            updater.parametersWriter(parameters[0], parameters[1], parameters[2], parameters[3]);
        } catch (Exception e) {
            throw new ConvertException("Errore nella scrittura dei parametri", e);
        }
    }

    public void addRecipe(Recipe recipe) {
        try {
            updater.recipeWriter(recipe);
        } catch (Exception e) {
            throw new ConvertException("Errore nella scrittura della ricetta", e);
        }
    }

    public void addDrink(IdentifiableIngredient drink) {
        try {
            updater.drinkWriter(drink);
        } catch (Exception e) {
            throw new ConvertException("Errore nella scrittura della bevanda", e);
        }
    }

    public void addExtra(IdentifiableIngredient extra) {
        try {
            updater.extraWriter(extra);
        } catch (Exception e) {
            throw new ConvertException("Errore nella scrittura dell'extra", e);
        }
    }

    public void addThemedMenu(Menu menu) {
        try {
            updater.menuWriter(menu);
        } catch (Exception e) {
            throw new ConvertException("Errore nella scrittura del menù tematico", e);
        }
    }

    public <T extends Identifiable> boolean remove(String type, List<T> items, int id) {
        try {
            for (T item : items) {
                if (item.getId() == id) {
                    updater.deleteElement(type, id);
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            throw new ConvertException("Errore nella rimozione di " + type, e);
        }
    }

    public <T extends Identifiable> int calculateNewId(List<T> items) {
        int id = 1;
        if (!items.isEmpty()) {
            id = items.get(items.size() - 1).getId() + 1;
        }
        return id;
    }

    public int calculateMenuWorkload(List<Recipe> recipes) {
        int workload = 0;
        for (Recipe recipe : recipes) {
            workload += recipe.getWorkload();
        }
        return workload;
    }

    public Date convertReadDate(String date) {
        return new Date(date.split("-")[2] + "/" + date.split("-")[1] + "/" + date.split("-")[0]);
    }

}
