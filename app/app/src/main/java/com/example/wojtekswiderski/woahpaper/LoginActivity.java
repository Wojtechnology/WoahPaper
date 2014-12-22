package com.example.wojtekswiderski.woahpaper;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.os.StrictMode;
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

    private EditText userInput;
    //private EditText passInput;
    private Button submitButton;

    private Context context;
    private int process;
    private String UUID;

    @Override
    protected void onCreate(Bundle extra) {
        super.onCreate(extra);
        setContentView(R.layout.activity_login);

        context = getApplicationContext();
        process = 0;
        TelephonyManager tManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        UUID = tManager.getDeviceId();
        Log.i("STUFF", UUID);

        //Enabling internet access for the app
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //Creates objects for forms and button
        userInput = (EditText) findViewById(R.id.userBox);
        //passInput = (EditText) findViewById(R.id.passBox);
        submitButton = (Button) findViewById(R.id.submitBox);

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

        /*passInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(((EditText) view).getHint().equals("")) ((EditText) view).setHint(R.string.password_hint);
                else ((EditText) view).setHint("");
            }
        });*/

        submitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View view) {

                TelephonyManager tManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                UUID = tManager.getDeviceId();
                Log.i("STUFF", UUID);

                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(view.getContext().INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                /*String user = userInput.getText().toString().toLowerCase();
                //String pass = passInput.getText().toString();
                String url = "http://woahpaper.wojtechnology.com/user/" + user + "/";
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
                    } else if (response.toString().equals("incorrect password")) {
                        Toast.makeText(view.getContext(), "Wrong Password", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(view.getContext(), "Created User", Toast.LENGTH_SHORT).show();
                        loginAction(view, user);
                    }


                } catch (MalformedURLException ex) {
                    ex.printStackTrace();
                    Toast.makeText(view.getContext(), "Wrong URL", Toast.LENGTH_SHORT).show();
                } catch (IOException ey) {
                    ey.printStackTrace();
                    Toast.makeText(view.getContext(), "Server Down", Toast.LENGTH_SHORT).show();
                }*/
            }
        });

    }

    public void loginAction(View view, String user){
        if(process == 0) {
            process = 1;
            try {
                Intent i = new Intent(this, SendActivity.class);
                i.putExtra("user", user);
                startActivity(i);
                Log.i("First", "Logged In");
                finish();
            } catch (Exception ex) {
                Log.e("main", ex.toString());
                process = 0;
            }
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
