package com.azcltd.android.test.babenko.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by Roman Babenko (r.babenko@amber.software) on 5/16/2017.
 */

public class AlertDialogFragment extends DialogFragment {
    public static final String DIALOG_MESSAGE_KEY = "com.azcltd.android.test.babenko.fragments.AlertDialogFragment.DIALOG_MESSAGE_KEY";
    public static final String DIALOG_TITLE_KEY = "com.azcltd.android.test.babenko.fragments.AlertDialogFragment.DIALOG_TITLE_KEY";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        String message = arguments.getString(DIALOG_MESSAGE_KEY);
        String title = arguments.getString(DIALOG_TITLE_KEY);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title)
                .setMessage(message);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();
    }
}
