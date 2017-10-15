package com.example.android.askamrita;

/**
 * Created by krohith on 14/10/17.
 */

public class Post {
    String clubname;
    String content;
    public Post(String clubname,String content){
        this.clubname=clubname;
        this.content=content;
    }
    String getClubname(){
        return this.clubname;
    }
    String getContent(){
        return this.content;
    }
}
