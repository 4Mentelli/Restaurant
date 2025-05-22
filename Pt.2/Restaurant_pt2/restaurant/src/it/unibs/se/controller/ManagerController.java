package it.unibs.se.controller;

import it.unibs.se.model.*;
import it.unibs.se.view.ManagerView;

import java.util.ArrayList;
import java.util.List;

public class ManagerController {
    private final Manager manager;
    private final ManagerView managerView;

    public ManagerController(Manager manager, ManagerView managerView) {
        this.manager = manager;
        this.managerView = managerView;
    }

    public void start() {
        checkParameters();
        int option;
        do {
            managerView.showCurrentDate();
            managerView.showMenu();
            option = managerView.readOption(0, 5);
            switch (option) {
                case 1 -> {
                    managerView.showRecipesMenu();
                    manageRecipes(managerView.readOption(1, 3));
                }
                case 2 -> {
                    managerView.showDrinksMenu();
                    manageDrinks(managerView.readOption(1, 3));
                }
                case 3 -> {
                    managerView.showExtrasMenu();
                    manageExtras(managerView.readOption(1, 3));
                }
                case 4 -> {
                    managerView.showThemedMenusMenu();
                    manageThemedMenus(managerView.readOption(1, 3));
                }
                case 5 -> {
                    int[] parameters = manager.getParameters();
                    managerView.showRestaurantProperties(parameters, manager.getWorkloadForRestaurant());
                    managerView.waitForEnter();
                }
            }
        } while (option != 0);
    }

    private void checkParameters() {
        if (manager.getTotalSeats() == 0) {
            int[] parameters = managerView.readParameters();
            manager.writeParameters(parameters);
        }
    }

    private void manageRecipes(int option) {
        switch (option) {
            case 1 -> {
                managerView.printAllRecipes(manager.getRecipes());
                managerView.showRecipeSize(manager.getRecipes().size());
                managerView.waitForEnter();
            }
            case 2 -> {
                Recipe new_recipe = handleNewRecipeRequest();
                manager.addRecipe(new_recipe);
                managerView.showSuccessfulOperationMessage();
            }
            case 3 -> {
                if (manager.getRecipes().isEmpty()) {
                    managerView.showRecipeSize(0);
                    managerView.waitForEnter();
                } else {
                    managerView.newLine();
                    managerView.printRecipesList(manager.getRecipes());
                    managerView.showCancelOption();
                    int idToDelete = managerView.readIdToDelete();
                    if (manager.remove("recipe", manager.getRecipes(), idToDelete))
                        managerView.showSuccessfulOperationMessage();
                    else if (idToDelete != 0) {
                        managerView.showNoIDMatchMessage();
                        managerView.waitForEnter();
                    }
                }
            }
        }
    }

    private void manageDrinks(int option) {
        switch (option) {
            case 1 -> {
                managerView.printAllDrinks(manager.getDrinks());
                managerView.showDrinksSize(manager.getDrinks().size());
                managerView.waitForEnter();
            }
            case 2 -> {
                manager.addDrink(handleNewDrinkRequest());
                managerView.showSuccessfulOperationMessage();
            }
            case 3 -> {
                if (manager.getDrinks().isEmpty()) {
                    managerView.showDrinksSize(0);
                    managerView.waitForEnter();
                } else {
                    managerView.printAllDrinks(manager.getDrinks());
                    managerView.showCancelOption();
                    int idToDelete = managerView.readIdToDelete();
                    if (manager.remove("drink", manager.getDrinks(), idToDelete))
                        managerView.showSuccessfulOperationMessage();
                    else if (idToDelete != 0) {
                        managerView.showNoIDMatchMessage();
                        managerView.waitForEnter();
                    }
                }
            }
        }
    }

    private void manageExtras(int option) {
        switch (option) {
            case 1 -> {
                managerView.printAllExtras(manager.getExtras());
                managerView.showExtrasSize(manager.getExtras().size());
                managerView.waitForEnter();
            }
            case 2 -> {
                manager.addExtra(handleNewExtraRequest());
                managerView.showSuccessfulOperationMessage();
            }
            case 3 -> {
                if (manager.getExtras().isEmpty()) {
                    managerView.showExtrasSize(0);
                    managerView.waitForEnter();
                } else {
                    managerView.printAllExtras(manager.getExtras());
                    managerView.showCancelOption();
                    int idToDelete = managerView.readIdToDelete();
                    if (manager.remove("extra", manager.getExtras(), idToDelete))
                        managerView.showSuccessfulOperationMessage();
                    else if (idToDelete != 0) {
                        managerView.showNoIDMatchMessage();
                        managerView.waitForEnter();
                    }
                }
            }
        }
    }

