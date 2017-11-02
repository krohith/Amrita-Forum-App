package com.example.android.askamrita;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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
    TextView upvotes;
    String ups;
    String k;
    int clicks = 0;
    SwipeRefreshLayout lay;
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
        lay = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        rol = activity_login.rollNo.getText().toString().toUpperCase();
        headroll = (TextView) header.findViewById(R.id.roll);
        headroll.setText(rol);
        username = (TextView) header.findViewById(R.id.nam);
        username.setText(activity_login.user);
        mDrawerlayout = (DrawerLayout) findViewById(R.id.sidebar);
        mToggle = new ActionBarDrawerToggle(activity_home.this, mDrawerlayout, R.string.open, R.string.close);
        mDrawerlayout.addDrawerListener(mToggle);
        mToggle.syncState();
        lay.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent i = new Intent(activity_home.this,activity_home.class);
                startActivity(i);
            }
        });
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
    public void handler(View v){
        LinearLayout m = (LinearLayout)v.getParent();
        LinearLayout r = (LinearLayout)m.getParent();
        LinearLayout e = (LinearLayout)r.getChildAt(3);
        TextView up = (TextView)e.getChildAt(1);
        ups = up.getText().toString();
        TextView id = (TextView)m.getChildAt(2);
        k = id.getText().toString();
        clicks+=1;
        int a = Integer.parseInt(ups);
        up.setText(Integer.toString(a+1));
        upvoteAsyncTask task = new upvoteAsyncTask();
        task.execute();
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
                JSONArray a = obj.getJSONArray("clubnames");
                JSONArray b = obj.getJSONArray("usernames");
                for(int i=0;i<arr.length();i++){
                    String content = arr.getJSONObject(i).getString("content");
                    int likes =  arr.getJSONObject(i).getInt("likes");
                    String lik = Integer.toString(likes);
                    String c = b.getString(i);
                    String name = a.getString(i);
                    String id = Integer.toString(arr.getJSONObject(i).getInt("id"));
                    Post v = new Post(name,content,c,lik,id);
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
    public class upvoteAsyncTask extends AsyncTask<URL,Void,Void>{

        @Override
        protected Void doInBackground(URL... params) {
            String site = "http://amrita-forum-app.herokuapp.com/clubpost/"+k;
            URL ur = createUrl(site);


                try {
                    httprequest(ur);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            return null;

        }
    }
    private void httprequest(URL ur) throws IOException {
        HttpURLConnection urlconnect = null;
        String jsonResponse = null;
        urlconnect = (HttpURLConnection) ur.openConnection();
        urlconnect.setRequestMethod("POST");
        urlconnect.setDoOutput(true);
        urlconnect.setReadTimeout(10000);
        urlconnect.setConnectTimeout(15000);
        urlconnect.setRequestProperty("Content-Type","application/json");
        urlconnect.setRequestProperty("Accept","application/json");
        urlconnect.connect();
        JSONObject data = new JSONObject();
        try {
            data.put("value", 1);
        }catch (JSONException e) {
            e.printStackTrace();
        }
        OutputStream os = urlconnect.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(data.toString());
        writer.close();
        os.close();
        int responsecode = urlconnect.getResponseCode();
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
//            }
        if (responsecode == HttpURLConnection.HTTP_OK) { //success
            BufferedReader in = new BufferedReader(new InputStreamReader(urlconnect.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            jsonResponse = response.toString();
            // print result
            Log.v(LOG_TAG, response.toString());
        } else {
            Log.v(LOG_TAG, "POST request did not work.");
        }

    }
}
