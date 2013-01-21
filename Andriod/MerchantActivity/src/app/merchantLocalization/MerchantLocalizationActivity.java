package app.merchantLocalization;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MerchantLocalizationActivity extends Activity {
    /** Called when the activity is first created. */
	EditText inputUserName;
    EditText inputPassword;
    
    TextView username;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        inputUserName = (EditText) findViewById(R.id.username);
        inputPassword = (EditText) findViewById(R.id.password);
        Button loginButton = (Button) findViewById(R.id.loginButton);
 
        //Listening to button event
        loginButton.setOnClickListener(new View.OnClickListener(){
 
            public void onClick(View arg0) {
                //Starting a new Intent
                Intent homeScreen = new Intent(getApplicationContext(), HomeActivity.class);
 
                startActivity(homeScreen);
 
            }
        });
    }
}