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
import android.widget.ListView;

import java.util.ArrayList;

public class RecipeIngredientViewFragment extends Fragment implements RecipeAddIngredientFragment.OnFragmentInteractionListener{
    private ListView ingredientStorageList;
    private ArrayAdapter<Ingredient> ingredientArrayAdapter;
    private ArrayList<Ingredient> ingredients;
    private View view;

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

    @Override
    public void addIngredient(Ingredient ingredient) {

    }

    @Override
    public void editIngredient(Ingredient ingredient) {
    }
}