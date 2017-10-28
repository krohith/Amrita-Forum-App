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
import android.widget.EditText;
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

public class activity_home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle mToggle;
    EditText roll;
    public String rol;
    public static final String LOG_TAG = activity_login.class.getSimpleName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        roll = (EditText) findViewById(R.id.rollNo);
        rol = activity_login.rollNo.getText().toString();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity_home.this, activity_home.class);
                startActivity(i);
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        mDrawerlayout=(DrawerLayout) findViewById(R.id.sidebar);
        mToggle=new ActionBarDrawerToggle(activity_home.this,mDrawerlayout,R.string.open,R.string.close);
        mDrawerlayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        PostAsyncTask task = new PostAsyncTask();
        task.execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            Intent i = new Intent(activity_home.this, activity_home.class);
            startActivity(i);
        } else if (id == R.id.forum) {
            Intent i = new Intent(activity_home.this, activity_forum.class);
            startActivity(i);

        } else if (id == R.id.latest) {
            Intent i = new Intent(activity_home.this, activity_latest.class);
            startActivity(i);

        } else if (id == R.id.post) {
            Intent i = new Intent(activity_home.this, activity_post.class);
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
            String site = "http://192.168.43.149:8080/subscription/"+rol+"/JSON";
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
                    String name = a.getString(i);
                    Post v = new Post(name,content);
                    lis.add(v);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            updateUi(lis);

        }

    }
    ////////done by rohith
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
    //////////////done by rohith
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
