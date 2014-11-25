package com.example.wojtekswiderski.woahpaper;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

        /*ScrollView scroller = new ScrollView(this);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        //Creating objects to use as spaces in between tiles
        int spaceCounter = 0;
        TextView[] spaces = new TextView[4];
        for(int i = 0; i < 4; i++){
            spaces[i] = new TextView(this);
            spaces[i].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 10));
        }
        spaces[0].setBackgroundColor(0xFFFF8800);
        spaces[1].setBackgroundColor(0xFFCC0000);
        spaces[2].setBackgroundColor(0xFFCC0000);
        spaces[3].setBackgroundColor(0xFF0099CC);

        //Title for the app
        final TextView titleView = new TextView(this);
        titleView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 200));
        titleView.setBackgroundColor(0xFFFFBB33);
        titleView.setTypeface(Typeface.create("", Typeface.BOLD));
        titleView.setTextColor(0xFFFFFFFF);
        titleView.setText("WOAHPAPER");
        titleView.setGravity(Gravity.CENTER);
        titleView.setTextSize(50f);
        titleView.setPadding(0, 0, 0, -10);
        layout.addView(titleView);

        layout.addView(spaces[spaceCounter++]);

        //Filters for the inputs allowing usernames up to 12 letters
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

        layout.addView(spaces[spaceCounter++]);

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
        layout.addView(passWord);

        layout.addView(spaces[spaceCounter++]);

        //Submit button for the app
        final Button submit = new Button(this);
        submit.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 200));
        submit.setBackgroundColor(0xFF33B5E5);
        submit.setTextSize(50f);
        submit.setTextColor(0xFFFFFFFF);
        submit.setTypeface(Typeface.create("", Typeface.BOLD));
        submit.setText("SIGN IN");
        layout.addView(submit);

        layout.addView(spaces[spaceCounter++]);

        setContentView(scroller);
        scroller.addView(layout);

        View root = layout.getRootView();
        root.setBackgroundColor(0xFF99CC00);*/

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
