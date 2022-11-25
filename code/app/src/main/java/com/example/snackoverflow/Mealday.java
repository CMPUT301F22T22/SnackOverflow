package com.example.snackoverflow;

import java.util.ArrayList;
import java.util.Date;

/**
 * This class stores the recipes planned/stored for the day
 * @see MealdayAdapter
 * @see MealPlannerAdapter
 * */
public class Mealday {
    private Date date;
    private ArrayList<Recipe> meals;
    protected String id;

    /**
     * Constructor for the creating a new meal day
     * @param date the date for which the meal day is initialized
     * @param meals the meals to add to the day
     * */
    public Mealday(Date date, ArrayList<Recipe> meals) {
        this.meals = meals;
        this.date = date;
    }

    /**
     * Getter for date
     * @return the date of the current meal day
     * */
    public Date getDate() {
        return date;
    }

    /**
     * Setter for Date
     * @param date the date to set for the current meal day
     * */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Getter for meals
     * @return all the meals for the current meal day
     * */
    public ArrayList<Recipe> getMeals() {
        return meals;
    }

    /**
     * Setter for meals
     * @param meals all the meals for the current mealday
     * */
    public void setMeals(ArrayList<Recipe> meals) {
        this.meals = meals;
    }

    public ArrayList<Recipe> getMealsWithoutImage() {
        ArrayList<Recipe> mealsList = getMeals();
        for (int i = 0; i < mealsList.size(); i++ ) {
            mealsList.get(i).setImageBitmap(null);
        }
        return mealsList;
    }
}