package com.example.snackoverflow;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;

public class ModifyRecipe extends AppCompatActivity {
    private Recipe recipe;

    private EditText categoryField;
    private EditText servingsField;
    private EditText ingredientsField;
    private EditText commentsField;

    private long prevCost;

    private Button editButton;
    private Button applyButton;
    private Button viewButton;
    private Button deleteButton;

    // Referenced how to go back when back button is clicked
    // From: silvan
    // Date (last updated): Sep 12, 2020
    // Date accessed: Sep 21, 2022
    // License: Creative Commons License
    // URL: https://stackoverflow.com/questions/36433299/setdisplayhomeasupenabled-not-working-in-preferenceactivity
    @Override
    public boolean onSupportNavigateUp() {
        Intent modifyIntent = new Intent();
        modifyIntent.putExtra("ACTION_TYPE", "NONE");
        finish();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_recipe);

        Intent intent = getIntent();
        recipe = intent.getParcelableExtra("clickedRecipe");

        categoryField = (EditText) findViewById(R.id.edit_category_text);
        servingsField = (EditText) findViewById(R.id.edit_servings_text);
        ingredientsField = (EditText) findViewById(R.id.edit_ingredients_text);
        commentsField = (EditText) findViewById(R.id.edit_comments_text);

//        countField.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                if (editable.toString().equals("0")) {
//                    countField.setText("");
//                }
//            }
//        });

        editButton = (Button) findViewById(R.id.edit_button);
        applyButton = (Button) findViewById(R.id.apply_button);
        deleteButton = (Button) findViewById(R.id.delete_button);

        categoryField.setText(recipe.getRecipeCategory());
        servingsField.setText(Float.toString(recipe.getServings()));
        commentsField.setText(recipe.getComments());

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryField.setEnabled(true);
                servingsField.setEnabled(true);
                ingredientsField.setEnabled(true);
                commentsField.setEnabled(true);

                editButton.setVisibility(View.GONE);
                applyButton.setVisibility(View.VISIBLE);
            }
        });

        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryField.setEnabled(false);
                servingsField.setEnabled(false);
                ingredientsField.setEnabled(false);
                commentsField.setEnabled(false);

                editButton.setVisibility(View.VISIBLE);
                viewButton.setVisibility(View.GONE);
                applyButton.setVisibility(View.GONE);
            }
        });
        // Referenced how to pass data through Intents
        // From: Google-Android Studio Documentation
        // Date (last updated): Jun 08, 2022
        // Date accessed: Sep 21, 2022
        // License: Apache 2.0 license
        // URL: https://developer.android.com/reference/android/content/Intent
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String category = categoryField.getText().toString();
                String servings = servingsField.getText().toString();
                String ingredients = ingredientsField.getText().toString();
                String comments = commentsField.getText().toString();
                if (category.equals("") || servings.equals("") ||
                        ingredients.equals("") || comments.equals("")) {

                } else {
                    recipe.setRecipeCategory(category);
                    recipe.setServings(Float.parseFloat(servings));
                    recipe.setComments(comments);
                    Intent modifyIntent = new Intent();
                    modifyIntent.putExtra("ACTION_TYPE", "EDIT");
                    modifyIntent.putExtra("position", intent.getIntExtra("position", 0));
                    modifyIntent.putExtra("Recipe", (Parcelable) recipe);
                    setResult(-1, modifyIntent);
                    finish();
                }
            }
        });

//        deleteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent modifyIntent = new Intent();
//                modifyIntent.putExtra("ACTION_TYPE", "DELETE");
//                modifyIntent.putExtra("position", intent.getIntExtra("position", 0));
//                prevCost = Utils.getFinalCost(food.getCount(), food.getCost());
//                modifyIntent.putExtra("previousCost", prevCost);
//                setResult(-1, modifyIntent);
//                finish();
//            }
//        });
    }
}
