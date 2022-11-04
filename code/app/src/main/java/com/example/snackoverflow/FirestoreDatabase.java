package com.example.snackoverflow;

import android.net.Uri;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FirestoreDatabase {

    final static FirebaseFirestore db = FirebaseFirestore.getInstance();
    final static CollectionReference recipeCol = db.collection("recipe");
    final static CollectionReference ingredientsCol = db.collection("ingredient");
    final static CollectionReference MealPlanCol = db.collection("meal_plan");

    private final static ArrayList<Ingredient> ingredient_storage_list = new ArrayList<>();
    private final static ArrayList<String> ingredient_meal_plan_list = new ArrayList<>();
    private final static String IngredientsTAG = "Ingredient Storage Activity";
    private final static String MealsTag = "Meal Plan";
    private final static String RecipeTag = "Recipe";

    static void addIngredient(Ingredient newIngredient) {
        ingredientsCol
        .add(newIngredient)
        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(IngredientsTAG, "Ingredient document snapshot written with ID: " + documentReference.getId());
                newIngredient.setId(documentReference.getId());
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(IngredientsTAG, "Error adding ingredient document", e);
            }
        });
    };

    static void modifyIngredient() {};

    static void deleteIngredient(Ingredient ingredient) {
        ingredientsCol
        .document(ingredient.getId())
        .delete()
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(IngredientsTAG, "Ingredient document snapshot successfully deleted!");
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(IngredientsTAG, "Error deleting ingredient document", e);
            }
        });
    };

    static void fetchIngredients(ArrayAdapter<Ingredient> ingredientArrayAdapter,
                                 ArrayList<Ingredient> ingredients) {
//        ingredientsCol
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            Log.d(IngredientsTAG, "Ingredients retrieved successfully");
//                            ArrayList<Ingredient> allIngredients = new ArrayList<>();
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                String title = document.getString("title");
//                                String location = document.getString("location");
//                                int amount = document.getLong("amount").intValue();
//                                int unit = document.getLong("unit").intValue();
//                                Date bestBefore = document.getDate("bestBefore");
//                                String category = document.getString("category");
//                                System.out.println(title);
//                                System.out.println(location);
//                                System.out.println(amount);
//                                System.out.println(unit);
//                                System.out.println(bestBefore);
//                                System.out.println(category);
//
//                                Ingredient ingredient = new Ingredient(title, bestBefore, location, amount, unit, category);
//                                allIngredients.add(ingredient);
//                            }
//                            ingredientArrayAdapter.clear();
//                            ingredientArrayAdapter.addAll(allIngredients);
//                        }
//                    }
//                });
        ingredientsCol.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w(IngredientsTAG, "Failed to fetch ingredients.",error);
                    return;
                }
                ingredients.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    Log.d(IngredientsTAG, "Ingredients retrieved successfully");
                    String id = doc.getId();
                    String title = (String) doc.getData().get("title");
                    String location = (String) doc.getData().get("location");
                    int amount = doc.getLong("amount").intValue();
                    int unit = doc.getLong("unit").intValue();
                    Date bestBefore = doc.getDate("bestBefore");
                    String category = (String) doc.getData().get("category");

                    System.out.println(id);
                    System.out.println(title);
                    System.out.println(location);
                    System.out.println(amount);
                    System.out.println(unit);
                    System.out.println(bestBefore);
                    System.out.println(category);

                    Ingredient ingredientItem = new Ingredient(title, bestBefore, location, amount, unit, category);
                    ingredientItem.setId(id);
                    ingredients.add(ingredientItem); // Adding the ingredients from FireStore
                }
                // Notifying the adapter to render any new data fetched
                ingredientArrayAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched
            }
        });
    };

    static ArrayList<Ingredient> getIngredientsStorageList() {
        ingredientsCol.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w(IngredientsTAG, "Failed to fetch ingredients.",error);
                    return;
                }
                ingredient_storage_list.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    Log.d("lol", "Ingredients retrieved successfully");
                    String id = doc.getId();
                    String title = (String) doc.getData().get("title");
                    String location = (String) doc.getData().get("location");
                    int amount = doc.getLong("amount").intValue();
                    int unit = doc.getLong("unit").intValue();
                    Date bestBefore = doc.getDate("bestBefore");
                    String category = (String) doc.getData().get("category");

                    Ingredient ingredientItem = new Ingredient(title, bestBefore, location, amount, unit, category);
                    ingredientItem.setId(id);
                    ingredient_storage_list.add(ingredientItem); // Adding the ingredients from FireStore
                }
            }
        });
        return ingredient_storage_list;
    }

    /*static ArrayList<String> getIngredientsMealPlanList() {
        MealPlanCol.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w(IngredientsTAG, "Failed to fetch ingredients.",error);
                    return;
                }
                ingredient_meal_plan_list.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    String id = doc.getId();
                    String title = (String) doc.getData().get("title");
                    String location = (String) doc.getData().get("location");
                    int amount = doc.getLong("amount").intValue();
                    int unit = doc.getLong("unit").intValue();
                    Date bestBefore = doc.getDate("bestBefore");
                    String category = (String) doc.getData().get("category");

                    Ingredient ingredientItem = new Ingredient(title, bestBefore, location, amount, unit, category);
                    ingredientItem.setId(id);
                    ingredient_meal_plan_list.add(title); // Adding the ingredients from FireStore
                }
            }
        });
        return ingredient_meal_plan_list;
    }*/

    static void addRecipe(HashMap<String, Object> data, Uri uri) {
        recipeCol
            .add(data)
            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Log.d(RecipeTag, "Recipe document snapshot written with ID: " + documentReference.getId());
                    FirestoreDatabase.uploadImage(uri, documentReference.getId());
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(RecipeTag, "Error adding recipe document", e);
                }
            });
    };

    static void deleteRecipe(String id) {
        recipeCol.document(id).delete();
    };

    static void deleteMealPlan() {};

    static void fetchMealPlans() {
//        MealPlanCol
//            .addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
//                    FirebaseFirestoreException error) {
//                meals.clear();
//                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
//                {
//                    // Log.d(MealsTag, "Meal plan fetched successfully");
//                    Date date = doc.getDate("date");
//                    ArrayList<Recipe> mealsForDay = (ArrayList<Recipe>) doc.getData().get("meals");
//                    meals.add(new Mealday( date,  mealsForDay)); // Adding the meal days from FireStore
//                }
//                mealdayAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched
//            }
//        });
    };

    static void addShoppingList() {};

    static void addToShoppingList() {};

    static void modifyShoppingList() {};

    static void deleteShoppingList() {};

    static void deleteFromShoppingList() {};

    static void fetchShoppingList() {};

    static void uploadImage(Uri uri, String id) {
        if (uri != null){
            String filename = id;
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReference().child("recipe/"+filename+".jpg");
            storageReference.putFile(uri);
        }
    };

}
