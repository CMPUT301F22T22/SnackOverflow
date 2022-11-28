package com.example.snackoverflow;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Recipe class encapsulates an object for the recipe type where it
 * contains a title (description), time to prep, number of servings, category, instructions, comments,
 * ingredients required for recipe. It also stores intricate details like id of firebase document and
 * the image
 * */
public class Recipe implements Serializable, Parcelable {
    private String id;
    private String title;
    private int preptime;
    private float servings;
    private String recipeCategory;
    private String instructions;
    private String comments;
    private ArrayList<Ingredient> ingredients ;
    private Bitmap imageBitmap;


    /**
     * Constructor for initializing recipes with basic information
     * @param title a string describing the ingredient
     * @param prepTime an integer specifying the time it takes to prep the meal
     * @param servings a float defining the number of people it can serve
     * @param category a string describing what category the recipe is is
     * @param comments a string containing general comments about the recipe the user may have
     * @param instructions a string containing instructions to prepare the recipe
     * @param ingredients an arraylist with ingredients that the recipe needs
     * */
    public Recipe(String title, int prepTime, float servings, String category, String comments, String instructions, ArrayList<Ingredient> ingredients) {
        this.title = title;
        this.preptime = prepTime;
        this.servings = servings;
        this.recipeCategory = category;
        this.instructions = instructions;
        this.comments = comments;
        this.ingredients = ingredients != null ? ingredients : new ArrayList<Ingredient>();
    }

    /**
     * Constructor for initializing recipes with basic information
     * @param id a string containing the id of the recipe in firebase
     * @param title a string describing the ingredient
     * @param prepTime an integer specifying the time it takes to prep the meal
     * @param servings a float defining the number of people it can serve
     * @param category a string describing what category the recipe is is
     * @param comments a string containing general comments about the recipe the user may have
     * @param instructions a string containing instructions to prepare the recipe
     * @param imgBitmap the image for the recipe stored as a bitmap
     * */
    public Recipe(String id, String title, int prepTime, float servings, String category, String comments, String instructions, @Nullable Bitmap imgBitmap) {
        this.id = id;
        this.title = title;
        this.preptime = prepTime;
        this.servings = servings;
        this.recipeCategory = category;
        this.instructions = instructions;
        this.comments = comments;
        ingredients = new ArrayList<Ingredient>();
        this.imageBitmap = imgBitmap;
    }
    /**
     * Constructor for possing the recipe between activities
     * @param in the fields passed between activities
     * */
    protected Recipe(Parcel in) {
        id = in.readString();
        title = in.readString();
        preptime = in.readInt();
        servings = in.readFloat();
        recipeCategory = in.readString();
        instructions = in.readString();
        comments = in.readString();
        //ingredients = in.createStringArrayList();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    /**
     * Getter for id
     * returns the firebase id of the current recipe
     * @return the string with the firebase id of current recipe
     * */
    public String getId() {
        return id;
    }

    /**
     * Getter for Title
     * returns the title (description) of the current recipe
     * @return the string describing the title (description) of the recipe
     * */
    public String getTitle() {
        return title;
    }

    /**
     * Setter for Title
     * sets the title for the recipe
     * @param title the title we want to set to the recipe
     * */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter for prepTime
     * returns the time to prep of the current recipe
     * @return the int which is the time to prep the current meal
     * */
    public int getPreptime() {
        return preptime;
    }

    /**
     * Getter for servings
     * returns the number of servings of the current recipe
     * @return the int which is the number of servings the recipe makes
     * */
    public float getServings() {
        return servings;
    }

    /**
     * Setter for servings
     * sets the number of servings for the recipe
     * @param servings the number of servings we want to set to the recipe
     * */
    public void setServings(float servings) {
        this.servings = servings;
    }

    /**
     * Getter for recipe category
     * returns the category of the current recipe
     * @return the string which describes the category of the recipe
     * */
    public String getRecipeCategory() {
        return recipeCategory;
    }

    /**
     * Getter for recipe comments
     * returns the comments made by user for the current recipe
     * @return the string which describes the comments made by the user for the recipe
     * */
    public String getComments() {
        return comments;
    }

    /**
     * Getter for recipe instruction
     * returns the instructions to make the current recipe
     * @return the string which describes the instructions for the recipe
     * */
    public String getInstructions() {
        return instructions;
    }

    /**
     * Adds an ingredient to a recipe
     * @param ingredient the ingredient which we want to add to the recipe
     * */
    public void addIngredient(Ingredient ingredient){
        if (!ingredients.contains(ingredient)){
            ingredients.add(ingredient);
        }
        else{
            throw new IllegalArgumentException();
        }
    }

    /**
     * Removes an ingredient form a recipe
     * @param ingredient the ingredient which we want to remove from the recipe
     * */
    public void removeIngredient(Ingredient ingredient){
        if(ingredients.contains(ingredient)){
            ingredients.remove(ingredient);
        }
        else{
            throw new IllegalArgumentException();
        }
    }

    /**
     * Getter for ingredients
     * returns a list of ingredients needed for the current recipe
     * @return the list of ingredients in the current recipe
     * */
    public ArrayList<Ingredient> getIngredients(){
        return this.ingredients;
    }

    /**
     * Getter for image bitmap
     * returns the image for the recipe stored, in bitmap format
     * @return the bitmap of the image for the recipe
     * */
    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    /**
     * Setter for image bitmap
     * sets the image for the recipe
     * @param imageBitmap the image to assign to the recipe
     * */
    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeInt(preptime);
        parcel.writeFloat(servings);
        parcel.writeString(recipeCategory);
        parcel.writeString(instructions);
        parcel.writeString(comments);
    }

    /**
     * Comparator for recipes to check if two recipes are equal
     * @param o the object to compare a recipe with
     * */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        if (this.id != null) {
            return id.equals(recipe.getId());
        }
        return title.equals(recipe.getTitle());
    }
}
