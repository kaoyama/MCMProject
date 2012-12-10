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


public class HomeActivity extends Activity {
	/** Called when the activity is first created. */


	TextView username;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		Button logoutButton = (Button) findViewById(R.id.logoutButton);

		//Listening to button event
		logoutButton.setOnClickListener(new View.OnClickListener(){

			public void onClick(View arg0) {
				//Starting a new Intent
				Intent homeScreen = new Intent(getApplicationContext(), MerchantLocalizationActivity.class);

				startActivity(homeScreen);

			}
		});
		
		Button accountSettingsButton = (Button) findViewById(R.id.accountSettingsButton);

		//Listening to button event
		accountSettingsButton.setOnClickListener(new View.OnClickListener(){

			public void onClick(View arg0) {
				//Starting a new Intent
				Intent accountSettingsScreen = new Intent(getApplicationContext(), AccountSettingsActivity.class);

				startActivity(accountSettingsScreen);

			}
		});

	}
}