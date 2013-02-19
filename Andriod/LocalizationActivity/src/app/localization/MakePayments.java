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

public class MakePayments extends Activity {
	
	MakePayments currentThis = this;
	LinearLayout paymentLayout;
	
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.payments);
		paymentLayout = (LinearLayout)findViewById(R.id.paymentLayout);
		
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
			CustomDialog cd = new CustomDialog(MakePayments.this); 
			cd.showNotificationDialog("Could not get username.");
		}
		
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
				if(paid.equals("1")){


					Button tempButton = new Button(currentThis);
					tempButton.setText(merchant + " requests payment for: " + productIndex);
					tempButton.setOnClickListener(new View.OnClickListener(){

					public void onClick(View arg0) {
							AlertDialog.Builder builder = new AlertDialog.Builder(currentThis);
							builder.setMessage("Would you like to purchase " + productIndex + " for " + cost + "?")
										       .setTitle("Make Payment");
											builder.setPositiveButton("Purchase", new DialogInterface.OnClickListener() {
										           public void onClick(DialogInterface dialog, int id) {
										               /*
										        	   try{
										        	   JSONObject json = new JSONObject();
														json.put("userName", customerName);
														HttpParams httpParams = new BasicHttpParams();
												        HttpConnectionParams.setConnectionTimeout(httpParams,
												                TIMEOUT_MILLISEC);
												        HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
												        HttpClient client = new DefaultHttpClient(httpParams);
												        //
												        //String url = "http://10.0.2.2:8080/sample1/webservice2.php?" + 
												        //             "json={\"UserName\":1,\"FullName\":2}";
												        String url = "http://dana.ucc.nau.edu/~cs854/PHPToggleCustomerNotification.php";

												        HttpPost request = new HttpPost(url);
														request.setEntity(new ByteArrayEntity(json.toString().getBytes(
															        "UTF8")));														
												        request.setHeader("json", json.toString());
												        HttpResponse response;
														response = client.execute(request);
												        HttpEntity entity = response.getEntity();
										        	   
										           } catch (Throwable t) {
												        //Toast.makeText(this, "Request failed: " + t.toString(),
												        //        Toast.LENGTH_LONG).show();
												    }
										        	    Builder builder2 = new AlertDialog.Builder(currentThis); 
											    		builder2.setMessage("You have successfully charged " + customerName + "." );
											    		builder2.setCancelable(false); 
											    		builder2.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
													           public void onClick(DialogInterface dialog, int id) {
													               // User clicked OK button
													           }}); 
											    		AlertDialog dialog2 = builder2.create();
											    		dialog2.show();
											    		*/
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
					
				else{
					TextView paidCharge = new TextView(currentThis);
					paidCharge.setText("Paid: " + merchant + "\nFor: " + productIndex + "\nAt: " + purchaseTime);
					LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
					paymentLayout.addView(paidCharge,lp);
				}
			}
		}

        
		catch (Exception e) {
			CustomDialog cd3 = new CustomDialog(MakePayments.this); 
			cd3.showNotificationDialog("Invalid: " + e.getMessage());
		}
		
		
	}
}
