package com.example.snackoverflow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Ingredient Storage Activity to display the list of ingredients in the storage
 * extends AppCompactActivity
 * implements AddingredientFragment.OnFragementInteractionListener
 * @see Ingredient
 * @see AddIngredientFragment
 * @see IngredientAdapter
 * @see IngredientDetailsActivity
 * */
public class IngredientStorageActivity extends AppCompatActivity implements AddIngredientFragment.OnFragmentInteractionListener {
    private ListView ingredientStorageList;
    private ArrayAdapter<Ingredient> ingredientArrayAdapter;
    private ArrayList<Ingredient> ingredients;

    // TODO: Maybe change location to radio buttons for user to select
    // TODO: Error Check if the user adds an ingredient with incomplete details
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_storage);
        ingredientStorageList = findViewById(R.id.ingredient_storage_list);
        ingredients = new ArrayList<>();

        ingredientArrayAdapter = new IngredientAdapter(this, ingredients);

        ingredientStorageList.setAdapter(ingredientArrayAdapter);
        FirestoreDatabase.fetchIngredients(ingredientArrayAdapter, ingredients);

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
                        return true;
                    case R.id.shoppinglist:
                        startActivity(new Intent(getApplicationContext(),ShoppingListActivity.class));
                        overridePendingTransition(0,0);
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

    /**
     * Deletes the particular ingredient when prompted by the delete icon
     * @param v view that the icon is present on
     * */
    public void deleteIngredientAtPosition(View v) {
        int position = ingredientStorageList.getPositionForView((View) v.getParent());
        Ingredient selectedIngredient = (Ingredient) ingredientStorageList.getItemAtPosition(position);
        FirestoreDatabase.deleteIngredient(selectedIngredient);
    }

    @Override
    public void onOkPressed(Ingredient selectedIngredient) {
        FirestoreDatabase.addIngredient(selectedIngredient);
    }
}