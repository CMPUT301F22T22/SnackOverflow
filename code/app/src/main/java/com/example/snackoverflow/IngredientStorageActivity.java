package com.example.snackoverflow;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
        ingredients.add(new Ingredient("Apple", "2022-09-08", "Pantry", 65, 7));
        ingredients.add(new Ingredient("Yogurt", "2022-09-08", "Fridge", 12, 2));

        ingredientArrayAdapter = new IngredientAdapter(this, ingredients);

        ingredientStorageList.setAdapter(ingredientArrayAdapter);


    }
}