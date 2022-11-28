package com.example.snackoverflow;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;


/**
 * Add Recipe Class for adding new Recipe to Recipes storage
 * extends AppCompatActivity
 * implements RecipeIngredientFragment.OnFragmentInteractionListener
 * implements DeleteConformationFragment.OnFragmentInteractionListener
 * @see Recipe
 * @see RecipeActivity
 * */
public class AddRecipe extends AppCompatActivity implements RecipeIngredientFragment.OnFragmentInteractionListener, DeleteConformationFragment.OnFragmentInteractionListener{

    public CircleImageView imageView;
    private Drawable imageViewDrawable;
    private Drawable imageViewBackground;
    private int imageViewRadius;
    private TextInputLayout titleText;
    private TextInputLayout categoryText;
    private TextInputLayout servingText;
    private TextInputLayout prepText;
    private ArrayAdapter<Ingredient> ingredientArrayAdapter;
    private ArrayList<Ingredient> ingredients;
    private ListView ingredientsView;
    private Button showMore;
    private Button addIngredient;
    private Button addRecipe;
    private TextInputLayout instructionsText;
    private TextInputLayout commentsText;
    private Fragment IngredientsView;
    private Uri uri;

    // EditText elements
    EditText editTitleText;
    EditText editCategoryText;
    EditText editServingText;
    EditText editPrepText;
    EditText editInstructionsText;
    EditText editCommentsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        // Initializing variables
        imageView = findViewById(R.id.recipe_addPhoto);
        // imageview default
        imageViewDrawable = imageView.getDrawable();
        imageViewBackground = imageView.getBackground();
        imageViewRadius = imageView.getLayoutParams().width;

        // Get UI elements
        titleText = findViewById(R.id.recipe_title);
        categoryText = findViewById(R.id.recipe_category);
        servingText = findViewById(R.id.recipe_servings);
        prepText = findViewById(R.id.recipe_preptime);
        instructionsText = findViewById(R.id.recipe_instructions);
        commentsText = findViewById(R.id.recipe_comments);
        ingredientsView = findViewById(R.id.ingredient_preview);
        showMore = findViewById(R.id.recipe_showmore);
        addIngredient = findViewById(R.id.recipe_add_ingredient);
        addRecipe = findViewById(R.id.recipe_add_recipe);

        // Get EditText elements
        editTitleText = findViewById(R.id.edit_recipe_title);
        editCategoryText = findViewById(R.id.edit_recipe_category);
        editServingText = findViewById(R.id.edit_recipe_servings);
        editPrepText = findViewById(R.id.edit_preptime);
        editInstructionsText = findViewById(R.id.edit_recipe_instructions);
        editCommentsText = findViewById(R.id.edit_recipe_comments);

        ingredients = new ArrayList<Ingredient>();

