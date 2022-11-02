package com.example.snackoverflow;

import java.io.Serializable;
import java.util.Date;

public class Ingredient implements Serializable  {
    private String title;
    private Date bestBefore;
    private String location;
    private int amount;
    private int unit;
    private String category;
    protected String id;

    public Ingredient(String description, Date date, String location, int amount, int unit, String category) {
        this.title = description;
        this.location = location;
        this.amount = amount;
        this.unit = unit;
        this.bestBefore = date;
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return title.equals(that.title);
    }
}
