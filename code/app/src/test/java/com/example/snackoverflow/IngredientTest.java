package com.example.snackoverflow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Date;

public class IngredientTest {

    /**
     * Test the ingredient constructor
     */
    @Test
    public void testIngredientConstructor(){
        Ingredient ingredient = new Ingredient("pasta",new Date(1234567890),"Pantry",1,1,"Misc");
        assertEquals("pasta", ingredient.getTitle());
        assertEquals(new Date(1234567890), ingredient.getBestBefore());
        assertEquals("Pantry", ingredient.getLocation());
        assertEquals(1, ingredient.getAmount());
        assertEquals(1, ingredient.getUnit());
        assertEquals("Misc", ingredient.getCategory());
    }
}
