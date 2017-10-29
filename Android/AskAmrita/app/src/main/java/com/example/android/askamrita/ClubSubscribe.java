package com.example.android.askamrita;

import android.widget.Button;

/**
 * Created by krohith on 28/10/17.
 */

public class ClubSubscribe {
    String name;
    String b;
    public ClubSubscribe(String name,String b1){
        this.name = name;
        this.b = b1;
    }
    String getName(){
        return this.name;
    }
    String getB(){
        return this.b;
    }
}
