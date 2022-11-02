package com.example.snackoverflow;

import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
<<<<<<< HEAD
=======
import java.util.Date;
>>>>>>> eaee606 (add firestore functions to add/delete/fetch ingredients)
import java.util.Map;

public class FirestoreDatabase {

    final static FirebaseFirestore db = FirebaseFirestore.getInstance();
    final static CollectionReference recipeCol = db.collection("recipe");
    final static CollectionReference ingredientsCol = db.collection("ingredient");

    private final static String IngredientsTAG = "IngredientStorageActivity";

    static void addIngredient(Ingredient newIngredient) {
        ingredientsCol
        .add(newIngredient)
        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(IngredientsTAG, "Ingredient document snapshot written with ID: " + documentReference.getId());
                newIngredient.id = documentReference.getId();
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
        .document(ingredient.id)
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

    static void fetchIngredients(ArrayAdapter<Ingredient> ingredientArrayAdapter) {
        ingredientsCol
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d(IngredientsTAG, "Ingredients retrieved successfully");
//                            this.title = description;
//                            this.location = location;
//                            this.amount = amount;
//                            this.unit = unit;
//                            this.bestBefore = date;
//                            this.category = category;
//public Ingredient(String description, Date date, String location, int amount, int unit, String category)
                            ArrayList<Ingredient> allIngredients = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String title = document.getString("title");
                                String location = document.getString("location");
                                int amount = document.getLong("amount").intValue();
                                int unit = document.getLong("unit").intValue();
                                Date bestBefore = document.getDate("bestBefore");
                                String category = document.getString("category");
                                System.out.println(title);
                                System.out.println(location);
                                System.out.println(amount);
                                System.out.println(unit);
                                System.out.println(bestBefore);
                                System.out.println(category);

                                Ingredient ingredient = new Ingredient(title, bestBefore, location, amount, unit, category);
                                allIngredients.add(ingredient);
                            }
                            ingredientArrayAdapter.clear();
                            ingredientArrayAdapter.addAll(allIngredients);
                        }
                    }
                });
    };

    static void addRecipe() {};

    static void modifyRecipe(String id, Map<String, Object> data) {
        recipeCol.document(id).update(data);
    };

    static void deleteRecipe(String id) {
        recipeCol.document(id).delete();
    };

    static void addMealPlan() {};

    static void modifyMealPlan() {};

    static void deleteMealPlan() {};

    static void fetchMealPlans() {};

    static void addShoppingList() {};

    static void addToShoppingList() {};

    static void modifyShoppingList() {};

    static void deleteShoppingList() {};

    static void deleteFromShoppingList() {};

    static void fetchShoppingList() {};

}
