package com.example.finalproj_03_nfc_loginpasswordstorage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproj_03_nfc_loginpasswordstorage.adapters.CustomCredentialsAdapter;
import com.example.finalproj_03_nfc_loginpasswordstorage.adapters.DialogAddNewCredential;
import com.example.finalproj_03_nfc_loginpasswordstorage.credential.CredentialObject;
import com.example.finalproj_03_nfc_loginpasswordstorage.database.DBHelper;

import java.io.IOException;
import java.util.List;

public class LoggedInView extends AppCompatActivity {

    private String userName;
    private String password;
    private TextView userNameText;
    private TextView passwordText;
    private Button logoutButton;
    private Button addNewButton;
    private RecyclerView credentialsList;
    //lifted list for THIS user
    private Tag nfcTag;
    private NFCManager nfcManager = new NFCManager(this);
    private NfcAdapter nfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in_view);

        nfcAdapter = nfcManager.verifyNFC();

        Bundle incomingBundle = getIntent().getExtras();
        userName = incomingBundle.getString("USERNAME");
        password = incomingBundle.getString("PASSWORD");

        userNameText = findViewById(R.id.logged_view_username);
        passwordText = findViewById(R.id.logged_view_password);
        logoutButton = findViewById(R.id.logged_view_logout_button);
        addNewButton = findViewById(R.id.logged_view_add_new_button);
        credentialsList = findViewById(R.id.logged_view_recycler);

        userNameText.setText(userName);
        passwordText.setText(password);

        refreshCredentialObjects();

        logoutButton.setOnClickListener(view -> {
            finish();
        });
        addNewButton.setOnClickListener(view -> {
            DialogAddNewCredential addNewDialog = new DialogAddNewCredential(this);
            addNewDialog.show(getSupportFragmentManager(), "add new credential dialog");
        });


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
            nfcTag = intent.getParcelableExtra(nfcAdapter.EXTRA_TAG);
            }
    }

    private void refreshCredentialObjects(){
        DBHelper dbHelper = new DBHelper(this);
        List<CredentialObject> credentialObjects = dbHelper.getCredentialsByUserPass(userName, password);
        CustomCredentialsAdapter credentialsAdapter = new CustomCredentialsAdapter(this, credentialObjects);
        credentialsList.setAdapter(credentialsAdapter);
        credentialsList.setLayoutManager(new LinearLayoutManager(this));
        //may need layout manager
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void addCredentialObject(CredentialObject newCredentialObject){
        DBHelper dbHelper = new DBHelper(this);
        dbHelper.addCredentialObject(newCredentialObject);
        refreshCredentialObjects();
    }

    public static final String INTENT_ACTION_WRITE_NFC = "intent.write.nfc.lab03";
    public static final String EXTRA_USERNAME = "extra.username.nfc_content";
    public static final String EXTRA_PASSWORD = "extra.password.nfc_content";

    class WriteNFCBroadcastReciever extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String incomingUserName = intent.getStringExtra(EXTRA_USERNAME) ;
            String incomingPassword = intent.getStringExtra(EXTRA_PASSWORD) ;
            nfcManager.writeNFCtag(incomingUserName, incomingPassword, nfcTag);
        }
    }

    private WriteNFCBroadcastReciever writeNFCBroadcastReciever = new WriteNFCBroadcastReciever();
    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = new Intent(this, getClass())
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                getApplicationContext(), 0, intent, 0);


        IntentFilter ndefFilter = new IntentFilter(
                NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndefFilter.addDataType("text/plain");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            e.printStackTrace();
        }
        IntentFilter techFilter = new IntentFilter(
                NfcAdapter.ACTION_TECH_DISCOVERED);
        IntentFilter[] filters = new IntentFilter[] { ndefFilter, techFilter };


        String[][] techLists = new String[][] {
                new String[] { Ndef.class.getName() },
                new String[] { MifareUltralight.class.getName() },
                new String[] { NfcA.class.getName() } };

        nfcAdapter.enableForegroundDispatch(this, pendingIntent, null,
                techLists);

        IntentFilter writeBroadcastFilter = new IntentFilter();
        writeBroadcastFilter.addAction(INTENT_ACTION_WRITE_NFC);
        registerReceiver(writeNFCBroadcastReciever, writeBroadcastFilter);

    }
    @Override
    protected void onPause() {
        super.onPause();
        nfcAdapter.disableForegroundDispatch(this);
        unregisterReceiver(writeNFCBroadcastReciever);
    }
}