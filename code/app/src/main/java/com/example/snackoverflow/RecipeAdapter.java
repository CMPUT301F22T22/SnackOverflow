package com.example.snackoverflow;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class RecipeAdapter extends ArrayAdapter<Recipe> {
    private ArrayList<Recipe> cookbook;
    private Context context;

    public RecipeAdapter(Context context, ArrayList<Recipe> cookbook){
        super(context,0,cookbook);
        this.cookbook = cookbook;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view== null){
            view= LayoutInflater.from(context).inflate(R.layout.recipe_content,parent,false);
        }

        Recipe recipe = cookbook.get(position);
        TextView recipeTitle = view.findViewById(R.id.recipe_title);
        TextView recipePreptime = view.findViewById(R.id.recipe_preptime);
        TextView servings = view.findViewById(R.id.recipe_servings);
        TextView category = view.findViewById(R.id.recipe_category);
        ImageView photo = view.findViewById(R.id.photoImageView);

        recipeTitle.setText(recipe.getTitle());
        recipePreptime.setText(recipe.getPreptimeTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        servings.setText(Float.toString(recipe.getServings()));
        category.setText(recipe.getRecipeCategory());
        //TODO: photo.setImageResource(
        return view;
    }
}
