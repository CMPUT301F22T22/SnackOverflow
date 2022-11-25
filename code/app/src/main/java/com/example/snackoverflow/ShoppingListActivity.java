package com.example.snackoverflow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.EventListener;

import java.lang.ref.Reference;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The main activity page for shopping list. This page shows the ingredients a user needs to buy from the store.
 * These ingredients can be manually added using the floating action button, or are automatically added when the meal specified does not have the required ingredients.
 * The user can view, add ingredients, and check them off the list with the provided checkbox.
 * Each entry in the shopping list is an Ingredient object, with a description, category, unit, and amount. The remaining
 * fields (best before date, location) are left null. When a user completes the shopping list, they are prompted to enter the
 * remaining fields (best before date, location).
 * */
public class ShoppingListActivity extends AppCompatActivity implements ShoppingListAddItemFragment.OnFragmentInteractionListener {
    private ListView shoppingList;
    private ArrayAdapter<Ingredient> shoppingListAdapter;
    private ArrayList<Ingredient> shoppingItems;
    private final static ArrayList<Ingredient> firebase_ingredient_meal_plan_list = new ArrayList<>();
    private final static ArrayList<Ingredient> firebase_ingredient_storage_list = new ArrayList<>();
    private final static ArrayList<String> toRemove = new ArrayList<>();
    private final static HashMap<String, Integer> firebase_ingredient_meal_plan_hashmap = new HashMap<>();
    private final static HashMap<String, Integer> firebase_ingredient_storage_hashmap = new HashMap<>();
    private String currSortOrder = "inc";
    /**
     * Used to start the ShoppingListActivity. If the activity needs to be recreated, it can be passed to onCreate as a bundle
     * to recreate the activity. The method is also called, when the orientation of the device change, termination of the app.
     * @param savedInstanceState Contains the most recently supplied data in a bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        shoppingItems = new ArrayList<>();

        // Linking the listview to an arraylist and setting adapter
        shoppingList = findViewById(R.id.shopping_list_storage_list);
        shoppingListAdapter = new ShoppingListAdapter(this, shoppingItems);
        shoppingList.setAdapter(shoppingListAdapter);

        FirebaseFirestore db_instance = FirebaseFirestore.getInstance();
        CollectionReference ingredientsCol = db_instance.collection("ingredient");
        CollectionReference MealPlanCol = db_instance.collection("meal_plan");

        ingredientsCol.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                if (error != null) {
                    //Log.w(IngredientsTAG, "Failed to fetch ingredients.",error);
                    return;
                }
                firebase_ingredient_storage_list.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                    Log.d("lol", "Ingredients retrieved successfully");
                    String id = doc.getId();
                    String title = (String) doc.getData().get("title");
                    String category = doc.get("category").toString();
                    Integer unit = Integer.parseInt(doc.get("unit").toString());
                    Integer amount = Integer.parseInt(doc.get("amount").toString());
                    firebase_ingredient_storage_list.add(new Ingredient(title, amount, unit, category)); // Adding the ingredients from FireStore
                }

            }

        });

        MealPlanCol.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                if (error != null) {
                    //Log.w(IngredientsTAG, "Failed to fetch ingredients.", error);
                    return;
                }
                toRemove.clear();
                firebase_ingredient_meal_plan_list.clear();
                shoppingItems.clear();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    ArrayList<Object> mealsForDay = (ArrayList<Object>) doc.getData().get("meals");
                    for (Object meal : mealsForDay) {
                        Map<String, Object> mealMap = (Map<String, Object>) meal;
//                        String id = mealMap.get("id").toString();
                        Object servings = (Object) mealMap.get("servings");
                        Double servingInDouble = Double.parseDouble(servings.toString());
                        Integer servingInInt = servingInDouble.intValue();
                        Log.d("brother", servings.toString());
                        //Integer servingsInt = servings.intValue();
                        ArrayList<Object> ingredients = (ArrayList<Object>) mealMap.get("ingredients");
                        //ArrayList<Ingredient> ingredients2 = new ArrayList<>();
                        for (Object ing: ingredients) {
                            Map<String, Object> ingredMap = (Map<String, Object>) ing;
                            String title = ingredMap.get("title").toString();
                            String category = ingredMap.get("category").toString();
                            Integer unit = Integer.parseInt(ingredMap.get("unit").toString())*servingInInt;
                            Integer amount = Integer.parseInt(ingredMap.get("amount").toString());
                            firebase_ingredient_meal_plan_list.add(new Ingredient(title, amount, unit, category));
                        }
                    }
                }
                Log.d("arrlist", firebase_ingredient_meal_plan_list.toString());
                firebase_ingredient_meal_plan_hashmap.clear();
                firebase_ingredient_storage_hashmap.clear();


                for (Ingredient ing: firebase_ingredient_meal_plan_list) {
                    int count = firebase_ingredient_meal_plan_hashmap.containsKey(ing.getTitle()) ? firebase_ingredient_meal_plan_hashmap.get(ing.getTitle()) : 0;
                    firebase_ingredient_meal_plan_hashmap.put(ing.getTitle(), count + ing.getUnit());
                }

                for (Ingredient ing: firebase_ingredient_storage_list) {
                    int count = firebase_ingredient_storage_hashmap.containsKey(ing.getTitle()) ? firebase_ingredient_storage_hashmap.get(ing.getTitle()) : 0;
                    firebase_ingredient_storage_hashmap.put(ing.getTitle(), count + ing.getUnit());
                }

                Log.d("mapmeal", firebase_ingredient_meal_plan_hashmap.toString());
                Log.d("mapstorage", firebase_ingredient_storage_hashmap.toString());
                Log.d("mapingredient", firebase_ingredient_storage_hashmap.toString());
                Log.d("shoppinglistbefore", String.valueOf(shoppingItems.size()));

                /*
                // Handle duplicated ingredients which are present in storage
                for (String ing: firebase_ingredient_meal_plan_hashmap.keySet()) {
                    for (Ingredient elem: firebase_ingredient_storage_list) {
                        Log.d("inhere", elem.getTitle());
                        if (ing.equals(elem.getTitle())) {
                            Log.d("herenow", firebase_ingredient_meal_plan_hashmap.get(ing).toString());
                            Log.d("herenow2", String.valueOf(elem.getUnit()));
                            if (firebase_ingredient_meal_plan_hashmap.get(ing) > elem.getUnit()) {
                                shoppingItems.add(new Ingredient(ing, elem.getAmount(), firebase_ingredient_meal_plan_hashmap.get(ing) - elem.getUnit(), elem.getCategory()));
                                Log.d("deeb", shoppingItems.toString());
                                firebase_ingredient_meal_plan_hashmap.remove(ing);
                                shoppingListAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                 */
                // Handle duplicated ingredients which are present in storage
                String ingCatStorage = "unknown";
                int ingAmountStorage = 0;
                for (String ingMeal: firebase_ingredient_meal_plan_hashmap.keySet()) {
                    for (String ingStorage: firebase_ingredient_storage_hashmap.keySet()) {
                        if (ingMeal.equals(ingStorage)) {
                            Log.d("itsequalbro", ingMeal);
                            if (firebase_ingredient_meal_plan_hashmap.get(ingMeal) > firebase_ingredient_storage_hashmap.get(ingMeal)) {
                                Log.d("itsmorebro", ingMeal);
                                for (Ingredient ingObject: firebase_ingredient_storage_list) {
                                    Log.d("why", firebase_ingredient_meal_plan_hashmap.get(ingMeal).toString());
                                    if (ingMeal.equals(ingObject.getTitle()) && !toRemove.contains(ingMeal)) {
                                        ingCatStorage = ingObject.getCategory();
                                        ingAmountStorage = ingObject.getAmount();
                                        toRemove.add(ingMeal);
                                        shoppingItems.add(new Ingredient(ingMeal, ingAmountStorage, firebase_ingredient_meal_plan_hashmap.get(ingMeal) - firebase_ingredient_storage_hashmap.get(ingMeal), ingCatStorage));
                                        Log.d("hey", "lol");
                                        shoppingListAdapter.notifyDataSetChanged();
                                    }
                                }
                            }
                            else {
                                toRemove.add(ingMeal);
                            }
                        }
                }
                Log.d("shoppinglistafter", String.valueOf(shoppingItems.size()));
                Log.d("toremove", String.valueOf(toRemove.toString()));

                }
                for (String removeob: toRemove) {
                    firebase_ingredient_meal_plan_hashmap.remove(removeob);
                }

