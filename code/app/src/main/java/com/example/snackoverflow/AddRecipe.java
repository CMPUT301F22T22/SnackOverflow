package com.example.snackoverflow;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import de.hdodenhof.circleimageview.CircleImageView;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

// TODO: How to request user permission for gallery access with the new
// Android API
public class AddRecipe extends AppCompatActivity {

    public CircleImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        imageView = findViewById(R.id.addPhoto);

//        //You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
//        androidx.activity.result.ActivityResultLauncher<Intent> startForProfileImageResult = registerForActivityResult(
//                new ActivityResultContracts.GetContent(),
//            new ActivityResultCallback<ActivityResult>() {
//            @Override
//            public void onActivityResult(ActivityResult result) {
//                //System.out.println(result.getResultCode());
//                //if (result.getResultCode() == Activity.RESULT_OK) {
//                    // There are no request codes
//                    //Intent data = result.getData();
//                    Uri uri = result;
//                    imageView.setImageURI(uri);
//                    imageView.setBackgroundResource(0);
//                    imageView.setPadding(0, 0, 0, 0);
//               // }
//            }
//        }
//
//        );

        ActivityResultLauncher mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        if (intent != null) {
                            System.out.println("intent is not null");
                            System.out.println(intent);
                            System.out.println(intent.getData());
                            Uri uri = intent.getData();
                            imageView.setImageURI(uri);
                            imageView.setBackgroundResource(0);
                            imageView.setPadding(0, 0, 0, 0);
                        }
                    }
                });


//        ActivityResultLauncher<Intent> pickPhotoLauncher = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                new ActivityResultCallback<ActivityResult>() {
//
//                    @Override
//                    public void onActivityResult(ActivityResult result) {
//                        if (result.getResultCode() == Activity.RESULT_OK) {
//
//                            Intent data = result.getData();
//                            System.out.println("this is the intent");
//                            System.out.println(data);
//                            // Here the data will be Uri.
//
//                        }
//                    }
//                });

//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Uri uri;
//
//                if (true){
//                    uri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
//                }  else {
//                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//                }
//
//                Intent intent = new Intent(Intent.ACTION_PICK, uri);
//                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                pickPhotoLauncher.launch(intent);
//            }
//        });


        imageView.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {

                                             ImagePicker.Builder with = ImagePicker.with(AddRecipe.this);
                                                     with.compress(1024) ;        //Final image size will be less than 1 MB(Optional)
                                                     with.maxResultSize(1080, 1080);  //Final image resolution will be less than 1080 x 1080(Optional)
                                                     with.createIntent(new Function1<Intent, Unit>() {
                                                 @Override
                                                 public Unit invoke(Intent Intent) {
                                                     mStartForResult.launch(Intent );
                                                     return null;
                                                 }
                                             });
                                             }
                                         });




    }

//        //You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
//        androidx.activity.result.ActivityResultLauncher<Intent> ActivityResultLauncher = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                new ActivityResultCallback<ActivityResult>() {
//                    @Override
//                    public void onActivityResult(ActivityResult result) {
//                        System.out.println(result.getResultCode());
//                        if (result.getResultCode() == Activity.RESULT_OK) {
//                            // There are no request codes
//                            //Intent data = result.getData();
//                            Uri uri = result.getData();
//                            imageView.setImageURI(uri);
//                            imageView.setBackgroundResource(0);
//                            imageView.setPadding(0, 0, 0, 0);
//                        }
//                    }
//                });



//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                ImagePicker.with(AddRecipe.this)
////                        .crop()
////                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
////                        .start(20);
//
//                ImagePicker.with(AddRecipe.this)
//                        .compress(1024)         //Final image size will be less than 1 MB(Optional)
//                        .maxResultSize(1080, 1080)  //Final image resolution will be less than 1080 x 1080(Optional)
//                        .createIntent { intent ->{
//                    ActivityResultLauncher.launch(intent);
//                }
//
//                //}
//
//            }
//        };
//    });


//    private val startForProfileImageResult =
//            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
//            val resultCode = result.resultCode;
//        val data = result.data;
//
//        if (resultCode == Activity.RESULT_OK) {
//            //Image Uri will not be null for RESULT_OK
//            val fileUri = data?.data!!;
//
//                    mProfileUri = fileUri
//            imgProfile.setImageURI(fileUri)
//        } else if (resultCode == ImagePicker.RESULT_ERROR) {
//            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
//        } else {
//            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
//        }
//    }




//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        System.out.println("in onActivityResult");
//        if (requestCode == 20 && resultCode == RESULT_OK) {
//            Uri uri = data.getData();
//            imageView.setImageURI(uri);
//            imageView.setBackgroundResource(0);
//            imageView.setPadding(0, 0, 0, 0);
//        }
//    }

//    public void openSomeActivityForResult() {
//        Intent intent = new Intent(this, AddRecipe.class);
//        ActivityResultLauncher.launch(intent);
//    }

    //
};
