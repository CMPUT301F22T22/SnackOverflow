package com.example.snackoverflow;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONObject;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    ListView recipeList;
    ArrayAdapter<Recipe> recipeArrayAdapter;
    ArrayList<Recipe> recipeDataList;

    private ActivityResultLauncher<Intent> getModifiedData = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {

                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        String actionType = intent.getStringExtra("ACTION_TYPE");
                        int position = intent.getIntExtra("position", 0);
//                        if (actionType.equals("EDIT")) {
//
//                            Recipe recipe = intent.getParcelableExtra("Recipe");
//                        } else if (actionType.equals("DELETE")) {
//                            int position = intent.getIntExtra("position", 0);
//                            long prevCost = intent.getLongExtra("previousCost", 0);
//                            sum = sum - prevCost;
//                            foodArrayList.remove(position);
//                            totalCostText.setText("Total Cost = " + String.valueOf(sum));
//                            if (foodArrayList.size() == 0) {
//                                findViewById(R.id.none_display_text).setVisibility(View.VISIBLE);
//                            }
//                            foodListAdapter.notifyDataSetChanged();
//                        }
                    }
                }
            });
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

        recipeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(MainActivity.this, ModifyRecipe.class);
                Recipe clickedRecipe = recipeDataList.get(position);
                intent.putExtra("clickedRecipe", (Parcelable) clickedRecipe);
                intent.putExtra("position", position);
                getModifiedData.launch(intent);
            }
        });

    }
}