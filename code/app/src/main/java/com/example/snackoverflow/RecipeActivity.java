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
        String[] recipestitle = {"Curry", "NOODLES"};
        int[] servings = {1, 2};
        String[] categories = {"Lunch", "Dinner"};


        //Test Data
        for (int i = 0; i < recipestitle.length; i++) {
            recipeDataList.add(new Recipe(recipestitle[i], LocalTime.now(), 2.0f, "Lunch", "HAHA", new ArrayList<String>(Arrays.asList(new String[]{"Nidal", "Nasemm"}))));
        }


        recipeArrayAdapter = new RecipeAdapter(this, recipeDataList);
        recipeList.setAdapter(recipeArrayAdapter);


    }
}
