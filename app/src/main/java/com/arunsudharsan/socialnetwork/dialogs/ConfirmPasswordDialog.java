package com.arunsudharsan.socialnetwork.dialogs;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.arunsudharsan.socialnetwork.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by root on 14/12/17.
 */

public class ConfirmPasswordDialog extends android.support.v4.app.DialogFragment {


    public interface onConfirmPasswordListener {
        public void onConfirmPassword(String Password);
    }

    onConfirmPasswordListener onConfirmPasswordListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_confirm_password, container, false);
        TextView confirm, cancel;
        final EditText etPassword;
        confirm = v.findViewById(R.id.dialogconfirm);
        cancel = v.findViewById(R.id.dialogcancel);
        etPassword = v.findViewById(R.id.etpasswordreauth);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String passwrod = etPassword.getText().toString();
                if (!TextUtils.isEmpty(passwrod)) {
                    onConfirmPasswordListener.onConfirmPassword(passwrod);
                    getDialog().dismiss();
                } else {

                    Toast.makeText(getActivity(), "Enter a valid password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onConfirmPasswordListener = (ConfirmPasswordDialog.onConfirmPasswordListener) getTargetFragment();
    }
}
