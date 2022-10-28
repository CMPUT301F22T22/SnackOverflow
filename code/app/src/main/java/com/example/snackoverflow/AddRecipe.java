package com.example.snackoverflow;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputLayout;

import de.hdodenhof.circleimageview.CircleImageView;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

// TODO: How to request user permission for gallery access with the new
// Android API
public class AddRecipe extends AppCompatActivity implements RecipeAddIngredientFragment.OnFragmentInteractionListener{

    public CircleImageView imageView;
    private TextInputLayout titletext;
    private TextInputLayout categorytext;
    private TextInputLayout servingtext;
    private TextView ingredients;
    private TextInputLayout instructionstext;
    private TextInputLayout commentstext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        imageView = findViewById(R.id.recipe_addPhoto);
        ingredients = findViewById(R.id.recipe_addIngredients);
        titletext = findViewById(R.id.recipe_title);
        categorytext = findViewById(R.id.recipe_category);
        servingtext = findViewById(R.id.recipe_servings);
        instructionstext = findViewById(R.id.recipe_instructions);
        commentstext = findViewById(R.id.recipe_comments);
        imageView = findViewById(R.id.recipe_addPhoto);

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
                ImagePicker.Builder with = ImagePicker.with(AddRecipe.this);
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
        ingredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new RecipeAddIngredientFragment().show(getSupportFragmentManager(), "Add_Ingredient");
            }
        });
    }

    @Override
    public void Add_food(Ingredient ingredient) {
        // Placeholder
        ingredients.setText(ingredient.getDescription());
    }

    @Override
    public void Edit_food(Ingredient ingredient) {

    }
}
