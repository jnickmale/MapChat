package edu.temple.mapchat;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.widget.EditText;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class BeamTriggeredMessageReceiver extends AppCompatActivity {
    PublicKey pubKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            processIntent(getIntent());
        }

    }

    @Override
    public void onNewIntent(Intent intent) {
        // onResume gets called after this to handle the intent
        setIntent(intent);
    }

    /**
     * Parses the NDEF Message from the intent and prints to the TextView
     */
    void processIntent(Intent intent) {
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
                NfcAdapter.EXTRA_NDEF_MESSAGES);

        if(rawMsgs[0].toString().equals("AndroidBeamEncryption/vnd.com.android.beam/key")) //is a key//rawMsgs[0] should be the mime type
         {
            // only one message sent during the beam
            NdefMessage msg = (NdefMessage) rawMsgs[0];
            // record 0 contains the MIME type, record 1 is the AAR, if present
            String keyString = new String(msg.getRecords()[0].getPayload());
             try {
                 setPemPublicKey(keyString, "RSA");
             } catch (Exception e) {
                 e.printStackTrace();
             }
             //pubKey = ;
        }
    }



    public void setPemPublicKey(String keyStringPEM, String algorithm) throws Exception {

        byte [] decoded = Base64.decode(keyStringPEM, Base64.DEFAULT);

        String temp = new String(decoded);
        String publicKeyString = temp.replace("-----BEGIN PUBLIC KEY-----\n", "");
        publicKeyString = publicKeyString.replace("-----END PUBLIC KEY-----", "");




        X509EncodedKeySpec spec =
                new X509EncodedKeySpec(publicKeyString.getBytes());
        KeyFactory kf = KeyFactory.getInstance(algorithm);
        pubKey = kf.generatePublic(spec);
    }
}
