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
 * View to display the buttons for different screens
 * 
 * 
 * 
 */
public class HomeActivity extends Activity {
	/** Called when the activity is first created. */


	TextView username;

	public void onBackPressed() {
	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		
		Button logoutButton = (Button) findViewById(R.id.logoutButton);

		//Logout button
		logoutButton.setOnClickListener(new View.OnClickListener(){

			public void onClick(View arg0) {
				//Starting a new Intent
				Intent homeScreen = new Intent(getApplicationContext(), MerchantLocalizationActivity.class);

				startActivity(homeScreen);

			}
		});
		
		Button pendingTransactionsButton = (Button) findViewById(R.id.chargeButton);

		//Pending Transactions Button
		pendingTransactionsButton.setOnClickListener(new View.OnClickListener(){

			public void onClick(View arg0) {
				//Starting a new Intent
				Intent chargeScreen = new Intent(getApplicationContext(), PendingTransactions.class);

				startActivity(chargeScreen);

			}
		});
		
		Button accountSettingsButton = (Button) findViewById(R.id.accountSettingsButton);

		//Account settings button
		accountSettingsButton.setOnClickListener(new View.OnClickListener(){

			public void onClick(View arg0) {
				//Starting a new Intent
				Intent accountSettingsScreen = new Intent(getApplicationContext(), AccountSettingsActivity.class);

				startActivity(accountSettingsScreen);

			}
		});
		
		Button customersButton = (Button) findViewById(R.id.customersButton); 
		
		//Customer list button
		customersButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent customerScreen = new Intent(getApplicationContext(), Customers.class); 
				startActivity(customerScreen); 
			}
		});
		
		Button manageDealsButton = (Button) findViewById(R.id.manageAds); 
		
		//Manage deals button
		manageDealsButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent manageAdsScreen = new Intent(getApplicationContext(), ManageDeals.class); 
				startActivity(manageAdsScreen); 
			}
		});
		
		Button transactionsButton = (Button) findViewById(R.id.transactionsButton); 
		//Transaction history button
		transactionsButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent transactionsScreen = new Intent(getApplicationContext(), Transactions.class); 
				startActivity(transactionsScreen); 
			}
		});
	}
}