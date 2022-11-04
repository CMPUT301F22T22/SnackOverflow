package com.example.snackoverflow;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
/**
 * View Ingredient Fragment that pops up when a user wants to view all
 * Ingredient added to a recipe
 * Extends DialogFragment
 * @see Ingredient
 * @see Recipe
 * */
public class RecipeIngredientViewFragment extends Fragment {
    private ListView ingredientStorageList;
    private ArrayAdapter<Ingredient> ingredientArrayAdapter;
    private ArrayList<Ingredient> ingredients;
    private View view;

    /**
     * Constructor for the Ingredient View Fragment for the Recipe Object
     * @param ingredients the list of ingredients
     * */
    public RecipeIngredientViewFragment(ArrayList<Ingredient> ingredients){
        this.ingredients = ingredients;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_recipe_ingredient_view, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ingredientStorageList = view.findViewById(R.id.list);
        ingredientArrayAdapter = new IngredientAdapter(getContext(), ingredients, "recipe");
        ingredientStorageList.setAdapter(ingredientArrayAdapter);
    }
}