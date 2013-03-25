package app.localization;

import java.util.ArrayList;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import app.utilities.CommonUtilities;
import app.utilities.CustomDialog;
import app.utilities.RestClient;

/**
 * Payment history list.  Pulls subscribed or nearby merchant information from the database. 
 * Need service-oriented architecture and needs three elements: 
 * external database, web-service, mobile web-service client. 
 * @author Chihiro
 */

public class PaymentHistory extends Activity {
	/** Called when the activity is first created. */

	TextView username;
	TextView result; 

	String dbResult; 

	static int TIMEOUT_MILLISEC = 3000; 

	List<String> listContents; 
	ListView myListView; 
	ArrayAdapter<String> adapter; 
	PaymentHistory currentThis = this; 

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history);

		listContents = new ArrayList<String>();

		myListView = (ListView)findViewById(R.id.historyList);

		// Get list of merchants from database
		getData(); 

	}

	/**
	 * Connect to web service (database) 
	 */
	public void getData() {
		getMerchants(); 
	}

	public void getMerchants() {

		String username = CommonUtilities.getUsername(PaymentHistory.this); 
		JSONObject jsonIn = new JSONObject();

		try {
			jsonIn.put("userName", username);
		} catch (Exception e) {
			Log.v("Merchants", "JSON Exception");
		}

		final JSONArray jsonArray = RestClient.connectToDatabase(CommonUtilities.PAYMENTS_URL, jsonIn);

		// if there are no transactions, notify the user 
		if (jsonArray == null || jsonArray.length() == 0) {
			CustomDialog cd = new CustomDialog(PaymentHistory.this); 
			cd.showNotificationDialog("There are no recent transactions at this time.");
		}

		// if there are transactions
		else {

			runOnUiThread(new Runnable() {

				@Override
				public void run() {

					try {							
						listContents = new ArrayList<String>(jsonArray.length());

						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject jo = jsonArray.getJSONObject(i); 
							String merchant = jo.getString("merchant");
							String productIndex = jo.getString("productIndex"); 
							String purchaseTime = jo.getString("purchaseTime"); 
							String cost = jo.getString("cost"); 
							String paid = jo.getString("paid");
							String cancelled = jo.getString("cancelled");

							String str = "";
							// payment has been made for the specific product 
							if (paid.equals("1")) {
								str = "Successful transaction from ";
							} else {
								str = "Pending transaction from "; 
							}	
							if (cancelled.equals("1")) {
								str = "Cancelled transaction from ";
							}
							str += merchant  + "\n" + 
									"$" + cost + " for " + productIndex + "\n" + 
									purchaseTime;
							
							listContents.add(str); 
						}


					} catch (JSONException e) {
						CustomDialog cd = new CustomDialog(PaymentHistory.this); 
						cd.showNotificationDialog(e.getMessage()); 
					}

					adapter = new ArrayAdapter<String>(currentThis, 
							android.R.layout.simple_list_item_1, listContents); 
					adapter.setNotifyOnChange(true); 
					myListView.setAdapter(adapter); 
				}
			});
		}
	}
}