package com.example.flashcardmobile.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import org.jetbrains.annotations.NotNull;

public class DeleteConfirmationDialog extends DialogFragment {
    
    public interface DeleteDialogListener {
        void onConfirmDelete();
    }
    
    private DeleteDialogListener deleteDialogListener;
    
    public void setDeleteDialogListener(DeleteDialogListener deleteDialogListener) {
        this.deleteDialogListener = deleteDialogListener;
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setMessage("Do you really want to delete this deck?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("Delete operation");
                        deleteDialogListener.onConfirmDelete();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("Canceling");
                        dialog.cancel();
                    }
                });
        return dialog.create();
    }
}
