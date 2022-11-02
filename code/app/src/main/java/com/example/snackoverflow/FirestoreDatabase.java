package com.example.snackoverflow;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class FirestoreDatabase {

    final static FirebaseFirestore db = FirebaseFirestore.getInstance();
    final static CollectionReference recipeCol = db.collection("recipe");
    final static CollectionReference ingredientsCol = db.collection("ingredient");

    private final static String IngredeintsTAG = "IngredientStorageActivity";

    static void addIngredient(Ingredient newIngredient) {
                ingredientsCol
                .add(newIngredient)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(IngredeintsTAG, "Ingredient DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(IngredeintsTAG, "Error adding ingredient document", e);
                    }
                });
    };

    static void modifyIngredient() {};

    static void deleteIngredient() {};

    static void fetchIngredients() {};

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
