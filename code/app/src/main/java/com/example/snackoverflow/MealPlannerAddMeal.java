package com.example.snackoverflow;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
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
    private boolean view;
    final static FirebaseFirestore db = FirebaseFirestore.getInstance();
    final static CollectionReference recipeCol = db.collection("recipe");
    // Data from other activities
    ArrayList<Recipe> recipeDataList = new ArrayList<Recipe>();
    ArrayList<Ingredient> ingredientDataList;
    ArrayList<Ingredient> addedIngredients;
    private Double servingFinal;
    // Data storage
    private Spinner spinner;
    ArrayAdapter<CharSequence> spinnerAdapter;
    private TextView TextViewDate;
    private RadioButton ingredientRadioButton;
    private LinearLayout linearLayout;
    private RadioButton recipeRadioButton;
    private TextInputLayout unit;
    private TextInputEditText unit_editText;
    private ArrayAdapter<Ingredient> ingredientArrayAdapter;
    private Button button;
    private TextInputLayout ingredientDescription;
    private TextInputLayout mealplannerCategory;
    private TextInputLayout mealplannerAmt;
    private Mealday mealDay;
    private Recipe recipe;
    // date picker
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    // listener for Meal Planner to implement the 3 main functions
    public OnFragmentInteractionListener listener;

    /**
     * Constructor for the add meal
     * */
    public MealPlannerAddMeal() {
        view = false;
    }

    /**
     * Constructor for add meal to meal planner
     * @param mealDay the meal day
     * @param recipe the recipe to be added
     * */
    public MealPlannerAddMeal(Mealday mealDay, Recipe recipe, Double serving){
        this.view = true;
        this.mealDay = mealDay;
        this.recipe = recipe;
        this.servingFinal = serving;
    }

    /**
     * interface for OnFragmentInteractionListener that implements mainly 3
     * function 1) adding a meal
     *          2) deleting a meal
     *          3) deleting the whole day (if no meals remaining on the day)
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
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //Inflate the layout
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.mealplanner_add_meal_fragment, null);

        spinner = view.findViewById(R.id.spinner);
        ingredientDescription = view.findViewById(R.id.ingredientDesc);
        mealplannerAmt = view.findViewById(R.id.mealplanner_amount_text);
        mealplannerCategory = view.findViewById(R.id.mealplanner_category_text);
        TextInputEditText TextViewDate = view.findViewById(R.id.text_view_date);
        ingredientRadioButton = view.findViewById(R.id.radio_button_ingredient);
        recipeRadioButton = view.findViewById(R.id.radio_button_recipe);
        unit = view.findViewById(R.id.unit);

        recipeDataList = new ArrayList<Recipe>();
        ingredientDataList = new ArrayList<Ingredient>();
        addedIngredients = new ArrayList<Ingredient>();

        if (this.view){
            addedIngredients = recipe.getIngredients();
        }

        //Giving the user the option to pick date from the Datepicker with limited range
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        TextViewDate.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our edit text.
                                TextViewDate.setText(year + "-" + (monthOfYear+1) + "-" + dayOfMonth);
                            }
                        },
                        year, month, day);

                datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
                datePickerDialog.getDatePicker().setMaxDate(cal.getTimeInMillis() + 518400000L);
                datePickerDialog.show();
            }
        });

        ArrayList<CharSequence> recipeNames = new ArrayList<CharSequence>();

        //Setting the spinner to retrieve recipes from firebase
        recipeNames.add("Select");
        spinnerAdapter = new ArrayAdapter<CharSequence>(getContext(),
                android.R.layout.simple_spinner_item,
                recipeNames);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(0);

        recipeDataList = new ArrayList<Recipe>();
        recipeCol.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                recipeDataList.clear();
                try {
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String id = doc.getId();
                        System.out.println(id);
                        Map<String, Object> data = doc.getData();
                        String title = data.get("title").toString();
                        int prep_time = Integer.valueOf(data.get("prep_time").toString());
                        float servings = Float.parseFloat(data.get("servings").toString());
                        String category = data.get("category").toString();
                        String instructions = data.get("instructions").toString();
                        String comments = data.get("comments").toString();
                        ArrayList<Object> ingredientArray = (ArrayList<Object>) data.get("ingredients");
                        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
                        for (int i = 0; i < ingredientArray.size(); i++) {
                            Map<String, Object> ingredientMap = (Map<String, Object>) ingredientArray.get(i);
                            ingredients.add(new Ingredient(ingredientMap.get("title").toString(), Integer.valueOf(ingredientMap.get("amount").toString()),
                                    Integer.valueOf(ingredientMap.get("unit").toString()), ingredientMap.get("category").toString()));
                        }
//
                        StorageReference storageRef = FirebaseStorage.getInstance().getReference("recipe/" + id + ".jpg");
                        int imageTrackingData = Integer.valueOf(data.get("image_tracker").toString());
                        try {
                            File localFile = File.createTempFile("tempfile", ".jpg");
                            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Bitmap imgBitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                    Recipe recipe = new Recipe(id, title, prep_time, servings,
                                            category, comments, instructions,ingredients, imgBitmap);
                                    recipeDataList.add(recipe);
                                    for (int i =1;i<=recipeDataList.size();i++){
                                        if(!recipeNames.contains(recipeDataList.get(i-1).getTitle())) {
                                            recipeNames.add(recipeDataList.get(i - 1).getTitle());
                                        }
                                    }

                                    spinnerAdapter.notifyDataSetChanged();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Recipe recipe = new Recipe(id, title, prep_time, servings,
                                            category, comments, instructions,ingredients, null);
                                    recipeDataList.add(recipe);
                                    spinnerAdapter.notifyDataSetChanged();
                                }
                            });

                        } catch (IOException e) {

                        }
                    }
//                    recipeArrayAdapter.notifyDataSetChanged();
                } catch (NullPointerException e) {
                }
            }

        });

        spinner.setOnItemSelectedListener(this);
        //Now the fragment has two radio button listener, which edits the fragment in
        //such a way to get the required inputs based on if the user needs to add recipe or ingredient

        ingredientRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner.setVisibility(View.GONE);
                ingredientDescription.setVisibility(View.VISIBLE);
                unit.setVisibility(View.VISIBLE);
                unit.setHint("Unit");
                mealplannerAmt.setVisibility(View.VISIBLE);
                mealplannerCategory.setVisibility(View.VISIBLE);
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
                mealplannerAmt.setVisibility(View.GONE);
                mealplannerCategory.setVisibility(View.GONE);
                unit.setVisibility(View.VISIBLE);
                unit.setHint("Servings");
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        if (this.view == false) {
            return builder
                    .setView(view)
                    .setTitle("Add Meal")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Error chceking for empty inputs
                            if(Objects.equals(spinner.getSelectedItemPosition(), 0) && recipeRadioButton.isChecked()){
                                new ErrorFragment("Invalid Recipe Chosen").show(getParentFragmentManager(), "error");
                            }
                            if(ingredientRadioButton.isChecked() && !addedIngredients.isEmpty()){
                                new ErrorFragment("No Ingredients Chosen").show(getParentFragmentManager(), "error");
                            }
                            else{
                                String date_text = TextViewDate.getText().toString();
                                if(Objects.equals(date_text,"Date")){
                                    new ErrorFragment("Invalid Date Chosen").show(getParentFragmentManager(), "error");
                                }
                                if(!ingredientRadioButton.isChecked() && !recipeRadioButton.isChecked()){
                                    new ErrorFragment("Chose recipe or ingredients").show(getParentFragmentManager(), "error");
                                }
                                else {
                                    //If recipe is selected, user is able to adjust servings accordingly in order to add
                                    //scaled number of ingredients for the recipe to the shopping list
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

                                    //For ingredients, user inputs what they would like to have and other details regarding ingredient
                                    if (ingredientRadioButton.isChecked()){
                                        String text = ingredientDescription.getEditText().getText().toString();
                                        String id = "00000000000000";
                                        Ingredient foo = new Ingredient(ingredientDescription.getEditText().getText().toString(),Integer.valueOf(mealplannerAmt.getEditText().getText().toString()),Integer.valueOf(unit.getEditText().getText().toString()),mealplannerCategory.getEditText().getText().toString());
                                        addedIngredients.add(foo);
                                        recipe = new Recipe(id,text, 0, 1, addedIngredients.get(0).getCategory(), "", "", addedIngredients,null);
                                        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.grocery);
                                        recipe.setImageBitmap(bm);

                                        servingFinal = 1.0d;
                                    }
                                    Date date = null;
                                    try {
//                                      date = stringToDate(date_text);
                                        date = dateFormat.parse(date_text);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    //fulfills our Adding of a recipe/ingredient
                                    listener.addMeal(recipe, date, servingFinal);
                                }
                            }
                        }
                    }).create();
        }
        //In this condition, fragment is enabled to view the details of existing meal or choose to delete
        else{
            //Editing fragment to view details of an ingredient
            if (Objects.equals(recipe.getId(), "00000000000000")) {
                ingredientRadioButton.setChecked(true);
                recipeRadioButton.setClickable(false);
                ingredientRadioButton.setClickable(false);
                unit.setVisibility(View.VISIBLE);
                unit.setHint("Servings");
                unit.getEditText().setText(Integer.valueOf(recipe.getIngredients().get(0).getUnit()).toString());
                unit.setEnabled(false);
                ingredientDescription.setVisibility(View.VISIBLE);
                ingredientDescription.getEditText().setText(recipe.getTitle());
                ingredientDescription.setEnabled(false);
                mealplannerCategory.setVisibility(View.VISIBLE);
                mealplannerCategory.getEditText().setText(recipe.getRecipeCategory().substring(1));
                mealplannerCategory.setEnabled(false);
                TextViewDate.setEnabled(false);

            }
            else{
                //Editing fragment to view details of an ingredient
                recipeNames.clear();
                recipeNames.add(recipe.getTitle());
                spinner.setSelection(Arrays.asList(recipeNames).indexOf(recipe.getTitle()));
                recipeRadioButton.setChecked(true);
                recipeRadioButton.setClickable(false);
                ingredientRadioButton.setClickable(false);
                spinner.setVisibility(View.VISIBLE);
                spinnerAdapter = new ArrayAdapter<CharSequence>(getContext(), android.R.layout.simple_spinner_item, recipeNames);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(spinnerAdapter);
                spinner.setEnabled(false);
                unit.setVisibility(View.VISIBLE);
                unit.setHint("Servings");
                unit.getEditText().setText(servingFinal.toString());
                unit.setEnabled(false);
                TextViewDate.setEnabled(false);

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
                            //calling the second function of our listener
                            listener.deleteMealPlan(mealDay,recipe);
                            //if the day has no meals, day is removed with the help of our 3rd function
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
}
