package com.example.wojtekswiderski.woahpaper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;


public class SendActivity extends Activity {

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";

    private final String SENDER_ID = "553555318597";

    static final String TAG = "WoahPaper";

    private TextView userTitle;
    private EditText recipInput;
    private EditText wordInput;
    private Button sendButton;

    private Context context;
    private GoogleCloudMessaging gcm;
    private AtomicInteger msgId = new AtomicInteger();
    private SharedPreferences prefs;
    private String regid;
    private String user;
    private String uniqueID;
    private String recip;
    private String word;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        context = getApplicationContext();

        //Enabling internet access for the app
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //Gets info from home page
        Intent pastIntent = getIntent();
        user = pastIntent.getStringExtra("user");
        uniqueID = pastIntent.getStringExtra("uuid");

        //Objects for areas on the app
        userTitle = (TextView) findViewById(R.id.userTitleBox);
        recipInput = (EditText) findViewById(R.id.recipientBox);
        wordInput = (EditText) findViewById(R.id.wordBox);
        sendButton = (Button) findViewById(R.id.sendButton);

        //Sets title to username
        userTitle.setText("HELLO " + user.toUpperCase());

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
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(view.getContext().INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                recip = recipInput.getText().toString().toLowerCase();
                word = wordInput.getText().toString().toLowerCase();

                sendWord();
            }
        });

        if(checkPlayServices()){
            Log.i(TAG, "Valid Google Play Services APK found.");

            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationID(context);

            if(regid.isEmpty()){
                registerInBackground();
            }
        }else{
            Log.i(TAG, "No valid Google Play Services APK found.");
        }

    }

    // You need to do the Play Services APK check here too.
    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }

    public boolean sendWord(){
        if(recip.equals("")){
            Log.e(TAG, "No recipient");
            Toast.makeText(context, "Choose a recipient", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(word.equals("")){
            Log.e(TAG, "No word");
            Toast.makeText(context, "Choose a word", Toast.LENGTH_SHORT).show();
            return false;
        }
        String url = "http://woahpaper.wojtechnology.com/send/" + recip + "/" + word;
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
                logoutAction();
                return false;
            } else if(response.toString().equals("regid taken")) {
                Log.e(TAG, "Recipient does not exist");
                Toast.makeText(context, "Recipient does not exist", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                Log.i(TAG, "Sent word");
                afterSend();
                return false;
            }
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            Toast.makeText(context, "Wrong url", Toast.LENGTH_SHORT).show();
            logoutAction();
        } catch (IOException ey) {
            ey.printStackTrace();
            Toast.makeText(context, "Server down", Toast.LENGTH_SHORT).show();
            logoutAction();
        }
        return false;
    }

    public boolean changeRegID(){
        if(uniqueID.equals("")){
            Log.e(TAG, "No uniqueID");
            return false;
        }
        if(regid.equals("")){
            Log.e(TAG, "No regID");
            return false;
        }
        String url = "http://woahpaper.wojtechnology.com/updateReg/" + uniqueID + "/" + regid;
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
                logoutAction();
                return false;
            } else if(response.toString().equals("regid taken")) {
                Log.e(TAG, "RegID is taken");
                logoutAction();
                return false;
            } else if(response.toString().equals("no user")){
                Log.e(TAG, "Failed to find user");
                logoutAction();
            } else {
                Log.i(TAG, "Changed regID");
                return false;
            }
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            Toast.makeText(context, "Wrong url", Toast.LENGTH_SHORT).show();
            logoutAction();
        } catch (IOException ey) {
            ey.printStackTrace();
            Toast.makeText(context, "Server down", Toast.LENGTH_SHORT).show();
            logoutAction();
        }
        return false;
    }

    public void logoutAction() {
        try {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            Log.i(TAG, "Logged out");
            finish();
            return;
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
    }

    public void afterSend() {
        try {
            Intent i = new Intent(this, SendActivity.class);
            i.putExtra("user", user);
            i.putExtra("uuid", uniqueID);
            startActivity(i);
            Log.i(TAG, "Done sending");
            finish();
            return;
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    //Looks for previously created registration id
    //Returns whatever if finds
    private String getRegistrationID(Context context){
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationID = prefs.getString(PROPERTY_REG_ID, "");
        if(registrationID.isEmpty()){
            Log.i(TAG, "Registration not found.");
            return "";
        }
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if(registeredVersion != currentVersion){
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationID;
    }

    private SharedPreferences getGCMPreferences(Context context){
        return getSharedPreferences(SendActivity.class.getSimpleName(), Context.MODE_PRIVATE);
    }

    private static int getAppVersion(Context context){
        try{
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        }catch(PackageManager.NameNotFoundException e){
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;
                    sendRegistrationIdToBackend();
                    storeRegistrationId(context, regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    logoutAction();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Log.i(TAG, msg);
            }
        }.execute(null, null, null);
    }

    private void sendRegistrationIdToBackend() {
        changeRegID();
        Log.i(TAG, "Send registration to backend");
    }

    private void storeRegistrationId(Context context, String regId){
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
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