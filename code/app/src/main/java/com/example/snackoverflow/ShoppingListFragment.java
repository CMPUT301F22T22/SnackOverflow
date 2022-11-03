package com.example.snackoverflow;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ShoppingListFragment extends DialogFragment {
    private EditText shoppingDesc;
    private EditText shoppingCategory;
    private EditText shoppingAmount;
    private EditText shoppingUnit;
    private OnFragmentInteractionListener listener;

    @Override
    public void onStart() {
        super.onStart();
    }

    public interface OnFragmentInteractionListener {
        void onOkPressed(Ingredient selectedIngredient);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString());
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_shopping_item_fragment_layout, null);
        shoppingDesc = view.findViewById(R.id.shopping_item_description_editText);
        shoppingAmount = view.findViewById(R.id.shopping_item_amount_editText);
        shoppingCategory = view.findViewById(R.id.shopping_item_category_editText);
        shoppingUnit = view.findViewById(R.id.shopping_item_unit_editText);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        return builder
                .setView(view)
                .setTitle("Add Item to Shopping List")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String shoppingDescEntry = shoppingDesc.getText().toString();
                        String shoppingCategoryEntry = shoppingCategory.getText().toString();
                        Integer shoppingAmountEntry = Integer.parseInt(shoppingAmount.getText().toString());
                        Integer shoppingUnitEntry = Integer.parseInt(shoppingUnit.getText().toString());
                        listener.onOkPressed(new Ingredient(shoppingDescEntry, shoppingAmountEntry, shoppingUnitEntry, shoppingCategoryEntry));
                    }
                }).create();
    }
}
