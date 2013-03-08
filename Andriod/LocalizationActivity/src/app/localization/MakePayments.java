package app.localization;

import java.io.FileInputStream;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import app.utilities.CommonUtilities;
import app.utilities.CustomDialog;
import app.utilities.RestClient;
/**
 * Notifications list.  Pulls subscribed or nearby merchant information from the database. 
 * Need service-oriented architecture and needs three elements: 
 * external database, web-service, mobile web-service client. 
 * @author Chihiro
 */

public class MakePayments extends Activity {
	
	MakePayments currentThis = this;
	LinearLayout paymentLayout;
	String tempUserName = "";
	
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.payments);
		paymentLayout = (LinearLayout)findViewById(R.id.paymentLayout);
		
		// Get list of notifications from database
		getData(); 
	}


	public void getData() {
		//Grab Username from Android DB
		try {
			FileInputStream fis = openFileInput("username_file");
			StringBuffer sb = new StringBuffer("");
			int ch;
			while((ch = fis.read())!= -1){
				sb.append((char)ch);
			}
			fis.close();
			tempUserName = sb.toString();
		} catch (Exception e) {
			CustomDialog cd = new CustomDialog(MakePayments.this); 
			cd.showNotificationDialog("Could not get username.");
		}
		final String userName = tempUserName;
		
		JSONArray jsonArray = null;
		try {
			JSONObject json = new JSONObject();
			json.put("userName", userName);

			jsonArray = RestClient.connectToDatabase(
					CommonUtilities.PAYMENTS_URL, json);
			
		} catch (Exception e) {
			CustomDialog cd3 = new CustomDialog(MakePayments.this); 
			cd3.showNotificationDialog("Failed here");
		}
		
		try {
			for(int i = 0; i < jsonArray.length(); i++){
				final String merchant = jsonArray.getJSONObject(i).getString(
						"merchant");
				
				final String productIndex = jsonArray.getJSONObject(i).getString(
						"productIndex");
				
				final String purchaseTime = jsonArray.getJSONObject(i).getString(
						"purchaseTime");
				
				final String cost = jsonArray.getJSONObject(i).getString(
						"cost");
				
				final String paid = jsonArray.getJSONObject(i).getString(
						"paid");
				if(paid.equals("0")){


					Button tempButton = new Button(currentThis);
					tempButton.setText(merchant + " requests payment for: " + productIndex);
					tempButton.setOnClickListener(new View.OnClickListener(){

					public void onClick(View arg0) {
							AlertDialog.Builder builder = new AlertDialog.Builder(currentThis);
							builder.setMessage("Would you like to purchase " + productIndex + " for $" + cost + "?")
										       .setTitle("Make Payment");
											builder.setPositiveButton("Purchase", new DialogInterface.OnClickListener() {
										           public void onClick(DialogInterface dialog, int id) {
										        	   JSONArray jsonArray = null;
										       		try {
										       			JSONObject json = new JSONObject();
										       			json.put("paid", 1);
										       			json.put("cancelled", 0);
										       			json.put("userName", userName);
										       			json.put("productIndex", productIndex);

										       			jsonArray = RestClient.connectToDatabase(
										       					CommonUtilities.UPDATEPAYMENT_URL, json);
										       			CustomDialog cd3 = new CustomDialog(MakePayments.this); 
										       			cd3.showNotificationDialog("You have successfully paid " + merchant + " for" + productIndex + "." );
										       			
										       		} catch (Exception e) {
										       			CustomDialog cd3 = new CustomDialog(MakePayments.this); 
										       			cd3.showNotificationDialog("Failed here");
										       		}
										       		// Starting a new intent
													Intent paymentScreen = new Intent(getApplicationContext(), MakePayments.class);
													startActivity(paymentScreen);
			
										           }
										       });
										builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
										           public void onClick(DialogInterface dialog, int id) {
										               // User cancelled the dialog
										           }
										       });
									        // If the response does not enclose an entity, there is no need
										AlertDialog dialog = builder.create();
										dialog.show();
									}
								});
								LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
								paymentLayout.addView(tempButton,lp);
							
				}
			}
		}

        
		catch (Exception e) {
			CustomDialog cd3 = new CustomDialog(MakePayments.this); 
			cd3.showNotificationDialog("Invalid: " + e.getMessage());
		}
		
		
	}
}
