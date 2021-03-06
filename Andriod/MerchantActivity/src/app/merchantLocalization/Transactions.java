package app.merchantLocalization;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
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
 * Payment history list. 
 * 
 * 
 * @author Chihiro
 */

public class Transactions extends Activity {
	/** Called when the activity is first created. */

	TextView username;
	TextView result; 

	String dbResult; 

	static int TIMEOUT_MILLISEC = 3000; 

	List<String> listContents; 
	ListView myListView; 
	ArrayAdapter<String> adapter; 

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.transactions);

		listContents = new ArrayList<String>();

		myListView = (ListView)findViewById(R.id.transactionsList);

		// Get list of merchants from database
		getTransactionHistory(); 
		
		// home button
		Button homeButton = (Button) findViewById(R.id.homeButton);
		homeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Starting a new intent
				Intent homeScreen = new Intent(getApplicationContext(), HomeActivity.class);
				startActivity(homeScreen); 	      
			}
		});
	}

	/**
	 * Connect to web service (database) 
	 */

	public void getTransactionHistory() {

		String username = CommonUtilities.getUsername(Transactions.this); 
		JSONObject jsonIn = new JSONObject();

		try {
			jsonIn.put("merchant", username);
		} catch (Exception e) {
			Log.v("Merchants", "JSON Exception");
		}

		final JSONArray jsonArray = RestClient.connectToDatabase(CommonUtilities.TRANSACTIONS_URL, jsonIn);

		// if there are no transactions, notify the user 
		if (jsonArray == null || jsonArray.length() == 0) {
			CustomDialog cd = new CustomDialog(Transactions.this); 
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
							String customer = jo.getString("customer");
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
							str += customer  + "\n" + 
									"$" + cost + " for " + productIndex + "\n" + 
									purchaseTime;
							
							listContents.add(str); 
						}


					} catch (JSONException e) {
						CustomDialog cd = new CustomDialog(Transactions.this); 
						cd.showNotificationDialog(e.getMessage()); 
					}

					adapter = new ArrayAdapter<String>(Transactions.this, 
							android.R.layout.simple_list_item_1, listContents); 
					adapter.setNotifyOnChange(true); 
					myListView.setAdapter(adapter); 
				}
			});
		}
	}
}