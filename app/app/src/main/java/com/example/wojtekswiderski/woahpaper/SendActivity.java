package com.example.wojtekswiderski.woahpaper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class SendActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        //Enabling internet access for the app
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //Gets info from home page
        Intent pastIntent = getIntent();
        String userName = pastIntent.getStringExtra("user");

        //Objects for areas on the app
        final TextView userTitle = (TextView) findViewById(R.id.userTitleBox);
        final EditText recipInput = (EditText) findViewById(R.id.recipientBox);
        final EditText wordInput = (EditText) findViewById(R.id.wordBox);
        final Button sendButton = (Button) findViewById(R.id.sendButton);

        //Sets title to username
        userTitle.setText("HELLO " + userName.toUpperCase());

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
        recipInput.setFilters(userFilterArray);
        wordInput.setFilters(userFilterArray);

        //Turns off hint when clicked
        recipInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (((EditText) view).getHint().equals(""))
                    ((EditText) view).setHint(R.string.recipient_hint);
                else ((EditText) view).setHint("");
            }
        });

        wordInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (((EditText) view).getHint().equals(""))
                    ((EditText) view).setHint(R.string.word_hint);
                else ((EditText) view).setHint("");
            }
        });

        //Button press
        sendButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(view.getContext().INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                String recip = recipInput.getText().toString().toLowerCase();
                String word = wordInput.getText().toString();
                String url = "http://woahpaper.wojtechnology.com/send/" + recip.toLowerCase() + "/" + word.toLowerCase();
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

                    Toast.makeText(view.getContext(), response.toString(), Toast.LENGTH_SHORT).show();

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_send, menu);
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
}
