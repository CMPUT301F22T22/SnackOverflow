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
import androidx.fragment.app.FragmentActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Ingredient Adapter to view the Ingredients in list view
 * @see Ingredient
 * */
public class IngredientAdapter extends ArrayAdapter<Ingredient> {
    private ArrayList<Ingredient> ingredients;
    private Context context;
    private boolean recipeCheck;

    /**
     * Constructor for the Ingredient Adapter
     * @param context
     * @param ingredients ArrayList storing ingredients
     * */
    public IngredientAdapter(Context context, ArrayList<Ingredient> ingredients){
        super(context, 0, ingredients);
        this.ingredients = ingredients;
        this.context = context;
        this.recipeCheck = false;
    }

    /**
     * Constructor for Ingredient Adapter for Recipe
     * @param context
     * @param ingredients ArrayList storing ingredients
     * @param recipe
     * */
    public IngredientAdapter(Context context, ArrayList<Ingredient> ingredients, String recipe){
        super(context, 0, ingredients);
        this.ingredients = ingredients;
        this.context = context;
        if (recipe == "recipe"){
            this.recipeCheck = true;
        }
        else{
            this.recipeCheck = false;
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (recipeCheck == false){
            if(view == null){
                view = LayoutInflater.from(context).inflate(R.layout.ingredient_content, parent,false);
            }
            Ingredient ingredient = ingredients.get(position);
            TextView ingredientDescription = view.findViewById(R.id.ingredient_description);
            ingredientDescription.setText(ingredient.getTitle());
            TextView ingredientBestBefore = view.findViewById(R.id.recipe_ingredient_amount);
            TextView ingredientUnit = view.findViewById(R.id.ingredient_unit);
            ingredientBestBefore.setText(getDateText(ingredient));
            ingredientUnit.setText(String.valueOf(ingredient.getUnit()));
        }
        else{
            if(view == null){
                view = LayoutInflater.from(context).inflate(R.layout.recipe_ingredient_content, parent,false);
                System.out.println("drawn");
            }
            Ingredient ingredient = ingredients.get(position);
            TextView ingredientDescription = view.findViewById(R.id.ingredient_description);
            ingredientDescription.setText(ingredient.getTitle());
            TextView ingredientAmount = view.findViewById(R.id.recipe_ingredient_amount);
            TextView ingredientUnit = view.findViewById(R.id.ingredient_unit);
            ingredientAmount.setText(String.valueOf(ingredient.getAmount()));
            ingredientUnit.setText(String.valueOf(ingredient.getUnit()));
            ImageButton editIngredient = view.findViewById(R.id.edit_ingredient);
            editIngredient.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new RecipeIngredientFragment(ingredient).show(((FragmentActivity)context).getSupportFragmentManager(), "Edit_Ingredient");
                }
            });
            ImageButton deleteIngredient = view.findViewById(R.id.delete_ingredient);
            deleteIngredient.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new DeleteConformationFragment<Ingredient>(ingredient, ingredient.getTitle()).show(((FragmentActivity)context).getSupportFragmentManager(), "Delete_Ingredient");
                }
            });
        }
        return view;
    }

    /**
     * Build text for best before date
     * @param ingredient The ingredient
     * @return The best before date info in string format
     */
    protected static String getDateText(Ingredient ingredient) {
        Date date = ingredient.getBestBefore();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        return dateFormat.format(date);
    }
}
