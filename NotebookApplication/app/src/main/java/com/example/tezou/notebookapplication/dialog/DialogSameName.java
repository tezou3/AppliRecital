package com.example.tezou.notebookapplication.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

/**
 * Created by tezou on 2018/02/04.
 */

public class DialogSameName extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(android.R.string.dialog_alert_title)
                .setMessage("すでに登録されている名前です")
                .create();
        return dialog;
    }
}
