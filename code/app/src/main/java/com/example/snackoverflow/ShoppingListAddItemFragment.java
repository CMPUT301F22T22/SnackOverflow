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

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/**
 * Inflates a user friendly interface to add an item to the shopping list. The interface contains
 * fields for Description, Category, Unit, and Amount of the Ingredient. The other attributes
 * of this Ingredient instance are initialized to null.
 */
public class ShoppingListAddItemFragment extends DialogFragment {
    private EditText shoppingDesc;
    private EditText shoppingCategory;
    private EditText shoppingAmount;
    private EditText shoppingUnit;
    private OnFragmentInteractionListener listener;
    private boolean isNull = false;

    @Override
    public void onStart() {
        super.onStart();
    }

    /**
     * Handles the fragment-to-activity communication.
     */
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

    private void setErrorMessage(EditText edt, String errorMessage) {
        edt.setError(errorMessage);
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
                        isNull = false;
                        String shoppingDescEntry = shoppingDesc.getText().toString();
                        String shoppingCategoryEntry = shoppingCategory.getText().toString();
                        String tempShoppingAmount = shoppingAmount.getText().toString();
                        String tempShoppingUnit = shoppingUnit.getText().toString();
                        Integer shoppingAmountEntry = 0;
                        Integer shoppingUnitEntry = 0;
                        if (shoppingDescEntry.length() == 0) {
                            isNull = true;
                            setErrorMessage(shoppingDesc, "Required");
                        }
                        if (shoppingCategoryEntry.length() == 0) {
                            isNull = true;
                            shoppingCategory.setError("Required");
                        }
                        if (tempShoppingAmount.length() == 0) {
                            isNull = true;
                            shoppingAmount.setError("Required");
                        }
                        else {
                            shoppingAmountEntry = Integer.parseInt(shoppingAmount.getText().toString());
                        }
                        if (tempShoppingUnit.length() == 0) {
                            isNull = true;
                            shoppingUnit.setError("Required");
                        }
                        else {
                            shoppingUnitEntry = Integer.parseInt(shoppingUnit.getText().toString());
                        }
                        if (shoppingDescEntry.length() == 0) {
                            isNull = true;
                            shoppingDesc.setError("Required");
                        }
                        if (!isNull) {
                            listener.onOkPressed(new Ingredient(shoppingDescEntry, shoppingAmountEntry, shoppingUnitEntry, shoppingCategoryEntry));
                        }
                    }
                }).create();
    }
}
