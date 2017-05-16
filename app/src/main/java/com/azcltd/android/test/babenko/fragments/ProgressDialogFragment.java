package com.azcltd.android.test.babenko.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.ContextThemeWrapper;
import android.view.Window;
import android.widget.ProgressBar;

import com.azcltd.android.test.babenko.R;


/**
 * Created by Roman Babenko on 16/05/17.
 */

public class ProgressDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Context dialogContext = new ContextThemeWrapper(getContext(), R.style.AppTheme);
        Dialog dialog = new Dialog(dialogContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.progress_dialog);
        ProgressBar progressBar = (ProgressBar) dialog.findViewById(R.id.progress_bar);
        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getContext(),
                R.color.progressDialogColor), android.graphics.PorterDuff.Mode.MULTIPLY);
        setCancelable(false);
        return dialog;
    }
}
