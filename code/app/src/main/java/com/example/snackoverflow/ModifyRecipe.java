package com.example.snackoverflow;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.github.dhaval2404.imagepicker.ImagePicker;

import java.text.ParseException;

import de.hdodenhof.circleimageview.CircleImageView;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class ModifyRecipe extends AppCompatActivity {
    private EditText categoryField;
    private EditText servingsField;
    private EditText ingredientsField;
    private EditText instructionsField;
    private EditText commentsField;

    public CircleImageView imageView;

    private Button editButton;
    private Button applyButton;
    private Button viewButton;
    private Button deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_recipe);

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

        categoryField = (EditText) findViewById(R.id.edit_recipe_category);
        servingsField = (EditText) findViewById(R.id.edit_recipe_servings);
        ingredientsField = (EditText) findViewById(R.id.edit_recipe_ingredients);
        instructionsField = (EditText) findViewById(R.id.edit_recipe_instructions);
        commentsField = (EditText) findViewById(R.id.edit_recipe_comments);

        viewButton = (Button) findViewById(R.id.view_recipe_button);
        editButton = (Button) findViewById(R.id.edit_recipe_button);
        applyButton = (Button) findViewById(R.id.apply_recipe_button);
        deleteButton = (Button) findViewById(R.id.delete_recipe_button);

//        categoryField.setText(recipe.getRecipeCategory());
//        servingsField.setText(Float.toString(recipe.getServings()));
//        commentsField.setText(recipe.getComments());

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryField.setEnabled(true);
                servingsField.setEnabled(true);
                ingredientsField.setEnabled(true);
                commentsField.setEnabled(true);

                editButton.setVisibility(View.GONE);
                applyButton.setVisibility(View.VISIBLE);
            }
        });

        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryField.setEnabled(false);
                servingsField.setEnabled(false);
                ingredientsField.setEnabled(false);
                commentsField.setEnabled(false);

                editButton.setVisibility(View.VISIBLE);
                viewButton.setVisibility(View.GONE);
                applyButton.setVisibility(View.GONE);
            }
        });

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String category = categoryField.getText().toString();
                String servings = servingsField.getText().toString();
                String ingredients = ingredientsField.getText().toString();
                String comments = commentsField.getText().toString();
                if (category.equals("") || servings.equals("") ||
                        ingredients.equals("") || comments.equals("")) {

                } else {
                    finish();
                }
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
}
