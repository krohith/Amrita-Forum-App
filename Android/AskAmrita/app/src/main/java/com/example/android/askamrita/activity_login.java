package com.example.android.askamrita;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public class activity_login extends AppCompatActivity {
    private static final String site = "http://192.168.43.149:8080/login";
    Button login;
    EditText rollNo, passKey;
    public static final String LOG_TAG = activity_login.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        rollNo = (EditText) findViewById(R.id.rollNo);
        passKey = (EditText) findViewById(R.id.passKey);
        login = (Button) findViewById(R.id.login_button);
        login();
    }


    public void login() {

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loginAsyncTask task = new loginAsyncTask();
                task.execute();
            }
        });
    }

    private class loginAsyncTask extends AsyncTask<URL, Void, Integer> {

        protected Integer doInBackground(URL... urls){
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
            int check = 0;
            try {
                JSONObject obj = new JSONObject(jsonResponse);
                check = obj.getInt("value");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return check;
        }
        @Override
        protected void onPostExecute(Integer check) {

            if (check == 0) {
                Toast.makeText(getApplicationContext(),"Wrong Roll no or password",Toast.LENGTH_LONG).show();

            }
            else{
                Intent i = new Intent(activity_login.this, activity_home.class);
                startActivity(i);
            }

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
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type","application/json");
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.connect();
            JSONObject data = new JSONObject();

            data.put("roll",rollNo.getText().toString());
            data.put("password",passKey.getText().toString());
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
//            }
            if (responsecode == HttpURLConnection.HTTP_OK) { //success
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
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

        } catch (IOException e) {
            // TODO: Handle the exception
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return jsonResponse;
    }

}

