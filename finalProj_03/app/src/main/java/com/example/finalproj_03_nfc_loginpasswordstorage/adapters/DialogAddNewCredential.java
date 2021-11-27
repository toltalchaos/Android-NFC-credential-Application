package com.example.finalproj_03_nfc_loginpasswordstorage.adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.finalproj_03_nfc_loginpasswordstorage.LoggedInView;
import com.example.finalproj_03_nfc_loginpasswordstorage.R;
import com.example.finalproj_03_nfc_loginpasswordstorage.credential.CredentialObject;

public class DialogAddNewCredential extends DialogFragment {
    private LoggedInView loggedInView;

    public DialogAddNewCredential(LoggedInView loggedInView) {
        this.loggedInView = loggedInView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_service, null);

        //name
        EditText serviceName = dialogView.findViewById(R.id.dialog_add_service_name_input);
        //username
        EditText serviceUserName = dialogView.findViewById(R.id.dialog_add_service_user_input);
        //password
        EditText servicePassword = dialogView.findViewById(R.id.dialog_add_service_password_input);

        String userName = loggedInView.getUserName();
        String password = loggedInView.getPassword();

        Button confirmButton = dialogView.findViewById(R.id.dialog_add_confirm_button);
        Button cancelButton = dialogView.findViewById(R.id.dialog_add_cancel_button);
        //button save
        confirmButton.setOnClickListener(view -> {
            if (serviceName.getText().toString().isEmpty() ^ serviceUserName.getText().toString().isEmpty() ^ servicePassword.getText().toString().isEmpty()){
                Toast.makeText(loggedInView, "PLEASE FILL IN ALL FIELDS", Toast.LENGTH_SHORT).show();
                dismiss();
            }
            else {
                CredentialObject newCredentialObject = new CredentialObject(
                        userName,
                        password,
                        serviceName.getText().toString(),
                        serviceUserName.getText().toString(),
                        servicePassword.getText().toString()
                );
                loggedInView.addCredentialObject(newCredentialObject);
                dismiss();
            }

        });
        // button cancel
        cancelButton.setOnClickListener(view -> {
            dismiss();
        });

        builder.setView(dialogView).setTitle("NEW credentials");
        return builder.create();
    }
}
