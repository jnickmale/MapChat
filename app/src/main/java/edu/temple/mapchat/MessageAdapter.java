package edu.temple.mapchat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by nmale_000 on 4/6/2018.
 */

public class MessageAdapter extends ArrayAdapter {
    private MessageAdapter adapter;
    private Context cont;

    public MessageAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
        super(context, resource, objects);
        adapter = this;
        cont = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LinearLayoutCompat llh = new LinearLayoutCompat(getContext());
        llh.setOrientation(LinearLayoutCompat.HORIZONTAL);
        TextView t1, t2;
        t1 = new TextView(getContext());
        t1.setText(((MyMessage)getItem(position)).getFrom());
        t2 = new TextView(getContext());
        t2.setText(((MyMessage)getItem(position)).getMessage());
        llh.addView(t1, 0);
        llh.addView(t2, 1);

        return llh;
    }
}
