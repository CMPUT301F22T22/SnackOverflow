package com.example.snackoverflow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;

/**
 * The main activity or the home page of the app
 * extends AppCompatActivity
 * implements MealPlannerAddMeal.OnFragmentInteractionListener
 * @see Mealday
 * @see MealdayAdapter
 * @see MealPlannerAddMeal
 * */
public class MainActivity extends AppCompatActivity implements MealPlannerAddMeal.OnFragmentInteractionListener{

    ExpandableListView mealList;
    ArrayList<Mealday> meals = new ArrayList<>();
    ExpandableListAdapter mealdayAdapter;
    TextView week;

    private void navbarSetup() {
        NavigationBarView navigationBarView=findViewById(R.id.bottom_navigation);
        navigationBarView.setSelectedItemId(R.id.mealplanner);
        navigationBarView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.mealplanner:
                        return true;
                    case R.id.shoppinglist:
                        startActivity(new Intent(getApplicationContext(),ShoppingListActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.ingredients:
                        startActivity(new Intent(getApplicationContext(),IngredientStorageActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.recipes:
                        startActivity(new Intent(getApplicationContext(),RecipeActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup Navbar
        navbarSetup();

        //The displays the starting date from which the user is able to create mealplans for
        week = findViewById(R.id.date_textview);
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("MMM dd");
        String startDate = dateFormat.format(cal.getTime());
        cal.add(Calendar.DAY_OF_MONTH,6);
        String endDate = dateFormat.format(cal.getTime());
        week.setText(startDate+" - "+endDate);
        mealList = (ExpandableListView) findViewById(R.id.mealplanner_list);

        FloatingActionButton addMeal = findViewById(R.id.add_mealplan);

        FragmentManager fm = getSupportFragmentManager();
        mealdayAdapter = new MealdayAdapter(this,meals,fm);
        mealList.setAdapter(mealdayAdapter);

        FirestoreDatabase.fetchMealPlans(mealdayAdapter,meals);


        mealList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;
            @Override
            public void onGroupExpand(int i) {
                if((previousGroup != -1) && (i!=previousGroup)){
                    mealList.collapseGroup(previousGroup);
                }
                previousGroup = i;
            }
        });
        addMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO add recipe data
                new MealPlannerAddMeal().show(getSupportFragmentManager(),"Add_meal");
                mealList.setAdapter(mealdayAdapter);
                FirestoreDatabase.fetchMealPlans(mealdayAdapter,meals);

            }
        });
    }

    /**
     * adds the meal when prompted by the MealPlannerAddMeal Dialog
     * @param recipe the recipe user wants to add to meal
     * @param date the date the user wants to add the meal plan too
     * */
    @Override
    public void addMeal(Recipe recipe, Date date, Double serving) {
        for(int i=0;i<meals.size();i++) {
            if (Objects.equals(meals.get(i).getDate() ,date)){
                meals.get(i).getMeals().add(recipe);
                meals.get(i).getServings().add(serving);
                // update recipes
                FirestoreDatabase.modifyMealPlan(meals.get(i));
                return;
            }
        }
        ArrayList<Recipe> recipes = new ArrayList<Recipe>();
        ArrayList<Double> servings = new ArrayList<Double>();
        recipes.add(recipe);
        servings.add(serving);
        Mealday mealDay = new Mealday(date, recipes, servings);
        meals.add(mealDay);
        Collections.sort(meals, new Comparator<Mealday>() {
            @Override
            public int compare(Mealday mealday, Mealday t1) {
                return mealday.getDate().compareTo(t1.getDate());
            }
        });
        FragmentManager fm = getSupportFragmentManager();
        mealdayAdapter = new MealdayAdapter(this,meals,fm);
        mealList.setAdapter(mealdayAdapter);
        FirestoreDatabase.addMealPlan(mealDay);
        ((BaseExpandableListAdapter)mealdayAdapter).notifyDataSetChanged();
    }

    /**
     * deletes the mealplan when on meals for the day exist
     * @param mealDay the day user wants to delete from the meal planner
     * */
    @Override
    public void deleteMeal(Mealday mealDay) {
        //checking the existence of this meal
        meals.remove(mealDay);
        FragmentManager fm = getSupportFragmentManager();
        mealdayAdapter = new MealdayAdapter(this,meals,fm);
        mealList.setAdapter(mealdayAdapter);
        FirestoreDatabase.fetchMealPlans(mealdayAdapter,meals);
        ((BaseExpandableListAdapter)mealdayAdapter).notifyDataSetChanged();
    }


    /**
     * deletes the meal when prompted by the MealPlannerAddMeal Dialog with the Day if the
     * last one in that day
     * @param mealday the day user wants to delete from the meal planner
     * */
    @Override
    public void deleteMealPlan(Mealday mealday, Recipe recipe) {
        Integer pos = mealday.getMeals().indexOf(recipe);
        mealday.getMeals().remove(recipe);
        ArrayList<Double> temp = new ArrayList<Double>();
        for (int i = 0; i < mealday.getServings().size(); i++){
            if(i != pos){
                temp.add(mealday.getServings().get(i));
            }
        }
        mealday.setServings(temp);
        FirestoreDatabase.modifyMealPlan(mealday);
        mealList.setAdapter(mealdayAdapter);
        FirestoreDatabase.fetchMealPlans(mealdayAdapter,meals);
        ((BaseExpandableListAdapter)mealdayAdapter).notifyDataSetChanged();

    }


}
