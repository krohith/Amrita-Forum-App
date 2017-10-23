package com.example.android.askamrita;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by krohith on 14/10/17.
 */

public class PostAdapter extends ArrayAdapter<Post>{
    public PostAdapter(Activity context, ArrayList<Post> posts) {
        super(context,0,posts);

    }
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_view, parent, false);
        }
        Post currentitem = getItem(position);
        TextView name = (TextView) listItemView.findViewById(R.id.clubname);

        name.setText(currentitem.getClubname());
        TextView con = (TextView) listItemView.findViewById(R.id.content);
        con.setText(currentitem.getContent());
        TextView uname = (TextView) listItemView.findViewById(R.id.uname);
        uname.setText(currentitem.getUsername());
        return listItemView;

    }
}
