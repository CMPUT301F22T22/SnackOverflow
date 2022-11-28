package com.example.snackoverflow;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

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
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MealPlannerTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void step1_start() throws Exception {
        Activity activity = rule.getActivity();
    }

    @Test
    public void step2_checkAddRecipeMeal() {

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        View fab = rule.getActivity().findViewById(R.id.add_mealplan);
        solo.clickOnView(fab);

        solo.clickOnRadioButton(0);
        solo.pressSpinnerItem(0,2);
        Spinner spinner = (Spinner)solo.getView("spinner");
        String text = spinner.getSelectedItem().toString();

        solo.enterText(((EditText)solo.getView("unit_add_meal")), "2" );

        View dateView = solo.getView("text_view_date");
        solo.clickOnView(dateView);
        solo.clickOnButton("OK");

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String dayText = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());

        solo.clickOnButton("Add");

        solo.sleep(1500);
        solo.clickOnText(dayText);

        assertTrue(solo.waitForText(text, 1, 2000));
    }

    @Test
    public void step3_checkDeleteRecipeMeal() {

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String dayText = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());

        solo.clickOnText(dayText);

        View mealImg = solo.getView("meal_image");
        solo.clickOnView(mealImg);

        View title = solo.getView("meal_title");
        String titleText = ((TextView)title).getText().toString();
        solo.clickOnButton("Delete");

        assertFalse(solo.waitForText(titleText, 1, 2000));
    }

    @Test
    public void step4_checkAddIngredientMeal() {
        View fab = rule.getActivity().findViewById(R.id.add_mealplan);
        solo.clickOnView(fab);

        solo.clickOnRadioButton(1);

        solo.sleep(500);
        solo.typeText((EditText) solo.getView("ingredient_desc_edit"), "Apple");
        solo.typeText((EditText) solo.getView("unit_add_meal"), "2");
        solo.typeText((EditText) solo.getView("mealplanner_category_text_edit"), "fruit");
        solo.typeText((EditText) solo.getView("mealplanner_amount_text_edit"), "1");

        View dateView = solo.getView("text_view_date");
        solo.clickOnView(dateView);
        solo.clickOnButton("OK");

        solo.clickOnButton("Add");

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String dayText = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());

        solo.sleep(1500);
        solo.clickOnText(dayText);

        assertTrue(solo.waitForText("Apple", 1, 2000));
    }

    @Test
    public void step5_checkDeleteIngredientMeal() {

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String dayText = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());

        solo.clickOnText(dayText);

        View mealImg = solo.getView("meal_image");
        solo.clickOnView(mealImg);
        solo.clickOnButton("Delete");

        assertFalse(solo.waitForText("Apple", 1, 2000));
    }
    
    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

}

