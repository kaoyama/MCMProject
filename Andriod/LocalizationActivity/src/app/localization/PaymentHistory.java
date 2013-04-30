package app.localization;

import java.sql.Timestamp;
import java.text.DateFormat;
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
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import app.utilities.CommonUtilities;
import app.utilities.CustomDialog;
import app.utilities.RestClient;

/**
 * Payment history list. Pulls transaction history information from the
 * database. Need service-oriented architecture and needs three elements:
 * external database, web-service, mobile web-service client.
 * 
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

	LinearLayout historyLayout;
	TextView noHistoryText;

	/**
	 * Initializes the page when it is first created
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history);
		historyLayout = (LinearLayout) findViewById(R.id.historyLayout);

		// initialize variables for list view
		listContents = new ArrayList<String>();
		myListView = (ListView) findViewById(R.id.historyList);

		// initialize variables for empty list
		noHistoryText = new TextView(PaymentHistory.this);
		noHistoryText.setText("There are no recent transactions at this time.");

		// Get list of merchants from database
		getHistory();

		// home button
		Button settingsButton = (Button) findViewById(R.id.homeButton);
		settingsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Starting a new intent
				Intent homeScreen = new Intent(getApplicationContext(),
						HomeActivity.class);
				startActivity(homeScreen);
			}
		});

	}

	/**
	 * Retrieves a list of transaction history from the database.
	 */
	public void getHistory() {

		String username = CommonUtilities.getUsername(PaymentHistory.this);
		JSONObject jsonIn = new JSONObject();

		try {
			jsonIn.put("userName", username);
		} catch (Exception e) {
			Log.v("Merchants", "JSON Exception");
		}

		final JSONArray jsonArray = RestClient.connectToDatabase(
				CommonUtilities.PAYMENTS_URL, jsonIn);

		// if there are no transactions, notify the user
		if (jsonArray == null || jsonArray.length() == 0) {
			LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT);
			historyLayout.addView(noHistoryText, lp);
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
							str += merchant + "\n" + "$" + cost;

							if (productIndex != null
									&& !productIndex.equals("")) {
								str += " for " + productIndex;
							}

							// convert purchase time to non-military time
							Timestamp ts = Timestamp.valueOf(purchaseTime);
							String time = DateFormat.getDateTimeInstance()
									.format(ts.getTime());

							str += "\n" + time;

							listContents.add(str);
						}

					} catch (JSONException e) {
						CustomDialog cd = new CustomDialog(PaymentHistory.this);
						cd.showNotificationDialog(e.getMessage());
					}

					adapter = new ArrayAdapter<String>(PaymentHistory.this,
							android.R.layout.simple_list_item_1, listContents);
					adapter.setNotifyOnChange(true);
					myListView.setAdapter(adapter);
				}
			});
		}
	}
}