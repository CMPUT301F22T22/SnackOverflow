package com.example.snackoverflow;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ErrorFragment extends DialogFragment {
    private String errorMessage;

    public ErrorFragment(String errorMessage){
        this.errorMessage = errorMessage;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //Inflate the layout
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.error_fragment, null);
        TextView textViewError = view.findViewById(R.id.text_view_error);
        textViewError.setText(errorMessage);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Error")
                .setNeutralButton("Ok",null).create();
    }
}
