package com.example.tezou.notebookapplication.dialog;

import android.app.DialogFragment;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

/**
 * Created by tezou on 2018/02/05.
 */

public class DialogNotFound extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(android.R.string.dialog_alert_title)
                .setMessage("見つかりませんでした")
                .create();
        return dialog;
    }

}
