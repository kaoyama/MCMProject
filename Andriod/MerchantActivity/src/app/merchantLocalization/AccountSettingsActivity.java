package app.merchantLocalization;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.graphics.Color;
import android.location.*;
import app.merchantLocalization.R;

/**
 * View Merchant Account Settings
 * Currently just dummy data
 * 
 * 
 */
public class AccountSettingsActivity extends Activity {
	


	TextView username;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.accountsettings);

		Button homeButton = (Button) findViewById(R.id.homeButton);
		 
        homeButton.setOnClickListener(new View.OnClickListener(){
 
            public void onClick(View arg0) {
                //Sends user back to Home Page
                Intent homeScreen = new Intent(getApplicationContext(), HomeActivity.class);
 
                startActivity(homeScreen);
 
            }
        });

	}
}