package com.example.snackoverflow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MealdayTest {
    private Mealday MockMeal(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        ArrayList<Recipe> MockRecipe = new ArrayList<Recipe>();
        MockRecipe.add(new Recipe("nidal", 120,2.5f,"Lunch","nice","Heat" ));
        MockRecipe.add(new Recipe("nidal", 120,2.5f,"Lunch","nice","Heat" ));
        MockRecipe.add(new Recipe("nidal", 120,2.5f,"Lunch","nice","Heat" ));
        MockRecipe.add(new Recipe("nidal", 120,2.5f,"Lunch","nice","Heat" ));
        MockRecipe.add(new Recipe("nidal", 120,2.5f,"Lunch","nice","Heat" ));
        MockRecipe.add(new Recipe("nidal", 120,2.5f,"Lunch","nice","Heat" ));

        Mealday MockMeal = null;
        try {
            MockMeal = new Mealday(dateFormat.parse("2022-10-21"),MockRecipe);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return MockMeal;
    }
    private Recipe mockRecipe(){
        Ingredient eggs = new Ingredient("Eggs", new Date(),"Pantry",12,2,"Vegetarian");
        Ingredient milk = new Ingredient("Milk",new Date(),"Fridge",2,2,"Vegetarian");

        Recipe random = new Recipe("Pizza",120,2.5f,"Lunch","Tasty","Boil" );
        random.addIngredient(eggs);
        random.addIngredient(milk);

        return random;
    }
    @Test
    public void testAddException(){
        Mealday MockMeal = MockMeal();
        assertEquals(6, MockMeal.getMeals().size());
        MockMeal.getMeals().add(mockRecipe());
        assertEquals(7, MockMeal.getMeals().size());
        assertTrue(MockMeal.getMeals().contains(mockRecipe()));
    }
    @Test
    public void testRemoveException(){
        Mealday MockMeal = MockMeal();
        assertEquals(6, MockMeal.getMeals().size());
        MockMeal.getMeals().add(mockRecipe());
        MockMeal.getMeals().remove(mockRecipe());
        assertFalse(MockMeal.getMeals().contains(mockRecipe()));
    }
}
