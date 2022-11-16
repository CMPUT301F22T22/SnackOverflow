package com.example.snackoverflow;

import java.util.Comparator;

public class SortComparator {
    public static class TitleComparator implements Comparator<Recipe> {


        @Override
        public int compare(Recipe r1, Recipe r2) {
            return r1.getTitle().compareTo(r2.getTitle());
        }

        @Override
        public Comparator<Recipe> reversed() {
            return Comparator.super.reversed();
        }
    }

    public static class CategoryComparator implements Comparator<Recipe> {


        @Override
        public int compare(Recipe r1, Recipe r2) {
            return r1.getRecipeCategory().compareTo(r2.getRecipeCategory());
        }

        @Override
        public Comparator<Recipe> reversed() {
            return Comparator.super.reversed();
        }
    }

    public static class PrepTimeComparator implements Comparator<Recipe> {


        @Override
        public int compare(Recipe r1, Recipe r2) {
            return Integer.compare(r1.getPreptime(), r2.getPreptime());
        }

        @Override
        public Comparator<Recipe> reversed() {
            return Comparator.super.reversed();
        }
    }

    public static class ServingsComparator implements Comparator<Recipe> {


        @Override
        public int compare(Recipe r1, Recipe r2) {
            return Float.compare(r1.getServings(), r2.getServings());
        }

        @Override
        public Comparator<Recipe> reversed() {
            return Comparator.super.reversed();
        }
    }
}
