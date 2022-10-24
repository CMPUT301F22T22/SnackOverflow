package com.example.snackoverflow;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.view.View;

//import com.example.snackoverflow.databinding.ActivityMainBinding;

public class AddRecipe extends AppCompatActivity {

    public ImageView imageView;
    public Uri imageUri;
    //ActivityMainBinding binding;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        //binding = ActivityMainBinding.inflate(getLayoutInflater());
        //setContentView(binding.getRoot());
        imageView = findViewById(R.id.addPhoto);

//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED) {
//                    Intent intent = new Intent();
//                    intent.setType("image/*");
//                    intent.setAction(Intent.ACTION_GET_CONTENT);
//                    startActivityForResult(intent, 1);
//                //}
////                else{
////                    ActivityCompat.requestPermissions(AddRecipe.this,
////                            new String[]{
////                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
////                            },1);
////                }
//
//            }
//        });

//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//               selectImage();
//            }
//        });
//    }
//
//    private void selectImage(){
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(intent, 1);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
//            Uri imageUri = data.getData();
//            imageView.setImageURI(imageUri);
//
//        }
//    }

    //2
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        System.out.println("in onActivityResult");
//        if (requestCode == 1 && resultCode == RESULT_OK) {
//            Uri uri = data.getData();
//            Context context = AddRecipe.this;
//            path = RealPathUtil.getRealPath(context, uri);
//            //Bitmap bitmap = BitmapFactory.decodeFile(path);
//            //imageView.setImageBitmap(bitmap);
//
//        }
//    }

}