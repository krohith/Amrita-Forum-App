package com.example.android.askamrita;

import android.widget.Adapter;

/**
 * Created by krohith on 29/10/17.
 */

public class userpost {
    String content;
    String name;
    public userpost(String content,String name){
        this.name=name;
        this.content=content;
    }
    public String getContent(){
        return this.content;
    }
    public String getName(){
        return this.name;
    }
}
