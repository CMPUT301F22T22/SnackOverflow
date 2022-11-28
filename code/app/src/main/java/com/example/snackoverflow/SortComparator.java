package com.example.snackoverflow;

import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

/**
 * Sort Comparator for handling sorting for Recipes, Ingredients and Shopping List
 * @see Recipe
 * @see RecipeActivity
 * @see Ingredient
 * @see IngredientStorageActivity
 * @see ShoppingListActivity
 */
public class SortComparator {
    /**
     * Title Comparator for sorting by Title
     */
    public static class TitleComparator implements Comparator<Object> {
        /**
         * Comapres the titles of two objects
         * @param o1
         * @param o2
         * @return
         */
        @Override
        public int compare(Object o1, Object o2) {
            String objectType = o1.getClass().toString();
            if (objectType.substring(objectType.lastIndexOf('.')+1).equals("Recipe")) {
                return ((Recipe) o1).getTitle().toLowerCase().compareTo(((Recipe)o2).getTitle().toLowerCase());
            }
            if (objectType.substring(objectType.lastIndexOf('.')+1).equals("Ingredient")) {
                return ((Ingredient) o1).getTitle().toLowerCase().compareTo(((Ingredient)o2).getTitle().toLowerCase());
            }

            return -1;
        }

        /**
         * Compares the titles for reversed order
         * @return
         */
        @Override
        public Comparator<Object> reversed() {
            return Comparator.super.reversed();
        }
    }

    /**
     * Category comparator for sorting by Category
     */
    public static class CategoryComparator implements Comparator<Object> {

        /**
         * Comapring the object's category
         * @param o1
         * @param o2
         * @return
         */
        @Override
        public int compare(Object o1, Object o2) {
            String objectType = o1.getClass().toString();
            if (objectType.substring(objectType.lastIndexOf('.')+1).equals("Recipe")) {
                return ((Recipe) o1).getRecipeCategory().toLowerCase().compareTo(((Recipe)o2).getRecipeCategory().toLowerCase());
            }
            if (objectType.substring(objectType.lastIndexOf('.')+1).equals("Ingredient")) {
                return ((Ingredient) o1).getCategory().toLowerCase().compareTo(((Ingredient)o2).getCategory().toLowerCase());
            }
            return -1;
        }

        /**
         * Comparing the category for reversed sorting
         * @return
         */
        @Override
        public Comparator<Object> reversed() {
            return Comparator.super.reversed();
        }
    }

    /**
     * Prep Time Comparator for sorting by Prep Time
     */
    public static class PrepTimeComparator implements Comparator<Object> {

        /**
         * Comparing the objects for Prep Time
         * @param o1
         * @param o2
         * @return
         */
        @Override
        public int compare(Object o1, Object o2) {
            String objectType = o1.getClass().toString();
            if (objectType.substring(objectType.lastIndexOf('.')+1).equals("Recipe")) {
                return Integer.compare(((Recipe) o1).getPreptime(), ((Recipe) o2).getPreptime());
            }
            return -1;
        }

        /**
         * Comparing the object's Prep time for reversed sorting
         * @return
         */
        @Override
        public Comparator<Object> reversed() {
            return Comparator.super.reversed();
        }
    }

    /**
     * Servings comparator for sorting by Servings
     */
    public static class ServingsComparator implements Comparator<Object> {

        /**
         * Compares the servings
         * @param o1
         * @param o2
         * @return
         */
        @Override
        public int compare(Object o1, Object o2) {
            String objectType = o1.getClass().toString();
            if (objectType.substring(objectType.lastIndexOf('.')+1).equals("Recipe")) {
                return Float.compare(((Recipe) o1).getServings(), ((Recipe) o2).getServings());
            }
            return -1;
        }

        /**
         * Compares the servings for reversed sorting
         * @return
         */
        @Override
        public Comparator<Object> reversed() {
            return Comparator.super.reversed();
        }
    }

    /**
     * Best Before Comparator for Sorting by Best Before Date
     */
    public static class BestBeforeComparator implements Comparator<Object> {

        /**
         * Compares the object's best before date
         * @param o1
         * @param o2
         * @return
         */
        @Override
        public int compare(Object o1, Object o2) {
            String objectType = o1.getClass().toString();
            if (objectType.substring(objectType.lastIndexOf('.')+1).equals("Ingredient")) {
                return ((Ingredient) o1).getBestBefore().compareTo(((Ingredient)o2).getBestBefore());
            }
            return -1;
        }

        /**
         * Compares the Best Before Date for reversed sorting
         * @return
         */
        @Override
        public Comparator<Object> reversed() {
            return Comparator.super.reversed();
        }

    }

    /**
     * Location Comparator for Sorting by Location
     */
    public static class LocationComparator implements Comparator<Object> {

