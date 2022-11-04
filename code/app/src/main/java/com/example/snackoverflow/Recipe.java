package com.example.snackoverflow;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Recipe class to model a recipe containing title, preptime, servings,
 * category, instructions, comments, ingredients, imageBitmap
 * implements Serializable
 * implements Parcelable
 * @see RecipeActivity
 * @see RecipeAdapter
 * @see RecipeIngredientFragment
 * @see RecipeIngredientViewFragment
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
    //TODO: Add path to photograph(?)

    /**
     * Constructor for the creating a new recipe
     * @param title  a description for the recipe
     * @param prepTime the prep time for the recipe
     * @param servings the number of servings the recipe is for
     * @param category the category of the recipe
     * @param comments the comments for the recipe
     * @param instructions the instructions for the recipe
     * */
    public Recipe(String title, int prepTime, float servings, String category, String comments, String instructions) {
        this.title = title;
        this.preptime = prepTime;
        this.servings = servings;
        this.recipeCategory = category;
        this.instructions = instructions;
        this.comments = comments;
        ingredients = new ArrayList<Ingredient>();
    }

    /**
     * Creates a recipe from a parcel
     * @param in the parcel containing the contents of a recipe
     * */
    protected Recipe(Parcel in) {
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
     * Constructor for the creating a new recipe
     * @param title  a description for the recipe
     * @param preptime the prep time for the recipe
     * @param servings the number of servings the recipe is for
     * @param category the category of the recipe
     * @param comments the comments for the recipe
     * @param instructions the instructions for the recipe
     * @param imgBitmap the image bitmap for the recipe
     * */
    public Recipe(String title, int preptime, float servings, String category, String comments, String instructions, @Nullable Bitmap imgBitmap) {
        this.title = title;
        this.preptime = preptime;
        this.servings = servings;
        this.recipeCategory = category;
        this.instructions = instructions;
        this.comments = comments;
        ingredients = new ArrayList<Ingredient>();
        this.imageBitmap = imgBitmap;
    }

    /**
     * Getter for the id
     * @return the id of the recipe
     * */
    public String getId() {
        return id;
    }

    /**
     * Getter for the title
     * @return the title of the recipe
     * */
    public String getTitle() {
        return title;
    }

    /**
     * Setter for the title
     * @param title the title for the recipe
     * */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter for the prep time
     * @return the prep time of the recipe
     * */
    public int getPreptime() {
        return preptime;
    }

    /**
     * Setter for the title
     * @param time the prep time for the recipe
     * */
    public void setPreptime(int time) {
        this.preptime = time;
    }

    /**
     * Getter for the number of servings
     * @return the number of servings for the recipe
     * */
    public float getServings() {
        return servings;
    }

    /**
     * Setter for the number of servings
     * @param servings the number of servings for the recipe
     * */
    public void setServings(float servings) {
        this.servings = servings;
    }

    /**
     * Getter for the Recipe Category
     * @return the category of the recipe
     * */
    public String getRecipeCategory() {
        return recipeCategory;
    }

    /**
     * Setter for the category of the recipe
     * @param recipeCategory the category for the recipe
     * */
    public void setRecipeCategory(String recipeCategory) {
        this.recipeCategory = recipeCategory;
    }

    /**
     * Getter for the Comments
     * @return the comments for the recipe
     * */
    public String getComments() {
        return comments;
    }

    /**
     * Set the comments for the recipe
     * @param comments for the recipe
     * */
    public void setComments(String comments) {
        this.comments = comments;
    }

    /**
     * Getter for the Instructions
     * @return the instructions for the recipe
     * */
    public String getInstructions() {
        return instructions;
    }

    /**
     * Setter for the Instructions
     * @param instructions the instructions for the recipe
     * */
    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    /**
     * adds Ingredient to the recipe
     * @param ingredient ingredient to add to the recipe
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
     * remove the ingredient from the recipe
     * @param ingredient the ingredient to remove from the recipe
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
     * Returns a list for all the ingredients of the recipe
     * @return list of all the ingredients of the recipe
     * */
    public ArrayList<Ingredient> getIngredients(){
        return this.ingredients;
    }

    /**
     * Getter for the Image BitMap
     * @return the image bitmap
     * */
    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    /**
     * Setter for the Image Bitmap
     * @param imageBitmap the bitmap for the image
     * */
    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    /**
     * describe the contents for the recipe
     * @return integer 0
     * */
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(title);
        parcel.writeInt(preptime);
        parcel.writeFloat(servings);
        parcel.writeString(recipeCategory);
        parcel.writeString(instructions);
        parcel.writeString(comments);
    }

    /**
     * Checks for the equality of recipe object
     * */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return title.equals(recipe.title);
    }
}
