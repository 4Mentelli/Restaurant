package it.unibs.se.view;

import it.unibs.fp.mylib.InputDati;
import it.unibs.se.model.*;

import java.util.List;

public class ManagerView implements View {

    @Override
    public void showMenu() {
        showMessage("1 - Gestisci Ricette");
        showMessage("2 - Gestisci Bevande");
        showMessage("3 - Gestisci Extra");
        showMessage("4 - Gestisci Menù Tematici");
        showMessage("5 - Visualizza proprietà Ristorante");
        showMessage("0 - Menu principale");
    }

    public void showRecipesMenu() {
        newLine();
        showMessage("1 - Visualizza Ricette");
        showMessage("2 - Aggiungi Ricetta");
        showMessage("3 - Rimuovi Ricetta");
    }

    public void showDrinksMenu() {
        newLine();
        showMessage("1 - Visualizza Bevande");
        showMessage("2 - Aggiungi Bevanda");
        showMessage("3 - Rimuovi Bevanda");
    }

    public void showExtrasMenu() {
        newLine();
        showMessage("1 - Visualizza Extra");
        showMessage("2 - Aggiungi Extra");
        showMessage("3 - Rimuovi Extra");
    }

    public void showThemedMenusMenu() {
        newLine();
        showMessage("1 - Visualizza Menù Tematici");
        showMessage("2 - Aggiungi Menù Tematico");
        showMessage("3 - Rimuovi Menù Tematico");
    }

    public void showRestaurantProperties(int[] parameters, int workload_for_restaurant) {
        newLine();
        showMessage("Numero di posti a sedere: " + parameters[0]);
        showMessage("Carico di lavoro per persona: " + parameters[1]);
        showMessage("Carico di lavoro del ristorante: " + workload_for_restaurant);
        showMessage("Consumo pro capite di bevande: " + parameters[2]);
        showMessage("Consumo pro capite di generi alimentari extra: " + parameters[3]);
    }

    public void showRecipeSize(int size) {
        newLine();
        showMessage("Ci sono " + size + " ricette");
    }

    public void showMenuSize(int size) {
        newLine();
        showMessage("Ci sono " + size + " menù");
    }

    public void showDrinksSize(int size) {
        newLine();
        showMessage("Ci sono " + size + " bevande");
    }

    public void showExtrasSize(int size) {
        newLine();
        showMessage("Ci sono " + size + " generi alimentari extra");
    }

    public void showWorkloadLimitMessage() {
        showMessage("Il piatto non può essere inserito perché supera il carico di lavoro per una persona");
        newLine();
    }

    public void showNoRecipesMessage() {
        showMessage("Non sono presenti ricette inserite dal gestore, non è possibile creare un menù tematico!");
    }

    public void showInvalidDateRangeMessage() {
        showMessage("La data di inizio è successiva alla data di fine. Riprova.");
    }

    public void showTemporaryMenu(List<Recipe> dishes) {
        if (!dishes.isEmpty()) {
            newLine();
            showMessage("[ Piatti aggiunti ]");
            printRecipesList(dishes);
            newLine();
        }
    }

    public void showCancelOption() {
        showMessage("0 - ANNULLA");
    }

    public void printAllRecipes(List<Recipe> recipes) {
        for (Recipe recipe : recipes)
            printRecipe(recipe);
    }

    public void printRecipe(Recipe recipe) {
        newLine();
        showMessage("RICETTA " + recipe.getId());
        showMessage("Nome: " + recipe.getName());
        showMessage("Ingredienti:");
        for (GenericIngredient ingredient : recipe.getIngredients())
            showMessage("- " + ingredient.getName() + " " + ingredient.getQuantity() + " " + ingredient.getUnitOfMeasure());
        showMessage("Carico di lavoro: " + recipe.getWorkload());
        showMessage("Dal " + recipe.getStartDate().getString() + " al " + recipe.getEndDate().getString() + "\n");
    }

