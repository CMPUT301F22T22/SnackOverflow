package com.example.snackoverflow;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

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
    public void checkIngredientStorage() {
        View fab = rule.getActivity().findViewById(R.id.add_recipe_button);
        solo.assertCurrentActivity("Wrong Activity", RecipeActivity.class);
        solo.clickOnView(fab);
        solo.enterText((EditText) solo.getView(R.id.edit_recipe_title), "Pasta");
        solo.enterText((EditText) solo.getView(R.id.edit_recipe_category), "Carbs");
        solo.enterText((EditText) solo.getView(R.id.edit_recipe_servings), "3");
        solo.enterText((EditText) solo.getView(R.id.edit_preptime), "30");
        solo.enterText((EditText) solo.getView(R.id.edit_recipe_instructions), "Add sauce to pasta.");
        solo.enterText((EditText) solo.getView(R.id.edit_recipe_comments), "Eat it warm.");
        View fab2 = rule.getActivity().findViewById(R.id.recipe_add_recipe);
        solo.clickOnView(fab2);
        View donebtn = solo.getView(R.id.edit_text_input_description_Fragment);
        solo.enterText((EditText) solo.waitForView(R.id.edit_text_input_description_Fragment,1,500), "Tomato");
        //solo.clickOnButton("OK");
    }


}