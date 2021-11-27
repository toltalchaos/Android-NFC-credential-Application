package com.example.finalproj_03_nfc_loginpasswordstorage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
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
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private EditText userName;
    private EditText password;
    private String userNamePayload;
    private String passwordPayload;
    private Tag nfcTag;
    private NFCManager nfcManager = new NFCManager(this);
    private NfcAdapter nfcAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nfcAdapter = nfcManager.verifyNFC();

        userName = findViewById(R.id.activity_main_user_name);
        password = findViewById(R.id.activity_main_password);


    }
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
    }
    @Override
    protected void onPause() {
        super.onPause();
        nfcAdapter.disableForegroundDispatch(this);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        //this is designed to use an NFC using only one payload, for multiple payloads convert string output to array value and add to the array
        super.onNewIntent(intent);
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {

            nfcTag = intent.getParcelableExtra(nfcAdapter.EXTRA_TAG);
            Ndef tagNdefObj = Ndef.get(nfcTag);

            try {
                tagNdefObj.connect();
                NdefMessage ndefMessage = tagNdefObj.getNdefMessage();
                if (ndefMessage != null){
                    NdefRecord ndefRecord = ndefMessage.getRecords()[0];
                    String payloadString = new String(ndefRecord.getPayload());
                    userNamePayload = payloadString;
                    ndefRecord = ndefMessage.getRecords()[1];
                    payloadString = new String(ndefRecord.getPayload());
                    Toast.makeText(this, payloadString, Toast.LENGTH_SHORT).show();
                    passwordPayload = payloadString;
                }
                tagNdefObj.close();
            }catch (Exception e){
                Toast.makeText(this, "ERROR Reading, may be empty card", Toast.LENGTH_SHORT).show();
                try {
                    tagNdefObj.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }


            userName.setText(userNamePayload);
            password.setText(passwordPayload);
        }


    }

    public void loginClick(View view) {
        //BUILD A NEW INTENT TO START THE LOGGED IN VIEW ACTIVITY
        //USE USERNAME AND PASSWORD AS EXTRAS FOR THE PAGE TO BE ABLE TO PULL THE CORRECT DATA FROM THE DB
        Intent loginIntent = new Intent(this, LoggedInView.class);
        Bundle credentialBundle = new Bundle();
        credentialBundle.putString("USERNAME", userName.getText().toString());
        credentialBundle.putString("PASSWORD", password.getText().toString());
        loginIntent.putExtras(credentialBundle);
        startActivity(loginIntent);

    }

    public void mainActivityWriteToNFC(View view) {
        nfcManager.writeNFCtag(userName.getText().toString(), password.getText().toString(), nfcTag);
    }
}