package com.example.android.askamrita;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
/**
 * Created by krohith on 23/10/17.
 */

public class newpost extends AppCompatActivity implements AdapterView.OnItemSelectedListener ,NavigationView.OnNavigationItemSelectedListener {
    private Button post;
    private Spinner spinner;
    private String site;
    private EditText et;
    public String c;
    private DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle mToggle;
    private static String[] names = activity_login.names;
    public static final String LOG_TAG = newpost.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newpost);
        et = (EditText) findViewById(R.id.conten);
        spinner = (Spinner)findViewById(R.id.spin);
        post = (Button)findViewById(R.id.postbtn);
        ArrayAdapter<String> ada = new ArrayAdapter<String>(newpost.this,android.R.layout.simple_spinner_item,names);
        ada.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(ada);
        spinner.setOnItemSelectedListener(this);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mDrawerlayout = (DrawerLayout) findViewById(R.id.sidebar4);
        mToggle = new ActionBarDrawerToggle(newpost.this, mDrawerlayout, R.string.open, R.string.close);
        mDrawerlayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            Intent i = new Intent(newpost.this, activity_home.class);
            startActivity(i);
        } else if (id == R.id.forum) {
            Intent i = new Intent(newpost.this, ForumActivity.class);
            startActivity(i);

        } else if (id == R.id.latest) {
            Intent i = new Intent(newpost.this, LatestActivity.class);
            startActivity(i);

        } else if (id == R.id.subscriptions) {
            Intent i = new Intent(newpost.this, SubscriptionActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.sidebar4);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



        @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String cname =parent.getItemAtPosition(position).toString();
        site  = "http://amrita-forum-app.herokuapp.com/create/"+activity_login.rollNo.getText().toString().toUpperCase()+"/"+cname.toString();
            post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    newAsyncTask task = new newAsyncTask();
                    task.execute();
                }
            });
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private class newAsyncTask extends AsyncTask<URL, Void, Void> {

        protected Void doInBackground(URL... urls) {
            URL ur = createUrl(site);

            try {
                try {
                    makeHttpRequest(ur);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            Toast.makeText(getApplicationContext(),"Successfully Posted",Toast.LENGTH_LONG).show();
            Intent i = new Intent(newpost.this,activity_home.class);
            startActivity(i);
        }
    }
    private URL createUrl(String Stringurl) {
        URL ur = null;
        try{
            ur = new URL(Stringurl);
        }catch (MalformedURLException ex){
            Log.e(LOG_TAG, "Error with creating URL", ex);
            return null;
        }
        return ur;
    }
    private void makeHttpRequest(URL url) throws IOException, JSONException {
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type","application/json");
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.connect();
            JSONObject data = new JSONObject();
            c=et.getText().toString();
            data.put("content",c);
            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(data.toString());
            writer.close();
            os.close();
            int responsecode = urlConnection.getResponseCode();
            Log.v(LOG_TAG,"Response Code :: "+responsecode);
//                inputStream = urlConnection.getInputStream();
//            StringBuilder output = new StringBuilder();
//            if (inputStream != null) {
//                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
//                BufferedReader reader = new BufferedReader(inputStreamReader);
//                String line = reader.readLine();
//                while (line != null) {
//                    output.append(line);
//                    line = reader.readLine();
//                }
//

        } catch (IOException e) {
            // TODO: Handle the exception
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

}