        /**
         * Compares the objects Location
         * @param o1
         * @param o2
         * @return
         */
        @Override
        public int compare(Object o1, Object o2) {
            String objectType = o1.getClass().toString();
            if (objectType.substring(objectType.lastIndexOf('.')+1).equals("Ingredient")) {
                return ((Ingredient) o1).getLocation().toLowerCase().compareTo(((Ingredient)o2).getLocation().toLowerCase());
            }
            return -1;
        }

        /**
         * Compares the object's Location for reversed sorting
         * @return
         */
        @Override
        public Comparator<Object> reversed() {
            return Comparator.super.reversed();
        }

    }

    /**
     * Handle sorting of ingredients
     * @param position position in the drop down list
     * @param currSortOrder current sorting order set
     * @param list the list of ingredients
     * @param adapter the ingredients adapter
     */
    public static void handleSortIngredientsBy(int position, String currSortOrder, ArrayList<Ingredient> list, ArrayAdapter<Ingredient> adapter) {
        switch (position) {
            case 0:
                if (currSortOrder.equals("dec")) {
                    Collections.sort(list, new SortComparator.TitleComparator().reversed());
                } else {
                    Collections.sort(list, new SortComparator.TitleComparator());
                }
                break;
            case 1:
                if (currSortOrder.equals("dec")) {
                    Collections.sort(list, new SortComparator.BestBeforeComparator().reversed());
                } else {
                    Collections.sort(list, new SortComparator.BestBeforeComparator());
                }
                break;
            case 2:
                if (currSortOrder.equals("dec")) {
                    Collections.sort(list, new SortComparator.LocationComparator().reversed());
                } else {
                    Collections.sort(list, new SortComparator.LocationComparator());
                }
                break;
            case 3:
                if (currSortOrder.equals("dec")) {
                    Collections.sort(list, new SortComparator.CategoryComparator().reversed());
                } else {
                    Collections.sort(list, new SortComparator.CategoryComparator());
                }
                break;
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * Handles sorting for shopping list
     * @param position position in the drop down list
     * @param currSortOrder current sorting order set
     * @param list the list of shopping list items
     * @param adapter the ingredients adapter
     */
    public static void handleSortShoppingListBy(int position, String currSortOrder, ArrayList<Ingredient> list, ArrayAdapter<Ingredient> adapter) {
        switch (position) {
            case 0:
                if (currSortOrder.equals("dec")) {
                    Collections.sort(list, new SortComparator.TitleComparator().reversed());
                } else {
                    Collections.sort(list, new SortComparator.TitleComparator());
                }
                break;
            case 1:
                if (currSortOrder.equals("dec")) {
                    Collections.sort(list, new SortComparator.CategoryComparator().reversed());
                } else {
                    Collections.sort(list, new SortComparator.CategoryComparator());
                }
                break;
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * Handles sorting for Recipes
     * @param position position in the drop down list
     * @param currSortOrder current sorting order set
     * @param list the list of recipes
     * @param adapter the recipe adapter
     */
    public static void handleSortRecipesBy(int position, String currSortOrder, ArrayList<Recipe> list, ArrayAdapter<Recipe> adapter) {
        switch (position) {
            case 0:
                if (currSortOrder.equals("dec")) {
                    Collections.sort(list, new SortComparator.TitleComparator().reversed());
                } else {
                    Collections.sort(list, new SortComparator.TitleComparator());
                }
                break;
            case 1:
                if (currSortOrder.equals("dec")) {
                    Collections.sort(list, new SortComparator.PrepTimeComparator().reversed());
                } else {
                    Collections.sort(list, new SortComparator.PrepTimeComparator());
                }
                break;
            case 2:
                if (currSortOrder.equals("dec")) {
                    Collections.sort(list, new SortComparator.ServingsComparator().reversed());
                } else {
                    Collections.sort(list, new SortComparator.ServingsComparator());
                }
                break;
            case 3:
                if (currSortOrder.equals("dec")) {
                    Collections.sort(list, new SortComparator.CategoryComparator().reversed());
                } else {
                    Collections.sort(list, new SortComparator.CategoryComparator());
                }
                break;
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * Handles the Sorting order for Ingredients, Recipes and Shopping List Items
     * @param position position in the drop down list
     * @param currSortOrder current sorting order set
     * @param list the list of recipes
     * @param adapter the recipe adapter
     * @param <T> the type of the object it is used for
     * @return the updated currSortOrder
     */
    public static <T> String handleSortOrder(int position, String currSortOrder, ArrayList<T> list, ArrayAdapter<T> adapter) {
        if (position == 1 && currSortOrder.equals("inc")) {
            Collections.reverse(list);
            currSortOrder = "dec";
        } else if (position == 0 && currSortOrder.equals("dec")) {
            Collections.reverse(list);
            currSortOrder = "inc";
        }
        adapter.notifyDataSetChanged();
        return currSortOrder;
    }

}
