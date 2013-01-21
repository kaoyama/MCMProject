package app.merchant.localiztion;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import app.merchantLocalization.R;


public class AccountSettingsActivity extends Activity {
	/** Called when the activity is first created. */


	TextView username;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.accountsettings);

		Button homeButton = (Button) findViewById(R.id.homeButton);
		 
        //Listening to button event
        homeButton.setOnClickListener(new View.OnClickListener(){
 
            public void onClick(View arg0) {
                //Starting a new Intent
                Intent homeScreen = new Intent(getApplicationContext(), HomeActivity.class);
 
                startActivity(homeScreen);
 
            }
        });

	}
}