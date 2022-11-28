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
        MockRecipe.add(new Recipe("Margherita", 120,2.5f,"Breakfast","Great with ranch","Heat", new ArrayList<Ingredient>()));
        MockRecipe.add(new Recipe("Chicken Tikka", 120,2.5f,"Lunch"," Nice curry with roti","Boil" ,new ArrayList<Ingredient>()));
        MockRecipe.add(new Recipe("Samosa", 120,2.5f,"Dinner","Great for snacks","Heat" ,new ArrayList<Ingredient>() ));

        Mealday MockMeal = null;
        try {
            ArrayList<Double> servingsforEach = new ArrayList<>();
            servingsforEach.add(1.0);
            servingsforEach.add(2.0);
            servingsforEach.add(3.0);
            MockMeal = new Mealday(dateFormat.parse("2022-10-21"),MockRecipe,servingsforEach);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return MockMeal;
    }
    private Recipe mockRecipe(){
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        Ingredient eggs = new Ingredient("Eggs", new Date(),"Pantry",12,2,"Vegetarian");
        Ingredient milk = new Ingredient("Milk",new Date(),"Fridge",2,2,"Vegetarian");
        Recipe random = new Recipe("Pizza",120,2.5f,"Lunch","Tasty","Boil",ingredients);
        return random;
    }
    @Test
    public void testAddException(){
        Mealday MockMeal = MockMeal();
        assertEquals(3, MockMeal.getMeals().size());
        MockMeal.getMeals().add(mockRecipe());
        assertEquals(4, MockMeal.getMeals().size());
        assertTrue(MockMeal.getMeals().contains(mockRecipe()));
    }
    @Test
    public void testRemoveException(){
        Mealday MockMeal = MockMeal();
        assertEquals(3, MockMeal.getMeals().size());
        MockMeal.getMeals().add(mockRecipe());
        MockMeal.getMeals().remove(mockRecipe());
        assertFalse(MockMeal.getMeals().contains(mockRecipe()));
    }
}
