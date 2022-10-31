package com.example.snackoverflow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class IngredientAdapter extends ArrayAdapter<Ingredient> {
    private ArrayList<Ingredient> ingredients;
    private Context context;
    private boolean recipeCheck;

    public IngredientAdapter(Context context, ArrayList<Ingredient> ingredients){
        super(context, 0, ingredients);
        this.ingredients = ingredients;
        this.context = context;
        this.recipeCheck = false;
    }
    public IngredientAdapter(Context context, ArrayList<Ingredient> ingredients, String recipe){
        super(context, 0, ingredients);
        this.ingredients = ingredients;
        this.context = context;
        if (recipe == "recipe"){
            this.recipeCheck = true;
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.ingredient_content, parent,false);
        }
        Ingredient ingredient = ingredients.get(position);
        TextView ingredientDescription = view.findViewById(R.id.ingredient_description);
        ingredientDescription.setText(ingredient.getDescription());

        if (recipeCheck == false){
            TextView ingredientBestBefore = view.findViewById(R.id.ingredient_bestBefore);
            TextView ingredientUnit = view.findViewById(R.id.ingredient_unit);
            ingredientBestBefore.setText(String.valueOf(ingredient.getBestBefore()));
            ingredientUnit.setText(String.valueOf(ingredient.getUnit()));
        }

        else{
            TextView TextViewAmount = view.findViewById(R.id.TextView_Bestbefore);
            TextViewAmount.setText("Amount:");
            TextView ingredientAmount = view.findViewById(R.id.ingredient_bestBefore);
            TextView ingredientUnit = view.findViewById(R.id.ingredient_unit);
            ingredientAmount.setText(String.valueOf(ingredient.getAmount()));
            ingredientUnit.setText(String.valueOf(ingredient.getUnit()));
//            ImageButton editIngredient = view.findViewById(R.id.edit_ingredient);
//            editIngredient.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    new RecipeAddIngredientFragment(ingredient);
//                }
//            });
        }

        return view;
    }
}
