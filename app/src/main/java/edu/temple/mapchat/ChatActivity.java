package edu.temple.mapchat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class ChatActivity extends AppCompatActivity {

    private String user, from;
    private RSAPublicKey key;
    private RSAPrivateKey myKey;
    private ListView messagesView;
    private EditText sendText;
    private ArrayList<MyMessage> messagesArray;
    private MessageAdapter messageAdapter;
    private String mes;
    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messagesArray = new ArrayList<MyMessage>();
        messageAdapter = new MessageAdapter(this, android.R.layout.simple_list_item_1, messagesArray);

        Intent intent = getIntent();
        user = intent.getStringExtra("user");
        key = parseKey(intent.getStringExtra("key"));
        from = intent.getStringExtra("from");
        messagesView = findViewById(R.id.messagesView);
        sendText = findViewById(R.id.sendText);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String decrypted = decryptMessage(intent.getStringExtra("message"));

                addMessage(intent.getStringExtra("from"), decrypted);
            }
        };
        this.registerReceiver(receiver, new IntentFilter("com.example.broadcast.AMessage"));

        findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mes = sendText.getText().toString();
                sendMessage(mes);
                addMessage(from, mes);
            }
        });
    }

    public void addMessage(String from, String mes){
        messagesArray.add(new MyMessage(from, mes));
        messageAdapter.notifyDataSetChanged();
    }

    public void sendMessage(String text){
        JSONObject sending = createMessage(text);
        text = encryptMessage(text);
        mes = text;
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                "https://kamorris.com/lab/send_message.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("OK")) {

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                EditText editText = (EditText) findViewById(R.id.myUsername);
                Map<String, String> params = new HashMap<String, String>();
                params.put("user", from);
                params.put("partneruser", user);
                params.put("message", mes);

                return params;
            }
        };
        // Add StringRequest to the RequestQueue
        requestQueue.add(stringRequest);
    }

    public String encryptMessage(String text){
        Cipher c = null;
        byte[] encryptedBytes;
        byte[] base64EncryptedBytes = null;
        try{
            c = Cipher.getInstance("RSA");
            c.init(Cipher.ENCRYPT_MODE, key);
            encryptedBytes = c.doFinal(text.getBytes(StandardCharsets.UTF_8));
            base64EncryptedBytes = Base64.encode(encryptedBytes, Base64.DEFAULT);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String decryptMessage(String text){
        byte[] decryptedBytes = null;
        Cipher c = null;
        try {
            c = Cipher.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }

        try {
            c.init(Cipher.DECRYPT_MODE, myKey);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }


        try {
            decryptedBytes = c.doFinal(Base64.decode(text.getBytes(), Base64.DEFAULT));
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        String retval = "";
        if(decryptedBytes != null){
            retval = new String(decryptedBytes);
        }
        return retval;
    }

    public JSONObject createMessage(String message){
        JSONObject retval = new JSONObject();
        try {
            retval.put("to", user);
            retval.put("from", from);
            retval.put("message", message);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return retval;
    }

    public RSAPublicKey parseKey(String strKey){
        //TODO parse the key from the string
        return null;
    }
}
