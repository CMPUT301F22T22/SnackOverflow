package com.example.snackoverflow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class IngredientStorageActivity extends AppCompatActivity implements AddIngredientFragment.OnFragmentInteractionListener {
    private ListView ingredientStorageList;
    private ArrayAdapter<Ingredient> ingredientArrayAdapter;
    private ArrayList<Ingredient> ingredients;

    // TODO: Maybe change location to radio buttons for user to select
    //TODO: fix best before display as it only shows null now
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_storage);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        ingredientStorageList = findViewById(R.id.ingredient_storage_list);
        ingredients = new ArrayList<>();
        //ingredients.add(new Ingredient("Apple", "2022-09-08", "Pantry", 65, 7, "Fresh Produce"));
        //ingredients.add(new Ingredient("Yogurt", "2022-09-08", "Fridge", 12, 2, "Dairy"));

        ingredientArrayAdapter = new IngredientAdapter(this, ingredients);

        ingredientStorageList.setAdapter(ingredientArrayAdapter);

        ingredientStorageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*
                Title: How to Send Data From One Activity to Second Activity in Android?
                URL: https://www.geeksforgeeks.org/how-to-send-data-from-one-activity-to-second-activity-in-android/
                Date Received: 25 September 2022
                License: CCBY-SA
                 */
                Intent intent = new Intent(IngredientStorageActivity.this, IngredientDetailsActivity.class);
                intent.putExtra("selectedIngredient", (Serializable) ingredientStorageList.getItemAtPosition(position));
                startActivity(intent);
            }
        });

        final FloatingActionButton addIngredientButton = findViewById(R.id.add_ingredient_button);
        addIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddIngredientFragment().show(getSupportFragmentManager(), "ADD_INGREDIENT");
            }
        });

        NavigationBarView navigationBarView=findViewById(R.id.bottom_navigation);
        navigationBarView.setSelectedItemId(R.id.ingredients);
        navigationBarView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.ingredients:
                    case R.id.shoppinglist:
                        return true;
                    case R.id.mealplanner:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
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
    public void onOkPressed(Ingredient selectedIngredient) {
        ingredientArrayAdapter.add(selectedIngredient);
    }
}