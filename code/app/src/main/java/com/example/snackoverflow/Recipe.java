package com.example.snackoverflow;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class Recipe implements Serializable, Parcelable {
    private String id;
    private String title;
    private int preptime;
    private float servings;
    private String recipeCategory;
    private String instructions;
    private String comments;
    private ArrayList<Ingredient> ingredients ;
    //TODO: Add path to photograph(?)


    public Recipe(String title, int preptime, float servings, String recipeCategory, String comments, String instructions) {
        this.title = title;
        this.preptime = preptime;
        this.servings = servings;
        this.recipeCategory = recipeCategory;
        this.instructions = instructions;
        this.comments = comments;
        ingredients = new ArrayList<Ingredient>();
    }

    public Recipe(String title, int preptime, float servings, String recipeCategory, String comments, String instructions, ArrayList<Ingredient> ingredients) {
        this.title = title;
        this.preptime = preptime;
        this.servings = servings;
        this.recipeCategory = recipeCategory;
        this.instructions = instructions;
        this.comments = comments;
        this.ingredients = new ArrayList<Ingredient>();
    }

    protected Recipe(Parcel in) {
        title = in.readString();
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

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPreptimeTime() {
        return preptime;
    }

    public void setPreptimeTime(int time) {
        this.preptime = preptime;
    }

    public float getServings() {
        return servings;
    }

    public void setServings(float servings) {
        this.servings = servings;
    }

    public String getRecipeCategory() {
        return recipeCategory;
    }

    public void setRecipeCategory(String recipeCategory) {
        this.recipeCategory = recipeCategory;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }


    public void addIngredient(Ingredient ingredient){
        if (!ingredients.contains(ingredient)){
            ingredients.add(ingredient);
        }
        else{
            throw new IllegalArgumentException();
        }
    }

    public void removeIngredient(Ingredient ingredient){
        if(ingredients.contains(ingredient)){
            ingredients.remove(ingredient);
        }
        else{
            throw new IllegalArgumentException();
        }
    }

    public ArrayList<Ingredient> getIngredients(){
        return this.ingredients;
    }

    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(title);
        parcel.writeFloat(servings);
        parcel.writeString(recipeCategory);
        parcel.writeString(instructions);
        parcel.writeString(comments);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return title.equals(recipe.title);
    }
}
