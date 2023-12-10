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
        void onConfirmDelete(String confirmationType);
    }
    
    private DeleteDialogListener deleteDialogListener;
    private String dialogMessage;
    private String confirmationType;

    public DeleteConfirmationDialog(String dialogMessage, String confirmationType) {
        this.dialogMessage = dialogMessage;
        this.confirmationType = confirmationType;
    }

    public void setDeleteDialogListener(DeleteDialogListener deleteDialogListener) {
        this.deleteDialogListener = deleteDialogListener;
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setMessage(dialogMessage)
                .setPositiveButton("Delete", (dialog1, which) -> {
                    deleteDialogListener.onConfirmDelete(confirmationType);
                })
                .setNegativeButton("Cancel", (dialog12, which) -> {
                    dialog12.cancel();
                });
        return dialog.create();
    }
}
