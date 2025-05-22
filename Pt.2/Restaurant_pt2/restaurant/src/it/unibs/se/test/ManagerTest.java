package it.unibs.test;

import it.unibs.se.model.GenericIngredient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ManagerTest {

    @BeforeEach
    void setUp() {
        // Resetta i parametri prima di ogni test

    }


    @Test
    void testInitializeParameters() {
        // Simula l'inizializzazione dei parametri


        // Calcola il carico totale


        // Verifica che i parametri siano corretti
        //assertEquals(50, Manager.getN_client());

    }




    @Test
    void testAddAndViewDrinks() {
        // Crea bevande manualmente
        GenericIngredient drink1 = new GenericIngredient("Coca-Cola", 1,  "pezzi");
        GenericIngredient drink2 = new GenericIngredient("Fanta",2, "pezzi");

        List<GenericIngredient> drinks = new ArrayList<>();
        drinks.add(drink1);
        drinks.add(drink2);

        // Verifica il contenuto della lista
        assertEquals(2, drinks.size());
        assertEquals("Coca-Cola", drinks.get(0).getName());
        assertEquals("Fanta", drinks.get(1).getName());
    }

    @Test
    void testAddAndViewExtras() {
        // Crea extra manualmente
        GenericIngredient extra1 = new GenericIngredient("Pane", 1, "litri");
        GenericIngredient extra2 = new GenericIngredient("Grissini", 1, "litri");

        List<GenericIngredient> extras = new ArrayList<>();
        extras.add(extra1);
        extras.add(extra2);

        // Verifica il contenuto della lista
        assertEquals(2, extras.size());
        assertEquals("Pane", extras.get(0).getName());
        assertEquals("Grissini", extras.get(1).getName());
    }
}