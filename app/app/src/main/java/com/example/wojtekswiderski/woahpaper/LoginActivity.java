package com.example.wojtekswiderski.woahpaper;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.os.StrictMode;
import android.os.Handler;
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

    @Override
    protected void onCreate(Bundle extra) {
        super.onCreate(extra);
        setContentView(R.layout.activity_login);

        //Enabling internet access for the app
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //Creates objects for forms and button
        final EditText userInput = (EditText) findViewById(R.id.userBox);
        final EditText passInput = (EditText) findViewById(R.id.passBox);
        final Button submitButton = (Button) findViewById(R.id.submitBox);

        //Caps the input
        InputFilter[] userFilterArray = new InputFilter[2];
        userFilterArray[0] = new InputFilter.AllCaps();
        userFilterArray[1] = new InputFilter.LengthFilter(12);
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

        passInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(((EditText) view).getHint().equals("")) ((EditText) view).setHint(R.string.password_hint);
                else ((EditText) view).setHint("");
            }
        });

        submitButton.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(final View view, MotionEvent motionEvent) {
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(view.getContext().INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                String user = userInput.getText().toString().toLowerCase();
                String pass = passInput.getText().toString();
                String url = "http://woahpaper.wojtechnology.com/user/" + user + "/" + pass;
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

                    if (response.toString().equals("correct password")) {
                        loginAction(view, user);
                        finish();
                    } else if (response.toString().equals("incorrect password")) {
                        Toast.makeText(view.getContext(), "Wrong Password", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(view.getContext(), "Created User", Toast.LENGTH_SHORT).show();
                        loginAction(view, user);
                        finish();
                    }


                } catch (MalformedURLException ex) {
                    ex.printStackTrace();
                    Toast.makeText(view.getContext(), "Wrong URL", Toast.LENGTH_SHORT).show();
                } catch (IOException ey) {
                    ey.printStackTrace();
                    Toast.makeText(view.getContext(), "Server Down", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

    }

    public void loginAction(View view, String user){
        try{
            Intent i = new Intent(this, SendActivity.class);
            i.putExtra("user", user);
            startActivity(i);
        }
        catch(Exception ex)
        {
            Log.e("main",ex.toString());
        }
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
