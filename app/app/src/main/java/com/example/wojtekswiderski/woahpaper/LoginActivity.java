package com.example.wojtekswiderski.woahpaper;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.os.StrictMode;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends Activity {

    private TextView titleText;
    private EditText userInput;
    private Button submitButton;

    private Context context;
    private String user = "";
    private String uniqueID = "";

    private final String TAG = "Woahpaper";

    @Override
    protected void onCreate(Bundle extra) {
        super.onCreate(extra);
        setContentView(R.layout.activity_login);

        //Gets context object and unique device id (UUID)
        context = getApplicationContext();
        TelephonyManager tManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        uniqueID = tManager.getDeviceId();
        Log.e(TAG, uniqueID);

        //Enabling internet access for the app
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //Checks if account already exists
        checkLogin();

        //Creates objects for forms and button
        userInput = (EditText) findViewById(R.id.userBox);
        submitButton = (Button) findViewById(R.id.submitBox);
        titleText = (TextView) findViewById(R.id.titleBox);

        //Caps the input
        InputFilter[] userFilterArray = new InputFilter[3];
        userFilterArray[0] = new InputFilter.AllCaps();
        userFilterArray[1] = new InputFilter.LengthFilter(12);
        userFilterArray[2] = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (!Character.isLetterOrDigit(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        };
        userInput.setFilters(userFilterArray);

        //Turns off hint when clicked
        userInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (((EditText) view).getHint().equals(""))
                    ((EditText) view).setHint(R.string.username_hint);
                else ((EditText) view).setHint("");
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(view.getContext().INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                user = userInput.getText().toString().toLowerCase();
                Log.i(TAG, user);

                if(createNew()) changeUser();
            }
        });

    }

    public void loginAction() {
        try {
            Intent i = new Intent(this, SendActivity.class);
            i.putExtra("user", user);
            i.putExtra("uuid", uniqueID);
            startActivity(i);
            Log.i(TAG, "Logged in");
            finish();
            return;
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
    }

    public boolean checkLogin(){
        if(uniqueID.equals("")){
            Log.e(TAG, "No uniqueID");
            return false;
        }
        String url = "http://woahpaper.wojtechnology.com/login/" + uniqueID;
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            if(response.equals("failure")){
                Log.e(TAG, "Problem with Database");
                return false;
            }
            else if(!response.toString().equals("no account")){
                user = response.toString();
                Log.i(TAG, user);
                loginAction();
                return true;
            }else{
                Log.e(TAG, "Did not find account");
                return false;
            }

        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            Toast.makeText(context, "Wrong URL", Toast.LENGTH_SHORT).show();
        } catch (IOException ey) {
            ey.printStackTrace();
            Toast.makeText(context, "Server Down", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    //Returns whether this UUID already has an existing account
    public boolean createNew(){
        if(uniqueID.equals("")){
            Log.e(TAG, "No uniqueID");
            return false;
        }
        if(user.equals("")){
            Log.e(TAG, "No user");
            Toast.makeText(context, "Please enter a username", Toast.LENGTH_SHORT).show();
            return false;
        }
        String url = "http://woahpaper.wojtechnology.com/new/" + uniqueID + "/" + user;
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            if (response.equals("failure")) {
                Log.e(TAG, "Problem with database");
                return false;
            } else if (response.toString().equals("uuid taken")) {
                Log.e(TAG, "Device already has account");
                return true;
            } else if(response.toString().equals("user taken")) {
                Log.e(TAG, "Username is taken");
                Toast.makeText(context, "Choose another username", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                Log.i(TAG, "Created account");
                loginAction();
                return false;
            }
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            Toast.makeText(context, "Wrong URL", Toast.LENGTH_SHORT).show();
        } catch (IOException ey) {
            ey.printStackTrace();
            Toast.makeText(context, "Server down", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public boolean changeUser(){

        if(uniqueID.equals("")){
            Log.e(TAG, "No uniqueID");
            return false;
        }
        if(user.equals("")){
            Log.e(TAG, "No user");
            Toast.makeText(context, "Please enter a username", Toast.LENGTH_SHORT).show();
            return false;
        }
        String url = "http://woahpaper.wojtechnology.com/updateUser/" + uniqueID + "/" + user;
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            if (response.equals("failure")) {
                Log.e(TAG, "Problem with database");
                return false;
            } else if(response.toString().equals("user taken")) {
                Log.e(TAG, "Username is taken");
                Toast.makeText(context, "Choose another username", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                Log.i(TAG, "Changed username");
                loginAction();
                return false;
            }
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            Toast.makeText(context, "Wrong URL", Toast.LENGTH_SHORT).show();
        } catch (IOException ey) {
            ey.printStackTrace();
            Toast.makeText(context, "Server down", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}