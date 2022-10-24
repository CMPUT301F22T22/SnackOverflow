package com.example.snackoverflow;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputLayout;

import de.hdodenhof.circleimageview.CircleImageView;

// TODO: How to request user permission for gallery access with the new
// Android API
public class AddRecipe extends AppCompatActivity implements add_ingredient_fragment.OnFragmentInteractionListener{

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

        imageView = findViewById(R.id.addPhoto);
        ingredients = findViewById(R.id.adding);
        titletext = findViewById(R.id.title);
        categorytext = findViewById(R.id.recipeCategory);
        servingtext = findViewById(R.id.servings);
        instructionstext = findViewById(R.id.instructions);
        commentstext = findViewById(R.id.comments);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(AddRecipe.this)
                        .crop()
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start(20);

            }
        });
        ingredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new add_ingredient_fragment().show(getSupportFragmentManager(), "Add_Ingredient");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("in onActivityResult");
        if (requestCode == 20 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            imageView.setImageURI(uri);
            imageView.setBackgroundResource(0);
            imageView.setPadding(0, 0, 0, 0);
        }
    }

    @Override
    public void Add_food(Ingredient ingredient) {
        // Test
        ingredients.setText(ingredient.getDescription());
    }

    @Override
    public void Edit_food(Ingredient ingredient) {

    }
}