package com.example.snackoverflow;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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
    public void step1_start() throws Exception {
        Activity activity = rule.getActivity();
    }

    @Test
    public void step2_checkRecipe() {
        View fab = rule.getActivity().findViewById(R.id.add_recipe_button);
        solo.assertCurrentActivity("Wrong Activity", RecipeActivity.class);
        solo.clickOnView(fab);
        // Check if switch to new activity occurs
        assertTrue("Failed to switch to AddRecipe activity",
                solo.waitForActivity(AddRecipe.class, 2000));
    }

    @Test
    public void step3_checkShowIngredients() {
        step2_checkRecipe();
        View addIngredient = solo.getView("recipe_add_ingredient");
        solo.clickOnView(addIngredient);

        solo.waitForFragmentByTag("Add_Ingredient",2000);

        // Fill in ingredient fragment
        solo.typeText((EditText)solo.getView("edit_ingredient_description"),"Tomato");
        solo.typeText((EditText)solo.getView("edit_ingredient_amount"),"11");
        solo.typeText((EditText)solo.getView("edit_ingredient_unit"),"13");
        solo.typeText((EditText)solo.getView("edit_ingredient_category"),"vegetable");

        // Add ingredient
        solo.clickOnButton("Ok");

        View showMore = solo.getView("recipe_showmore");
        solo.clickOnView(showMore);

        assertTrue(solo.waitForText("Tomato", 1, 6000));
    }


    @Test
    public void step4_checkEditIngredient() {
        step3_checkShowIngredients();
        ListView listView = (ListView)solo.getView(R.id.list);
        View view = listView.getChildAt(0);
        ImageButton editButton=(ImageButton)view.findViewById(R.id.edit_ingredient);
        solo.clickOnView(editButton);
        solo.clearEditText((EditText) solo.getView("edit_ingredient_description"));

        solo.typeText((EditText)solo.getView("edit_ingredient_description"),"Onion");
        solo.clickOnButton("Ok");

        assertTrue(solo.waitForText("Onion", 1, 6000));
    }

    @Test
    public void step5_checkDeleteIngredient() {
        step3_checkShowIngredients();
        ListView listView = (ListView)solo.getView(R.id.list);
        System.out.println("this is the initial sice");
        System.out.println( listView.getAdapter().getCount() );
        View view = listView.getChildAt(0);
        ImageButton deleteButton=(ImageButton)view.findViewById(R.id.delete_ingredient);
        solo.clickOnView(deleteButton);
        solo.clickOnButton("Yes");

        solo.sleep(1000);
        
        // check if list is empty
        Boolean deleted = 0 == listView.getAdapter().getCount();

        // Make sure ingredient is no longer there
        assertTrue(deleted);
    }

    @Test
    public void step6_checkAddRecipe() {
        step2_checkRecipe();
        View addIngredient = solo.getView("recipe_add_ingredient");
        View addRecipe = solo.getView("recipe_add_recipe");

        // fill in recipe details
        solo.enterText((EditText) solo.getView(R.id.edit_recipe_title), "Anchellini");
        solo.enterText((EditText) solo.getView(R.id.edit_recipe_category), "Carbs");
        solo.enterText((EditText) solo.getView(R.id.edit_recipe_servings), "3");
        solo.enterText((EditText) solo.getView(R.id.edit_preptime), "30");
        solo.enterText((EditText) solo.getView(R.id.edit_recipe_instructions), "Add sauce to pasta.");
        solo.enterText((EditText) solo.getView(R.id.edit_recipe_comments), "Eat it warm.");

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
        assertTrue(solo.waitForText("Anchellini", 1, 6000));
    }

    @Test
    public void step7_checkModifyRecipe() {
        ListView listView = (ListView)solo.getView(R.id.recipe_list);
        View view = listView.getChildAt(0);
        solo.clickOnView(view);

        View editRecipe = solo.getView("edit_recipe_button");
        solo.clickOnView(editRecipe);

        // wait for element to be visible
        solo.sleep(1000);

        solo.clearEditText((EditText) solo.getView(R.id.edit_recipe_title));
        solo.enterText((EditText) solo.getView(R.id.edit_recipe_title), "Alfabeto");

        View applyRecipeChanges = solo.getView("apply_recipe_button");
        solo.clickOnView(applyRecipeChanges);

        // Confirm that the Recipe is edited
        assertTrue(solo.waitForText("Alfabeto", 1, 6000));
    }

    @Test
    public void step8_checkDeleteRecipe() {
        ListView listView = (ListView)solo.getView(R.id.recipe_list);
        View view = listView.getChildAt(0);
        solo.clickOnView(view);

        View deleteRecipe = solo.getView("delete_recipe_button");
        solo.clickOnView(deleteRecipe);

        assertFalse(solo.waitForText("Alfabeto", 1, 5000));
    }

    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}