package com.example.snackoverflow;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.github.dhaval2404.imagepicker.ImagePicker;
import de.hdodenhof.circleimageview.CircleImageView;

public class AddRecipe extends AppCompatActivity {

    public CircleImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        imageView = findViewById(R.id.addPhoto);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(AddRecipe.this)
                        .crop()
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start(20);

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
}