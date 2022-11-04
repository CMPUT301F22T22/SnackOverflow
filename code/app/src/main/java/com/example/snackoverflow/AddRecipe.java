package com.example.snackoverflow;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputLayout;
import com.google.rpc.context.AttributeContext;

import java.util.ArrayList;

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
// TODO: How to request user permission for gallery access with the new
// Android API
public class AddRecipe extends AppCompatActivity implements RecipeIngredientFragment.OnFragmentInteractionListener, DeleteConformationFragment.OnFragmentInteractionListener{

    public CircleImageView imageView;
    private Drawable imageViewDrawable;
    private Drawable imageViewBackground;
    private int imageViewRadius;
    private TextInputLayout titleText;
    private TextInputLayout categoryText;
    private TextInputLayout servingText;
    private TextInputLayout prepText;
    private TextView ingredient_1;
    private TextView ingredient_2;
    private TextView ingredient_3;
    private ArrayList<TextView> ingredient_views;
    private ArrayList<Ingredient> ingredients;
    private Button showMore;
    private Button addIngredient;
    private Button addRecipe;
    private TextInputLayout instructionsText;
    private TextInputLayout commentsText;
    private Fragment IngredientsView;

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
        //
        titleText = findViewById(R.id.recipe_title);
        categoryText = findViewById(R.id.recipe_category);
        servingText = findViewById(R.id.recipe_servings);
        prepText = findViewById(R.id.recipe_preptime);
        instructionsText = findViewById(R.id.recipe_instructions);
        commentsText = findViewById(R.id.recipe_comments);
        ingredient_1 = findViewById(R.id.Ingredient_1);
        ingredient_2 = findViewById(R.id.Ingredient_2);
        ingredient_3 = findViewById(R.id.Ingredient_3);
        showMore = findViewById(R.id.recipe_showmore);
        addIngredient = findViewById(R.id.recipe_add_ingredient);
        addRecipe = findViewById(R.id.recipe_add_recipe);

        ingredients = new ArrayList<Ingredient>();
        ingredient_views = new ArrayList<TextView>();

        ingredient_views.add(ingredient_1);
        ingredient_views.add(ingredient_2);
        ingredient_views.add(ingredient_3);

        // Register activity result to handle the Image the user selected
        ActivityResultLauncher selectImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        if (intent != null) {
                            System.out.println("intent is not null");
                            System.out.println(intent);
                            System.out.println(intent.getData());
                            Uri uri = intent.getData();
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
    }
    /**
     * Adds the particular ingredient when prompted by the RecipeIngredientFragment
     * @param ingredient the ingredient user wants to add
     * */
    @Override
    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
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
                .replace(R.id.constraintLayout, IngredientsView).commit();
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
        int last_index = ingredients.size()-1;
        for (int i = 0; i < 2; i++){
            ingredient_views.get(i).setText("Ingredient");
        }
        for (int i = 0; i<=last_index;i++){
            ingredient_views.get(i).setText(ingredients.get(last_index - i).getTitle());
            if (i == 2){
                break;
            }
        }
    }

    /**
     * changes the enabilty of add views
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
}
