package com.location.demo.permission;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.location.demo.R;

public class PermissionDialog extends DialogFragment implements DialogInterface.OnClickListener {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.permission_tip);
        builder.setMessage(R.string.permission_msg);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.permission_btn_setting, this);
        builder.setNegativeButton(R.string.permission_btn_quit, this);
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        dismiss();
        if (getActivity() != null) {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                //setting detail intent
                final Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            } else {
                getActivity().finish();
            }
        }
    }
}
