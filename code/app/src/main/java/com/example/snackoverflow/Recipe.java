package com.example.snackoverflow;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Objects;

public class Recipe implements Serializable {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return title.equals(recipe.title);
    }
}