                // Handle ingredients (duplicated and non-duplicated in meal plan) which are NOT in storage
                for (String ing: firebase_ingredient_meal_plan_hashmap.keySet()) {
                    String ingCat = "Unknown";
                    Integer ingAmount = 0;
                    for (Ingredient ingMeal: firebase_ingredient_meal_plan_list) {
                        if (ing == ingMeal.getTitle()) {
                            ingCat = ingMeal.getCategory();
                            ingAmount = ingMeal.getAmount();
                        }
                    }
                    shoppingItems.add(new Ingredient(ing, ingAmount, firebase_ingredient_meal_plan_hashmap.get(ing), ingCat));
                    shoppingListAdapter.notifyDataSetChanged();
                }
                Log.d("shoppinglistafter1", String.valueOf(shoppingItems.size()));
            }

        });

        String[] sortBySpinnerList = new String[] {"Title", "Category"};
        String[] sortOrderSpinnerList = new String[] {"Low-High/A-Z", "High-Low/Z-A"};
        Spinner sortBySpinner = (Spinner) findViewById(R.id.sort_by_spinner);
        Spinner sortOrderSpinner = (Spinner) findViewById(R.id.sort_order_spinner);
        ArrayAdapter<String> sortByAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, sortBySpinnerList);
        ArrayAdapter<String> sortOrderAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, sortOrderSpinnerList);
        sortByAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortOrderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sortBySpinner.setAdapter(sortByAdapter);
        sortOrderSpinner.setAdapter(sortOrderAdapter);

        LinearLayout sortByLayout = (LinearLayout) findViewById(R.id.sort_by_layout);
        LinearLayout sortOrderLayout = (LinearLayout) findViewById(R.id.sort_order_layout);

        sortByLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortBySpinner.performClick();
            }
        });

        sortOrderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortOrderSpinner.performClick();
            }
        });
        sortBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                handleSortBy(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sortOrderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                handleSortOrder(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Setting up NavBar
        NavigationBarView navigationBarView = findViewById(R.id.bottom_navigation);
        navigationBarView.setSelectedItemId(R.id.shoppinglist);
        navigationBarView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            /**
             * Start corresponding activity when any button in the navbar is clicked. It uses the existing
             * application context to start the activity, so that all previous changes made to that activity
             * are saved
             * @param item
             * @return true if a valid button is clicked and the activity starts, false if invalid
             */
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Switch to corresponding activity when a button is clicked
                switch ((item.getItemId())) {
                    case R.id.ingredients:
                        startActivity(new Intent(getApplicationContext(),IngredientStorageActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.shoppinglist:
                        return true;
                    case R.id.mealplanner:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.recipes:
                        startActivity(new Intent(getApplicationContext(),RecipeActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                // Return false otherwise
                return false;
            }
        });

        // Add floating action button to add ingredients to the shopping list
        final FloatingActionButton addShoppingItemButton = findViewById(R.id.add_to_shopping_list_button);
        addShoppingItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get support manager when the button is clicked
                new ShoppingListAddItemFragment().show(getSupportFragmentManager(), "ADD_SHOPPING_LIST_ITEM");
            }
        });


    }

    public void handleSortBy(int position) {
        switch (position) {
            case 0:
                if (currSortOrder.equals("dec")) {
                    Collections.sort(shoppingItems, new SortComparator.TitleComparator().reversed());
                } else {
                    Collections.sort(shoppingItems, new SortComparator.TitleComparator());
                }
                break;
            case 1:
                if (currSortOrder.equals("dec")) {
                    Collections.sort(shoppingItems, new SortComparator.CategoryComparator().reversed());
                } else {
                    Collections.sort(shoppingItems, new SortComparator.CategoryComparator());
                }
                break;
        }
        shoppingListAdapter.notifyDataSetChanged();
    }

    public void handleSortOrder(int position) {
        if (position == 1 && currSortOrder.equals("inc")) {
            Collections.reverse(shoppingItems);
            currSortOrder = "dec";
        } else if (position == 0 && currSortOrder.equals("dec")) {
            Collections.reverse(shoppingItems);
            currSortOrder = "inc";
        }
        shoppingListAdapter.notifyDataSetChanged();
    }

    /**
     * Add ingredient to the listview when the OK button is pressed in the user-friendly fragment
     * @param selectedIngredient
     */
    @Override
    public void onOkPressed(Ingredient selectedIngredient) {
        // Add ingredient to list when 'OK' is pressed in the fragment
        shoppingListAdapter.add(selectedIngredient);
    }
}