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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Ingredient Storage Activity to display the list of ingredients in the storage
 * extends AppCompactActivity
 * implements AddingredientFragment.OnFragementInteractionListener
 * @see Ingredient
 * @see AddIngredientFragment
 * @see IngredientAdapter
 * @see IngredientDetailsActivity
 * */
public class IngredientStorageActivity extends AppCompatActivity implements AddIngredientFragment.OnFragmentInteractionListener, DeleteConformationFragment.OnFragmentInteractionListener{
    private ListView ingredientStorageList;
    private ArrayAdapter<Ingredient> ingredientArrayAdapter;
    private ArrayList<Ingredient> ingredients;
    private String currSortOrder = "inc";

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

        String[] sortBySpinnerList = new String[] {"Title", "Best Before", "Location", "Category"};
        String[] sortOrderSpinnerList = new String[] {"Low-High/A-Z", "High-Low/Z-A"};
        Spinner sortBySpinner = (Spinner) findViewById(R.id.sort_by_spinner);
        Spinner sortOrderSpinner = (Spinner) findViewById(R.id.sort_order_spinner);
        ArrayAdapter<String> sortByAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, sortBySpinnerList);
        ArrayAdapter<String> sortOrderAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, sortOrderSpinnerList);
        sortByAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortOrderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sortBySpinner.setAdapter(sortByAdapter);
        sortOrderSpinner.setAdapter(sortOrderAdapter);

        LinearLayout sortByLayout = (LinearLayout) findViewById(R.id.sort_by_layout);
        LinearLayout sortOrderLayout = (LinearLayout) findViewById(R.id.sort_order_layout);

        sortByLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortBySpinner.performClick();
            }
        });

        sortOrderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortOrderSpinner.performClick();
            }
        });
        sortBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                handleSortBy(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sortOrderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                handleSortOrder(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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

    public void handleSortBy(int position) {
        switch (position) {
            case 0:
                if (currSortOrder.equals("dec")) {
                    Collections.sort(ingredients, new SortComparator.TitleComparator().reversed());
                } else {
                    Collections.sort(ingredients, new SortComparator.TitleComparator());
                }
                break;
            case 1:
                if (currSortOrder.equals("dec")) {
                    Collections.sort(ingredients, new SortComparator.BestBeforeComparator().reversed());
                } else {
                    Collections.sort(ingredients, new SortComparator.BestBeforeComparator());
                }
                break;
            case 2:
                if (currSortOrder.equals("dec")) {
                    Collections.sort(ingredients, new SortComparator.LocationComparator().reversed());
                } else {
                    Collections.sort(ingredients, new SortComparator.LocationComparator());
                }
                break;
            case 3:
                if (currSortOrder.equals("dec")) {
                    Collections.sort(ingredients, new SortComparator.CategoryComparator().reversed());
                } else {
                    Collections.sort(ingredients, new SortComparator.CategoryComparator());
                }
                break;
        }
        ingredientArrayAdapter.notifyDataSetChanged();
    }

    public void handleSortOrder(int position) {
        if (position == 1 && currSortOrder.equals("inc")) {
            Collections.reverse(ingredients);
            currSortOrder = "dec";
        } else if (position == 0 && currSortOrder.equals("dec")) {
            Collections.reverse(ingredients);
            currSortOrder = "inc";
        }
        ingredientArrayAdapter.notifyDataSetChanged();
    }

    /**
     * Deletes the particular ingredient when prompted by the delete icon
     * @param v view that the icon is present on
     * */
    public void deleteIngredientAtPosition(View v) {
        int position = ingredientStorageList.getPositionForView((View) v.getParent());
        Ingredient selectedIngredient = (Ingredient) ingredientStorageList.getItemAtPosition(position);
        new DeleteConformationFragment<Ingredient>(selectedIngredient, selectedIngredient.getTitle()).show(getSupportFragmentManager(), "Delete_Ingredient");
    }

    public void editIngredientAtPosition(View v) {
        int position = ingredientStorageList.getPositionForView((View) v.getParent());
        Ingredient selectedIngredient = (Ingredient) ingredientStorageList.getItemAtPosition(position);
        new AddIngredientFragment().newInstance(selectedIngredient).show(getSupportFragmentManager(), "EDIT_INGREDIENT");
    }

    @Override
    public void onOkPressed(Ingredient selectedIngredient) {
        FirestoreDatabase.addIngredient(selectedIngredient);
    }

    @Override
    public void deleteObject(Object object) {
        FirestoreDatabase.deleteIngredient((Ingredient) object);
    }
}