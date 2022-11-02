package com.example.snackoverflow;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import java.time.LocalTime;
import java.util.ArrayList;

public class RecipeActivity extends AppCompatActivity {
    ListView recipeList;
    ArrayAdapter<Recipe> recipeArrayAdapter;
    ArrayList<Recipe> recipeDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        recipeList = findViewById(R.id.recipe_list);
        recipeDataList = new ArrayList<>();

        String []recipestitle = {"Curry","NOODLES"};
        int [] servings = {1,2};
        String []categories = {"Lunch","Dinner"};

        //Test Data
        for (int i =0;i<recipestitle.length;i++){
            recipeDataList.add(new Recipe(recipestitle[i], 120,2.0f,"Lunch","HAHA"));
        }

        recipeArrayAdapter = new RecipeAdapter(this, recipeDataList);
        recipeList.setAdapter(recipeArrayAdapter);

        NavigationBarView navigationBarView=findViewById(R.id.bottom_navigation);
        navigationBarView.setSelectedItemId(R.id.recipes);
        navigationBarView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.shoppinglist:
                    case R.id.recipes:
                        return true;
                    case R.id.ingredients:
                        startActivity(new Intent(getApplicationContext(),IngredientStorageActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.mealplanner:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        recipeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(RecipeActivity.this, ModifyRecipe.class);
                intent.putExtra("recipe", (Parcelable) recipeDataList.get(position));
                startActivity(intent);
            }
        });

        //Modify Button //TODO: Replace functionality or change when tests done
        FloatingActionButton modifyRecipeTestButton = findViewById(R.id.add_recipe_button);
        modifyRecipeTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecipeActivity.this, AddRecipe.class);
//                Intent intent = new Intent(RecipeActivity.this, AddRecipe.class);
                startActivity(intent);
            }
        });
    }
}