package com.example.snackoverflow;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class SortComparatorTest {
    private Recipe MockRecipe(String title, int prepTime, float servings, String category){
        Recipe recipe = new Recipe(title, prepTime, servings, category, "comments", "instructions", new ArrayList<Ingredient>());
        return recipe;
    }

    private Ingredient MockIngredients(String title, int amount, int unit, String category){
        Ingredient ingredient = new Ingredient(title, amount, unit, category);
        return ingredient;
    }

    @Test
    public void testCompareRecipe() {
        Recipe recipe1 = MockRecipe("atitle", 1, 1.0F, "acat");
        Recipe recipe2 = MockRecipe("btitle", 2, 2.0F, "bcat");

        Assert.assertEquals(new SortComparator.TitleComparator().compare(recipe1, recipe2), -1);
        Assert.assertEquals(new SortComparator.PrepTimeComparator().compare(recipe2, recipe1), 1);

        Assert.assertEquals(new SortComparator.PrepTimeComparator().compare(recipe1, recipe2), -1);
        Assert.assertEquals(new SortComparator.PrepTimeComparator().compare(recipe2, recipe1), 1);

        Assert.assertEquals(new SortComparator.ServingsComparator().compare(recipe1, recipe2), -1);
        Assert.assertEquals(new SortComparator.ServingsComparator().compare(recipe2, recipe1), 1);

        Assert.assertEquals(new SortComparator.CategoryComparator().compare(recipe1, recipe2), -1);
        Assert.assertEquals(new SortComparator.CategoryComparator().compare(recipe2, recipe1), 1);
    }

}
