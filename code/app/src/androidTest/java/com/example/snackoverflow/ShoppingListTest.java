package com.example.snackoverflow;
import com.robotium.solo.Solo;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
 import org.junit.Assert;
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
    public void step1_start() throws Exception {
        Activity activity = rule.getActivity();
    }

    @Test
    public void step2_checkCancelAddIngredient() {
        solo.assertCurrentActivity("Wrong Activity", ShoppingListActivity.class);
        ListView listView = (ListView)solo.getView(R.id.shopping_list_storage_list);
        View view = listView.getChildAt(0);
        ImageButton addButton=(ImageButton)view.findViewById(R.id.add_shopping_list_item);
        solo.clickOnView(addButton);
        EditText title =  (EditText) solo.getView(R.id.ingredient_description_editText);
        String titleText = title.getText().toString();
        solo.clickOnButton("Cancel");
        Assert.assertTrue(solo.waitForText(titleText, 1, 2000));
    }

    @Test
    public void step3_checkAddIngredient() {
        solo.assertCurrentActivity("Wrong Activity", ShoppingListActivity.class);
        solo.sleep(2000);
        ListView listView = (ListView)solo.getView(R.id.shopping_list_storage_list);
        View view = listView.getChildAt(0);
        ImageButton addButton=(ImageButton)view.findViewById(R.id.add_shopping_list_item);
        solo.clickOnView(addButton);
        EditText title =  (EditText) solo.getView(R.id.ingredient_description_editText);
        String titleText = title.getText().toString();
        solo.clickOnRadioButton(0);

        View dateTextBox = solo.getView(R.id.ingredient_bestBefore_editText);
        solo.clickOnView(dateTextBox);
        solo.clickOnButton("Cancel");

        solo.clickOnButton("OK");
        solo.sleep(1000);
        Assert.assertFalse(solo.waitForText(titleText, 1, 2000));
    }

    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