    private void manageThemedMenus(int option) {
        switch (option) {
            case 1 -> {
                managerView.printAllMenus(manager.getThemedMenus());
                managerView.showMenuSize(manager.getThemedMenus().size());
                managerView.waitForEnter();
            }
            case 2 -> {
                Menu new_menu = handleNewMenuRequest();
                if (new_menu != null) {
                    manager.addThemedMenu(new_menu);
                    managerView.showSuccessfulOperationMessage();
                } else {
                    managerView.showNoRecipesMessage();
                    managerView.waitForEnter();
                }
            }
            case 3 -> {
                if (manager.getThemedMenus().isEmpty()) {
                    managerView.showMenuSize(0);
                    managerView.waitForEnter();
                } else {
                    managerView.printMenusList(manager.getThemedMenus());
                    managerView.showCancelOption();
                    int idToDelete = managerView.readIdToDelete();
                    if (manager.remove("menu", manager.getThemedMenus(), idToDelete))
                        managerView.showSuccessfulOperationMessage();
                    else if (idToDelete != 0) {
                        managerView.showNoIDMatchMessage();
                        managerView.waitForEnter();
                    }
                }
            }
        }
    }

    private Recipe handleNewRecipeRequest() {
        managerView.newLine();
        String recipe_name = managerView.readNotEmptyRecipeName();
        int id = manager.calculateNewId(manager.getRecipes());
        List<GenericIngredient> ingredients = handleRecipeIngredientsRequest();
        int workload = ingredients.size() * 2;
        Date[] period = handleRecipePeriodRequest();
        return new Recipe(recipe_name, id, workload, ingredients, period[0], period[1]);
    }

    private IdentifiableIngredient handleNewDrinkRequest() {
        managerView.newLine();
        String drinkName = managerView.readNotEmptyDrinkName();
        int id = manager.calculateNewId(manager.getDrinks());
        return IngredientFactory.createDrink(drinkName, 0, id);
    }

    private IdentifiableIngredient handleNewExtraRequest() {
        managerView.newLine();
        String extraName = managerView.readNotEmptyExtraName();
        int id = manager.calculateNewId(manager.getExtras());
        return IngredientFactory.createExtra(extraName, 0, id);
    }

    private Menu handleNewMenuRequest() {

        if (manager.getRecipes().isEmpty()) {
            return null;
        }
        managerView.newLine();
        String new_menu_name = managerView.readNotEmptyMenuName();
        int id = manager.calculateNewId(manager.getThemedMenus());
        int max_workload = (manager.getWorkloadForClient() * 4) / 3;
        List<Recipe> new_menu_dishes = handleRecipeListRequest(max_workload);
        int menu_workload = manager.calculateMenuWorkload(new_menu_dishes);

        return new Menu(new_menu_name, new_menu_dishes, id, menu_workload);
    }

    private List<Recipe> handleRecipeListRequest(int workload_limit) {
        List<Recipe> new_menu_dishes = new ArrayList<>();
        int recipe_to_add;
        do {
            managerView.printAllRecipesWorkload(manager.getRecipes());
            managerView.showTemporaryMenu(new_menu_dishes);
            recipe_to_add = managerView.readRecipeToAddToMenu(manager.getRecipes());

            for (Recipe recipe : manager.getRecipes()) {
                if (recipe.getId() == recipe_to_add && recipe.getWorkload() <= workload_limit) {
                    new_menu_dishes.add(recipe);
                    workload_limit -= recipe.getWorkload();
                } else if (recipe.getId() == recipe_to_add && recipe.getWorkload() > workload_limit) {
                    managerView.showWorkloadLimitMessage();
                    managerView.waitForEnter();
                }
            }
        } while (recipe_to_add != 0);
        return new_menu_dishes;
    }

    private Date[] handleRecipePeriodRequest() {
        Date[] period = manager.getRestaurantOpeningPeriod();

        if (managerView.readNewRecipePeriodOption() == 2) {
            do {
                String start = managerView.readStartDate();
                period[0] = manager.convertReadDate(start);

                String end = managerView.readEndDate();
                period[1] = manager.convertReadDate(end);

                if (period[0].isAfter(period[1])) {
                    managerView.showInvalidDateRangeMessage();
                }
            } while (period[0].isAfter(period[1]));
        }
        return period;
    }

    private List<GenericIngredient> handleRecipeIngredientsRequest() {
        List<GenericIngredient> ingredients = new ArrayList<>();
        while (true) {
            managerView.newLine();
            String name = managerView.readIngredientName();
            if (name.equalsIgnoreCase("N")) break;
            float quantity = managerView.readIngredientQuantity();
            String unit = managerView.readIngredientUnit();
            ingredients.add(new GenericIngredient(name, quantity, unit));
        }
        return ingredients;
    }

}
