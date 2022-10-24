package com.example.snackoverflow;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements add_ingredient_fragment.OnFragmentInteractionListener{
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

        // TEST FOR INGREDIENT ADD
        recipeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                new add_ingredient_fragment().show(getSupportFragmentManager(), "Add_Ingredient");
            }
        });


    }

    @Override
    public void Add_food(Ingredient ingredient) {

    }

    @Override
    public void Edit_food(Ingredient ingredient) {

    }
}