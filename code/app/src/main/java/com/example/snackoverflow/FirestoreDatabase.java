package com.example.snackoverflow;

import android.net.Uri;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;

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

/**
 * Class toring all the Firestore Database related functionality
 *
 * */
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

    /**
     * Add an Ingredient to the Firebase Storage
     * @param newIngredient the new ingredient to be added
     * */
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

    /**
     * To modify the existing ingredient
     * */
    static void modifyIngredient() {};

    /**
     * To delete an existing ingredient from the storage
     * @param ingredient the ingredient to be deleted
     * */
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

    /**
     * Fetches the ingredients from the Firebase storage
     * @param ingredientArrayAdapter the array adapter for Ingredient Objects
     * @param ingredients  the array containing all the ingredients
     * */
    static void fetchIngredients(ArrayAdapter<Ingredient> ingredientArrayAdapter,
                                 ArrayList<Ingredient> ingredients) {
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
                    ingredientItem.id = id;
                    ingredients.add(ingredientItem); // Adding the ingredients from FireStore
                }
                // Notifying the adapter to render any new data fetched
                ingredientArrayAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched
            }
        });
    };

    /**
     * Gets the Ingredient Storage List form the Database
     * @return an arraylist conatining all the ingredients from the storage
     * */
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
                    ingredientItem.id = id;
                    ingredient_storage_list.add(ingredientItem); // Adding the ingredients from FireStore
                }
            }
        });
        return ingredient_storage_list;
    }

    static ArrayList<String> getIngredientsMealPlanList() {
        MealPlanCol.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w(IngredientsTAG, "Failed to fetch ingredients.", error);
                    return;
                }
                ingredient_meal_plan_list.clear();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    ArrayList<Object> mealsForDay = (ArrayList<Object>) doc.getData().get("meals");
                    for (Object obj : mealsForDay) {
                        Map<String, Object> mapping = (Map<String, Object>) obj;
                        ArrayList<String> mealplaningredients = (ArrayList<String>) mapping.get("ingredients");
                        for (String mealplaningredient : mealplaningredients) {
                            ingredient_meal_plan_list.add(mealplaningredient);
                        }
                    }
                }
            }
        });
        //System.out.println(ingredient_meal_plan_list.size());
        return ingredient_meal_plan_list;
    }

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
    
    /**
     * deletes a recipe from the storage
     * @param id id of the recipe to be deleted
     * */
    static void deleteRecipe(String id) {
        recipeCol.document(id).delete();
    };

    static void addMealPlan(Mealday mealDay) {
        MealPlanCol
                .add(mealDay)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(MealsTag, "Meal day document snapshot written with ID: " + documentReference.getId());
                        mealDay.id = documentReference.getId();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(MealsTag, "Error adding Meal day document", e);
                    }});
    };

//    static void modifyMealPlan(int i,ArrayList<Mealday> meals) {
//        MealPlanCol
//                .document(meals.get(i).id).update("meals", meals.get(i).getMeals())
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Log.d(MealsTag, "DocumentSnapshot successfully updated!");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w(MealsTag, "Error updating document", e);
//                    }
//   });
//};


    static void deleteMealPlan(int i, ArrayList<Mealday> meals) {
        MealPlanCol.document(meals.get(i).id).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(MealsTag, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(MealsTag, "Error updating document", e);
                    }
                });

    };

    static void fetchMealPlans(ExpandableListAdapter mealdayAdapter,
                               ArrayList<Mealday> meals) {
        MealPlanCol
            .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                meals.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)

                {
                    // Log.d(MealsTag, "Meal plan fetched successfully");
                    Date date = doc.getDate("date");
                    ArrayList<Object> mealsForDay = (ArrayList<Object>) doc.getData().get("meals");
                    ArrayList<Recipe> mealsfortheDay = new ArrayList<>();
                    for (Object meal:mealsForDay) {
                        Map<String, Object> mealMap = (Map<String, Object>) meal;
                        String title = mealMap.get("title").toString();
                        String instructions = mealMap.get("instructions").toString();
                        int preptime = Integer.parseInt(mealMap.get("preptime").toString());
                        float servings = Float.parseFloat(mealMap.get("servings").toString());
                        String recipeCategory = mealMap.get("recipeCategory").toString();
                        String comments = mealMap.get("comments").toString();
//                        String id = mealMap.get("id").toString();
                        ArrayList<Ingredient> ingredients = (ArrayList<Ingredient>) mealMap.get("ingredients");
                        Recipe recipe = new Recipe(title,preptime,servings,recipeCategory,comments,instructions,ingredients);
                        mealsfortheDay.add(recipe);
                    }

//                  meals.add(new Mealday( date,  mealsForDay)); // Adding the meal days from FireStore
                    //for(QueryDocumentSnapshot meal: doc.getData().get("meals"))
                    meals.add(new Mealday( date,  mealsfortheDay)); // Adding the meal days from FireStore
                }
                ((BaseExpandableListAdapter)mealdayAdapter).notifyDataSetChanged(); // Notifying the adapter to render any new data fetched
            }
        });
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
