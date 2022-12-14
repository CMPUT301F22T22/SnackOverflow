package com.example.snackoverflow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
/**
 * MealPlannerAdapter to view the meals in Recycler view
 * @see Mealday
 * @see MealdayAdapter
 * */
 public class MealPlannerAdapter extends RecyclerView.Adapter<MealPlannerAdapter.ViewHolder> {

    private Context context;
    private Mealday mealDay;
    private ArrayList<Recipe> meals;
    private ArrayList<Double> servings;
    private FragmentManager fm;

     /**
      * Constuctor for MealPlannerAdapter
      * @param mealDay The day of the week and the included recipes
      * @param context
      * @param fm Fragment Manager
      * @see Context
      * @see FragmentManager
      * */
    public MealPlannerAdapter(Context context, Mealday mealDay, FragmentManager fm) {
        this.context = context;
        this.mealDay = mealDay;
        this.meals = mealDay.getMeals();
        this.fm = fm;
        this.servings = mealDay.getServings();
    }

    @Override
    public MealPlannerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View cardView = layoutInflater.inflate(R.layout.mealplanner_child,null,false);
        ViewHolder viewHolder = new ViewHolder(cardView);
        viewHolder.mealImage = (ImageView) cardView.findViewById(R.id.meal_image);
        viewHolder.category = (TextView) cardView.findViewById(R.id.meal_category);
        viewHolder.title = (TextView) cardView.findViewById(R.id.meal_title);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull MealPlannerAdapter.ViewHolder holder, int position) {
        ImageView mealImageView = (ImageView) holder.mealImage;
        mealImageView.setImageBitmap(meals.get(position).getImageBitmap());
        TextView categoryTextView = (TextView) holder.category;
        categoryTextView.setText(meals.get(position).getRecipeCategory());

        TextView titleTextView = (TextView) holder.title;
        titleTextView.setText(meals.get(position).getTitle());

        mealImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MealPlannerAddMealFragment(mealDay, meals.get(position), (double)servings.get(position)).show(fm,"Edit_meal");
            }
        });

    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView mealImage;
        TextView title;
        TextView category;

        public ViewHolder(View itemView){
            super(itemView);
            mealImage = (ImageView) itemView.findViewById(R.id.meal_image);
            title = (TextView) itemView.findViewById(R.id.meal_title);
            category = (TextView) itemView.findViewById(R.id.meal_category);
        }
    }
}