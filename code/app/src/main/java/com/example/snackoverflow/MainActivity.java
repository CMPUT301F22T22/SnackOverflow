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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements MealPlannerAddMeal.OnFragmentInteractionListener{

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
        ArrayList<Recipe> Monday = new ArrayList<Recipe>();
        Monday.add(new Recipe("nidal", 120,2.5f,"Lunch","nice","Heat" ));
        Monday.add(new Recipe("nidal", 120,2.5f,"Lunch","nice","Heat" ));
        Monday.add(new Recipe("nidal", 120,2.5f,"Lunch","nice","Heat" ));
        Monday.add(new Recipe("nidal", 120,2.5f,"Lunch","nice","Heat" ));
        Monday.add(new Recipe("nidal", 120,2.5f,"Lunch","nice","Heat" ));
        Monday.add(new Recipe("nidal", 120,2.5f,"Lunch","nice","Heat" ));
        Mealday monday = new Mealday(LocalDate.now(),Monday);

        ArrayList<Recipe> Tuesday = new ArrayList<Recipe>();
        
        Tuesday.add(new Recipe("nidal", 120,2.5f,"Lunch","nice","boil" ));
        Tuesday.add(new Recipe("nidal", 120,2.5f,"Lunch","nice","boil"));
        Tuesday.add(new Recipe("nidal", 120,2.5f,"Lunch","nice", "boil"));
        Mealday tuesday = new Mealday(LocalDate.parse("2022-10-21"),Monday);

        meals.add(monday);
        meals.add(tuesday);

    }

    @Override
    public void addMeal(Recipe recipe, LocalDate date) {
        for(int i=0;i<meals.size();i++){
            if (Objects.equals(meals.get(i).getDate() ,date)){
                System.out.println("Date is right");
                meals.get(i).getMeals().add(recipe);
                return;
            }
        }
        ArrayList<Recipe> recipes = new ArrayList<Recipe>();
        recipes.add(recipe);
        meals.add(new Mealday(date, recipes));
        FragmentManager fm = getSupportFragmentManager();
        mealdayAdapter = new MealdayAdapter(this,meals,fm);
        mealslist.setAdapter(mealdayAdapter);
    }

    @Override
    public void deleteMealDay(Mealday mealDay) {
        meals.remove(mealDay);
        FragmentManager fm = getSupportFragmentManager();
        mealdayAdapter = new MealdayAdapter(this,meals,fm);
        mealslist.setAdapter(mealdayAdapter);
    }
}