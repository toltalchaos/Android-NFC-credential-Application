package com.example.finalproj_03_nfc_loginpasswordstorage.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproj_03_nfc_loginpasswordstorage.LoggedInView;
import com.example.finalproj_03_nfc_loginpasswordstorage.R;
import com.example.finalproj_03_nfc_loginpasswordstorage.credential.CredentialObject;

import java.util.List;

public class CustomCredentialsAdapter extends RecyclerView.Adapter<CustomCredentialsAdapter.CredentialViewHolder> {
    private Context context;
    private List<CredentialObject> credentialObjects;

    public CustomCredentialsAdapter(Context context, List<CredentialObject> credentialObjects) {
        this.context = context;
        this.credentialObjects = credentialObjects;
    }

    @NonNull
    @Override
    public CredentialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View credentialView = inflater.inflate(R.layout.custom_user_pass_item, parent, false);
        CredentialViewHolder viewHolder = new CredentialViewHolder(credentialView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CredentialViewHolder holder, int position) {
        CredentialObject currentCredentialObject = credentialObjects.get(position);
        holder.serviceName.setText(currentCredentialObject.getServiceName());
        holder.userName.setText(currentCredentialObject.getServiceUserName());
        holder.password.setText(currentCredentialObject.getServicePassword());
    }

    @Override
    public int getItemCount() {
        return credentialObjects.size();
    }

    public class CredentialViewHolder extends RecyclerView.ViewHolder{
        //items in custom_user_pass_item
        TextView serviceName;
        TextView userName;
        TextView password;
        Button writeToNFCButton;

        public CredentialViewHolder(@NonNull View itemView) {
            super(itemView);

            serviceName = itemView.findViewById(R.id.custom_item_service_name);
            userName = itemView.findViewById(R.id.custom_item_user_info);
            password = itemView.findViewById(R.id.custom_item_password_info);
            writeToNFCButton = itemView.findViewById(R.id.custom_item_write_nfc_button);

            writeToNFCButton.setOnClickListener(view -> {
                //create broadcast with current item stuff to write to NFC
                Intent writeToNFCIntent = new Intent();
                writeToNFCIntent.setAction(LoggedInView.INTENT_ACTION_WRITE_NFC);
                writeToNFCIntent.putExtra(LoggedInView.EXTRA_USERNAME, userName.getText().toString());
                writeToNFCIntent.putExtra(LoggedInView.EXTRA_PASSWORD, password.getText().toString());
                itemView.getContext().sendBroadcast(writeToNFCIntent);
            });
        }
    }
}
