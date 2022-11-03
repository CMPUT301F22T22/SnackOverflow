package com.example.snackoverflow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class ShoppingListActivity extends AppCompatActivity implements ShoppingListAddItemFragment.OnFragmentInteractionListener {
    private ListView shoppingList;
    private ArrayAdapter<Ingredient> shoppingListAdapter;
    private ArrayList<Ingredient> shoppingItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        shoppingItems = new ArrayList<>();

        shoppingItems.add(new Ingredient("Apple", 3, 4, "Fresh"));
        shoppingItems.add(new Ingredient("Bread", 3, 1, "Bakery"));

        shoppingList = findViewById(R.id.shopping_list_storage_list);
        shoppingListAdapter = new ShoppingListAdapter(this, shoppingItems);
        shoppingList.setAdapter(shoppingListAdapter);

        NavigationBarView navigationBarView = findViewById(R.id.bottom_navigation);
        navigationBarView.setSelectedItemId(R.id.shoppinglist);
        navigationBarView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch ((item.getItemId())) {
                    case R.id.ingredients:
                        startActivity(new Intent(getApplicationContext(),IngredientStorageActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.shoppinglist:
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

        final FloatingActionButton addShoppingItemButton = findViewById(R.id.add_to_shopping_list_button);
        addShoppingItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ShoppingListAddItemFragment().show(getSupportFragmentManager(), "ADD_SHOPPING_LIST_ITEM");
            }
        });


    }

    @Override
    public void onOkPressed(Ingredient selectedIngredient) {
        shoppingListAdapter.add(selectedIngredient);
    }
}