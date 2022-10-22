package com.example.snackoverflow;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.time.LocalTime;
import java.util.ArrayList;

public class Recipe implements Serializable {
    private String title;
    private LocalTime preptime;
    private float servings;
    private String recipeCategory;
    private String comments;
    private ArrayList<String> ingriedients;
    //TODO: Add path to photograph(?)


    public Recipe(String title, LocalTime preptime, float servings, String recipeCategory, String comments, ArrayList<String> ingriedients) {
        this.title = title;
        this.preptime = preptime;
        this.servings = servings;
        this.recipeCategory = recipeCategory;
        this.comments = comments;
        this.ingriedients = ingriedients;
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

    public ArrayList<String> getIngriedients() {
        return ingriedients;
    }

    public void setIngriedients(ArrayList<String> ingriedients) {
        this.ingriedients = ingriedients;
    }
}
