package com.example.snackoverflow;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

/**
 * Modify Recipe Class to modify existing recipes and the ingredients present in the recipes
 * extends AppCompatActivity
 * implements RecipeIngredientFragment.OnFragmentInteractionListener, DeleteConformationFragment.OnFragmentInteractionListener
 * @see RecipeActivity
 * @see Recipe
 * @see Ingredient
 * */
public class ModifyRecipe extends AppCompatActivity implements RecipeIngredientFragment.OnFragmentInteractionListener, DeleteConformationFragment.OnFragmentInteractionListener{
    private EditText titleField;
    private EditText categoryField;
    private EditText servingsField;
    private EditText instructionsField;
    private EditText commentsField;
    private EditText prepField;
    private TextView ingredient_1;
    private TextView ingredient_2;
    private TextView ingredient_3;
    private ArrayList<TextView> ingredient_views;
    private ArrayList<Ingredient> ingredients;
    private Fragment IngredientsView;
    private Button showMore;
    private Button addIngredient;

    public CircleImageView imageView;
    private Drawable imageViewDrawable;
    private Drawable imageViewBackground;
    private int imageViewRadius;

    private Button editButton;
    private Button applyButton;
    private Button viewButton;
    private Button deleteButton;
    private int imageTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Set variables to data stored in recipe object
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_recipe);

        Intent intent = getIntent();
        Recipe recipe = intent.getParcelableExtra("recipe");
        String recipeId = intent.getStringExtra("recipeId");
        imageView = findViewById(R.id.edit_recipe_photo);
        StorageReference storageRef = FirebaseStorage.getInstance().getReference("recipe/"+recipeId+".jpg");
        try {
            File localFile = File.createTempFile("tempfile", ".jpg");
            storageRef.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            imageView.setImageBitmap(BitmapFactory.decodeFile(localFile.getAbsolutePath()));
                            imageView.setBackgroundColor(Color.TRANSPARENT);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
        } catch (IOException e) {}
        // imageview default
        imageViewDrawable = imageView.getDrawable();
        imageViewBackground = imageView.getBackground();
        imageViewRadius = imageView.getLayoutParams().width;
        //

        imageTracker = intent.getIntExtra("imageTracker", 0);
        // Register activity result to handle the Image the user selected
        ActivityResultLauncher selectImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent imageIntent = result.getData();
                        if (imageIntent != null) {
                            System.out.println("intent is not null");
                            System.out.println(imageIntent);
                            System.out.println(imageIntent.getData());
                            Uri uri = imageIntent.getData();
                            // set the display picture to the
                            // picture that was selected by the user
                            imageView.setImageURI(uri);
                            imageView.setBackgroundResource(0);
                            imageView.setPadding(0, 0, 0, 0);
                            uploadImage(uri, recipeId);
                            imageTracker += 1;
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
                    ImagePicker.Builder with = ImagePicker.with(ModifyRecipe.this);
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

        titleField = (EditText) findViewById(R.id.edit_recipe_title);
        categoryField = (EditText) findViewById(R.id.edit_recipe_category);
        servingsField = (EditText) findViewById(R.id.edit_recipe_servings);
        prepField = (EditText) findViewById(R.id.edit_recipe_preptime);
        instructionsField = (EditText) findViewById(R.id.edit_recipe_instructions);
        commentsField = (EditText) findViewById(R.id.edit_recipe_comments);
        titleField.setText(recipe.getTitle());
        categoryField.setText(recipe.getRecipeCategory());
        servingsField.setText(String.valueOf(recipe.getServings()));
        prepField.setText(String.valueOf(recipe.getPreptime()));
        ingredient_1 = findViewById(R.id.Ingredient_1);
        ingredient_2 = findViewById(R.id.Ingredient_2);
        ingredient_3 = findViewById(R.id.Ingredient_3);
        instructionsField.setText(recipe.getInstructions());
        commentsField.setText(recipe.getComments());

        ArrayList<Ingredient> recipeIngredients = recipe.getIngredients();
        if (recipeIngredients != null) {
            for (int i = 0; i < recipeIngredients.size(); i++) {
                ingredients.add(recipeIngredients.get(i));
            }
            if (ingredients.size() > 0) {
                ingredient_1.setText(ingredients.get(0).getTitle());
                if (ingredients.size() > 1) {
                    ingredient_1.setText(ingredients.get(1).getTitle());
                    if (ingredients.size() > 2) {
                        ingredient_1.setText(ingredients.get(2).getTitle());
                    }
                }
            }
        }

        showMore = findViewById(R.id.recipe_showmore);
        addIngredient = findViewById(R.id.recipe_add_ingredient);

        viewButton = (Button) findViewById(R.id.view_recipe_button);
        editButton = (Button) findViewById(R.id.edit_recipe_button);
        applyButton = (Button) findViewById(R.id.apply_recipe_button);
        deleteButton = (Button) findViewById(R.id.delete_recipe_button);

        ingredients = new ArrayList<Ingredient>();
        ingredient_views = new ArrayList<TextView>();

        ingredient_views.add(ingredient_1);
        ingredient_views.add(ingredient_2);
        ingredient_views.add(ingredient_3);

        fetchIngredients(recipeId);


        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                titleField.setEnabled(true);
                prepField.setEnabled(true);
                categoryField.setEnabled(true);
                servingsField.setEnabled(true);
                showMore.setEnabled(true);
                addIngredient.setEnabled(true);
                instructionsField.setEnabled(true);
                commentsField.setEnabled(true);

                editButton.setVisibility(View.GONE);
                applyButton.setVisibility(View.VISIBLE);
                viewButton.setVisibility(View.VISIBLE);
            }
        });

        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleField.setEnabled(false);
                prepField.setEnabled(false);
                categoryField.setEnabled(false);
                servingsField.setEnabled(false);
                showMore.setEnabled(false);
                addIngredient.setEnabled(false);
                instructionsField.setEnabled(false);
                commentsField.setEnabled(false);

                editButton.setVisibility(View.VISIBLE);
                viewButton.setVisibility(View.GONE);
                applyButton.setVisibility(View.GONE);
            }
        });

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleField.getText().toString();
                String category = categoryField.getText().toString();
                String servings = servingsField.getText().toString();
                String prepTime = prepField.getText().toString();
                String instructions = instructionsField.getText().toString();
                String comments = commentsField.getText().toString();
                if (titleField.equals("") || category.equals("") || servings.equals("") ||
                        ingredients.equals("") || comments.equals("")) {
                } else {
                    Intent modifyIntent = new Intent();
                    modifyIntent.putExtra("ACTION_TYPE", "EDIT");
                    modifyIntent.putExtra("recipeId", recipeId);
                    modifyIntent.putExtra("title", title);
                    modifyIntent.putExtra("category", category);
                    modifyIntent.putExtra("servings", Float.valueOf(servings));
                    modifyIntent.putExtra("prep_time", Integer.valueOf(prepTime));
                    modifyIntent.putExtra("instructions", instructions);
                    modifyIntent.putExtra("image_tracker", imageTracker);
                    ArrayList<String> ingredientTitles = new ArrayList<>();
                    ArrayList<String> ingredientUnit = new ArrayList<>();
                    for (Ingredient ingredient: ingredients) {
                        ingredientTitles.add(ingredient.getTitle());
                        ingredientUnit.add(String.valueOf(ingredient.getUnit()));
                    }
                    modifyIntent.putExtra("comments", comments);
                    modifyIntent.putStringArrayListExtra("ingredientTitles", ingredientTitles);
                    modifyIntent.putStringArrayListExtra("ingredientUnit", ingredientUnit);
                    setResult(RESULT_OK, modifyIntent);
                    finish();
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
                findViewById(R.id.constraintLayout).setVisibility(View.INVISIBLE);
                changeClickState(false);
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.Main, IngredientsView)
                        .commit();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent modifyIntent = new Intent();
                modifyIntent.putExtra("ACTION_TYPE", "DELETE");
                modifyIntent.putExtra("recipeId", recipeId);
                setResult(RESULT_OK, modifyIntent);
                finish();
            }
        });
    }

    /**
     * Add an ingredient to an existing recipe
     * @param ingredient ingredient to add to the recipe
     * */
    @Override
    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
        refreshIngredientsShown();
    }

    /**
     * Edits an existing ingredient in an existing recipe
     * @param ingredient ingredient to edit
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
            System.out.println("back");
            getSupportFragmentManager().beginTransaction()
                    .remove(IngredientsView)
                    .commit();
            IngredientsView = null;
            changeClickState(true);
            refreshIngredientsShown();
            findViewById(R.id.constraintLayout).setVisibility(View.VISIBLE);
        }
        else {
            super.onBackPressed();
        }
    }

    /**
     * refresh the ingredients shown on the recipe
     * */
    private void refreshIngredientsShown(){
        int last_index = ingredients.size()-1;
        for (int i = 0; i < 3; i++){
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
     * uploads an image to the recipe
     * @param uri path to the temporary stprage of the image
     * @param id id of the recipe
     * */
    public void uploadImage(Uri uri, String id) {
        String filename = id;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference().child("recipe/"+filename+".jpg");
        storageReference.putFile(uri);
    };

    /**
     * Enables the fields in the recipe
     * @param state
     * */
    private void changeClickState(boolean state){
        imageView.setEnabled(state);
        titleField.setEnabled(state);
        prepField.setEnabled(state);
        categoryField.setEnabled(state);
        servingsField.setEnabled(state);
        showMore.setEnabled(state);
        addIngredient.setEnabled(state);
        instructionsField.setEnabled(state);
        commentsField.setEnabled(state);
        addIngredient.setEnabled(state);
        showMore.setEnabled(state);
        viewButton.setEnabled(state);
        editButton.setEnabled(state);
        applyButton.setEnabled(state);
        deleteButton.setEnabled(state);
    }

    public void fetchIngredients(String recipeId) {
        FirebaseFirestore.getInstance()
                .collection("recipe")
                .document(recipeId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot doc = task.getResult();
                        Map<String, Object> data = doc.getData();
                        ArrayList<Object> ingredientArray = (ArrayList<Object>) data.get("ingredients");
                        try {
                            for (Object ingredient: ingredientArray) {
                                Map<String, Object> ingredientMap = (Map<String, Object>) ingredient;
                                String title = ingredientMap.get("title").toString();
                                Integer unit = Integer.valueOf(ingredientMap.get("unit").toString());
                                ingredients.add(new Ingredient(title,
                                        null, null, 0, unit,
                                        null));
                            }
                        } catch (ConcurrentModificationException e) {

                        }
                        refreshIngredientsShown();
                    }
                });

        // Set ingredients text view
    }

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
