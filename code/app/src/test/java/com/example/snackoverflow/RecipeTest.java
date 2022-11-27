package com.example.snackoverflow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RecipeTest {
    private Recipe mockRecipe(){
        Ingredient eggs = new Ingredient("Eggs", new Date(),"Pantry",12,2,"Vegetarian");
        Ingredient milk = new Ingredient("Milk",new Date(),"Fridge",2,2,"Vegetarian");

        Recipe random = new Recipe("Pizza",120,2.5f,"Lunch","Tasty","Boil" );
        random.addIngredient(eggs);
        random.addIngredient(milk);

        return random;
    }

    private Ingredient mockIngredient(){
        Date exp = new Date();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return new Ingredient("marina",new Date(1234567890),"Pantry",1,1,"Misc");
    }

    @Test
    void testAddException(){
        Recipe recipe = mockRecipe();
        assertEquals(2, recipe.getIngredients().size());
        recipe.addIngredient(mockIngredient());
        assertEquals(3,recipe.getIngredients().size());
        assertTrue(recipe.getIngredients().contains(mockIngredient()));
    }

    @Test
    void testremoveIngredient(){
        Recipe recipe = mockRecipe();
        assertThrows(IllegalArgumentException.class,()->{
            recipe.removeIngredient(mockIngredient());
        });
        recipe.addIngredient(mockIngredient());
        recipe.removeIngredient(mockIngredient());
        assertFalse(recipe.getIngredients().contains(mockIngredient()));
    }

}
