package com.example.snackoverflow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

// Note: For now meal plan is implemented as a list of recipes as
// none of other functionalities are implemented
public class MainActivity extends AppCompatActivity implements MealPlannerAddMeal.OnFragmentInteractionListener{

    //TODO: Follow camel case naming convention
    ExpandableListView mealslist;
    ArrayList<Mealday> meals = new ArrayList<>();
    ExpandableListAdapter mealdayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavigationBarView navigationBarView=findViewById(R.id.bottom_navigation);
        navigationBarView.setSelectedItemId(R.id.mealplanner);
        navigationBarView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.mealplanner:
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
        mealslist = (ExpandableListView) findViewById(R.id.mealplanner_list);
        FloatingActionButton addMeal = findViewById(R.id.add_mealplan);
        data();

        FragmentManager fm = getSupportFragmentManager();
        mealdayAdapter = new MealdayAdapter(this,meals,fm);
        mealslist.setAdapter(mealdayAdapter);

        mealslist.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;
            @Override
            public void onGroupExpand(int i) {
                if((previousGroup != -1) && (i!=previousGroup)){
                    mealslist.collapseGroup(previousGroup);
                }
                previousGroup = i;
            }
        });
        addMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO add recipe data
                new MealPlannerAddMeal().show(getSupportFragmentManager(),"Add_meal");
            }
        });
    }



    /*
    Test DATA
     */
    private void data(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        ArrayList<Recipe> Monday = new ArrayList<Recipe>();
        Monday.add(new Recipe("nidal", 120,2.5f,"Lunch","nice","Heat" ));
        Monday.add(new Recipe("nidal", 120,2.5f,"Lunch","nice","Heat" ));
        Monday.add(new Recipe("nidal", 120,2.5f,"Lunch","nice","Heat" ));
        Monday.add(new Recipe("nidal", 120,2.5f,"Lunch","nice","Heat" ));
        Monday.add(new Recipe("nidal", 120,2.5f,"Lunch","nice","Heat" ));
        Monday.add(new Recipe("nidal", 120,2.5f,"Lunch","nice","Heat" ));
        //Mealday monday = new Mealday(LocalDate.now(),Monday);

        ArrayList<Recipe> Tuesday = new ArrayList<Recipe>();
        
        Tuesday.add(new Recipe("nidal", 120,2.5f,"Lunch","nice","boil" ));
        Tuesday.add(new Recipe("nidal", 120,2.5f,"Lunch","nice","boil"));
        Tuesday.add(new Recipe("nidal", 120,2.5f,"Lunch","nice", "boil"));
        Mealday tuesday = null;
        try {
            tuesday = new Mealday(dateFormat.parse("2022-10-21"),Monday);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //meals.add(monday);
       // meals.add(tuesday);

    }
    /**
     * addes the meal when prompted by the MealPlannerAddMeal Dialog
     * @param recipe the recipe user wants to add to meal
     * @param date the date the user wants to add the meal plan too
     * */
    @Override
    public void addMeal(Recipe recipe, Date date) {
        for(int i=0;i<meals.size();i++) {
            if (Objects.equals(meals.get(i).getDate() ,date)){
                System.out.println("Dates are equal");
                meals.get(i).getMeals().add(recipe);
                for(Recipe r : meals.get(i).getMeals()) {
                    System.out.println(r.getTitle());

                }
                // update recipes
                //FirestoreDatabase.modifyMealPlan(i,meals);
                return;
            }
        }
        ArrayList<Recipe> recipes = new ArrayList<Recipe>();
        recipes.add(recipe);
        Mealday mealDay = new Mealday(date, recipes);
        meals.add(mealDay);
        FragmentManager fm = getSupportFragmentManager();
        mealdayAdapter = new MealdayAdapter(this,meals,fm);
        mealslist.setAdapter(mealdayAdapter);
        //FirestoreDatabase.addMealPlan(mealDay);
    }
    /**
     * deletes the meal when prompted by the MealPlannerAddMeal Dialog
     * @param mealDay the day user wants to delete from the meal planner
     * */
    @Override
    public void deleteMealDay(Mealday mealDay) {
        meals.remove(mealDay);
        FragmentManager fm = getSupportFragmentManager();
        mealdayAdapter = new MealdayAdapter(this,meals,fm);
        mealslist.setAdapter(mealdayAdapter);
    }
}