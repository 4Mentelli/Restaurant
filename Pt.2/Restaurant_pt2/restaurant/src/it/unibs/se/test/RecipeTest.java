package it.unibs.test;

import it.unibs.se.model.Date;
import it.unibs.se.model.GenericIngredient;
import it.unibs.se.model.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class RecipeTest {

    private Recipe recipe;

    @BeforeEach
    void setUp() {
        // Inizializza una ricetta base per i test
        recipe = new Recipe("Carbonara", 1, 30, new ArrayList<>(), new Date("21/01/2025"), new Date("23/12/2025"));
    }

    @Test
    void testRecipeCreation() {
        // Verifica che la ricetta sia stata creata correttamente
        assertEquals("Carbonara", recipe.getName());
        assertEquals(1, recipe.getId());
        assertEquals(30, recipe.getWorkload());
        assertEquals(0, recipe.getIngredients().size()); // Nessun ingrediente all'inizio
        assertEquals("20/01/2025", recipe.getStartDate().getString());
        assertEquals("23/12/2025", recipe.getEndDate().getString());
    }

    @Test
    void testAddIngredients() {
        // Aggiungi ingredienti alla ricetta
        GenericIngredient ingredient1 = new GenericIngredient("Pasta", 200, "grammi");
        GenericIngredient ingredient2 = new GenericIngredient("Guanciale", 150, "grammi");

        recipe.addIngredients(ingredient1);
        recipe.addIngredients(ingredient2);

        // Verifica che gli ingredienti siano stati aggiunti
        assertEquals(2, recipe.getIngredients().size());
        assertEquals("Pasta", recipe.getIngredients().get(0).getName());
        assertEquals("Guanciale", recipe.getIngredients().get(1).getName());
    }


}
