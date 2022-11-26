package com.example.snackoverflow;

import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

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
        TextView date = fab.findViewById(R.id.text_view_date);
        date.setText("2022-11-03");
        solo.clickOnRadioButton(0);
        onView()
        solo.clickOnButton("Add");

        assertTrue(solo.waitForText("Butter", 1, 2000));
    }

}
