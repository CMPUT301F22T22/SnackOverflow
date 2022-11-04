package com.example.snackoverflow;
import com.robotium.solo.Solo;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;

public class IngredientStorageTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<IngredientStorageActivity> rule =
            new ActivityTestRule<>(IngredientStorageActivity.class, true, true);

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
        View fab = rule.getActivity().findViewById(R.id.add_ingredient_button);
        solo.assertCurrentActivity("Wrong Activity", IngredientStorageActivity.class);
        solo.clickOnView(fab);
        solo.enterText((EditText) solo.getView(R.id.ingredient_description_editText), "Butter");
        solo.enterText((EditText) solo.getView(R.id.ingredient_category_editText), "Baking");
        solo.enterText((EditText) solo.getView(R.id.ingredient_amount_editText), "3");
        solo.enterText((EditText) solo.getView(R.id.ingredient_location_editText), "Fridge");
        solo.enterText((EditText) solo.getView(R.id.ingredient_unit_editText), "1");
        solo.enterText((EditText) solo.getView(R.id.ingredient_bestBefore_editText), "2022-09-08");
        solo.clickOnButton("OK");
    }

    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

}
