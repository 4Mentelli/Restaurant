package it.unibs.test;
import it.unibs.se.model.Date;
import it.unibs.se.model.Menu;
import it.unibs.se.model.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class MenuTest {

    private Menu menu;

    @BeforeEach
    void setUp() {
        // Inizializza un menù base per i test
        menu = new Menu("Menù di Natale", new ArrayList<>(), 50, 53);
    }

    @Test
    void testMenuCreation() {
        // Verifica che il menù sia stato creato correttamente
        assertEquals("Menù di Natale", menu.getName());
        assertEquals(0, menu.getDishes().spliterator().estimateSize()); // Nessuna ricetta all'inizio
        assertEquals(50, menu.getId());
    }

    @Test
    void testAddDishes() {
        // Aggiungi ricette al menù
        Recipe recipe1 = new Recipe("Carbonara", 1, 30, new ArrayList<>(), new Date("20/01/2025"), new Date("23/12/2025"));
        Recipe recipe2 = new Recipe("Amatriciana", 2, 30, new ArrayList<>(), new Date("20/01/2025"), new Date("23/12/2025"));
        ArrayList<Recipe> dishes = new ArrayList<>();
        dishes.add(recipe1);
        dishes.add(recipe2);
        menu = new Menu("Menù di Natale", dishes, 50, 53);

        // Verifica che le ricette siano state aggiunte
        assertEquals(2, menu.getDishes().spliterator().estimateSize());
        assertEquals("Carbonara", dishes.get(0).getName());
        assertEquals("Amatriciana", dishes.get(1).getName());
    }

}
