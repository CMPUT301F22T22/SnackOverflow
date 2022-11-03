package com.example.snackoverflow;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class FirestoreDatabase {

    final static FirebaseFirestore db = FirebaseFirestore.getInstance();
    final static CollectionReference recipeCol = db.collection("recipe");

    static void addIngredient() {};

    static void modifyIngredient() {};

    static void deleteIngredient() {};

    static void fetchIngredients() {};

    static void addRecipe() {};

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
