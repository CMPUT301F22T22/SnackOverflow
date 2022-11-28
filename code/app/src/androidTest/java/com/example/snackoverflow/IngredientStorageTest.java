package com.example.snackoverflow;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.robotium.solo.Solo;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
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
    public void step1_start() throws Exception {
        Activity activity = rule.getActivity();
    }

    @Test
    public void step2_checkAddIngredient() {
        View fab = rule.getActivity().findViewById(R.id.add_ingredient_button);
        solo.assertCurrentActivity("Wrong Activity", IngredientStorageActivity.class);
        solo.clickOnView(fab);
        solo.clickOnRadioButton(0);
        solo.enterText((EditText) solo.getView(R.id.ingredient_description_editText), "Acai");
        solo.enterText((EditText) solo.getView(R.id.ingredient_category_editText), "Baking");
        solo.enterText((EditText) solo.getView(R.id.ingredient_amount_editText), "3");
        solo.enterText((EditText) solo.getView(R.id.ingredient_unit_editText), "1");

        View dateTextBox = solo.getView(R.id.ingredient_bestBefore_editText);
        solo.clickOnView(dateTextBox);
        solo.sleep(1500);
        solo.setDatePicker(0, 2025, 1, 1);
        solo.clickOnButton(3);

        solo.clickOnButton("OK");

        assertTrue(solo.waitForText("Acai", 1, 2000));
    }

    @Test
    public void step3_checkViewIngredient() {
        ListView listView = (ListView)solo.getView(R.id.ingredient_storage_list);
        View view = listView.getChildAt(0);
        solo.clickOnView(view);
        // Check if switch to new activity occurs
        assertTrue("Failed to switch to Ingredients details",
                solo.waitForActivity(IngredientDetailsActivity.class, 2000));

        assertTrue(solo.waitForText("Acai", 1, 2000));
    }

    @Test
    public void step4_checkEditIngredient() {
        ListView listView = (ListView)solo.getView(R.id.ingredient_storage_list);
        View view = listView.getChildAt(0);
        ImageButton editButton=(ImageButton)view.findViewById(R.id.edit_ingredient);
        solo.clickOnView(editButton);
        solo.clearEditText((EditText) solo.getView(R.id.ingredient_description_editText));
        solo.enterText((EditText) solo.getView(R.id.ingredient_description_editText), "Apple");
        solo.clickOnButton("OK");

        assertTrue(solo.waitForText("Apple", 1, 2000));
    }

    @Test
    public void step5_checkDeleteIngredient() {
        ListView listView = (ListView)solo.getView(R.id.ingredient_storage_list);
        View view = listView.getChildAt(0);
        ImageButton deleteButton=(ImageButton)view.findViewById(R.id.delete_ingredient);
        solo.clickOnView(deleteButton);
        solo.waitForFragmentByTag("Delete_Ingredient",6000);
        solo.clickOnButton("Yes");

        assertFalse(solo.waitForText("Apple", 1, 2000));
    }

    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

}

