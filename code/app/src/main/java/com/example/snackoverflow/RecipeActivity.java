package com.example.snackoverflow;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

interface FirebaseListener {
    public void onAction(String title, int prep_time, float servings, String category, String comments,
                         String instructions,@Nullable Bitmap imgBitmap);
}
public class RecipeActivity extends AppCompatActivity implements FirebaseListener {
    ListView recipeList;
    ArrayAdapter<Recipe> recipeArrayAdapter;
    ArrayList<Recipe> recipeDataList;
    ArrayList<String> recipeIdList = new ArrayList<String>();
    Map<String, ArrayList<String>> ingredientIds =  new HashMap();
    int imageTrackingData = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);


        ActivityResultLauncher<Intent> getModifiedData = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent intent = result.getData();
                            String actionType = intent.getStringExtra("ACTION_TYPE");
                            String recipeId = intent.getStringExtra("recipeId");
                            if (actionType.equals("EDIT")) {
                                Map<String, Object> data= new HashMap<String, Object>();
                                data.put("title", intent.getStringExtra("title"));
                                data.put("category", intent.getStringExtra("category"));
                                data.put("servings", intent.getFloatExtra("servings", 0));
                                data.put("prep_time", intent.getIntExtra("prep_time", 0));
                                data.put("instructions", intent.getStringExtra("instructions"));
                                data.put("image_tracker", intent.getIntExtra("image_tracker",0));
                                data.put("comments", intent.getStringExtra("comments"));
                                FirebaseFirestore.getInstance().collection("recipe").
                                        document(recipeId).update(data);
                            } else if (actionType.equals("DELETE")) {
                                FirebaseFirestore.getInstance().collection("recipe").
                                        document(recipeId).delete();
                                FirebaseStorage.getInstance().getReference("recipe/"+recipeId+".jpg")
                                        .delete();
                                int ind = recipeIdList.indexOf(recipeId);
                                recipeDataList.remove(ind);
                                recipeIdList.remove(ind);
                                ingredientIds.remove(recipeId);
                                recipeArrayAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });

        recipeList = findViewById(R.id.recipe_list);
        recipeDataList = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("recipe");

        recipeArrayAdapter = new RecipeAdapter(this, recipeDataList);
        recipeList.setAdapter(recipeArrayAdapter);

        NavigationBarView navigationBarView=findViewById(R.id.bottom_navigation);
        navigationBarView.setSelectedItemId(R.id.recipes);
        navigationBarView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.shoppinglist:
                        startActivity(new Intent(getApplicationContext(),ShoppingListActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.recipes:
                        return true;
                    case R.id.ingredients:
                        startActivity(new Intent(getApplicationContext(),IngredientStorageActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.mealplanner:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                recipeDataList.clear();
                try {
                    for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                    {
                        String id = doc.getId();
                        Map<String, Object> data = doc.getData();
                        String title = data.get("title").toString();
                        int prep_time = ((Long) data.get("prep_time")).intValue();
                        float servings = Float.valueOf(data.get("servings").toString());
                        String category = data.get("category").toString();
                        String instructions = data.get("instructions").toString();
                        String comments = data.get("comments").toString();
                        ArrayList<String> ingredientIdList = new ArrayList<String>();

                        StorageReference storageRef = FirebaseStorage.getInstance().getReference("recipe/"+id+".jpg");
                        imageTrackingData = Integer.valueOf(data.get("image_tracker").toString());
                        try {
                            File localFile = File.createTempFile("tempfile",".jpg");
                            storageRef.getFile(localFile)
                                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                            Bitmap imgBitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                            onAction(title, prep_time,servings,category,comments,instructions,imgBitmap);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            onAction(title, prep_time,servings,category,comments,instructions, null);
                                        }
                                    });
                        } catch (IOException e) {

                        }for (Object iid: (ArrayList) data.get("ingredients")) {
                        ingredientIdList.add(iid.toString());
                    }
                        ingredientIds.put(id, ingredientIdList);
                        recipeIdList.add(id);
                }
                } catch (NullPointerException e) {
                    System.out.println("EH");
                }
            }
        });

        recipeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(RecipeActivity.this, ModifyRecipe.class);
                intent.putExtra("recipe", (Parcelable) recipeDataList.get(position));
                intent.putExtra("recipeId", recipeIdList.get(position));
                intent.putStringArrayListExtra("ingredientIds", ingredientIds.get(recipeIdList.get(position)));
                intent.putExtra("imageTracker", imageTrackingData);
                getModifiedData.launch(intent);
            }
        });

        //Modify Button //TODO: Replace functionality or change when tests done
        FloatingActionButton modifyRecipeTestButton = findViewById(R.id.add_recipe_button);
        modifyRecipeTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecipeActivity.this, AddRecipe.class);
                startActivity(intent);
            }
        });
    }
    public ArrayList<Recipe> getRecipes(){
        return recipeDataList;
    }

    @Override
    public void onAction(String title, int prep_time, float servings, String category, String comments,
                         String instructions,@Nullable Bitmap imgBitmap) {
        if (imgBitmap == null) {
            recipeDataList.add(new Recipe(title, prep_time, servings, category, comments, instructions));
        } else {
            recipeDataList.add(new Recipe(title, prep_time, servings, category, comments, instructions, imgBitmap));
        }
        recipeArrayAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    };

}