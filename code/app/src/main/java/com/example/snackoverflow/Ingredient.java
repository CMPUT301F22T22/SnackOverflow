package com.example.snackoverflow;

import java.io.Serializable;
import java.util.Date;

/**
 * Ingredient class encapsulates an object for Ingredient type where it
 * contains a title (description), best before date, location, amount, unit and category
 * */
public class Ingredient implements Serializable  {
    private String title;
    private Date bestBefore;
    private String location;
    private int amount;
    private int unit;
    private String category;
    protected String id;

    /**
     * Constructor for initializing Ingredients in the ShoppingList
     * @param description a string describing the ingredient
     * @param amount an integer specifying the cost of the ingredient
     * @param unit an integer defining the quantity of the ingredient
     * @param category a string describing what category the ingredient is is
     * */
    public Ingredient(String description, int amount, int unit, String category)  {
        this.title = description;
        this.location = null;
        this.amount = amount;
        this.unit = unit;
        this.category = category;
        this.bestBefore = null;
    }

    /**
     * Constructor for initializing Ingredients
     * @param description a string describing the ingredient
     * @param date date defining the best before date of the ingredient
     * @param location a string defining location of the ingredient in the storage
     * @param amount an integer specifying the cost of the ingredient
     * @param unit an integer defining the quantity of the ingredient
     * @param category a string describing what category the ingredient is is
     * */
    public Ingredient(String description, Date date, String location, int amount, int unit, String category) {
        this.title = description;
        this.location = location;
        this.amount = amount;
        this.unit = unit;
        this.bestBefore = date;
        this.category = category;
    }

    /**
     * Getter for Title
     * returns the title (description) of the current ingredient
     * @return the string describing the title (description) of the ingredient
     * */
    public String getTitle() {
        return title;
    }

    /**
     * Setter for Title
     * sets the title (description) of the ingredient
     * @param title a string describing the title (description) of the ingredient
     * */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter for Best Before
     * returns the best before date of the ingredient
     * @return the best before date of the ingredient
     * */
    public Date getBestBefore() {
        return bestBefore;
    }

    /**
     * Setter for Best Before
     * sets the best before date for an ingredient
     * @param bestBefore the best before date of the ingredient
     * */
    public void setBestBefore(Date bestBefore) {
        this.bestBefore = bestBefore;
    }

    /**
     * Getter for the location
     * returns the location of the ingredient
     * @return a string describing the location of the ingredient
     * */
    public String getLocation() {
        return location;
    }

    /**
     * Setter for Location
     * sets the location for the ingredient
     * @param location a string describing the location of the ingredient
     * */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Getter for Amount
     * returns the amount of the ingredient
     * @return an integer reflecting the amount of the ingredient
     * */
    public int getAmount() {
        return amount;
    }

    /**
     * Setter for Amount
     * sets the amount of the ingredient
     * @param amount an integer describing the amount of the ingredient
     * */
    public void setAmount(int amount) {
        this.amount = amount;
    }

    /**
     * Getter for Unit
     * returns the number of units for ingredient
     * @return an integer reflecting the the number of units for the ingredient
     * */
    public int getUnit() {
        return unit;
    }

    /**
     * Setter for Unit
     * sets the number of units of the ingredient
     * @param unit an integer describing the number of units of the ingredient
     * */
    public void setUnit(int unit) {
        this.unit = unit;
    }

    /**
     * Getter for category
     * returns the category of the ingredient
     * @return a string, category of the ingredient
     * */
    public String getCategory() {
        return category;
    }

    /**
     * Setter for category
     * sets the category of the ingredient
     * @param category a string describing the category of the ingredient
     * */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Getter for the id
     * returns the id of the ingredient
     * @return a string id for the ingredient
     * */
    public String getId() {
        return id;
    }

    /**
     * Setter for the id
     * sets the id for the ingredient
     * @param id a string describing the id of the ingredient
     * */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Overriding the equals function to find equality between ingredients
     * @param o Object to equate with
     * */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return title.equals(that.title);
    }
}
