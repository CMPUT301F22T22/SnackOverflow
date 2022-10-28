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
    private TextInputLayout text_input_description;
    private TextInputLayout text_input_amount;
    private TextInputLayout text_input_unit;
    private TextInputLayout text_input_category;
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
        void Add_food(Ingredient ingredient);
        void Edit_food(Ingredient ingredient);
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
        text_input_description = view.findViewById(R.id.text_input_description);
        text_input_amount = view.findViewById(R.id.text_input_amount);
        text_input_unit = view.findViewById(R.id.text_input_unit);
        text_input_category = view.findViewById(R.id.text_input_category);

        if (edit == true){
            // MAKE CHANGES WHEN INGREDIENT MADE
            // Set data
            text_input_description.getEditText().setText(ingredient.getDescription());
            text_input_amount.getEditText().setText(ingredient.getAmount());
            text_input_unit.getEditText().setText(ingredient.getUnit());
            text_input_category.getEditText().setText(ingredient.getCategory());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        if (edit == false){
            return builder
                    .setView(view)
                    .setTitle("Add Ingredient")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String Description = text_input_description.getEditText().getText().toString();
                            Integer Amount = Integer.valueOf(text_input_amount.getEditText().getText().toString());
                            Integer Unit = Integer.valueOf(text_input_unit.getEditText().getText().toString());
                            String Category = text_input_category.getEditText().getText().toString();

                            listener.Add_food(new Ingredient(Description, null, null,Amount, Unit, Category));
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
                            String Description = text_input_description.getEditText().getText().toString();
                            Integer Amount = Integer.valueOf(text_input_amount.getEditText().getText().toString());
                            Integer Unit = Integer.valueOf(text_input_unit.getEditText().getText().toString());
                            String Category = text_input_category.getEditText().getText().toString();

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
                            listener.Edit_food(ingredient);
                        }
                    }).create();
        }
    }
}

