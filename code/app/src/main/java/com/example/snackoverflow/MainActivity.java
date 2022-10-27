package com.example.snackoverflow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    ListView recipeList;
    ArrayAdapter<Recipe> recipeArrayAdapter;
    ArrayList<Recipe> recipeDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recipeList = findViewById(R.id.recipeListView);
        recipeDataList = new ArrayList<>();
        String []recipestitle = {"Curry","NOODLES"};
        int [] servings = {1,2};
        String []categories = {"Lunch","Dinner"};


        //Test Data
        for (int i =0;i<recipestitle.length;i++){
            recipeDataList.add(new Recipe(recipestitle[i], LocalTime.now(),2.0f,"Lunch","HAHA",new ArrayList<String>(Arrays.asList(new String[]{"Nidal","Nasemm"}))));
        }

        recipeArrayAdapter = new RecipeAdapter(this,recipeDataList);
        recipeList.setAdapter(recipeArrayAdapter);
        Button storageActivity = findViewById(R.id.storage_activity);
        storageActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, IngredientStorageActivity.class);
                startActivity(intent);
            }
        });

        Button modifyRecipeTestButton = findViewById(R.id.modify_recipe_test_button);
        modifyRecipeTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ModifyRecipe.class);
                startActivity(intent);
            }
        });

    }
}