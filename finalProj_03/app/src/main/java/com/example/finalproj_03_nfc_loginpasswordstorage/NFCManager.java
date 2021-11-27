package com.example.finalproj_03_nfc_loginpasswordstorage;
import android.app.Activity;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.nio.charset.StandardCharsets;

public class NFCManager {
    private Activity activity;
    private NfcAdapter nfcAdpt;

    public NFCManager(Activity activity) {
        this.activity = activity;
    }

    public NfcAdapter verifyNFC() {

        nfcAdpt = NfcAdapter.getDefaultAdapter(activity);

        if (nfcAdpt == null){
            Toast.makeText(activity, "NFC not supported", Toast.LENGTH_LONG).show();}

        if (!nfcAdpt.isEnabled()){
            Toast.makeText(activity, "NFC not Enabled", Toast.LENGTH_LONG).show();}
        return nfcAdpt;

    }

    public void writeNFCtag(String userNamePayloadString, String passwordPayloadString, Tag nfcTag){
        if (nfcTag != null){
            NdefRecord[] records = new NdefRecord[2];
            records[0] = createNdefRecord(userNamePayloadString);
            records[1] = createNdefRecord(passwordPayloadString);
            NdefMessage newNdefMessage = new NdefMessage(records);
            writeNFCtag(newNdefMessage, nfcTag);
        }
        else {
            Toast.makeText(activity, "NO NFC TAG DETECTED", Toast.LENGTH_SHORT).show();
        }
        
    }
    public void writeNFCtag(NdefMessage ndefMessage, Tag nfcTag){

        try {
            Ndef ndef = Ndef.get(nfcTag);
            if (ndef != null){
                Toast.makeText(activity, "Attempting Write", Toast.LENGTH_SHORT).show();
                ndef.connect();
                ndef.writeNdefMessage(ndefMessage);
                ndef.close();
                Toast.makeText(activity, "finished Write", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Log.e("ERROR WRITING NFC", "writeNFCtag: " + e.getMessage());
            Toast.makeText(activity, "there was an error " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public NdefRecord createNdefRecord(String payloadString){
        //short TNF
        // byte[] type
        //byte[] id
        //byte[] payload
        NdefRecord newNdefRecord = new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, null, payloadString.getBytes());
        return newNdefRecord;
    }
}
