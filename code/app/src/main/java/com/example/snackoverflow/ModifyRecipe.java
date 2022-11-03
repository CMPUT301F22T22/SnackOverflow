package com.example.snackoverflow;

import android.app.Activity;
import android.content.Intent;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class ModifyRecipe extends AppCompatActivity implements RecipeAddIngredientFragment.OnFragmentInteractionListener {
    private EditText titleField;
    private EditText categoryField;
    private EditText servingsField;
    private EditText instructionsField;
    private EditText commentsField;
    private TextView ingredient_1;
    private TextView ingredient_2;
    private TextView ingredient_3;
    private ArrayList<TextView> ingredient_views;
    private ArrayList<Ingredient> ingredients;
    private Fragment IngredientsView;
    private Button showMore;
    private Button addIngredient;

    public CircleImageView imageView;

    private Button editButton;
    private Button applyButton;
    private Button viewButton;
    private Button deleteButton;

    public Uri imageUri;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Set variables to data stored in recipe object
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_recipe);

        Intent intent = getIntent();
        Recipe recipe = intent.getParcelableExtra("recipe");
        String recipeId = intent.getStringExtra("recipeId");
        ArrayList<String> ingredientIds = intent.getStringArrayListExtra("ingredientIds");
        imageView = findViewById(R.id.edit_recipe_photo);

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
                            imageUri = uri;
                        }
                    }
                });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Allow user to select a picture from the gallery
                // or take a picture using the camera
                ImagePicker.Builder with = ImagePicker.with(ModifyRecipe.this);
                with.crop(1f, 1f);
                with.compress(1024) ;        //Final image size will be less than 1 MB
                with.maxResultSize(1080, 1080);  //Final image resolution will be less than 1080 x 1080
                with.createIntent(new Function1<Intent, Unit>() {
                    @Override
                    public Unit invoke(Intent Intent) {
                        selectImage.launch(Intent );
                        return null;
                    }
                });
            }
        });

        titleField = (EditText) findViewById(R.id.edit_recipe_title);
        categoryField = (EditText) findViewById(R.id.edit_recipe_category);
        servingsField = (EditText) findViewById(R.id.edit_recipe_servings);
        instructionsField = (EditText) findViewById(R.id.edit_recipe_instructions);
        commentsField = (EditText) findViewById(R.id.edit_recipe_comments);
        titleField.setText(recipe.getTitle());
        categoryField.setText(recipe.getRecipeCategory());
        servingsField.setText(String.valueOf(recipe.getServings()));
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

        // Set ingredients text view
        int last_index = ingredients.size()-1;
        for (int i = 0; i<=last_index;i++){
            ingredient_views.get(i).setText(ingredients.get(last_index - i).getTitle());
            if (i == 2){
                break;
            }
        }


        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                titleField.setEnabled(true);
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
                String instructions = instructionsField.getText().toString();
                String comments = commentsField.getText().toString();
                if (titleField.equals("") || category.equals("") || servings.equals("") ||
                        ingredients.equals("") || comments.equals("")) {
                } else {
                    if (imageUri != null) {
                        uploadImage(imageUri, id);
                    }
                    Map<String, Object> data= new HashMap<String, Object>();
                    data.put("title", title);
                    data.put("category", category);
                    data.put("servings", Float.valueOf(servings));
                    data.put("instructions", instructions);
                    data.put("comments", comments);
                    FirestoreDatabase.modifyRecipe(recipeId, data);
                    finish();
                }
            }
        });
        addIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new RecipeAddIngredientFragment().show(getSupportFragmentManager(), "Add_Ingredient");
            }
        });
        showMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IngredientsView = new RecipeIngredientViewFragment(ingredients);
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.constraintLayout, IngredientsView)
                        .commit();
            }
        });

//        deleteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent modifyIntent = new Intent();
//                modifyIntent.putExtra("ACTION_TYPE", "DELETE");
//                modifyIntent.putExtra("position", intent.getIntExtra("position", 0));
//                prevCost = Utils.getFinalCost(food.getCount(), food.getCost());
//                modifyIntent.putExtra("previousCost", prevCost);
//                setResult(-1, modifyIntent);
//                finish();
//            }
//        });
    }

    @Override
    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
        refreshIngredientsShown();
    }

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
            System.out.println("back");
            getSupportFragmentManager().beginTransaction()
                    .remove(IngredientsView)
                    .commit();
            refreshIngredientsShown();
        }
        else {
            super.onBackPressed();
        }
    }
    private void refreshIngredientsShown(){
        int last_index = ingredients.size()-1;
        for (int i = 0; i<=last_index;i++){
            ingredient_views.get(i).setText(ingredients.get(last_index - i).getTitle());
            if (i == 2){
                break;
            }
        }
    }
    public void uploadImage(Uri uri, String id) {
        String filename = id;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference().child("images/"+filename);
        storageReference.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        System.out.println("GREAT SUCCESS");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("USELESS");
                    }
                });
    }
}
