package it.unibs.test;


import it.unibs.se.model.GenericIngredient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IngredientTest {
    @Test
    void testIngredientCreation(){
        var ingredient = new GenericIngredient("Pasta", 1, "grammi");
        assertEquals("Pasta", ingredient.getName());
        assertEquals(1, ingredient.getQuantity());
        assertEquals("grammi", ingredient.getUnitOfMeasure());
    }

}
