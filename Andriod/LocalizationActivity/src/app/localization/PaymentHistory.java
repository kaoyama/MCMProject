package app.localization;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import app.utilities.*;
/**
 * Notifications list.  Pulls subscribed or nearby merchant information from the database. 
 * Need service-oriented architecture and needs three elements: 
 * external database, web-service, mobile web-service client. 
 * @author Chihiro
 */

public class PaymentHistory extends Activity {
	
	PaymentHistory currentThis = this;
	LinearLayout paymentHistoryLayout;
	
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.payments);
		paymentHistoryLayout = (LinearLayout)findViewById(R.id.paymentLayout);
		
		// Get list of notifications from database
		getData(); 
	}


	public void getData() {
		String userName = "";
		//Grab Username from Android DB
		try {
			FileInputStream fis = openFileInput("username_file");
			StringBuffer sb = new StringBuffer("");
			int ch;
			while((ch = fis.read())!= -1){
				sb.append((char)ch);
			}
			fis.close();
			userName = sb.toString();
		} catch (Exception e) {
			CustomDialog cd = new CustomDialog(PaymentHistory.this); 
			cd.showNotificationDialog("Could not get username.");
		}
		
		JSONArray jsonArray = null;
		try {
			JSONObject json = new JSONObject();
			json.put("userName", userName);

			jsonArray = RestClient.connectToDatabase(
					CommonUtilities.PAYMENTS_URL, json);
			
		} catch (Exception e) {
			CustomDialog cd3 = new CustomDialog(PaymentHistory.this); 
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
				if(paid.equals("1")){
					TextView paidCharge = new TextView(currentThis);
					paidCharge.setText("Purchased: " + productIndex + "\nFrom: " + merchant + "\n" + purchaseTime);
					LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
					paymentHistoryLayout.addView(paidCharge,lp);
							
				}
			}
		}

        
		catch (Exception e) {
			CustomDialog cd3 = new CustomDialog(PaymentHistory.this); 
			cd3.showNotificationDialog("Invalid: " + e.getMessage());
		}
		
		
	}
}
