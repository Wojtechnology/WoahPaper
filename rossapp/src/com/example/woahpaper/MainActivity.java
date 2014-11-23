package com.example.woahpaper;



import com.example.loginscreen.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
//DOG
public class MainActivity extends Activity {

   private EditText  username=null;
   private EditText  password=null;
   private Button login;
   int counter = 3;
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      username = (EditText)findViewById(R.id.editText1);
      password = (EditText)findViewById(R.id.editText2);
      login = (Button)findViewById(R.id.button1);
   }

   public void login(View view){
      if(username.getText().toString().equals("ADMIN") && 
      password.getText().toString().equals("6969")){
      Toast.makeText(getApplicationContext(), "Redirecting...", 
      Toast.LENGTH_SHORT).show();}
   }	
   /*else{
      Toast.makeText(getApplicationContext(), "Wrong Credentials",
      Toast.LENGTH_SHORT).show();
      attempts.setBackgroundColor(Color.RED);	
      counter--;
      attempts.setText(Integer.toString(counter));
      if(counter==0){
         login.setEnabled(false);
      }

   }*/


   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.main, menu);
      return true;
   }

}