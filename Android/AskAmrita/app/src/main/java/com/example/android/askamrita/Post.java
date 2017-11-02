package com.example.android.askamrita;

/**
 * Created by krohith on 14/10/17.
 */

public class Post {
    String clubname;
    String content;
    String username;
    String num;
    String id;
    String button;
    public Post(String clubname,String content,String username,String num,String id,String button){
        this.clubname=clubname;
        this.content=content;
        this.username=username;
        this.num = num;
        this.id =id;
        this.button=button;
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
    String getNum(){return this.num;}
    String getid(){return this.id;}
    String getButton(){return this.button;}
}
