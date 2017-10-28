package com.example.android.askamrita;

/**
 * Created by krohith on 14/10/17.
 */

public class Post {
    String clubname;
    String content;
    String username;
    public Post(String clubname,String content,String username){
        this.clubname=clubname;
        this.content=content;
        this.username=username;
    }
    String getClubname(){
        return this.clubname;
    }
    String getContent(){
        return this.content;
    }
    String getUsername(){
        return this.username;
    }
}
