package edu.temple.mapchat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nmale_000 on 4/6/2018.
 */

public class MyAdapter extends ArrayAdapter {
    private MyAdapter adapter;
    private Context cont;

    public MyAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
        super(context, resource, objects);
        adapter = this;
        cont = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View v = super.getView(position, convertView, parent);
        final int pos = position;
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Partner partner = (Partner) adapter.getItem(pos);
                if(((KeyHolder)cont).getKey(partner.getUser()) != null){
                    Intent myIntent = new Intent(cont, ChatActivity.class);
                    myIntent.putExtra("user", partner.getUser());
                    myIntent.putExtra("key", ((KeyHolder)cont).getKey(partner.getUser()));
                    EditText editText = (EditText) ((Activity)cont).findViewById(R.id.myUsername);
                    myIntent.putExtra("from", editText.getText().toString());
                    cont.startActivity(myIntent);
                }else{
                    Toast.makeText(cont, "You must retrieve this user's public key before chatting", Toast.LENGTH_LONG).show();
                }
            }
        });
        return v;
    }

    public interface KeyHolder{
        public String getKey(String user);
    }
}
