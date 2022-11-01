package com.example.snackoverflow;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

public class FirestoreDatabase {

    final static FirebaseFirestore db = FirebaseFirestore.getInstance();
    final static CollectionReference recipeCol = db.collection("recipe");

    static void addIngredient() {};

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

    static void fetchRecipes() {};

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
