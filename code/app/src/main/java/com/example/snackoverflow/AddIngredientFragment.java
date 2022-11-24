package com.example.snackoverflow;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * Add Ingredient Fragment that pops up when a user wants to add an
 * Ingredient to the storage
 * Extends DialogFragment
 * @see Ingredient
 * */
public class AddIngredientFragment extends DialogFragment {
    private TextInputLayout ingredientDescLayout;
    private TextInputLayout ingredientCategoryLayout;
    private EditText ingredientDesc;
    private EditText ingredientAmount;
    private EditText ingredientUnit;
    private EditText ingredientBestBefore;
    private RadioButton ingredientLocation;
    private RadioGroup locationRadioGroup;
    private EditText ingredientCategory;
    private OnFragmentInteractionListener listener;

    @Override
    public void onStart() {
        super.onStart();
    }

    /**
     * Implements the Fragment Interaction Listener and defines
     * the onOkPressed function
     * */
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
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_ingredient_fragment_layout, null);
        ingredientDescLayout = view.findViewById(R.id.ingredient_description_layout);
        ingredientCategoryLayout = view.findViewById(R.id.ingredient_category_layout);
        ingredientDesc = view.findViewById(R.id.ingredient_description_editText);
        ingredientBestBefore = view.findViewById(R.id.ingredient_bestBefore_editText);
        locationRadioGroup = view.findViewById(R.id.ingredient_location_radioGroup);
        ingredientAmount = view.findViewById(R.id.ingredient_amount_editText);
        ingredientUnit = view.findViewById(R.id.ingredient_unit_editText);
        ingredientCategory = view.findViewById(R.id.ingredient_category_editText);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        ingredientDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() > ingredientDescLayout.getCounterMaxLength()) {
                    ingredientDescLayout.setError("Max character length is " + ingredientDescLayout.getCounterMaxLength());
                } else {
                    ingredientDescLayout.setError(null);
                }
            }
        });

        ingredientCategory.addTextChangedListener(new TextWatcher() {
            @Override

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() > ingredientCategoryLayout.getCounterMaxLength()) {
                    ingredientCategoryLayout.setError("Max character length is " + ingredientCategoryLayout.getCounterMaxLength());
                } else {
                    ingredientCategoryLayout.setError(null);
                }
            }
        });

        Bundle initialBundle = getArguments();

        if (initialBundle == null) {

            ingredientBestBefore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Calendar c = Calendar.getInstance();
                    int year = c.get(Calendar.YEAR);
                    int month = c.get(Calendar.MONTH);
                    int day = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(
                            getContext(),
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {
                                    // on below line we are setting date to our edit text.
                                    ingredientBestBefore.setText(year + "-" + (monthOfYear+1) + "-" + dayOfMonth);
                                }
                            },
                            year, month, day);

                    datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
                    datePickerDialog.show();
                }
            });

            return builder
                    .setView(view)
                    .setTitle("Add Ingredient")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String ingredientDescString = ingredientDesc.getText().toString();
                            Integer ingredientAmountInt = (int) Double.parseDouble(ingredientAmount.getText().toString());
                            Integer ingredientUnitInt = Integer.parseInt(ingredientUnit.getText().toString());
                            String ingredientBestBeforeString = ingredientBestBefore.getText().toString();
                            Date ingredientBestBeforeDate = null;
                            try {
                                ingredientBestBeforeDate = dateFormat.parse(ingredientBestBeforeString);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            int selectedLocation = locationRadioGroup.getCheckedRadioButtonId();
                            ingredientLocation = (RadioButton) view.findViewById(selectedLocation);
                            String ingredientLocationString = ingredientLocation.getText().toString();

                            String ingredientCategoryString = ingredientCategory.getText().toString();

                            listener.onOkPressed(new Ingredient(ingredientDescString, ingredientBestBeforeDate, ingredientLocationString, ingredientUnitInt, ingredientAmountInt, ingredientCategoryString));
                        }
                    }).create();
        } else {
            Ingredient initialIngredient = (Ingredient) initialBundle.get("ingredient");
            ingredientDesc.setText(initialIngredient.getTitle());
            ingredientBestBefore.setText(dateFormat.format(initialIngredient.getBestBefore()));
            ingredientUnit.setText(String.valueOf(initialIngredient.getUnit()));
            ingredientAmount.setText(String.valueOf(initialIngredient.getAmount()));
            ingredientCategory.setText(initialIngredient.getCategory());
            String locationValue = initialIngredient.getLocation();

            Date currentDate = initialIngredient.getBestBefore();

            ingredientBestBefore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Calendar c = Calendar.getInstance();
                    c.setTime(currentDate);
                    int year = c.get(Calendar.YEAR);
                    int month = c.get(Calendar.MONTH);
                    int day = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(
                            getContext(),
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {
                                    // on below line we are setting date to our edit text.
                                    ingredientBestBefore.setText(year + "-" + (monthOfYear+1) + "-" + dayOfMonth);
                                }
                            },
                            year, month, day);

                    datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
                    datePickerDialog.show();
                }
            });

            if (Objects.equals(locationValue, "Freezer")) {
                RadioButton location = view.findViewById(R.id.location_freezer);
                location.setChecked(true);
            } else if (Objects.equals(locationValue, "Fridge")) {
                RadioButton location = view.findViewById(R.id.location_fridge);
                location.setChecked(true);
            } else if (Objects.equals(locationValue, "Pantry")) {
                RadioButton location = view.findViewById(R.id.location_pantry);
                location.setChecked(true);
            }

            return builder
                    .setView(view)
                    .setTitle("Edit Ingredient")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String ingredientDescString = ingredientDesc.getText().toString();
                            Integer ingredientAmountInt = (int) Double.parseDouble(ingredientAmount.getText().toString());
                            Integer ingredientUnitInt = Integer.parseInt(ingredientUnit.getText().toString());
                            String ingredientBestBeforeString = ingredientBestBefore.getText().toString();
                            Date ingredientBestBeforeDate = null;
                            try {
                                ingredientBestBeforeDate = dateFormat.parse(ingredientBestBeforeString);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            int selectedLocation = locationRadioGroup.getCheckedRadioButtonId();
                            ingredientLocation = (RadioButton) view.findViewById(selectedLocation);
                            String ingredientLocationString = ingredientLocation.getText().toString();

                            String ingredientCategoryString = ingredientCategory.getText().toString();

                            initialIngredient.setTitle(ingredientDescString);
                            initialIngredient.setAmount(ingredientAmountInt);
                            initialIngredient.setBestBefore(ingredientBestBeforeDate);
                            initialIngredient.setUnit(ingredientUnitInt);
                            initialIngredient.setCategory(ingredientCategoryString);
                            initialIngredient.setLocation(ingredientLocationString);
                        }
                    }).create();

        }
    }

    static AddIngredientFragment newInstance(Ingredient ingredient) {
        Bundle args = new Bundle();
        args.putSerializable("ingredient", ingredient);

        AddIngredientFragment fragment = new AddIngredientFragment();
        fragment.setArguments(args);
        return fragment;
    }

}