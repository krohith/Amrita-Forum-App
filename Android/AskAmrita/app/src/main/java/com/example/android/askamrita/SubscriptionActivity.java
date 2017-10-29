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
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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

public class SubscriptionActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle mToggle;
    public static final String LOG_TAG = activity_login.class.getSimpleName();
    String jav;
    String cname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);
        jav = activity_login.rollNo.getText().toString().toUpperCase();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mDrawerlayout = (DrawerLayout) findViewById(R.id.sidebar5);
        mToggle = new ActionBarDrawerToggle(SubscriptionActivity.this, mDrawerlayout, R.string.open, R.string.close);
        mDrawerlayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        subscribeAsyncTask task = new subscribeAsyncTask();
        task.execute();

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
            Intent i = new Intent(SubscriptionActivity.this, activity_home.class);
            startActivity(i);
        } else if (id == R.id.forum) {
            Intent i = new Intent(SubscriptionActivity.this, ForumActivity.class);
            startActivity(i);

        } else if (id == R.id.latest) {
            Intent i = new Intent(SubscriptionActivity.this, LatestActivity.class);
            startActivity(i);

        } else if (id == R.id.subscriptions) {
            Intent i = new Intent(SubscriptionActivity.this, SubscriptionActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.sidebar5);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void updateUi(ArrayList<ClubSubscribe> lis){
        ClubAdap Clu = new ClubAdap(this,lis);
        ListView listView = (ListView)findViewById(R.id.LV);
        listView.setAdapter(Clu);
    }
    public void updateUi2(){
        Intent i = new Intent(SubscriptionActivity.this,SubscriptionActivity.class);
        startActivity(i);
    }
    public void myClickHandler(View v){
        LinearLayout vw = (LinearLayout)v.getParent();
        TextView name = (TextView)vw.getChildAt(0);
        cname = name.getText().toString();
        clickAsyncTask c = new clickAsyncTask();
        c.execute();
    }
    public class subscribeAsyncTask extends AsyncTask<URL,Void,String>{
        protected String doInBackground(URL... urls){
            String sie = "http://amrita-forum-app.herokuapp.com/subscribe/"+jav;
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
            ArrayList<ClubSubscribe> lis = new ArrayList<ClubSubscribe>();

            try {
                JSONObject obj = new JSONObject(json);
                JSONArray arr = obj.getJSONArray("clubs");
                JSONArray arr1 = obj.getJSONArray("values");
                for(int i=0;i<arr.length();i++){
                    JSONObject o = arr.getJSONObject(i);
                    String name = o.getString("name");
                    JSONObject o1 = arr1.getJSONObject(i);
                    int val = o1.getInt("value");
                    String b;
                    if(val==1){
                        b = "Unsubscribe";
                    }
                    else{
                        b="Subscribe";
                    }
                    ClubSubscribe c = new ClubSubscribe(name,b);
                    lis.add(c);
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
    public class clickAsyncTask extends AsyncTask<URL,Void,String>{
        protected String doInBackground(URL... urls){
            String sit = "http://amrita-forum-app.herokuapp.com/change/"+jav+"/"+cname;
            URL ur = createUrl(sit);

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
            updateUi2();
        }

    }


}


