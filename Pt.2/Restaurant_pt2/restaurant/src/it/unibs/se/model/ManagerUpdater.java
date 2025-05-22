package it.unibs.se.model;

public interface ManagerUpdater {
    void parametersWriter(int totalSeats, int workloadForClient, int drinksPerPerson, int extrasPerPerson) throws Exception;
    void recipeWriter(Recipe recipe) throws Exception;
    void drinkWriter(IdentifiableIngredient drink) throws Exception;
    void extraWriter(IdentifiableIngredient extra) throws Exception;
    void menuWriter(Menu menu) throws Exception;
    void deleteElement(String type, int id) throws Exception;
}
