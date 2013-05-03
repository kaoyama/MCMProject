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
import android.widget.TableLayout;
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
 * Displays Pending Transactions
 * 
 * 
 * 
 */

@SuppressLint("NewApi")
public class PendingTransactions extends Activity {
	/** Called when the activity is first created. */
	
	TableLayout chargeLayout;
	
	PendingTransactions currentThis = this; 
	
	static int TIMEOUT_MILLISEC = 3000; 

	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pendingcharges);
		
		chargeLayout = (TableLayout) findViewById(R.id.chargeLayout);
				
		setUpLayout();
		
		
	}

	public void setUpLayout(){
		//Sets up a title for each column
		TableRow tempTableRow=new TableRow(getBaseContext());
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		tempTableRow.setLayoutParams(lp);
		//Create the titles for the columns	
		TextView titleView = new TextView(this);
		titleView.setText("Customer: ");
		TextView amountView = new TextView(this);
		amountView.setText("Amount: ");
		TextView optionsView = new TextView(this);
		optionsView.setText("Options: ");
		//Adds TextViews to columns
		tempTableRow.addView(titleView);
		tempTableRow.addView(amountView);
		tempTableRow.addView(optionsView);
		
		chargeLayout.addView(tempTableRow);
		
		getCharges();
	}
	
	public void getCharges() {
		
		//Gets merchant name
		String username = CommonUtilities.getUsername(PendingTransactions.this);
		JSONObject jsonIn = new JSONObject();
		
		
		try {
			jsonIn.put("merchant", username);
		} catch (Exception e) {
			Log.v("Merchants", "JSON Exception");
		}
		
		//Pulls pending transactions from database
		final JSONArray jsonArray = RestClient.connectToDatabase(CommonUtilities.PENDINGCHARGES_URL, jsonIn);
		
		if (jsonArray != null) {
			
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
						
					//Puts allpendings transactions in to a table, each with a cancel button
					try {							
						for (int i = 0; i < jsonArray.length(); i++) {
							final String cancelled = jsonArray.getJSONObject(i).getString("cancelled");
							final String paid = jsonArray.getJSONObject(i).getString("paid");
							if(cancelled.equals("0") && paid.equals("0")){
								final String transactionIndex = jsonArray.getJSONObject(i).getString("transactionIndex");
								final String customerName = jsonArray.getJSONObject(i).getString("customer");
								TextView showCustomer = new TextView(currentThis); 
								showCustomer.setText(customerName);
								final String amount = jsonArray.getJSONObject(i).getString("cost");
								TextView showAmount = new TextView(currentThis);
								showAmount.setText(amount);
								LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
								TableRow tempTableRow=new TableRow(getBaseContext());
								
								//Cancel button - allows merchant to cancel a pending transaction
								final Button tempButton= new Button(PendingTransactions.this);
								tempButton.setText("Cancel");
								tempButton.setOnClickListener(new View.OnClickListener(){
									//Cancels transaction in database
									public void onClick(View arg0) {
										JSONObject jsonIn2 = new JSONObject();
										try {
											jsonIn2.put("userName", customerName);
											jsonIn2.put("transactionIndex", transactionIndex);
											jsonIn2.put("cancelled", "1"); 
										} catch (Exception e) {
											Log.v("Merchants", "JSON Exception");
										}
										RestClient.connectToDatabase(CommonUtilities.UPDATEPAYMENT_URL, jsonIn2);
										chargeLayout.removeAllViews();
										setUpLayout();
									}
								});
								tempTableRow.addView(showCustomer);
								tempTableRow.addView(showAmount);
								tempTableRow.addView(tempButton);
								chargeLayout.addView(tempTableRow,lp);
							}
						}
						
					} catch (JSONException e) {
						CustomDialog cd = new CustomDialog(PendingTransactions.this); 
						cd.showNotificationDialog(e.getMessage()); 
					}
					
				}
			});
			
		} else {
			CustomDialog cd = new CustomDialog(PendingTransactions.this); 
			cd.showNotificationDialog("Transaction list is empty.");
		}
        
		
	}
	
}