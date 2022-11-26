package com.example.snackoverflow;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputLayout;

import java.security.DomainLoadStoreParameter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * A class modelling adding a meal to a Meal Planner
 * extends DialogFragment
 * implements AdapterView.OnItemSelectedListener
 * @see MealPlannerAdapter
 * @see Mealday
 * @see MealdayAdapter
 * */
public class MealPlannerAddMeal extends DialogFragment implements AdapterView.OnItemSelectedListener {
    // Check if we are editing data
    private boolean edit;
    // Data from other activities
    ArrayList<Recipe> recipeDataList;
    ArrayList<Ingredient> ingredientDataList;
    ArrayList<Ingredient> addedIngredients;
    private Double servingFinal;
    // Data storage
    private Spinner spinner;
    private ArrayAdapter<CharSequence> spinnerAdapter;
    private TextView TextViewDate;
    private RadioButton ingredientRadioButton;
    private RadioButton recipeRadioButton;
    private ListView ingredientsView;
    private TextInputLayout unit;
    private ArrayAdapter<Ingredient> ingredientArrayAdapter;
    private Button button;
    private TextInputLayout title;
    private Mealday mealDay;
    private Recipe recipe;
    // date picker
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    // listener for Meal Planner
    public OnFragmentInteractionListener listener;

    /**
     * Constructor for the add meal
     * */
    public MealPlannerAddMeal() {
        edit = false;
    }

    /**
     * Constructor for add meal to meal planner
     * @param mealDay the meal day
     * @param recipe the recipe to be added
     * */
    public MealPlannerAddMeal(Mealday mealDay, Recipe recipe, Double serving){
        this.edit = true;
        this.mealDay = mealDay;
        this.recipe = recipe;
        this.servingFinal = serving;
    }

    /**
     * interface for OnFragmentInteractionListener
     * */
    public interface OnFragmentInteractionListener {
        void addMeal(Recipe recipe, Date date, Double serving);

        void deleteMeal(Mealday mealday);

        void deleteMealPlan(Mealday mealday, Recipe recipe);
    }

    @Override
    public void onAttach(Context context) {
        // from lab
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @NonNull
    @Override
    //Todo impliment for multiple weeks
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //Inflate the layout
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.mealplanner_add_meal_fragment, null);
        spinner = view.findViewById(R.id.spinner);
        TextViewDate = view.findViewById(R.id.text_view_date);
        ingredientRadioButton = view.findViewById(R.id.radio_button_ingredient);
        recipeRadioButton = view.findViewById(R.id.radio_button_recipe);
        ingredientsView = view.findViewById(R.id.ingredients_view);
        unit = view.findViewById(R.id.unit);
        title = view.findViewById(R.id.text_input_title);
        button = view.findViewById(R.id.button);

        recipeDataList = new ArrayList<Recipe>();
        ingredientDataList = new ArrayList<Ingredient>();
        addedIngredients = new ArrayList<Ingredient>();

