package com.example.snackoverflow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;

/**
 * ShoppingListAdapter returns a view to display Ingredient objects in a user friendly, concise manner.
 * It displays the description, category, unit, and amount.
 */
public class ShoppingListAdapter extends ArrayAdapter<Ingredient>{
    private ArrayList<Ingredient> shoppingList;
    private Context context;

    /**
     * Constructor for the adapter
     * @param context: Interface to global information about an application environment
     * @param list: List of ingredients to display
     */
    public ShoppingListAdapter(Context context, ArrayList<Ingredient> list){
        super(context, 0, list);
        this.shoppingList = list;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.shopping_list_item, parent,false);
        }
        Ingredient ingredient = shoppingList.get(position);

        TextView itemDescription = view.findViewById(R.id.shopping_list_item_description);
        itemDescription.setText(ingredient.getTitle());

        TextView itemAmount = view.findViewById(R.id.shopping_list_item_amount);
        itemAmount.setText(String.valueOf(ingredient.getAmount()));

        TextView itemUnit = view.findViewById(R.id.shopping_list_item_unit);
        itemUnit.setText(String.valueOf(ingredient.getUnit()));

        TextView itemCategory = view.findViewById(R.id.shopping_list_item_category);
        itemCategory.setText(ingredient.getCategory());

        CheckBox checkBox = view.findViewById(R.id.check_shopping_list_item);
        checkBox.setChecked(ingredient.getCheckedShoppingList());

        return view;
    }
}