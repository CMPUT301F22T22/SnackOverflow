package com.example.snackoverflow;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
    static void modifyIngredient(Ingredient ingredient) {
        ingredientsCol
                .document(ingredient.id).update("amount", ingredient.getAmount(), "bestBefore", ingredient.getBestBefore(), "category", ingredient.getCategory(), "location", ingredient.getLocation(), "title", ingredient.getTitle(), "unit", ingredient.getUnit())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(IngredientsTAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(IngredientsTAG, "Error updating document", e);
                    }
                });
    };

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



    static void fetchRecipesForMealPlan(ArrayList<Recipe> recipes) {
        ingredientsCol.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                recipes.clear();
                try {
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String id = doc.getId();
                        System.out.println(id);
                        Map<String, Object> data = doc.getData();
                        String title = data.get("title").toString();
                        int prep_time = Integer.valueOf(data.get("prep_time").toString());
                        float servings = Float.parseFloat(data.get("servings").toString());
                        String category = data.get("category").toString();
                        String instructions = data.get("instructions").toString();
                        String comments = data.get("comments").toString();

                        StorageReference storageRef = FirebaseStorage.getInstance().getReference("recipe/" + id + ".jpg");
                        int imageTrackingData = Integer.valueOf(data.get("image_tracker").toString());
                        try {
                            File localFile = File.createTempFile("tempfile", ".jpg");
                            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Bitmap imgBitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                    Recipe recipe = new Recipe(id, title, prep_time, servings,
                                            category, comments, instructions, imgBitmap);
                                    recipes.add(recipe);
//                                    recipeArrayAdapter.notifyDataSetChanged();
//                                handleSortBy(0);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Recipe recipe = new Recipe(id, title, prep_time, servings,
                                            category, comments, instructions, null);
                                    recipes.add(recipe);
//                                    recipeArrayAdapter.notifyDataSetChanged();
//                                handleSortBy(0);
                                }
                            });
                        } catch (IOException e) {

                        }
                    }
//                    recipeArrayAdapter.notifyDataSetChanged();
                } catch (NullPointerException e) {
                }
            }

//        recipeCol.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
//                    FirebaseFirestoreException error) {
//                if (error != null) {
//                    Log.w(IngredientsTAG, "Failed to fetch ingredients.",error);
//                    return;
//                }
//                recipes.clear();
//                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
//                {
//                    Log.d(IngredientsTAG, "Ingredients retrieved successfully");
//                    String id = doc.getId();
//                    String title = (String) doc.getData().get("title");
//                    String location = (String) doc.getData().get("location");
//                    int amount = doc.getLong("amount").intValue();
//                    int unit = doc.getLong("unit").intValue();
//                    Date bestBefore = doc.getDate("bestBefore");
//                    String category = (String) doc.getData().get("category");
//                    Ingredient ingredientItem = new Ingredient(title, bestBefore, location, amount, unit, category);
//                    ingredientItem.id = id;
//                    recipes.add(ingredientItem); // Adding the ingredients from FireStore
//                }
//                // Notifying the adapter to render any new data fetched
//                recipeArrayAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched
//            }
//        });

        });
    }

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
                .document(mealDay.getDate().toString()).set(Arrays.asList(mealDay))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(MealsTag, "Meal day document snapshot written");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(MealsTag, "Error adding Meal day document", e);
                    }});
    };



    static void deleteMealPlan(Mealday mealDay) {
        MealPlanCol.document(mealDay.getDate().toString()).delete()
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

        static void modifyMealPlan(Mealday mealday) {
//            System.out.println("Im here ar modify");
//            Map<String,Object> city = new HashMap<>();
//            city.put("name","LA");
//
//            MealPlanCol.document("Modi").set(city));
        MealPlanCol
                .document(mealday.getDate().toString()).update("meals", Collections.singletonList(mealday.getMeals()))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
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
                    Log.d(MealsTag, "Meal plan fetched successfully");
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
//                        String imageTrackingData = mealMap.get("image_tracker").toString();
                        if (mealMap.get("image_tracker") != null ) {
                            loadImage(recipe);
                        }

                        mealsfortheDay.add(recipe);
                    }

//                  meals.add(new Mealday( date,  mealsForDay)); // Adding the meal days from FireStore
                    //for(QueryDocumentSnapshot meal: doc.getData().get("meals"))
                    meals.add(new Mealday( date,  mealsfortheDay));
                    Collections.sort(meals, new Comparator<Mealday>() {
                        @Override
                        public int compare(Mealday mealday, Mealday t1) {
                            return mealday.getDate().compareTo(t1.getDate());
                        }
                    });// Adding the meal days from FireStore
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

    public static void loadImage(Recipe recipe) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference("recipe/"+recipe.getId()+".jpg");
        try {
            File localFile = File.createTempFile("tempfile",".jpg");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap imgBitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    recipe.setImageBitmap(imgBitmap);
//                    recipeDataList.add(recipe);
//                    recipeArrayAdapter.notifyDataSetChanged();
//                    handleSortBy(0);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    loadImage(recipe);
                }
            });
        } catch (IOException e) {

        }
    }



}
