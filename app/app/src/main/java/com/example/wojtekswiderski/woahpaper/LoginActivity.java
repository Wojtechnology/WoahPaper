package com.example.wojtekswiderski.woahpaper;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.os.StrictMode;
import android.os.Handler;
import android.widget.Toast;
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

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        final EditText userInput = (EditText) findViewById(R.id.userBox);
        final EditText passInput = (EditText) findViewById(R.id.passBox);
        final Button submitButton = (Button) findViewById(R.id.submitBox);

        //Caps the input
        InputFilter[] userFilterArray = new InputFilter[2];
        userFilterArray[0] = new InputFilter.AllCaps();
        userFilterArray[1] = new InputFilter.LengthFilter(12);
        userInput.setFilters(userFilterArray);

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

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                view.setBackgroundColor(0xFF0099CC);
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(view.getContext().INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.setBackgroundColor(0xFF33B5E5);
                    }
                }, 200);
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

                    Toast.makeText(view.getContext(), response, Toast.LENGTH_SHORT).show();

                } catch (MalformedURLException ex) {
                    ex.printStackTrace();
                } catch (IOException ey){
                    ey.printStackTrace();
                }
            }
        });

        /*//Filters for the inputs allowing usernames up to 12 letters
        //Also caps the input
        InputFilter[] userFilterArray = new InputFilter[2];
        userFilterArray[0] = new InputFilter.LengthFilter(12);
        userFilterArray[1] = new InputFilter.AllCaps();

        InputFilter[] passFilterArray = new InputFilter[2];
        passFilterArray[0] = new InputFilter.LengthFilter(4);
        passFilterArray[1] = new InputFilter.AllCaps();

        //Input for the username
        final EditText userName = new EditText(this);
        userName.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 200));
        userName.setBackgroundColor(0xFFFF4444);
        userName.setTypeface(Typeface.create("", Typeface.BOLD));
        userName.setFilters(userFilterArray);
        userName.setHint("USERNAME");
        userName.setHintTextColor(0xFFFFFFFF);
        userName.setGravity(Gravity.CENTER);
        userName.setTextSize(50f);
        userName.setCursorVisible(false);
        userName.setTextColor(0xFFFFFFFF);
        layout.addView(userName);

        //password field
        final EditText passWord = new EditText(this);
        passWord.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 200));
        passWord.setBackgroundColor(0xFFFF4444);
        passWord.setTypeface(Typeface.create("", Typeface.BOLD));
        passWord.setFilters(passFilterArray);
        passWord.setHint("PASSWORD");
        passWord.setInputType(InputType.TYPE_CLASS_NUMBER);
        passWord.setTransformationMethod(PasswordTransformationMethod.getInstance());
        passWord.setHintTextColor(0xFFFFFFFF);
        passWord.setGravity(Gravity.CENTER);
        passWord.setTextSize(50f);
        passWord.setCursorVisible(false);
        passWord.setTextColor(0xFFFFFFFF);
        layout.addView(passWord);*/

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
