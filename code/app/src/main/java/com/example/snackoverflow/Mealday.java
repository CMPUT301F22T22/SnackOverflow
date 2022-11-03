package com.example.snackoverflow;

import java.util.ArrayList;
import java.util.Date;

/*
This class stores the recipes stored/planned for the day
 */
public class Mealday {
    private Date date;
    private ArrayList<Recipe> meals;
    protected String id;

    public Mealday(Date date, ArrayList<Recipe> meals) {
        this.meals = meals;
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ArrayList<Recipe> getMeals() {
        return meals;
    }

    public void setMeals(ArrayList<Recipe> meals) {
        this.meals = meals;
    }
}
