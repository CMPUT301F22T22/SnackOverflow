package com.example.snackoverflow;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class RecipeAddIngredientFragment extends DialogFragment {
    // Check if we are editing data
    private boolean edit;
    // Data storage
    private TextInputLayout textInputDescription;
    private TextInputLayout textInputLayout;
    private TextInputLayout textInputUnit;
    private TextInputLayout textInputCategory;
    // Ingredient object needs to be added
    private Ingredient ingredient;
    // listener for Recipe fragment
    private OnFragmentInteractionListener listener;

    public RecipeAddIngredientFragment(){
        edit = false;
    }
    public RecipeAddIngredientFragment(Ingredient ingredient){
        this.ingredient = ingredient;
        edit = true;
    }

    public interface OnFragmentInteractionListener {
        void addIngredient(Ingredient ingredient);
        void editIngredient(Ingredient ingredient);
    }

    @Override
    public  void onAttach(Context context){
        // from lab
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener){
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
        //Inflate the layout
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.recipe_add_ingredients_fragment_layout, null);
        textInputDescription = view.findViewById(R.id.text_input_description);
        textInputLayout = view.findViewById(R.id.text_input_amount);
        textInputUnit = view.findViewById(R.id.text_input_unit);
        textInputCategory = view.findViewById(R.id.text_input_category);

        if (edit == true){
            // MAKE CHANGES WHEN INGREDIENT MADE
            // Set data
            textInputDescription.getEditText().setText(ingredient.getDescription());
            textInputLayout.getEditText().setText(String.valueOf(ingredient.getAmount()));
            textInputUnit.getEditText().setText(String.valueOf(ingredient.getUnit()));
            textInputCategory.getEditText().setText(ingredient.getCategory());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
        if (edit == false){
            return builder
                    .setView(view)
                    .setTitle("Add Ingredient")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String Description = textInputDescription.getEditText().getText().toString();
                            Integer Amount = Integer.valueOf(textInputLayout.getEditText().getText().toString());
                            Integer Unit = Integer.valueOf(textInputUnit.getEditText().getText().toString());
                            String Category = textInputCategory.getEditText().getText().toString();

                            listener.addIngredient(new Ingredient(Description, null, null,Amount, Unit, Category));
                            listener = null;
                        }
                    }).create();
        }
        else {
            return builder
                    .setView(view)
                    .setTitle("Edit Ingredient")
                    .setNegativeButton("Cancel",null)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String Description = textInputDescription.getEditText().getText().toString();
                            Integer Amount = Integer.valueOf(textInputLayout.getEditText().getText().toString());
                            Integer Unit = Integer.valueOf(textInputUnit.getEditText().getText().toString());
                            String Category = textInputCategory.getEditText().getText().toString();

                            if (!Objects.equals(Description, "")){
                                ingredient.setDescription(Description);
                            }
                            if (!Objects.equals(Amount, "")){
                                ingredient.setAmount(Amount);
                            }
                            if (!Objects.equals(Unit, "")){
                                ingredient.setUnit(Unit);
                            }
                            if (!Objects.equals(Category,"")){
                                ingredient.setCategory(Category);
                            }
                            listener.editIngredient(ingredient);
                        }
                    }).create();
        }
    }
}

