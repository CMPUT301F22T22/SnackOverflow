package com.example.snackoverflow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * IngredientDetailsActivity defines the Activity that showcases
 * the Ingredient details when we click on it
 * Extends AppCompactActivity
 * @see Ingredient
 * @see IngredientStorageActivity
 * */
public class IngredientDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_details);
        // Get the Text Views defined in the XML files
        TextView ingredientDescription = (TextView) findViewById(R.id.details_ingredient_description);
        TextView ingredientUnit = (TextView) findViewById(R.id.details_unit);
        TextView ingredientBestBefore = (TextView) findViewById(R.id.details_best_before);
        TextView ingredientLocation = (TextView) findViewById(R.id.details_location);
        TextView ingredientAmount = (TextView) findViewById(R.id.details_amount);
        TextView ingredientCategory = (TextView) findViewById(R.id.details_category);

        Intent intent = getIntent();
        Ingredient selectedIngredient = (Ingredient) intent.getSerializableExtra("selectedIngredient");

        // Sets the values for the fields from the selected ingredient.
        ingredientDescription.setText(selectedIngredient.getTitle());
        ingredientUnit.setText(String.valueOf(selectedIngredient.getUnit()));
        ingredientBestBefore.setText(IngredientAdapter.getDateText(selectedIngredient));
        ingredientLocation.setText(selectedIngredient.getLocation());
        ingredientAmount.setText(String.valueOf(selectedIngredient.getAmount()));
        ingredientCategory.setText(selectedIngredient.getCategory());
    }
}