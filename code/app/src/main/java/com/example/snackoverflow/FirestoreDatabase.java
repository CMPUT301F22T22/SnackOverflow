package com.example.snackoverflow;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;

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
import com.google.firebase.storage.UploadTask;

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
 * Class storing all the Firestore Database related functionality
 * */
public class FirestoreDatabase {

    final static FirebaseFirestore db = FirebaseFirestore.getInstance();
    final static CollectionReference recipeCol = db.collection("recipe");
    final static CollectionReference ingredientsCol = db.collection("ingredient");
    final static CollectionReference MealPlanCol = db.collection("meal_plan");
    final static CollectionReference ShoppingListCol = db.collection("shopping_list");

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

    /**
     * Add an ShoppingList Item to the Firebase Storage
     * */
    static void addShoppingItem(HashMap<String, Object> data) {
        ShoppingListCol
            .document(data.get("title").toString()).set(data)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("ShoppingTAG", "Shopping List document snapshot written with ID: ");
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w("ShoppingTAG", "Error adding shopping list document", e);
                }

            });
    }

    /**
     * Deletes the shopping Item
     * @param data
     */
    static void deleteShoppingItem(String data) {
        ShoppingListCol
                .document(data)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(IngredientsTAG, "Shopping List document snapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(IngredientsTAG, "Error deleting shopping list document", e);
                    }
                });
    };

    /**
     * Add Recipe to the Database
     * @param data
     * @param uri
     */
    static void addRecipe(HashMap<String, Object> data, Uri uri) {
        recipeCol
            .add(data)
            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Log.d(RecipeTag, "Recipe document snapshot written with ID: " + documentReference.getId());
                    if (uri != null) {
                        FirestoreDatabase.uploadImage(uri, documentReference.getId());
                    } else {
                        Uri defaultUri = Uri.parse("android.resource://com.example.snackoverflow/drawable/food_icon");
                        FirestoreDatabase.uploadImage(defaultUri, documentReference.getId());
                    }
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
     * Add Meal Plan to the databse
     * @param mealDay
     */
    static void addMealPlan(Mealday mealDay) {
        MealPlanCol
                .document(mealDay.getDate().toString()).set(mealDay)
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

    /**
     * Delete Meal Plan from the Database
     * @param mealDay
     */
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

    /**
     * Modifies the Meal Plan
     * @param mealday
     */
    static void modifyMealPlan(Mealday mealday) {
    MealPlanCol
            .document(mealday.getDate().toString()).update("meals", mealday.getMealsWithoutImage())
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
        MealPlanCol
                .document(mealday.getDate().toString()).update("servings", mealday.getServings())
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

    /**
     * fetch the meal plans from the Database
     * @param mealdayAdapter
     * @param meals
     */
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
                            ArrayList<Double> mealServings = (ArrayList<Double>)doc.getData().get("servings");
                            ArrayList<Recipe> mealsfortheDay = new ArrayList<>();
                            for (Object meal:mealsForDay) {
                                Map<String, Object> mealMap = (Map<String, Object>) meal;
                                String title = mealMap.get("title").toString();
                                String instructions = mealMap.get("instructions").toString();
                                int preptime = Integer.parseInt(mealMap.get("preptime").toString());
                                float servings = Float.parseFloat(mealMap.get("servings").toString());
                                String recipeCategory = mealMap.get("recipeCategory").toString();
                                if (mealMap.get("recipeCategory").toString().charAt(0)=='!'){
                                    recipeCategory = mealMap.get("recipeCategory").toString().substring(1);
                                }

                                String comments = mealMap.get("comments").toString();
                                ArrayList<Object> ingredientArray = (ArrayList<Object>) mealMap.get("ingredients");
                                ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
                                for (int i = 0; i < ingredientArray.size(); i++) {
                                    Map<String, Object> ingredientMap = (Map<String, Object>) ingredientArray.get(i);
                                    ingredients.add(new Ingredient(ingredientMap.get("title").toString(), Integer.valueOf(ingredientMap.get("amount").toString()),
                                            Integer.valueOf(ingredientMap.get("unit").toString()), ingredientMap.get("category").toString()));
                                }
                                String id = null;
                                if(mealMap.get("id")!=null){
                                    id = mealMap.get("id").toString();
                                }
                                Recipe recipe = new Recipe(id,title,preptime,servings,recipeCategory,comments,instructions,ingredients,null);
                                try{
                                    loadImage(recipe);
                                }
                                catch(Exception e){
                                }
                                mealsfortheDay.add(recipe);
                            }

                            //for(QueryDocumentSnapshot meal: doc.getData().get("meals"))
                            meals.add(new Mealday( date,  mealsfortheDay, mealServings));
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

    /**
     * fetches recipes to be used in the meal plan spinner
     * @param spinnerAdapter
     * @param recipeNames array list that holds the recipe names
     * @see android.widget.SpinnerAdapter
     */
    static void fetchRecipestoMealPlanSpinner(ArrayAdapter<CharSequence> spinnerAdapter,ArrayList<CharSequence> recipeNames){
        ArrayList<Recipe> recipeDataList = new ArrayList<Recipe>();
        recipeCol.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                recipeDataList.clear();
                try {
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String id = doc.getId();
                        Map<String, Object> data = doc.getData();
                        String title = data.get("title").toString();
                        int prep_time = Integer.valueOf(data.get("prep_time").toString());
                        float servings = Float.parseFloat(data.get("servings").toString());
                        String category = data.get("category").toString();
                        String instructions = data.get("instructions").toString();
                        String comments = data.get("comments").toString();
                        ArrayList<Object> ingredientArray = (ArrayList<Object>) data.get("ingredients");
                        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
                        for (int i = 0; i < ingredientArray.size(); i++) {
                            Map<String, Object> ingredientMap = (Map<String, Object>) ingredientArray.get(i);
                            ingredients.add(new Ingredient(ingredientMap.get("title").toString(), Integer.valueOf(ingredientMap.get("amount").toString()),
                                    Integer.valueOf(ingredientMap.get("unit").toString()), ingredientMap.get("category").toString()));
                        }
//
                        StorageReference storageRef = FirebaseStorage.getInstance().getReference("recipe/" + id + ".jpg");
                        int imageTrackingData = Integer.valueOf(data.get("image_tracker").toString());
                        try {
                            File localFile = File.createTempFile("tempfile", ".jpg");
                            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Bitmap imgBitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                    Recipe recipe = new Recipe(id, title, prep_time, servings,
                                            category, comments, instructions,ingredients, imgBitmap);
                                    recipeDataList.add(recipe);
                                    for (int i =1;i<=recipeDataList.size();i++){
                                        if(!recipeNames.contains(recipeDataList.get(i-1).getTitle())) {
                                            recipeNames.add(recipeDataList.get(i - 1).getTitle());
                                        }
                                    }
                                    spinnerAdapter.notifyDataSetChanged();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Recipe recipe = new Recipe(id, title, prep_time, servings,
                                            category, comments, instructions,ingredients, null);
                                    recipeDataList.add(recipe);
                                    spinnerAdapter.notifyDataSetChanged();
                                }
                            });

                        } catch (IOException e) {

                        }
                    }
                } catch (NullPointerException e) {
                }
            }

        });
    }

    /**
     * Upload image to the database
     * @param uri
     * @param id
     */
    static void uploadImage(Uri uri, String id) {
        if (uri != null){
            String filename = id;
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReference().child("recipe/"+filename+".jpg");
            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    System.out.println("Working");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    System.out.println("Not working");
                }
            });
        }
    };

    /**
     * Loads the image from database
     * @param recipe
     */
    public static void loadImage(Recipe recipe) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference("recipe/"+recipe.getId()+".jpg");
        try {
            File localFile = File.createTempFile("tempfile",".jpg");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    if(!Objects.equals(recipe.getId(), "00000000000000")){
                        Bitmap imgBitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        recipe.setImageBitmap(imgBitmap);
                    }
                    else{

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        } catch (IOException e) {

        }
    }



}
