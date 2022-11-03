package com.example.snackoverflow;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecipeActivity extends AppCompatActivity {
    ListView recipeList;
    ArrayAdapter<Recipe> recipeArrayAdapter;
    ArrayList<Recipe> recipeDataList;
    ArrayList<String> recipeIdList = new ArrayList<String>();
    Map<String, ArrayList<String>> ingredientIds =  new HashMap();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

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

        recipeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(RecipeActivity.this, ModifyRecipe.class);
                intent.putExtra("recipe", (Parcelable) recipeDataList.get(position));
                startActivity(intent);
            }
        });

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                recipeDataList.clear();
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
                    recipeDataList.add(new Recipe(title, prep_time, servings, category, comments, instructions));
                    ArrayList<String> ingredientIdList = new ArrayList<String>();
                    for (Object iid: (ArrayList) data.get("ingredients")) {
                        ingredientIdList.add(iid.toString());
                    }
                    ingredientIds.put(id, ingredientIdList);
                    recipeIdList.add(id);
                }
                recipeArrayAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched
            }
        });

        recipeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(RecipeActivity.this, ModifyRecipe.class);
                intent.putExtra("recipe", (Parcelable) recipeDataList.get(position));
                intent.putExtra("recipeId", recipeIdList.get(position));
                intent.putStringArrayListExtra("ingredientIds", ingredientIds.get(recipeIdList.get(position)));
                startActivity(intent);
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
}