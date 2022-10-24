package com.example.snackoverflow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class IngredientStorageActivity extends AppCompatActivity {
    private ListView ingredientStorageList;
    private ArrayAdapter<Ingredient> ingredientArrayAdapter;
    private ArrayList<Ingredient> ingredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_storage);

        ingredientStorageList = findViewById(R.id.ingredient_storage_list);
        ingredients = new ArrayList<>();
        ingredients.add(new Ingredient("Apple", "2022-09-08", "Pantry", 65, 7, "Fresh Produce"));
        ingredients.add(new Ingredient("Yogurt", "2022-09-08", "Fridge", 12, 2, "Dairy"));

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


    }
}