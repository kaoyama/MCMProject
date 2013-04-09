package app.merchantLocalization;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.graphics.Color;
import android.location.*;
import app.merchantLocalization.R;
import app.utilities.CommonUtilities;
import app.utilities.CustomDialog;
import app.utilities.RestClient;


public class ChargeCustomer extends Activity {
	/** Called when the activity is first created. */


	String customerName;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chargecustomer);
		
		final String username = CommonUtilities.getUsername(ChargeCustomer.this); 
		
		Intent myIntent= getIntent(); // gets the previously created intent
		customerName = myIntent.getStringExtra("customerName");
		
		Button chargeButton = (Button) findViewById(R.id.chargeButton);
		chargeButton.setText("Charge " + customerName);
		
		final EditText chargeAmount = (EditText) findViewById(R.id.chargeAmount);
		
		chargeButton.setOnClickListener(new View.OnClickListener(){
			public void onClick(View arg0) {
				final Editable price = chargeAmount.getText();
				AlertDialog.Builder builder = new AlertDialog.Builder(ChargeCustomer.this);
				builder.setMessage("You are about to charge " + customerName + " for $" + price + "?")
				.setTitle("Make Payment");
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
						
						CustomDialog confirmation = new CustomDialog(ChargeCustomer.this); 
		       			confirmation.showNotificationDialog("You have successfully charged " + customerName + " for " + price + ".");
		       			chargeAmount.setText("");
					}
				});				
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
		
	}
}