package com.example.android.askamrita;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by krohith on 28/10/17.
 */

public class ClubAdap extends ArrayAdapter<ClubSubscribe> {
    public ClubAdap(Activity context, ArrayList<ClubSubscribe> clubs) {
        super(context,0,clubs);
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.club_view, parent, false);
        }
        ClubSubscribe currentitem = getItem(position);
        TextView name = (TextView) listItemView.findViewById(R.id.cname);

        name.setText(currentitem.getName());
        Button b = (Button) listItemView.findViewById(R.id.b1);
        b.setText(currentitem.getB());

        return listItemView;
    }
}
