package com.example.snackoverflow;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

public class SortComparatorTest {
    private Recipe MockRecipe(String title, int prepTime, float servings, String category){
        Recipe recipe = new Recipe(title, prepTime, servings, category, "comments", "instructions", new ArrayList<Ingredient>());
        return recipe;
    }

    private Ingredient MockIngredients(String title, Date date, String location,  String category){
        Ingredient ingredient = new Ingredient(title, date, location, 1, 1, category);
        return ingredient;
    }

    @Test
    public void testCompareRecipe() {
        Recipe recipe1 = MockRecipe("atitle", 1, 1.0F, "acat");
        Recipe recipe2 = MockRecipe("btitle", 2, 2.0F, "bcat");

        Assert.assertTrue(new SortComparator.TitleComparator().compare(recipe1, recipe2)< 0);
        Assert.assertTrue(new SortComparator.TitleComparator().compare(recipe2, recipe1)> 0);

        Assert.assertTrue(new SortComparator.PrepTimeComparator().compare(recipe1, recipe2) < 0);
        Assert.assertTrue(new SortComparator.PrepTimeComparator().compare(recipe2, recipe1) > 0);

        Assert.assertTrue(new SortComparator.ServingsComparator().compare(recipe1, recipe2)<0);
        Assert.assertTrue(new SortComparator.ServingsComparator().compare(recipe2, recipe1)>0);

        Assert.assertTrue(new SortComparator.CategoryComparator().compare(recipe1, recipe2) < 0);
        Assert.assertTrue(new SortComparator.CategoryComparator().compare(recipe2, recipe1)> 0);
    }

    @Test
    public void testCompareIngredients() {
        Ingredient ingredient1 = MockIngredients("atitle", new Date(), "Fridge", "acat");
        Ingredient ingredient2 = MockIngredients("btitle", new Date(new Date().getTime() + (86400000)), "Pantry", "bcat");

        Assert.assertTrue(new SortComparator.TitleComparator().compare(ingredient1, ingredient2)< 0);
        Assert.assertTrue(new SortComparator.TitleComparator().compare(ingredient2, ingredient1)> 0);

        Assert.assertTrue(new SortComparator.BestBeforeComparator().compare(ingredient1, ingredient2) < 0);
        Assert.assertTrue(new SortComparator.BestBeforeComparator().compare(ingredient2, ingredient1) > 0);

        Assert.assertTrue(new SortComparator.LocationComparator().compare(ingredient1, ingredient2)<0);
        Assert.assertTrue(new SortComparator.LocationComparator().compare(ingredient2, ingredient1)>0);

        Assert.assertTrue(new SortComparator.CategoryComparator().compare(ingredient1, ingredient2) < 0);
        Assert.assertTrue(new SortComparator.CategoryComparator().compare(ingredient2, ingredient1)> 0);
    }

}
