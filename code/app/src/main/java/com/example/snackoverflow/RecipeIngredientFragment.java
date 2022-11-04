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

public class RecipeIngredientFragment extends DialogFragment {
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

    public RecipeIngredientFragment(){
        edit = false;
    }
    public RecipeIngredientFragment(Ingredient ingredient){
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
        textInputUnit = view.findViewById(R.id.text_input_unit);

        if (edit == true){
            // MAKE CHANGES WHEN INGREDIENT MADE
            // Set data
            textInputDescription.getEditText().setText(ingredient.getTitle());
            textInputUnit.getEditText().setText(String.valueOf(ingredient.getUnit()));
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
                            Integer Amount = null;
                            Integer Unit = Integer.valueOf(textInputUnit.getEditText().getText().toString());
                            String Category = null;

                            listener.addIngredient(new Ingredient(Description, null, null, 0, Unit, null));
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
                            Integer Unit = Integer.valueOf(textInputUnit.getEditText().getText().toString());

                            if (!Objects.equals(Description, "")){
                                ingredient.setTitle(Description);
                            }

                            if (!Objects.equals(Unit, "")){
                                ingredient.setUnit(Unit);
                            }
                            listener.editIngredient(ingredient);
                        }
                    }).create();
        }
    }
}