        if (edit){
            addedIngredients = recipe.getIngredients();
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        TextViewDate.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        getContext(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        onDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getDatePicker().setMaxDate(cal.getTimeInMillis() + 518400000L);
                dialog.getDatePicker().setMinDate(cal.getTimeInMillis());
                dialog.show();
            }
        });
        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                if (day <= 9){
                    String date_s = year + "-" + month + "-0" + day;
                    TextViewDate.setText(date_s);
                }
                else {
                    String date_s = year + "-" + month + "-" + day;
                    TextViewDate.setText(date_s);
                }
            }
        };

        // DUMMY DATA
        String []recipestitle = {"Curry","NOODLES","nidal"};
        int [] servings = {1,2};
        String []categories = {"Lunch","Dinner","nice"};

        String[]ingredirenttitle = {"apple", "banana", "mango", "fish"};

        for (int i =0;i<ingredirenttitle.length;i++){
            ingredientDataList.add(new Ingredient(ingredirenttitle[i], 1, 2, "fruit"));
        }
        for (int i =0;i<recipestitle.length;i++){
            recipeDataList.add(new Recipe(recipestitle[i], 120,2.0f,"Lunch","HAHA","boil", ingredientDataList));
        }
        //

        String[] recipeNames = new String[recipeDataList.size()+1];
        recipeNames[0] = "Recipe";
        for (int i =1;i<=recipeDataList.size();i++){
            recipeNames[i] = recipeDataList.get(i-1).getTitle();
        }

        String[] ingredientsNames = new String[ingredientDataList.size()+1];
        ingredientsNames[0] = "Ingredient";
        for (int i =1;i<=ingredientDataList.size();i++){
            ingredientsNames[i] = ingredientDataList.get(i-1).getTitle();
        }
        spinner.setOnItemSelectedListener(this);;

        ingredientRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner.setVisibility(View.VISIBLE);
                spinnerAdapter = new ArrayAdapter<CharSequence>(getContext(), android.R.layout.simple_spinner_item, ingredientsNames);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(spinnerAdapter);
                spinner.setSelection(0);

                unit.setVisibility(View.VISIBLE);
                unit.setHint("Unit");
                button.setVisibility(View.VISIBLE);
                title.setVisibility(View.VISIBLE);
                ingredientsView.setVisibility(View.VISIBLE);
            }
        });

        recipeRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner.setVisibility(View.VISIBLE);
                spinnerAdapter = new ArrayAdapter<CharSequence>(getContext(), android.R.layout.simple_spinner_item, recipeNames);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(spinnerAdapter);
                spinner.setSelection(0);
                unit.setVisibility(View.VISIBLE);
                unit.setHint("Servings");
                button.setVisibility(View.GONE);
                title.setVisibility(View.GONE);
                ingredientsView.setVisibility(View.GONE);
            }
        });

        ingredientArrayAdapter = new IngredientAdapter(this.getContext(), addedIngredients, "meal_ingredients");
        ingredientsView.setAdapter(ingredientArrayAdapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Ingredient foo = ingredientDataList.get(spinner.getSelectedItemPosition() - 1);
                foo.setUnit(Integer.valueOf(unit.getEditText().getText().toString()));
                addedIngredients.add(foo);
                ingredientArrayAdapter.notifyDataSetChanged();
                setListViewHeightBasedOnChildren(ingredientsView);
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        if (edit == false) {
            return builder
                    .setView(view)
                    .setTitle("Add Meal")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if(Objects.equals(spinner.getSelectedItemPosition(), 0) && recipeRadioButton.isChecked()){
                                new ErrorFragment("Invalid Recipe Chosen").show(getParentFragmentManager(), "error");
                            }
                            if(ingredientRadioButton.isChecked() && addedIngredients.isEmpty()){
                                new ErrorFragment("No Ingredients Chosen").show(getParentFragmentManager(), "error");
                            }
                            else{
                                String date_text = TextViewDate.getText().toString();
                                //TODO: is this check necessary?
                                if(Objects.equals(date_text,"Date")){
                                    new ErrorFragment("Invalid Date Chosen").show(getParentFragmentManager(), "error");
                                }
                                if(!ingredientRadioButton.isChecked() && !recipeRadioButton.isChecked()){
                                    new ErrorFragment("Chose recipe or ingredients").show(getParentFragmentManager(), "error");
                                }
                                else {
                                    if (recipeRadioButton.isChecked()) {
                                        Double servings = Double.valueOf(unit.getEditText().getText().toString());
                                        servingFinal = servings;
                                        recipe = recipeDataList.get(spinner.getSelectedItemPosition() - 1);
                                        servings = (servings/recipe.getServings());
                                        for (Ingredient ingredient: recipe.getIngredients()){
                                            Integer unit = ingredient.getUnit();
                                            unit = (int)Math.ceil(unit*servings);
                                            ingredient.setUnit(unit);
                                        }
                                    }
                                    if (ingredientRadioButton.isChecked()){
                                        String text = title.getEditText().getText().toString();
                                        recipe = new Recipe(text, 0, 1, "Ingredient Recipe", "", "", addedIngredients);
                                        servingFinal = 1.0d;
                                    }
                                    Date date = null;
                                    try {
                                        date = dateFormat.parse(date_text);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    listener.addMeal(recipe, date, servingFinal);
                                }
                                }
                            }
                    }).create();
        }
        else{
            if (!Objects.equals(recipe.getRecipeCategory(), "Ingredient Recipe")) {
                recipeRadioButton.setChecked(true);
                recipeRadioButton.setClickable(false);
                ingredientRadioButton.setClickable(false);
                spinner.setVisibility(View.VISIBLE);
                spinnerAdapter = new ArrayAdapter<CharSequence>(getContext(), android.R.layout.simple_spinner_item, recipeNames);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(spinnerAdapter);
                spinner.setSelection(Arrays.asList(recipeNames).indexOf(recipe.getTitle()));
                spinner.setEnabled(false);
                unit.setVisibility(View.VISIBLE);
                unit.setHint("Servings");
                unit.getEditText().setText(servingFinal.toString());
                unit.setEnabled(false);

            }
            else{
                ingredientRadioButton.setChecked(true);
                recipeRadioButton.setClickable(false);
                ingredientRadioButton.setClickable(false);
                spinner.setVisibility(View.VISIBLE);
                spinnerAdapter = new ArrayAdapter<CharSequence>(getContext(), android.R.layout.simple_spinner_item, ingredientsNames);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(spinnerAdapter);
                spinner.setEnabled(false);
                unit.setVisibility(View.GONE);
                title.setVisibility(View.VISIBLE);
                title.getEditText().setText(recipe.getTitle());
                title.setClickable(false);
                ingredientsView.setVisibility(View.VISIBLE);
            }
            TextViewDate.setText(dateFormat.format(mealDay.getDate()).substring(0, 10));
            TextViewDate.setClickable(false);
            return builder
                    .setView(view)
                    .setTitle("View Meal")
                    .setNeutralButton("Cancel", null)
                    .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
//                            mealDay.getMeals().remove(recipe);
//                            FirestoreDatabase.modifyMealPlan(mealDay);
                            listener.deleteMealPlan(mealDay,recipe);
                            if (mealDay.getMeals().size() == 0){
                                String date_text = TextViewDate.getText().toString();
                                Date date = null;
                                try {
                                    date = dateFormat.parse(date_text);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                listener.deleteMeal(mealDay);
                                FirestoreDatabase.deleteMealPlan(mealDay);
                            }
                        }
                    })
                    //TODO: Change title and date (if needed)
