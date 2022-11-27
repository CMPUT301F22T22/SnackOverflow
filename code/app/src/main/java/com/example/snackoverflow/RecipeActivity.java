package com.example.snackoverflow;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Recipe Activity class used to display existing recipes created by the user along with images
 * extends AppCompatActivity
 * @see Recipe
 * @see ModifyRecipe
 * @see AddRecipe
 * @see RecipeAdapter
 * */
public class RecipeActivity extends AppCompatActivity {
    ListView recipeList;
    ArrayAdapter<Recipe> recipeArrayAdapter;
    ArrayList<Recipe> recipeDataList;
    String currSortOrder = "inc";


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
                                ArrayList<String> ingredientTitles = intent.getStringArrayListExtra("ingredientTitles");
                                ArrayList<String> ingredientUnit = intent.getStringArrayListExtra("ingredientUnit");
                                ArrayList<String> ingredientAmount = intent.getStringArrayListExtra("ingredientAmount");
                                ArrayList<String> ingredientCategory = intent.getStringArrayListExtra("ingredientCategory");
                                ArrayList<Object> recipeIngredientList= new ArrayList<Object>();
                                for (int i = 0; i < ingredientTitles.size(); i++ ) {
                                    Map<String, Object> recipeIngredients = new HashMap();
                                    recipeIngredients.put("title", ingredientTitles.get(i));
                                    recipeIngredients.put("unit", Integer.valueOf(ingredientUnit.get(i)));
                                    recipeIngredients.put("amount", Integer.valueOf(ingredientAmount.get(i)));
                                    recipeIngredients.put("category", ingredientCategory.get(i));
                                    recipeIngredientList.add(recipeIngredients);
                                }
                                data.put("ingredients", recipeIngredientList);
                                FirebaseFirestore.getInstance().collection("recipe").
                                        document(recipeId).update(data);
                            } else if (actionType.equals("DELETE")) {
                                FirebaseFirestore.getInstance().collection("recipe").
                                        document(recipeId).delete();
                                FirebaseStorage.getInstance().getReference("recipe/"+recipeId+".jpg")
                                        .delete();
                                // TODO: Delete from list
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

        String[] sortBySpinnerList = new String[] {"Title", "Prep Time", "Servings", "Category"};
        String[] sortOrderSpinnerList = new String[] {"Low-High/A-Z", "High-Low/Z-A"};
        Spinner sortBySpinner = (Spinner) findViewById(R.id.sort_by_spinner);
        Spinner sortOrderSpinner = (Spinner) findViewById(R.id.sort_order_spinner);
        ArrayAdapter<String> sortByAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, sortBySpinnerList);
        ArrayAdapter<String> sortOrderAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, sortOrderSpinnerList);
        sortByAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortOrderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sortBySpinner.setAdapter(sortByAdapter);
        sortOrderSpinner.setAdapter(sortOrderAdapter);

        LinearLayout sortByLayout = (LinearLayout) findViewById(R.id.sort_by_layout);
        LinearLayout sortOrderLayout = (LinearLayout) findViewById(R.id.sort_order_layout);

        sortByLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortBySpinner.performClick();
            }
        });

        sortOrderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortOrderSpinner.performClick();
            }
        });
        sortBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                handleSortBy(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sortOrderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                handleSortOrder(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                recipeDataList.clear();
                try {
                    for(QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                        String id = doc.getId();
                        System.out.println(id);
                        Map<String, Object> data = doc.getData();
                        String title = data.get("title").toString();
                        int prep_time = Integer.valueOf(data.get("prep_time").toString());
                        float servings = Float.parseFloat(data.get("servings").toString());
                        String category = data.get("category").toString();
                        String instructions = data.get("instructions").toString();
                        String comments = data.get("comments").toString();
                        Recipe recipe = new Recipe(id, title, prep_time, servings,
                                category, comments, instructions, null);
                        recipeDataList.add(recipe);
                        loadImage(recipe);
                    }
                    recipeArrayAdapter.notifyDataSetChanged();
                    handleSortBy(0);
                } catch (NullPointerException e) {
                }
            }
        });

        recipeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(RecipeActivity.this, ModifyRecipe.class);
                intent.putExtra("recipe", (Parcelable) recipeDataList.get(position));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    };

    public void handleSortBy(int position) {
        switch (position) {
            case 0:
                if (currSortOrder.equals("dec")) {
                    Collections.sort(recipeDataList, new SortComparator.TitleComparator().reversed());
                } else {
                    Collections.sort(recipeDataList, new SortComparator.TitleComparator());
                }
                break;
            case 1:
                if (currSortOrder.equals("dec")) {
                    Collections.sort(recipeDataList, new SortComparator.PrepTimeComparator().reversed());
                } else {
                    Collections.sort(recipeDataList, new SortComparator.PrepTimeComparator());
                }
                break;
            case 2:
                if (currSortOrder.equals("dec")) {
                    Collections.sort(recipeDataList, new SortComparator.ServingsComparator().reversed());
                } else {
                    Collections.sort(recipeDataList, new SortComparator.ServingsComparator());
                }
                break;
            case 3:
                if (currSortOrder.equals("dec")) {
                    Collections.sort(recipeDataList, new SortComparator.CategoryComparator().reversed());
                } else {
                    Collections.sort(recipeDataList, new SortComparator.CategoryComparator());
                }
                break;
        }
        recipeArrayAdapter.notifyDataSetChanged();
    }

    public void handleSortOrder(int position) {
        if (position == 1 && currSortOrder.equals("inc")) {
            Collections.reverse(recipeDataList);
            currSortOrder = "dec";
        } else if (position == 0 && currSortOrder.equals("dec")) {
            Collections.reverse(recipeDataList);
            currSortOrder = "inc";
        }
        recipeArrayAdapter.notifyDataSetChanged();
    }
    /**
     * loads an image from firestore for a specific recipe
     * @param recipe the recipe that we are trying to get image for
     * */
    public void loadImage(Recipe recipe) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference("recipe/"+recipe.getId()+".jpg");
        try {
            File localFile = File.createTempFile("tempfile",".jpg");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap imgBitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    recipeDataList.get(recipeDataList.indexOf(recipe)).setImageBitmap(imgBitmap);
                    recipeArrayAdapter.notifyDataSetChanged();
                    handleSortBy(0);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                        loadImage(recipe);
                    } catch (InterruptedException ex) {

                    }
                }
            });
        } catch (IOException e) {

        }
    }
}