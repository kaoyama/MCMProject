package app.merchantLocalization;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import app.merchantLocalization.R;
import app.utilities.AlertDialogManager;
import app.utilities.CommonUtilities;
import app.utilities.CustomDialog;
import app.utilities.RestClient;


/**
 * Customerlist.  Pulls subscribed or nearby customer names from the database. 
 * Need service-oriented architecture and needs three elements: 
 * external database, web-service, mobile web-service client. 
 * @author Chihiro
 * 
 * 
 * Notes:
 * For connection to work, Apache server must be handled to start PHP. 
 * Also, make sure NAU Wi-Fi is connected on the device. 
 * 
 * IP address changes for each Wi-Fi access! 
 *
 */

@SuppressLint("NewApi")
public class Customers extends Activity {
	/** Called when the activity is first created. */	
	LinearLayout customerLayout;
	
	Customers currentThis = this; 
	
	static int TIMEOUT_MILLISEC = 3000; 

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.customers);
		
		// initialize list view
		//listContents = new ArrayList<String>();
		
		customerLayout = (LinearLayout)findViewById(R.id.customerLayout);
		
		//Sets up a title for each column
				TableRow tempTableRow=new TableRow(getBaseContext());
				LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				tempTableRow.setLayoutParams(lp);
				//Create the titles for the columns	
				TextView titleView = new TextView(this);
				titleView.setText("Customer: ");
				TextView optionsView = new TextView(this);
				optionsView.setText("Options:");
				//Adds TextViews to columns
				tempTableRow.addView(titleView);
				tempTableRow.addView(optionsView);
				
				customerLayout.addView(tempTableRow);
		
		getCustomers(); 
	}

	public void getCustomers() {
		
		String username = CommonUtilities.getUsername(Customers.this); 
		JSONObject jsonIn = new JSONObject();
		
		
		try {
			jsonIn.put("userName", username);
		} catch (Exception e) {
			Log.v("Merchants", "JSON Exception");
		}
		
		//Pulls customers that are within acertain radius
		final JSONArray jsonArray = RestClient.connectToDatabase(CommonUtilities.NEARBYCUSTOMERS_URL, jsonIn);
		
		if (jsonArray != null) {
			
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
											
					try {							
						
						//Displays customers on app in a table
						for (int i = 0; i < jsonArray.length(); i++) {
							final String customerName = jsonArray.getJSONObject(i).getString("userName");
							TextView showCustomer = new TextView(currentThis);
							showCustomer.setText(customerName + "\n\n");
							LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
							TableRow tempTableRow=new TableRow(getBaseContext());
							
							//Charge button foreach individual customer
							final Button tempButton= new Button(Customers.this);
							tempButton.setText("Charge");
							tempButton.setOnClickListener(new View.OnClickListener(){
								//Starts ChargeCustomer screen for specific customer
								public void onClick(View arg0) {
									Intent chargeScreen = new Intent(getApplicationContext(), ChargeCustomer.class);
									chargeScreen.putExtra("customerName", customerName);
									startActivity(chargeScreen);
								}
							});
							tempTableRow.setLayoutParams(lp);
							tempTableRow.addView(showCustomer);
							tempTableRow.addView(tempButton);
							customerLayout.addView(tempTableRow,lp);
						}	
						
					} catch (JSONException e) {
						CustomDialog cd = new CustomDialog(Customers.this); 
						cd.showNotificationDialog(e.getMessage()); 
					}
					
				}
			});
			
		} 
		//Shown if no results are returned
		else {
			CustomDialog cd = new CustomDialog(Customers.this); 
			cd.showNotificationDialog("Customer list is empty.");
		}
		
	}
}
