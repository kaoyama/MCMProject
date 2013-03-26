package app.merchantLocalization;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
				JSONArray jsonArray = null;
				
				try {
					
	       			JSONObject json = new JSONObject();
	       			json.put("merchant", username);
	       			json.put("customer", customerName);
	       			json.put("cost", chargeAmount.getText());
	       			jsonArray = RestClient.connectToDatabase(CommonUtilities.CHARGECUSTOMER_URL, json);
	       			
	       		} catch (Exception e) {
	       			CustomDialog cd3 = new CustomDialog(ChargeCustomer.this); 
	       			cd3.showNotificationDialog("Failed To charge customer.");
	       		}
			}
		});
		
	}
}