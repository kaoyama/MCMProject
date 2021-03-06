package app.merchantLocalization;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import app.utilities.CommonUtilities;
import app.utilities.CustomDialog;
import app.utilities.RestClient;

/**
 * Allows Merchant to Input a charge amount for a certain user.
 * The User was already chosen from the customer list by the time this class is called.
 * 
 * 
 */
public class ChargeCustomer extends Activity {
	/** Called when the activity is first created. */


	String customerName;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chargecustomer);
		
		//Gets the username that was saved to a text file
		final String username = CommonUtilities.getUsername(ChargeCustomer.this); 
		
		Intent myIntent= getIntent(); // gets the previously created intent
		customerName = myIntent.getStringExtra("customerName");
		
		//Gets the button that charges a customer
		Button chargeButton = (Button) findViewById(R.id.chargeButton);
		chargeButton.setText("Charge " + customerName);
		
		final EditText chargeAmount = (EditText) findViewById(R.id.chargeAmount);
		
		//Sets functionality for charge button
		chargeButton.setOnClickListener(new View.OnClickListener(){
			public void onClick(View arg0) {
				//Gets charge amount
				final Editable price = chargeAmount.getText();
				
				//Confirms the charge for specified amount
				AlertDialog.Builder builder = new AlertDialog.Builder(ChargeCustomer.this);
				builder.setMessage("You are about to charge " + customerName + " for $" + price + "?")
				.setTitle("Make Payment");
				
				//If charge is confirmed, it is sent to the database
				builder.setPositiveButton("Charge", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						JSONArray jsonArray = null;
						
						try {
							
			       			JSONObject json = new JSONObject();
			       			json.put("merchant", username);
			       			json.put("customer", customerName);
			       			json.put("cost", price);
			       			jsonArray = RestClient.connectToDatabase(CommonUtilities.CHARGECUSTOMER_URL, json);
			       			
			       		} catch (Exception e) {
			       			CustomDialog cd3 = new CustomDialog(ChargeCustomer.this); 
			       			cd3.showNotificationDialog("Failed To charge customer.");
			       		}
						
						//Confirmation of successful charge
						CustomDialog confirmation = new CustomDialog(ChargeCustomer.this); 
		       			confirmation.showNotificationDialog("You have successfully charged " + customerName + " for " + price + ".");
		       			chargeAmount.setText("");
					}
				});
				//If charge not confirmed, it is cancelled. Nothing is sent to database
				builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// User cancelled the dialog
						chargeAmount.setText("");
					}
				});
				
				AlertDialog dialog = builder.create();
				dialog.show();
			}
		});
		
		
		// home button
		Button settingsButton = (Button) findViewById(R.id.homeButton);
		settingsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Starting a new intent
				Intent homeScreen = new Intent(getApplicationContext(), HomeActivity.class);
				startActivity(homeScreen); 	      
			}
		});
				
		chargeAmount.requestFocus();
		
	}
}