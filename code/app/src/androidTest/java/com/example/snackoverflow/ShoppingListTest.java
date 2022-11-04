package com.example.snackoverflow;
import com.robotium.solo.Solo;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;

public class ShoppingListTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<ShoppingListActivity> rule =
            new ActivityTestRule<>(ShoppingListActivity.class, true, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    @Test
    public void checkList() {
        View fab = rule.getActivity().findViewById(R.id.add_to_shopping_list_button);
        solo.assertCurrentActivity("Wrong Activity", ShoppingListActivity.class);
        solo.clickOnView(fab);
        solo.enterText((EditText) solo.getView(R.id.shopping_item_description_editText), "Butter");
        //solo.clearEditText((EditText) solo.getView(R.id.shopping_item_description_editText));
        //assertFalse(solo.waitForText("Butter"));
        //solo.enterText((EditText) solo.getView(R.id.shopping_item_description_editText), "Butter");
        solo.enterText((EditText) solo.getView(R.id.shopping_item_category_editText), "Baking");
        solo.enterText((EditText) solo.getView(R.id.shopping_item_amount_editText), "3");
        solo.enterText((EditText) solo.getView(R.id.shopping_item_unit_editText), "1");
        solo.clickOnButton("OK");

    }

    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
