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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class activity_home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle mToggle;
    EditText roll;
    TextView headroll;
    TextView username;
    Button btn;
    public String rol;
    public static final String LOG_TAG = activity_login.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        roll = (EditText) findViewById(R.id.rollNo);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity_home.this, newpost.class);
                startActivity(i);
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        rol = activity_login.rollNo.getText().toString().toUpperCase();
        headroll = (TextView) header.findViewById(R.id.roll);
        headroll.setText(rol);
        username = (TextView) header.findViewById(R.id.nam);
        username.setText(activity_login.user);
        mDrawerlayout = (DrawerLayout) findViewById(R.id.sidebar);
        mToggle = new ActionBarDrawerToggle(activity_home.this, mDrawerlayout, R.string.open, R.string.close);
        mDrawerlayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        PostAsyncTask task = new PostAsyncTask();
        task.execute();
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
    @Override
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
            Intent i = new Intent(activity_home.this, activity_home.class);
            startActivity(i);
        } else if (id == R.id.forum) {
            Intent i = new Intent(activity_home.this, ForumActivity.class);
            startActivity(i);

        } else if (id == R.id.latest) {
            Intent i = new Intent(activity_home.this, LatestActivity.class);
            startActivity(i);

        } else if (id == R.id.subscriptions) {
            Intent i = new Intent(activity_home.this, SubscriptionActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.sidebar);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void updateUi(ArrayList<Post> post){
        PostAdapter newpos = new PostAdapter(this,post);
        ListView listView = (ListView)findViewById(R.id.list_item);
        listView.setAdapter(newpos);
    }
    public class PostAsyncTask extends AsyncTask<URL,Void,String>{
        protected String doInBackground(URL... urls){
            String site = "http://amrita-forum-app.herokuapp.com/subscription/"+rol+"/JSON";
            URL ur = createUrl(site);

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
            ArrayList<Post> lis = new ArrayList<Post>();

            try {
                JSONObject obj = new JSONObject(json);
                JSONArray arr = obj.getJSONArray("post");
                for(int i=0;i<arr.length();i++){
                    String content = arr.getJSONObject(i).getString("content");
                    JSONArray a = obj.getJSONArray("clubnames");
                    JSONArray b = obj.getJSONArray("usernames");
                    String c = b.getString(i);
                    String name = a.getString(i);
                    Post v = new Post(name,content,c);
                    lis.add(v);
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
