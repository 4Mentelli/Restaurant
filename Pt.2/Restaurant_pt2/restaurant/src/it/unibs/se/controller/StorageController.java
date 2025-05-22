package it.unibs.se.controller;

import it.unibs.se.model.GenericIngredient;
import it.unibs.se.model.StorageManager;
import it.unibs.se.view.StorageView;

public class StorageController {
    private final StorageManager storeManager;
    private final StorageView storeView;

    public StorageController(StorageManager storeManager, StorageView storeView) {
        this.storeManager = storeManager;
        this.storeView = storeView;
    }

    public void start() {
        int option;
        do {
            storeView.showCurrentDate();
            storeView.showMenu();
            option = storeView.readOption(0, 3);

            switch (option) {
                case 1 -> {
                    storeView.showStorageRegister(storeManager.getStorage());
                    storeView.showStorageSize(storeManager.getStorage().size());

                    storeView.showDrinksRegister(storeManager.getDrinks());
                    storeView.showDrinksSize(storeManager.getDrinks().size());

                    storeView.showExtrasRegister(storeManager.getExtras());
                    storeView.showExtrasSize(storeManager.getExtras().size());

                    storeView.waitForEnter();
                }
                case 2 -> {
                    storeView.showStorageOptions();
                    handleAddProductRequest(storeView.readOption(1, 3));
                }
                case 3 -> {
                    storeView.showShoppingList(storeManager.makeShoppingList());
                    storeView.waitForEnter();
                }
            }
        } while (option != 0);
    }

    private void handleAddProductRequest(int option) {
        switch (option) {
            case 1 -> {
                storeView.showStorageRegister(storeManager.getStorage());
                storeView.showAddNewIngredientMessage();
                handleAddIngredientRequest(storeView.readOption(0, storeManager.getLastId(storeManager.getStorage())));
            }
            case 2 -> {
                storeView.showNewDrinkMessage();
                storeView.showDrinksRegister(storeManager.getDrinks());
                handleAddDrinkRequest(storeView.readOption(0, storeManager.getLastId(storeManager.getDrinks())));
            }
            case 3 -> {
                storeView.showNewExtraMessage();
                storeView.showExtrasRegister(storeManager.getExtras());
                handleAddExtraRequest(storeView.readOption(0, storeManager.getLastId(storeManager.getExtras())));
            }
        }
    }

    public void handleAddIngredientRequest(int option) {
        storeView.newLine();
        if (option == 0) {
            String name = storeView.readName();
            for (GenericIngredient ingredient : storeManager.getStorage())
                if (ingredient.getName().equalsIgnoreCase(name)) {
                    storeView.showAlreadyExistsMessage();
                    storeView.waitForEnter();
                    return;
                }
            float quantity = storeView.readQuantityIncrement();
            String unit = storeView.readUnit();
            storeManager.addNewIngredient(new GenericIngredient(name, quantity, unit));
        } else {
            float increment = storeView.readQuantityIncrement();
            storeManager.updateProduct("ingredient", option, increment);
        }
        storeView.showSuccessfulOperationMessage();
    }

    public void handleAddDrinkRequest(int option) {
        storeView.newLine();
        float increment = storeView.readQuantityIncrement();
        storeManager.updateProduct("drink", option, increment);
        storeView.showSuccessfulOperationMessage();
    }

    public void handleAddExtraRequest(int option) {
        storeView.newLine();
        float increment = storeView.readQuantityIncrement();
        storeManager.updateProduct("extra", option, increment);
        storeView.showSuccessfulOperationMessage();
    }

}
