package com.example.snackoverflow;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
/**
 * Delete Conformation pops up when a user wants to add an
 * delete an Object
 * Extends DialogFragment
 * */
public class DeleteConfirmationFragment<T> extends DialogFragment {
    private T object;
    private String title;
    // Listener for activity
    private OnFragmentInteractionListener listener;

    /**
     * Constructor for Delete confirmation fragment
     * @param object object to be deleted
     * @param title title of the object
     */
    public DeleteConfirmationFragment(T object, String title){
        this.object = object;
        this.title = title;
    }
    /**
     * Implements the Fragment Interaction Listener and defines
     * the deleteObject function
     * */
    public interface OnFragmentInteractionListener {
        void deleteObject(Object object);
    }
    @Override
    public  void onAttach(Context context){
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener){
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //Inflate the layout
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.delete_confirmation_fragment, null);
        TextView objectTitle = view.findViewById(R.id.textView_object);
        objectTitle.setText(title);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Delete Confirmation")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.deleteObject(object);
                    }
                }).create();
    }
}
