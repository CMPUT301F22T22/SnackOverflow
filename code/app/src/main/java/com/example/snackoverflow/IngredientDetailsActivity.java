package com.example.snackoverflow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class IngredientDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_details);

        TextView ingredientDescription = (TextView) findViewById(R.id.details_ingredient_description);
        TextView ingredientUnit = (TextView) findViewById(R.id.details_unit);
        TextView ingredientBestBefore = (TextView) findViewById(R.id.details_best_before);
        TextView ingredientLocation = (TextView) findViewById(R.id.details_location);
        TextView ingredientAmount = (TextView) findViewById(R.id.details_amount);
        TextView ingredientCategory = (TextView) findViewById(R.id.details_category);

        Intent intent = getIntent();
        Ingredient selectedIngredient = (Ingredient) intent.getSerializableExtra("selectedIngredient");

        ingredientDescription.setText(selectedIngredient.getTitle());
        ingredientUnit.setText(String.valueOf(selectedIngredient.getUnit()));
        ingredientBestBefore.setText(IngredientAdapter.getDateText(selectedIngredient));
        ingredientLocation.setText(selectedIngredient.getLocation());
        ingredientAmount.setText(String.valueOf(selectedIngredient.getAmount()));
        ingredientCategory.setText(selectedIngredient.getCategory());
    }
}