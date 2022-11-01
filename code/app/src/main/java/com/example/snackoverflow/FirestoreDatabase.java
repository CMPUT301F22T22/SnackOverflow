package com.example.snackoverflow;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public abstract class FirestoreDatabase {

    final static FirebaseFirestore db = FirebaseFirestore.getInstance();
    final static CollectionReference ingredientsCollection = db.collection("ingredient");

    void addIngredient() {};

    void modifyIngredient() {};

    void deleteIngredient() {};

    void fetchIngredients() {};

    void addRecipe() {};

    void modifyRecipe() {};

    void deleteRecipe() {};

    void fetchRecipes() {};

    void addMealPlan() {};

    void modifyMealPlan() {};

    void deleteMealPlan() {};

    void fetchMealPlans() {};

    void addShoppingList() {};

    void addToShoppingList() {};

    void modifyShoppingList() {};

    void deleteShoppingList() {};

    void deleteFromShoppingList() {};

    void fetchShoppingList() {};

}
