package com.example.android.askamrita;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class activity_login extends AppCompatActivity {
    Button login;
    EditText rollNo,passKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        rollNo=(EditText)findViewById(R.id.rollNo);
        passKey=(EditText)findViewById(R.id.passKey);
        login=(Button)findViewById(R.id.login_button);
        login();
    }


    public void login(){

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String roll[]=new String[3];
                roll[0]="cb.en.u4cse16046";
                roll[1]="cb.en.u4cse16048";
                roll[2]="cb.en.u4cse16053";
                String pass[]=new String[3];
                pass[0]="askamrita";
                pass[1]="askamrita";
                pass[2]="askamrita";
                String r=rollNo.getText().toString();
                String p=passKey.getText().toString();
                switch(r){
                    case "cb.en.u4cse16046":if(p.compareTo("askamrita")==0){
                        Intent i = new Intent(activity_login.this, activity_home.class);
                        startActivity(i);

                    }break;
                    case "cb.en.u4cse16048":if(p.compareTo("askamrita")==0){
                        Intent i = new Intent(activity_login.this, activity_home.class);
                        startActivity(i);

                    }break;
                    case "cb.en.u4cse16053":if(p.compareTo("askamrita")==0){
                        Intent i = new Intent(activity_login.this, activity_home.class);
                        startActivity(i);

                    }break;
                    default:

                        Toast.makeText(getApplicationContext(),"Wrong Roll no or password",Toast.LENGTH_LONG).show();
                }


            }
        });
    }



    }