        // Register activity result to handle the Image the user selected
        ActivityResultLauncher selectImage =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        if (intent != null) {
                            System.out.println("intent is not null");
                            System.out.println(intent);
                            System.out.println(intent.getData());
                            uri = intent.getData();
                            // set the display picture to the
                            // picture that was selected by the user
                            imageView.setImageURI(uri);
                            imageView.setBackgroundResource(0);
                            imageView.setPadding(0, 0, 0, 0);
                        }
                    }
                });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Allow user to select a picture from the gallery
                // or take a picture using the camera
                if (imageView.getDrawable() != imageViewDrawable){
                    new DeleteConformationFragment<CircleImageView>(imageView, "Image").show(getSupportFragmentManager(), "Delete image");
                }
                else {
                    ImagePicker.Builder with = ImagePicker.with(AddRecipe.this);
                    with.crop(1f, 1f);
                    with.compress(1024);        //Final image size will be less than 1 MB
                    with.maxResultSize(1080, 1080);  //Final image resolution will be less than 1080 x 1080
                    with.createIntent(new Function1<Intent, Unit>() {
                        @Override
                        public Unit invoke(Intent Intent) {
                            selectImage.launch(Intent);
                            return null;
                        }
                    });
                }
            }
        });

        ingredientArrayAdapter = new IngredientAdapter(this, ingredients, "recipe_ingredient_preview");
        ingredientsView.setAdapter(ingredientArrayAdapter);

        addIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new RecipeIngredientFragment().show(getSupportFragmentManager(), "Add_Ingredient");
            }
        });

        showMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IngredientsView = new RecipeIngredientViewFragment(ingredients);
                addRecipe.setVisibility(View.GONE);
                findViewById(R.id.constraintLayout).setVisibility(View.INVISIBLE);
                changeClickState(false);
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.Main, IngredientsView)
                        .commit();
            }
        });

        addRecipe.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String titleItem = editTitleText.getText().toString();
                final String categoryItem = editCategoryText.getText().toString();
                final String servingItem = editServingText.getText().toString();
                final String prepTimeItem = editPrepText.getText().toString();
                final String instructionsItem = editInstructionsText.getText().toString();
                final String commentsItem = editCommentsText.getText().toString();
                boolean invalidInput = false;

                if (titleItem.isEmpty()){
                    invalidInput = true;
                    setErrorMessage(editTitleText, "Title must not be empty");
                }
                if (categoryItem.isEmpty() ){
                    invalidInput = true;
                    setErrorMessage(editCategoryText, "Category must not be empty");
                }
                if (servingItem.isEmpty() || Float.parseFloat(servingItem) == 0){
                    invalidInput = true;
                    setErrorMessage(editServingText,
                            "Serving size must be a number greater than zero");
                }
                if (prepTimeItem.isEmpty() || Integer.parseInt(prepTimeItem) == 0){
                    invalidInput = true;
                    setErrorMessage(editPrepText,
                            "Prep time must be a number greater than zero");
                }
                if (instructionsItem.isEmpty()){
                    invalidInput = true;
                    setErrorMessage(editInstructionsText,
                            "Instructions must not be empty");
                }
                if(ingredients.isEmpty()){
                    invalidInput = true;
                    addIngredient.setError("At least one ingredient is required");
                }


                if (!invalidInput){
                    HashMap<String, Object> data = new HashMap<String,Object>();
                    data.put("title",titleItem);
                    data.put("category",categoryItem);
                    data.put("servings",Float.parseFloat(servingItem));
                    data.put("prep_time",Integer.parseInt(prepTimeItem));
                    data.put("instructions",instructionsItem);
                    data.put("comments",commentsItem);
                    data.put("ingredients",ingredients);
                    data.put("image_tracker",1);

                    FirestoreDatabase.addRecipe(data,uri);
                    finish();
                }
            }
        });
    }

    /**
     * Sets Error for EditText view
     * @param edt the EditText view
     * @param errorMessage the error message
     * */
    private void setErrorMessage(EditText edt, String errorMessage) {
        edt.setError(errorMessage);
    }

    /**
     * Adds the particular ingredient when prompted by the RecipeIngredientFragment
     * @param ingredient the ingredient user wants to add
     * */
    @Override
    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
        addIngredient.setError(null);
        refreshIngredientsShown();
    }
    /**
     * Edits the particular ingredient when prompted by the RecipeIngredientFragment
     * @param ingredient the ingredient user wants to edit
     * */
    @Override
    public void editIngredient(Ingredient ingredient) {
        IngredientsView = new RecipeIngredientViewFragment(ingredients);
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.Main, IngredientsView).commit();
    }

    @Override
    public void onBackPressed() {
        if (IngredientsView != null){
            getSupportFragmentManager().beginTransaction()
                    .remove(IngredientsView)
                    .commit();
            addRecipe.setEnabled(true);
            addRecipe.setVisibility(View.VISIBLE);
            refreshIngredientsShown();
            changeClickState(true);
            findViewById(R.id.constraintLayout).setVisibility(View.VISIBLE);
        }
        else {
            super.onBackPressed();
        }
    }

    /**
     * refreshed the view to display the last 3 added ingredient
     */
    private void refreshIngredientsShown(){
        ingredientArrayAdapter.notifyDataSetChanged();
        if (ingredients.size() <= 3) {
            setListViewHeightBasedOnChildren(ingredientsView);
        }
    }

    /**
     * changes the enability of add views
     * @param state is the state all views enability is set too
     */
    private void changeClickState(boolean state){
        imageView.setEnabled(state);
        titleText.setEnabled(state);
        categoryText.setEnabled(state);
        servingText.setEnabled(state);
        instructionsText.setEnabled(state);
        commentsText.setEnabled(state);
        showMore.setEnabled(state);
        addIngredient.setEnabled(state);
        addRecipe.setEnabled(state);
        return;
    }
    /**
     * Deletes the particular ingredient when prompted by the DeleteConformationFragment
     * @param object object that is to be deleted
     * */
    @Override
    public void deleteObject(Object object) {
        if (object.getClass() == Ingredient.class) {
            ingredients.remove(object);
            IngredientsView = new RecipeIngredientViewFragment(ingredients);
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.Main, IngredientsView)
                    .commit();
        }
        else{
            if (object.getClass() == CircleImageView.class){
                ((CircleImageView) object).getLayoutParams().width = imageViewRadius;
                ((CircleImageView) object).getLayoutParams().height =imageViewRadius;
                ((CircleImageView) object).setBackground(imageViewBackground);
                ((CircleImageView) object).setImageDrawable(imageViewDrawable);
                ((CircleImageView) object).setPadding(4,7,6,10);
            }
        }
    }

    // Stack Overflow https://stackoverflow.com/questions/29512281/how-to-make-listviews-height-to-grow-after-adding-items-to-it
    /**
     * Sets the height of the ingredients listview based on the number of children it has
     * @param listView the listview it checks to set height
     * */
    private void setListViewHeightBasedOnChildren(ListView listView) {
        Log.e("Listview Size ", "" + listView.getCount());
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {

            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();

    }
}
