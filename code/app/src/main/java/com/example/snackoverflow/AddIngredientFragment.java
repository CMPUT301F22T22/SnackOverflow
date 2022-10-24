package com.example.snackoverflow;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddIngredientFragment extends DialogFragment {
    private EditText ingredientDesc;
    private EditText ingredientAmount;
    private EditText ingredientUnit;
    private EditText ingredientBestBefore;
    private EditText ingredientLocation;
    private EditText ingredientCategory;
    private OnFragmentInteractionListener listener;
    private Ingredient selectedIngredient;

    @Override
    public void onStart() {
        super.onStart();
    }

    public interface OnFragmentInteractionListener {
        void onOkPressed(Ingredient selectedIngredient);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString());
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_ingredient_fragment_layout, null);
        ingredientDesc = view.findViewById(R.id.ingredient_description_editText);
        ingredientBestBefore = view.findViewById(R.id.ingredient_bestBefore_editText);
        ingredientLocation = view.findViewById(R.id.ingredient_location_editText);
        ingredientAmount = view.findViewById(R.id.ingredient_amount_editText);
        ingredientUnit = view.findViewById(R.id.ingredient_unit_editText);
        ingredientCategory = view.findViewById(R.id.ingredient_category_editText);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        ingredientBestBefore.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Is_Valid_date(foodBestBefore);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                isValidDate(ingredientBestBefore);
            }
        });

        ingredientLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                isValidLocation(ingredientLocation);
            }
        });

        return builder
                .setView(view)
                .setTitle("Add Ingredient")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String a = ingredientDesc.getText().toString();
                        Integer b = (int) Math.ceil(Double.parseDouble(ingredientAmount.getText().toString()));
                        Integer c = Integer.parseInt(ingredientUnit.getText().toString());
                        Date d = null;
                        try {
                            d = dateFormat.parse(ingredientBestBefore.getText().toString());
                        }
                        catch (ParseException e) {
                            e.printStackTrace();
                        }

                        String e = ingredientLocation.getText().toString();
                        String f = ingredientCategory.getText().toString();
                        listener.onOkPressed(new Ingredient(a, d, e, c, b, f));
                    }
                }).create();


    }

    private void isValidDate(EditText edt) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        try {
            String after = dateFormat.format(dateFormat.parse(edt.getText().toString()));
        } catch (ParseException e) {
            edt.setError("Format: yyyy-mm-dd");
        }

    }

    private void isValidLocation(EditText edt) {
        String txt = edt.getText().toString();

        if (!txt.matches("Pantry|Freezer|Fridge")) {
            edt.setError("One of Pantry, Fridge, or Freezer");
        }
    }
}
