package com.example.snackoverflow;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Objects;

public class RecipeIngredientViewFragment extends DialogFragment {
    private ListView ingredientStorageList;
    private ArrayAdapter<Ingredient> ingredientArrayAdapter;
    private ArrayList<Ingredient> ingredients;
    private Context context;

    public RecipeIngredientViewFragment(Context context, ArrayList<Ingredient> ingredients){
        this.context = context;
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.recipe_view_ingredients_fragment, null);

        ingredientStorageList = view.findViewById(R.id.list);

        ingredientArrayAdapter = new IngredientAdapter(context, ingredients, "recipe");
        ingredientStorageList.setAdapter(ingredientArrayAdapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Ingredients")
                .setNegativeButton("Cancel", null).create();

    }
}