    public void printRecipesList(List<Recipe> recipes) {
        for (Recipe recipe : recipes)
            showMessage(recipe.getId() + " - " + recipe.getName());
    }

    public void printAllMenus(List<Menu> menus) {
        for (Menu menu : menus)
            printAllThemedMenuInfo(menu);
    }

    public void printMenusList(List<Menu> menus) {
        newLine();
        for (Menu menu : menus)
            showMessage(menu.getId() + " - " + menu.getName());
    }

    private void printIdentifiable(IdentifiableIngredient ingredient) {
        showMessage(ingredient.getId() + " - " + ingredient.getName());
    }

    public void printAllDrinks(List<IdentifiableIngredient> drinks) {
        newLine();
        for (IdentifiableIngredient drink : drinks)
            printIdentifiable(drink);
    }

    public void printAllExtras(List<IdentifiableIngredient> extras) {
        newLine();
        for (IdentifiableIngredient extra : extras)
            printIdentifiable(extra);
    }

    public void printAllRecipesWorkload(List<Recipe> all_recipes) {
        for (Recipe recipe : all_recipes)
            showMessage(recipe.getId() + " - " + recipe.getName() + " - [carico di lavoro: " + recipe.getWorkload() + "]");
        showMessage("0 - Termina Menù");
    }

    public String readNotEmptyRecipeName() {
        return InputDati.leggiStringaNonVuota("Inserisci il nome della nuova ricetta: ");
    }

    public String readNotEmptyDrinkName() {
        return InputDati.leggiStringaNonVuota("Inserisci il nome della nuovo drink: ");
    }

    public String readNotEmptyExtraName() {
        return InputDati.leggiStringaNonVuota("Inserisci il nome del nuovo extra: ");
    }

    public String readNotEmptyMenuName() {
        return InputDati.leggiStringaNonVuota("Inserisci il nome del nuovo menù tematico: ");
    }

    public int readRecipeToAddToMenu(List<Recipe> all_recipes) {
        return InputDati.leggiIntero("Scegli l'id del piatto da inserire nel menù tematico: ", 0, all_recipes.size());
    }

    public int readNewRecipePeriodOption() {
        return InputDati.leggiIntero("Il piatto è disponibile per tutto l'anno? (1 - Si; 2 - No): ", 1, 2);
    }

    public String readStartDate() {
        return String.valueOf(InputDati.leggiData("Disponibile da: "));
    }

    public String readEndDate() {
        return String.valueOf(InputDati.leggiData("Fino a: "));
    }

    public String readIngredientName() {
        return InputDati.leggiStringaNonVuota("Inserisci un ingrediente (oppure 'N' per terminare): ");
    }

    public float readIngredientQuantity() {
        return InputDati.leggiFloatPositivo("Inserisci la sua quantità: ");
    }

    public String readIngredientUnit() {
        showMessage("1 - grammi\n2 - millilitri\n3 - pezzi");
        int unit_int = InputDati.leggiIntero("Scegli l'unità di misura: ", 1, 3);
        return switch (unit_int) {
            case 1 -> "g";
            case 2 -> "ml";
            default -> "pezzi";
        };
    }

    public int[] readParameters() {
        int[] parameters_array= new int[4];
        newLine();
        showMessage("Benvenuto! Prima di iniziare, inserisci i seguenti parametri del ristorante");
        parameters_array[0] = InputDati.leggiInteroPositivo("Numero di posti a sedere: ");
        parameters_array[1] = InputDati.leggiInteroPositivo("Carico di lavoro per persona (un valore di circa 40 indica un carico di lavoro normale): ");
        parameters_array[2] = InputDati.leggiInteroNonNegativo("Consumo pro capite di bevande: ");
        parameters_array[3] = InputDati.leggiInteroNonNegativo("Consumo pro capite di generi alimentari extra: ");
        return parameters_array;
    }

}
