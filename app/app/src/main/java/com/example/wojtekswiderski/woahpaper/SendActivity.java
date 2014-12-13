package com.example.wojtekswiderski.woahpaper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class SendActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

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
        InputFilter[] userFilterArray = new InputFilter[2];
        userFilterArray[0] = new InputFilter.AllCaps();
        userFilterArray[1] = new InputFilter.LengthFilter(12);
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
