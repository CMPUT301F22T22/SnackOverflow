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
import android.widget.TextView;

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
 * Ingredient to the storage, edit an already present ingredient in the storage
 * and when a user shops for ingredients in the shopping list and confims to add it to storage.
 *
 * Extends DialogFragment
 * @see Ingredient
 * */
public class AddIngredientFragment extends DialogFragment {
    // declaring the variables for Fields in the XML file
    private TextInputLayout ingredientDescLayout;
    private TextInputLayout ingredientCategoryLayout;
    private EditText ingredientDesc;
    private EditText ingredientAmount;
    private EditText ingredientUnit;
    private EditText ingredientBestBefore;
    private RadioButton ingredientLocation;
    private RadioGroup locationRadioGroup;
    private EditText ingredientCategory;

    /**
     * For Starting the fragment.
     */
    @Override
    public void onStart() {
        super.onStart();
    }

    /**
     * Constructor for Edit functionality for Ingredients.
     * @param ingredient the ingredient passed in or selected
     * @return None
     */
    static AddIngredientFragment newInstance(Ingredient ingredient) {
        // Creates a bundle and passes it tot he Add Ingredient fragment
        Bundle args = new Bundle();
        args.putSerializable("ingredient", ingredient);
        AddIngredientFragment fragment = new AddIngredientFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Sets the Calendar pop up for the Best Before Date field.
     * @param ingredientBestBefore Edit Text for the BEst Before field
     * @param currentDate The date to be set on the calendar
     */
    public void setCalendarForBestBefore(EditText ingredientBestBefore, Date currentDate) {
        // Sets the calendar View on click
        ingredientBestBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                // Gets the current date and sets it on calendar
                if (currentDate != null) {
                    c.setTime(currentDate);
                }
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // Implements the Date Picker Dialog for the user to select a date
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
                // Disable the past dates on the dialog
                datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
                // Shows th Dialog for the user to select the date
                datePickerDialog.show();
            }
        });
    }

    /**
     * Sets the limits on Text Fields, limiting them to 30 characters
     * @param field The Exit Text field to put the check on
     * @param layout The Layout for the Edit Text field
     */
    public void setErrors(EditText field, TextInputLayout layout) {
        field.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                // Set the error if the chaarcter count is greater the max length allowed.
                if (s.length() > layout.getCounterMaxLength()) {
                    layout.setError("Max character length is " + layout.getCounterMaxLength());
                } else {
                    layout.setError(null);
                }
            }
        });
    }

    /**
     * Sets the Locaton Radio Button based on the value of Location
     * @param view
     * @param locationValue The string value of the location of the ingredient
     */
    public void setLocationButton(View view, String locationValue){
        // Finds the Radio button and selects it corresponding to the locationValue
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
    }

    /**
     * Parses the initial fields and sets the values for the corresponding Edit Text Fields.
     * @param view
     * @param initialIngredient the Initial Ingredient that is selected.
     * @return True: if the ingredient is from shopping list
     *         False: if it is from the Storage.
     */
    public boolean parseInitialFields(View view, Ingredient initialIngredient){
        // Sets the Fields in the Fragments from the initial ingredient
        boolean isShoppingListItem = false;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        ingredientDesc.setText(initialIngredient.getTitle());
        try {
            ingredientBestBefore.setText(dateFormat.format(initialIngredient.getBestBefore()));
        } catch (NullPointerException e) {
            isShoppingListItem = true;
            ingredientBestBefore.setText("");
        }
        ingredientUnit.setText(String.valueOf(initialIngredient.getUnit()));
        ingredientAmount.setText(String.valueOf(initialIngredient.getAmount()));
        ingredientCategory.setText(initialIngredient.getCategory());
        setLocationButton(view, initialIngredient.getLocation());
        setCalendarForBestBefore(ingredientBestBefore, initialIngredient.getBestBefore());
        return isShoppingListItem;
    }

    /**
     * Returns an ingredient object from the all the values filled in by the user
     * in the fragment
     * @param view
     * @return Ingredient Object
     */
    public Ingredient parseFilledValues(View view) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String ingredientDescString = ingredientDesc.getText().toString();
        Integer ingredientAmountInt = (int) Double.parseDouble(ingredientAmount.getText().toString());
        Integer ingredientUnitInt = Integer.parseInt(ingredientUnit.getText().toString());
        String ingredientBestBeforeString = ingredientBestBefore.getText().toString();
        String ingredientCategoryString = ingredientCategory.getText().toString();

        ingredientLocation = (RadioButton) view.findViewById(locationRadioGroup.getCheckedRadioButtonId());
        String ingredientLocationString = ingredientLocation.getText().toString();

        Date ingredientBestBeforeDate = null;
        try {
            ingredientBestBeforeDate = dateFormat.parse(ingredientBestBeforeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // return the new ingredient created from the values inputted by the user
        return new Ingredient(ingredientDescString, ingredientBestBeforeDate, ingredientLocationString, ingredientAmountInt, ingredientUnitInt, ingredientCategoryString);
    }

    /**
     * Handles the creation and actions for the Add Ingredient Fragment.
     * @param savedInstanceState
     * @return
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Find the views defined in the XML files
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

        // Set Errors for description and Category field
        setErrors(ingredientDesc, ingredientDescLayout);
        setErrors(ingredientCategory, ingredientCategoryLayout);

        // Get the initial bundle if any
        Bundle initialBundle = getArguments();

        if (initialBundle == null) {
            // Sets the calendar view for best before Date
            setCalendarForBestBefore(ingredientBestBefore, null);

            return builder
                    .setView(view)
                    .setTitle("Add Ingredient")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Ingredient newIngredient = parseFilledValues(view);
                            // Add the ingredient to Firestore Database
                            FirestoreDatabase.addIngredient(newIngredient);
                        }
                    }).create();
        } else {
            // Get the initial Bundle
            Ingredient initialIngredient = (Ingredient) initialBundle.get("ingredient");
            // Parse the initial fields form the selected ingredient
            boolean isShoppingListItem = parseInitialFields(view, initialIngredient);

            // Define the Title of the fragment
            String fragmentTitle;
            if (isShoppingListItem) {
                fragmentTitle = "Add Ingredient to Storage";
            } else {
                fragmentTitle = "Edit Ingredient";
            }

            return builder
                    .setView(view)
                    .setTitle(fragmentTitle)
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Parse the values filled by the user
                            Ingredient updatedIngredient = parseFilledValues(view);

                            // Modify initial ingredient
                            initialIngredient.setTitle(updatedIngredient.getTitle());
                            initialIngredient.setAmount(updatedIngredient.getAmount());
                            initialIngredient.setBestBefore(updatedIngredient.getBestBefore());
                            initialIngredient.setUnit(updatedIngredient.getUnit());
                            initialIngredient.setCategory(updatedIngredient.getCategory());
                            initialIngredient.setLocation(updatedIngredient.getLocation());
                            if (isShoppingListItem) {
                                // If the item is from shopping list, add it to the database
                                FirestoreDatabase.addIngredient(initialIngredient);
                            } else {
                                // Modify the existing ingredient in the database
                                FirestoreDatabase.modifyIngredient(initialIngredient);
                            }
                        }
                    }).create();
        }
    }
}