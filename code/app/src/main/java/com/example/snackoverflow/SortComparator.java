package com.example.snackoverflow;

import java.util.Comparator;

public class SortComparator {
    public static class TitleComparator implements Comparator<Object> {
        @Override
        public int compare(Object o1, Object o2) {
            String objectType = o1.getClass().toString();
            if (objectType.substring(objectType.lastIndexOf('.')+1).equals("Recipe")) {
                return ((Recipe) o1).getTitle().compareTo(((Recipe)o2).getTitle());
            }
            if (objectType.substring(objectType.lastIndexOf('.')+1).equals("Ingredient")) {
                return ((Ingredient) o1).getTitle().compareTo(((Ingredient)o2).getTitle());
            }
            return -1;
        }

        @Override
        public Comparator<Object> reversed() {
            return Comparator.super.reversed();
        }
    }

    public static class CategoryComparator implements Comparator<Object> {


        @Override
        public int compare(Object o1, Object o2) {
            String objectType = o1.getClass().toString();
            if (objectType.substring(objectType.lastIndexOf('.')+1).equals("Recipe")) {
                return ((Recipe) o1).getRecipeCategory().compareTo(((Recipe)o2).getRecipeCategory());
            }
            if (objectType.substring(objectType.lastIndexOf('.')+1).equals("Ingredient")) {
                return ((Ingredient) o1).getCategory().compareTo(((Ingredient)o2).getCategory());
            }
            return -1;
        }

        @Override
        public Comparator<Object> reversed() {
            return Comparator.super.reversed();
        }
    }

    public static class PrepTimeComparator implements Comparator<Object> {


        @Override
        public int compare(Object o1, Object o2) {
            String objectType = o1.getClass().toString();
            if (objectType.substring(objectType.lastIndexOf('.')+1).equals("Recipe")) {
                return Integer.compare(((Recipe) o1).getPreptime(), ((Recipe) o2).getPreptime());
            }
            return -1;
        }

        @Override
        public Comparator<Object> reversed() {
            return Comparator.super.reversed();
        }
    }

    public static class ServingsComparator implements Comparator<Object> {


        @Override
        public int compare(Object o1, Object o2) {
            String objectType = o1.getClass().toString();
            if (objectType.substring(objectType.lastIndexOf('.')+1).equals("Recipe")) {
                return Float.compare(((Recipe) o1).getServings(), ((Recipe) o2).getServings());
            }
            return -1;
        }

        @Override
        public Comparator<Object> reversed() {
            return Comparator.super.reversed();
        }
    }

    public static class BestBeforeComparator implements Comparator<Object> {

        @Override
        public int compare(Object o1, Object o2) {
            String objectType = o1.getClass().toString();
            if (objectType.substring(objectType.lastIndexOf('.')+1).equals("Ingredient")) {
                return ((Ingredient) o1).getBestBefore().compareTo(((Ingredient)o2).getBestBefore());
            }
            return -1;
        }

        @Override
        public Comparator<Object> reversed() {
            return Comparator.super.reversed();
        }

    }

    public static class LocationComparator implements Comparator<Object> {

        @Override
        public int compare(Object o1, Object o2) {
            String objectType = o1.getClass().toString();
            if (objectType.substring(objectType.lastIndexOf('.')+1).equals("Ingredient")) {
                return ((Ingredient) o1).getLocation().compareTo(((Ingredient)o2).getLocation());
            }
            return -1;
        }

        @Override
        public Comparator<Object> reversed() {
            return Comparator.super.reversed();
        }

    }
}
