package com.example.snackoverflow;
import static org.junit.Assert.assertTrue;

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
import android.widget.ListView;

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

        assertTrue(solo.waitForText("Butter", 1, 2000));
    }

//    @Test
//    public void checkDeleteIngredient() {
//        checkIngredientStorage();
//        ListView listView = (ListView)solo.getView(R.id.ingredient_storage_list);
//        View view = listView.getChildAt(0);
//        solo.clickOnView(view);
//
//
//    }

    @Test
    public void checkViewIngredient() {
        checkIngredientStorage();
        ListView listView = (ListView)solo.getView(R.id.ingredient_storage_list);
        View view = listView.getChildAt(0);
        solo.clickOnView(view);
        // Check if switch to new activity occurs
        assertTrue("Failed to switch to Ingredients details",
                solo.waitForActivity(IngredientDetailsActivity.class, 2000));
    }

    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

}

