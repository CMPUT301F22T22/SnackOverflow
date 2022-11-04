package com.example.snackoverflow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.EventListener;

import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.Map;

/**
 * The main activity page for shopping list. This page shows the ingredients a user needs to buy from the store.
 * These ingredients can be manually added using the floating action button, or are automatically added when the meal specified does not have the required ingredients.
 * The user can view, add ingredients, and check them off the list with the provided checkbox.
 * Each entry in the shopping list is an Ingredient object, with a description, category, unit, and amount. The remaining
 * fields (best before date, location) are left null. When a user completes the shopping list, they are prompted to enter the
 * remaining fields (best before date, location).
 * */
public class ShoppingListActivity extends AppCompatActivity implements ShoppingListAddItemFragment.OnFragmentInteractionListener {
    private ListView shoppingList;
    private ArrayAdapter<Ingredient> shoppingListAdapter;
    private ArrayList<Ingredient> shoppingItems;

    /**
     * Used to start the ShoppingListActivity. If the activity needs to be recreated, it can be passed to onCreate as a bundle
     * to recreate the activity. The method is also called, when the orientation of the device change, termination of the app.
     * @param savedInstanceState Contains the most recently supplied data in a bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        shoppingItems = new ArrayList<>();
        // Adding two demo ingredients to demonstrate functionality
        shoppingItems.add(new Ingredient("Apple", 3, 4, "Fresh"));
        shoppingItems.add(new Ingredient("Bread", 3, 1, "Bakery"));

        ArrayList<Ingredient> firebaseIngredients = FirestoreDatabase.getIngredientsStorageList();

        // Linking the listview to an arraylist and setting adapter
        shoppingList = findViewById(R.id.shopping_list_storage_list);
        shoppingListAdapter = new ShoppingListAdapter(this, shoppingItems);
        shoppingList.setAdapter(shoppingListAdapter);

        // Setting up NavBar
        NavigationBarView navigationBarView = findViewById(R.id.bottom_navigation);
        navigationBarView.setSelectedItemId(R.id.shoppinglist);
        navigationBarView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            /**
             * Start corresponding activity when any button in the navbar is clicked. It uses the existing
             * application context to start the activity, so that all previous changes made to that activity
             * are saved
             * @param item
             * @return true if a valid button is clicked and the activity starts, false if invalid
             */
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Switch to corresponding activity when a button is clicked
                switch ((item.getItemId())) {
                    case R.id.ingredients:
                        startActivity(new Intent(getApplicationContext(),IngredientStorageActivity.class));
                        overridePendingTransition(0,0);
                        return true;
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
                // Return false otherwise
                return false;
            }
        });

        // Add floating action button to add ingredients to the shopping list
        final FloatingActionButton addShoppingItemButton = findViewById(R.id.add_to_shopping_list_button);
        addShoppingItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get support manager when the button is clicked
                new ShoppingListAddItemFragment().show(getSupportFragmentManager(), "ADD_SHOPPING_LIST_ITEM");
            }
        });


    }

    /**
     * Add ingredient to the listview when the OK button is pressed in the user-friendly fragment
     * @param selectedIngredient
     */
    @Override
    public void onOkPressed(Ingredient selectedIngredient) {
        // Add ingredient to list when 'OK' is pressed in the fragment
        shoppingListAdapter.add(selectedIngredient);
    }
}