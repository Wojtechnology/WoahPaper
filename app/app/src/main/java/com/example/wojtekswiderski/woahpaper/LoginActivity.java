package com.example.wojtekswiderski.woahpaper;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.CorrectionInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle extra) {
        super.onCreate(extra);
        setContentView(R.layout.activity_login);

        EditText userInput = (EditText) findViewById(R.id.userBox);
        EditText passInput = (EditText) findViewById(R.id.passBox);

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
