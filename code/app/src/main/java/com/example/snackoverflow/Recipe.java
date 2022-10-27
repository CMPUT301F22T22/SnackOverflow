package com.example.snackoverflow;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Objects;

public class Recipe implements Serializable, Parcelable {
    private String title;
    private LocalTime preptime;
    private float servings;
    private String recipeCategory;
    private String comments;
    private ArrayList<Ingredient> ingredients ;
    //TODO: Add path to photograph(?)


    public Recipe(String title, LocalTime preptime, float servings, String recipeCategory, String comments) {
        this.title = title;
        this.preptime = preptime;
        this.servings = servings;
        this.recipeCategory = recipeCategory;
        this.comments = comments;
        ingredients = new ArrayList<Ingredient>();
    }

    protected Recipe(Parcel in) {
        title = in.readString();
        servings = in.readFloat();
        recipeCategory = in.readString();
        comments = in.readString();
        ingriedients = in.createStringArrayList();
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalTime getPreptimeTime() {
        return preptime;
    }

    public void setPreptimeTime(LocalTime time) {
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

    public void addIngredient(Ingredient ingredient){
        if (!ingredients.contains(ingredient)){
            ingredients.add(ingredient);
        }
    }

    public void removeIngredient(Ingredient ingredient){
        if(ingredients.contains(ingredient)){
            ingredients.remove(ingredient);
        }
    }

    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(recipeCategory);
        parcel.writeFloat(servings);
        parcel.writeString(comments);
    }
}

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return title.equals(recipe.title);
    }
}
