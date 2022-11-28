package com.example.snackoverflow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

public class IngredientTest {

    /**
     * Test the ingredient constructor
     */
    @Test
    public void testIngredientConstructor(){
        Date date = new Date();
        Ingredient ingredient = new Ingredient("pasta",date,"Pantry",1,1,"Misc");
        assertEquals("pasta", ingredient.getTitle());
        assertEquals(date, ingredient.getBestBefore());
        assertEquals("Pantry", ingredient.getLocation());
        assertEquals(1, ingredient.getAmount());
        assertEquals(1, ingredient.getUnit());
        assertEquals("Misc", ingredient.getCategory());
    }

    @Test
    public void testIngredientConstructor2(){
        Ingredient ingredient = new Ingredient("pasta",1,1,"Misc");
        assertEquals("pasta", ingredient.getTitle());
        assertEquals(1, ingredient.getAmount());
        assertEquals(1, ingredient.getUnit());
        assertEquals("Misc", ingredient.getCategory());
    }

    @Test
    public void testEquals(){
        Date date = new Date();
        Ingredient ingredient = new Ingredient("pasta",date,"Pantry",1,1,"Misc");
        boolean resultSameIngredient = ingredient.equals(ingredient);
        assertTrue(resultSameIngredient);
        boolean resultNull = ingredient.equals(null);
        assertFalse(resultNull);
        ArrayList<Ingredient> ingredientsForRecipe = new ArrayList<>();
        ingredientsForRecipe.add(new Ingredient("test-ingredient-1", 1, 1, "test-category-1"));
        ingredientsForRecipe.add(new Ingredient("test-ingredient-2", 1, 1,"test-category-2"));
        Recipe recipe = new Recipe("Noodles", 1, 2, "Lunch", "mock-comments", "mock-instructions", ingredientsForRecipe);
        boolean resultDifferentClasses = ingredient.equals(recipe);
        assertFalse(resultDifferentClasses);
        Ingredient ingredient2 = new Ingredient("pasta", date, "Pantry", 2, 1, "Dry Foods");
        boolean resultSameTitle = ingredient.equals(ingredient2);
        assertTrue(resultSameTitle);
    }
}
