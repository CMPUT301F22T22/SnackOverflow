package com.example.snackoverflow;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

public class RecipeActivity extends AppCompatActivity {
    ListView recipeList;
    ArrayAdapter<Recipe> recipeArrayAdapter;
    ArrayList<Recipe> recipeDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        recipeList = findViewById(R.id.recipeListView);
        recipeDataList = new ArrayList<>();

        recipeArrayAdapter = new RecipeAdapter(this, recipeDataList);
        recipeList.setAdapter(recipeArrayAdapter);


    }
}
