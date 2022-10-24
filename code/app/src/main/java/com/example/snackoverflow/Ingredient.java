package com.example.snackoverflow;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Ingredient implements Serializable {
    private String description;
    private Date bestBefore;
    private String location;
    private int amount;
    private int unit;
    private String category;

    public Ingredient(String description, String dt, String location, int amount, int unit, String category) {
        this.description = description;
        try {
            this.bestBefore = new SimpleDateFormat("dd/MM/yyyy").parse(dt);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        this.location = location;
        this.amount = amount;
        this.unit = unit;
        this.category = category;
    }

    // For Recipe
    public Ingredient(String description, Integer amount, Integer unit, String category) {
        this.description = description;
        this.amount = amount;
        this.unit = unit;
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getBestBefore() {
        return bestBefore;
    }

    public void setBestBefore(Date bestBefore) {
        this.bestBefore = bestBefore;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
