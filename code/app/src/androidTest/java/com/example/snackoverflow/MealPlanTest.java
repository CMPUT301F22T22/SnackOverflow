package com.example.snackoverflow;

import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.material.textfield.TextInputLayout;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class MealPlanTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    @Test
    public void checkAddMeal() {
        View fab = rule.getActivity().findViewById(R.id.add_mealplan);
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(fab);
        TextView date = rule.getActivity().findViewById(R.id.text_view_date);
        date.setText("2022-11-03");
        solo.clickOnRadioButton(0);
        Spinner spinner = fab.findViewById(R.id.spinner);
        spinner.setSelection(1);
        String title = (String) spinner.getAdapter().getItem(1);
        solo.enterText(((TextInputLayout)solo.getView(R.id.unit)).getEditText(), "2" );
        solo.clickOnButton("Add");

        assertTrue(solo.waitForText(title, 1, 2000));
    }

}