//                    .setPositiveButton("Change", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            if(Objects.equals(spinner.getSelectedItemPosition(), 0)){
//                                new ErrorFragment("Invalid Recipe Chosen").show(getParentFragmentManager(), "error");
//                            }
//                            else {
//                                mealDay.getMeals().remove(recipe);
//                                FirestoreDatabase.modifyMealPlan(mealDay);
//                                if (mealDay.getMeals().size() == 0) {
//                                    listener.deleteMeal(mealDay);
//                                }
//                                String date_text = TextViewDate.getText().toString();
//                                Recipe recipe = recipeDataList.get(spinner.getSelectedItemPosition() - 1);
//                                try {
////                                    LocalDate date = stringToDate(date_text);
//                                    Date date = dateFormat.parse(date_text);
//                                    listener.addMeal(recipe, date);
//                                } catch (ParseException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }
//                    })
                    .create();
        }
    }

    /**
     * Validates a date that is parsed in
     * @param edt the edit text field to be validated
     * */
    private void isValidDate(EditText edt) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            String after = dateFormat.format(dateFormat.parse(edt.getText().toString()));
        } catch (ParseException e) {
            edt.setError("Format: yyyy-mm-dd");
        }

    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        spinner.setSelection(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void setListViewHeightBasedOnChildren(ListView listView) {
        Log.e("Listview Size ", "" + listView.getCount());
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        if (listAdapter.getCount() >= 3){
            return;
        }

        View listItem = listAdapter.getView(0, null, listView);
        listItem.measure(0, 0);
        int totalHeight = listItem.getMeasuredHeight()*(listAdapter.getCount());

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();

    }
}
