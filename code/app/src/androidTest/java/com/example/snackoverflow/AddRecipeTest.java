package com.example.snackoverflow;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class AddRecipeTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<AddRecipe> rule =
            new ActivityTestRule<>(AddRecipe.class, true, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    @Test
    public void checkAddRecipe() {
        View fab = rule.getActivity().findViewById(R.id.recipe_add_ingredient);
        solo.assertCurrentActivity("Wrong Activity", AddRecipe.class);
        solo.enterText((EditText) solo.getView(R.id.edit_recipe_title), "Pasta");
        solo.enterText((EditText) solo.getView(R.id.edit_recipe_category), "Carbs");
        solo.enterText((EditText) solo.getView(R.id.edit_recipe_servings), "3");
        solo.enterText((EditText) solo.getView(R.id.edit_preptime), "30");
        solo.enterText((EditText) solo.getView(R.id.edit_recipe_instructions), "Add sauce to pasta.");
        solo.enterText((EditText) solo.getView(R.id.edit_recipe_comments), "Eat it warm.");
        solo.clickOnView(solo.getView(R.id.recipe_add_ingredient));
        solo.clickOnView(fab);
        //View v = rule.getActivity().findViewById(R.id.text_input_description);

        solo.waitForFragmentByTag("Add_Ingredient",6000);
//        Fragment fragment = rule.getActivity().getSupportFragmentManager().findFragmentByTag("Add_Ingredient");
//        System.out.println("this is dww");
//        System.out.println(fragment);
        //solo.enterText((EditText) solo.getView("myDescription"), "Apple");
        EditText description = (EditText)solo.getView("edit_ingredient_description");
        //solo.clickOnView(e2);
        System.out.println("description");
        System.out.println(description);
        solo.enterText(description,"mi");
        solo.enterText(description,"lk");

        EditText amount = (EditText)solo.getView("edit_ingredient_amount");
        //solo.clickOnView(e2);
        System.out.println("amount");
        System.out.println(amount);
        solo.enterText(amount,"1");
        solo.enterText(amount,"1");

        EditText unit = (EditText)solo.getView("edit_ingredient_unit");
        //solo.clickOnView(e2);
        System.out.println("unit");
        System.out.println(unit);
        solo.enterText(unit,"1");
        solo.enterText(unit,"1");

        EditText category = (EditText)solo.getView("edit_ingredient_category");
        //solo.clickOnView(e2);
        System.out.println("category");
        System.out.println(category);
        solo.enterText(category,"yu");
        solo.enterText(category,"um");

        solo.clickOnButton("Ok");
        // Click add recipe button
        solo.clickOnButton(2);
        //Button addIngredientBtn= solo.getButton("ADD");


        //solo.enterText((EditText) solo.getView(R.id.myDescription), "Apple");
        //solo.enterText((EditText) solo.getView(R.id.myUnit), "31");
        //solo.enterText((EditText) solo.getView(R.id.myCategory), "fruit");
        //solo.clickOnText("Add");
        solo.waitForFragmentByTag("Add_Ingredient",5000);
        //rule.getActivity().getSupportFragmentManager().executePendingTransactions();


        //solo.enterText((EditText) solo.getView(R.id.text_input_description).getEditText(), "Pasta");

//        View fab = rule.getActivity().findViewById(R.id.recipe_add_ingredient);


//        View fab2 = rule.getActivity().findViewById(R.id.recipe_add_recipe);
//        solo.clickOnView(fab2);
        //Fragment f = solo.getView(R.id.edit_text_input_description_Fragment);
        //View view = solo.waitForView("Add_Ingredient",1,500));
        //solo.enterText((EditText) solo.waitForView("Add_Ingredient",1,500), "Tomato");
    }

    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

}