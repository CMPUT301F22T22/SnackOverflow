package com.example.snackoverflow;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

/*
This class stores the recipes stored/planned for the day
 */
public class Mealday {
    private LocalDate date;
    private ArrayList<Recipe> meals;

    public Mealday(LocalDate date, ArrayList<Recipe> meals) {
        this.meals = meals;
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public ArrayList<Recipe> getMeals() {
        return meals;
    }

    public void setMeals(ArrayList<Recipe> meals) {
        this.meals = meals;
    }
}
