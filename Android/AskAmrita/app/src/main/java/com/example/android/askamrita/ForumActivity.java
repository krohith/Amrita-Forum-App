package com.example.android.askamrita;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ForumActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle mToggle;
    public static final String LOG_TAG = activity_login.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ForumActivity.this, newpost.class);
                startActivity(i);
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        mDrawerlayout = (DrawerLayout) findViewById(R.id.sidebar2);
        mToggle = new ActionBarDrawerToggle(ForumActivity.this, mDrawerlayout, R.string.open, R.string.close);
        mDrawerlayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ForumAsyncTask task = new ForumAsyncTask();
        task.execute();
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    void updateUi(ArrayList<userpost> u){
        userAdapter usr = new userAdapter(this,u);
        ListView lisv = (ListView) findViewById(R.id.forum);
        lisv.setAdapter(usr);
    }
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            Intent i = new Intent(ForumActivity.this, activity_home.class);
            startActivity(i);
        } else if (id == R.id.forum) {
            Intent i = new Intent(ForumActivity.this, ForumActivity.class);
            startActivity(i);

        } else if (id == R.id.latest) {
            Intent i = new Intent(ForumActivity.this, LatestActivity.class);
            startActivity(i);

        } else if (id == R.id.subscriptions) {
            Intent i = new Intent(ForumActivity.this, SubscriptionActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.sidebar2);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public class ForumAsyncTask extends AsyncTask<URL,Void,String>{
        protected String doInBackground(URL... urls){
            String sie = "http://amrita-forum-app.herokuapp.com/forum/"+activity_home.rol.toUpperCase();
            URL ur = createUrl(sie);

            String jsonResponse = "";
            try {
                try {
                    jsonResponse = makeHttpRequest(ur);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return jsonResponse;
        }
        @Override
        protected void onPostExecute(String json) {
            ArrayList<userpost> lis = new ArrayList<userpost>();

            try {
                JSONObject obj = new JSONObject(json);
                JSONArray arr = obj.getJSONArray("pos");
                JSONArray arr1 = obj.getJSONArray("user");
                for(int i=0;i<arr.length();i++){
                    JSONObject o = arr.getJSONObject(i);
                    String content = o.getString("content");
                    JSONObject o1 = arr1.getJSONObject(i);
                    String user = o1.getString("name");
                    userpost u = new userpost(content,user);
                    lis.add(u);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            updateUi(lis);

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
    private String makeHttpRequest(URL url) throws IOException, JSONException {
        String jsonresponse = "";
        HttpURLConnection urlconnection = null;
        urlconnection = (HttpURLConnection) url.openConnection();
        urlconnection.setRequestMethod("GET");
        urlconnection.setReadTimeout(10000 /* milliseconds */);
        urlconnection.setConnectTimeout(15000 /* milliseconds */);
        urlconnection.connect();
        int responsecode = urlconnection.getResponseCode();
        if (responsecode == HttpURLConnection.HTTP_OK) { //success
            BufferedReader in = new BufferedReader(new InputStreamReader(urlconnection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            jsonresponse = response.toString();
            // print result
            Log.v(LOG_TAG, response.toString());
        }
        else{
            Log.e(LOG_TAG,"Error with GET request");
        }
        if(urlconnection!=null)
            urlconnection.disconnect();
        return jsonresponse;
    }
}
