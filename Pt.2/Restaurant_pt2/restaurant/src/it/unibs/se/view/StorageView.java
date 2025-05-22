package it.unibs.se.view;

import it.unibs.fp.mylib.InputDati;
import it.unibs.se.model.IdentifiableIngredient;
import it.unibs.se.model.GenericIngredient;

import java.util.List;

public class StorageView implements View {

    @Override
    public void showMenu() {
        showMessage("1 - Visualizza registro");
        showMessage("2 - Aggiungi merci al registro");
        showMessage("3 - Crea lista della spesa");
        showMessage("0 - Menu principale");
    }

    public void showStorageSize(int size) {
        newLine();
        showMessage("Ci sono " + size + " ingredienti nel magazzino");
    }

    public void showDrinksSize(int size) {
        newLine();
        showMessage("Ci sono " + size + " bevande nel magazzino");
    }

    public void showExtrasSize(int size) {
        newLine();
        showMessage("Ci sono " + size + " generi alimentari extra nel magazzino");
    }

    public void showStorageRegister(List<GenericIngredient> storage) {
        newLine();
        int id = 1;
        for (GenericIngredient ingredient : storage) {
            showMessage(id + " - " + ingredient.getName() + " " +  ingredient.getQuantity() + " " + ingredient.getUnitOfMeasure());
            id++;
        }
    }

    public void showDrinksRegister(List<IdentifiableIngredient> drinks) {
        newLine();
        int number = 1;
        for (IdentifiableIngredient drink : drinks) {
            showMessage(number + " - " + drink.getName() + " " +  drink.getQuantity() + " " + drink.getUnitOfMeasure());
            number++;
        }
    }

    public void showExtrasRegister(List<IdentifiableIngredient> extras) {
        newLine();
        int number = 1;
        for (IdentifiableIngredient extra : extras) {
            showMessage(number + " - " + extra.getName() + " " +  extra.getQuantity() + " " + extra.getUnitOfMeasure());
            number++;
        }
    }

    public void showShoppingList(List<GenericIngredient> shopping_list) {
        newLine();
        showMessage("LISTA DELLA SPESA PER LA SETTIMANA CORRENTE:");
        for (GenericIngredient ingredient : shopping_list)
            showMessage(ingredient.getName() + " - " + ingredient.getQuantity() + " " + ingredient.getUnitOfMeasure());
    }

    public void showAddNewIngredientMessage() {
        showMessage("0 - Aggiungi nuovo ingrediente");
    }

    public void showNewDrinkMessage() {
        newLine();
        showMessage("[ Solo il manager può aggiungere nuove bevande ]");
        showMessage("Scegli quale bevanda rifornire:");
    }

    public void showNewExtraMessage() {
        newLine();
        showMessage("[ Solo il manager può aggiungere nuovi generi alimentari extra ]");
        showMessage("Scegli quale genere alimentare extra rifornire:");
    }

    public void showAlreadyExistsMessage() {
        showMessage("L'ingrediente è già presente nel magazzino");
    }

    public float readQuantityIncrement() {
        return InputDati.leggiFloatPositivo("Inserisci la quantità: ");
    }

    public String readUnit() {
        int unit = InputDati.leggiIntero("1 - grammi\n2 - millilitri\n3 - pezzi\nScegli l'unità di misura: ", 1, 3);
        return switch (unit) {
            case 1 -> "g";
            case 2 -> "ml";
            default -> "pezzi";
        };
    }

    public String readName() {
        return InputDati.leggiStringaNonVuota("Inserisci il nome dell'ingrediente: ");
    }

    public void showStorageOptions() {
        newLine();
        showMessage("1 - Ingredienti");
        showMessage("2 - Bevande");
        showMessage("3 - Generi alimentari extra");
    }
}
