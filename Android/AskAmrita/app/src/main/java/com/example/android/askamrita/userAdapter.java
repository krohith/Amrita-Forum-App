package com.example.android.askamrita;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by krohith on 29/10/17.
 */

public class userAdapter extends ArrayAdapter<userpost> {
public userAdapter(Activity context, ArrayList<userpost> clubs) {
        super(context,0,clubs);
        }
public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
        listItemView = LayoutInflater.from(getContext()).inflate(
        R.layout.post_view, parent, false);
        }
        userpost currentitem = getItem(position);
        TextView name = (TextView) listItemView.findViewById(R.id.name);

        name.setText(currentitem.getName());
        TextView content = (TextView) listItemView.findViewById(R.id.content);
        content.setText(currentitem.getContent());
        return listItemView;
        }

}
