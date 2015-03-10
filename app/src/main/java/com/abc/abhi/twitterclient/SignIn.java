package com.abc.abhi.twitterclient;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.abc.abhi.webservice.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SignIn extends ActionBarActivity {
    public Button Signin;
    public EditText InputUsername, InputPassword;
    public   String paramUsername= null, paramPassword=null;
    private static final String TwitterURL = "http://api.twitter.com/1/users/show.json?id=";
    private ProgressDialog pDialog;
    private static  final String LOG_TAG = "Twitter Client";

    JSONParser jsonParser = new JSONParser();

    private static String KEY_ERROR = "error";
    private static String KEY_ERROR_MSG = "error_msg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Signin = (Button) findViewById(R.id.signin);
        InputUsername = (EditText) findViewById(R.id.username);
        InputPassword =(EditText) findViewById(R.id.password);
        Connectivity();
    }

    public void click(View v){
        switch (v.getId()){
            case R.id.signin:
                paramUsername = InputUsername.getText().toString();
                paramPassword = InputPassword.getText().toString();
                new AttemptLogin().execute();
                break;
        }
    }
    //Check network connection
    public void Connectivity(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()){
            //new TwitterTimeLine.execute(ScreenName);
        }
        else{
            Toast.makeText(getApplicationContext(),"Check Your phone has Network connectivity",Toast.LENGTH_LONG).show();
            Log.v(LOG_TAG,"No network connection available.");
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_in, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class AttemptLogin extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SignIn.this);
            pDialog.setMessage("Sign-IN");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            int code;
            try {
                // Building Parameters
                ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
                param.add(new BasicNameValuePair("email", paramUsername));
                param.add(new BasicNameValuePair("password", paramPassword));

                Log.d("request!", "starting");
                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(TwitterURL, "POST",
                        param);

                // check your log for json response
                Log.d("Login attempt", json.toString());

                // json success tag
                code = 1;
                if (code == 1) {
                   Intent i = new Intent(SignIn.this, Timeline.class);
                    finish();
                    startActivity(i);
                    return json.getString(KEY_ERROR);
                } else {
                    Log.d("Login Failure!", json.getString(KEY_ERROR_MSG));
                    return json.getString(KEY_ERROR);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (file_url != null) {
                Toast.makeText(SignIn.this, file_url, Toast.LENGTH_LONG).show();
            }

        }

    }

}
