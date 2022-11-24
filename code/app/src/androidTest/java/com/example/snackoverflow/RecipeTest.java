package com.example.snackoverflow;

import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RecipeTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<RecipeActivity> rule =
            new ActivityTestRule<>(RecipeActivity.class, true, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    @Test
    public void step1_checkRecipe() {
        View fab = rule.getActivity().findViewById(R.id.add_recipe_button);
        solo.assertCurrentActivity("Wrong Activity", RecipeActivity.class);
        solo.clickOnView(fab);
        // Check if switch to new activity occurs
        assertTrue("Failed to switch to AddRecipe activity",
                solo.waitForActivity(AddRecipe.class, 2000));
    }

    @Test
    public void step2_checkAddRecipe() {
        step1_checkRecipe();
        View addIngredient = solo.getView("recipe_add_ingredient");
        View addRecipe = solo.getView("recipe_add_recipe");

        // fill in recipe details
        solo.enterText((EditText) solo.getView(R.id.edit_recipe_title), "Pasta");
        solo.enterText((EditText) solo.getView(R.id.edit_recipe_category), "Carbs");
        solo.enterText((EditText) solo.getView(R.id.edit_recipe_servings), "3");
        solo.enterText((EditText) solo.getView(R.id.edit_preptime), "30");
        solo.enterText((EditText) solo.getView(R.id.edit_recipe_instructions), "Add sauce to pasta.");
        solo.enterText((EditText) solo.getView(R.id.edit_recipe_comments), "Eat it warm.");

        solo.clickOnView(solo.getView(R.id.recipe_add_ingredient));
        solo.clickOnView(addIngredient);
        solo.waitForFragmentByTag("Add_Ingredient",2000);

        // Fill in ingredient fragment
        solo.typeText((EditText)solo.getView("edit_ingredient_description"),"Tomato");
        solo.typeText((EditText)solo.getView("edit_ingredient_amount"),"11");
        solo.typeText((EditText)solo.getView("edit_ingredient_unit"),"13");
        solo.typeText((EditText)solo.getView("edit_ingredient_category"),"vegetable");

        // Add ingredient
        solo.clickOnButton("Ok");
        // Click add recipe button
        solo.clickOnView(addRecipe);

        // Check that the Recipe is added to list of Recipes
        assertTrue(solo.waitForText("Pasta", 1, 6000));
    }

    @Test
    public void step3_checkModifyRecipe() {
        ListView listView = (ListView)solo.getView(R.id.recipe_list);
        View view = listView.getChildAt(0);
        solo.clickOnView(view);

        View editRecipe = solo.getView("edit_recipe_button");
        solo.clickOnView(editRecipe);

        // wait for element to be visible
        solo.sleep(1000);

        solo.clearEditText((EditText) solo.getView(R.id.edit_recipe_title));
        solo.enterText((EditText) solo.getView(R.id.edit_recipe_title), "Soup");

        View applyRecipeChanges = solo.getView("edit_recipe_button");
        solo.clickOnView(applyRecipeChanges);





    }


    // check show more ingredients



    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

}