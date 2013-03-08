package app.localization;

/**
 * Coupons list.  Pulls subscribed coupons and ads from the database.
 * Need service-oriented architecture and needs three elements: 
 * external database, web-service, mobile web-service client. 
 * @author Chihiro
 */

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import app.utilities.CommonUtilities;
import app.utilities.CustomDialog;
import app.utilities.MySQLiteHelper;
import app.utilities.RestClient;

public class Deals extends Activity {
	
	TextView username;
	TextView result; 
	
	String dbResult; 
	
	static int TIMEOUT_MILLISEC = 3000; 
	
	List<String> listContents; 
	ListView myListView; 
	ArrayAdapter<String> adapter; 
	MySQLiteHelper db = new MySQLiteHelper(Deals.this);
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.deals);
		
		listContents = new ArrayList<String>();
		
		myListView = (ListView)findViewById(R.id.dealsList);
						
		// Get list of coupons from database
		db.onUpgrade(db.getReadableDatabase(), 1, 2);
		saveDealsToSQLite(); 
		getDeals();
		
		showSavedDeals(); 
	}
	
	public void showSavedDeals() {
		Cursor cursor = db.all(Deals.this); 
		
		String str;
		cursor.moveToFirst(); 
		while(cursor.isAfterLast() == false) {
			str = cursor.getString(1); 
			Log.d(Deals.class.toString(), str); 
			cursor.moveToNext(); 
		}
	}
	
	public void saveDealsToSQLite() {
		String username = CommonUtilities.getUsername(Deals.this); 
		JSONObject jsonIn = new JSONObject();
		
		try {
			jsonIn.put("userName", username);
		} catch (Exception e) {
			Log.v("Deals", "JSON Exception");
		}
		
		final JSONArray json = RestClient.connectToDatabase(
				app.utilities.CommonUtilities.GETDEALS_URL, jsonIn);
		
		try {
			for (int i = 0; i < json.length(); i++) {
				JSONObject jsonObj = json.getJSONObject(i);
				
				db.insert(
						jsonObj.getString("merchant"),
						jsonObj.getString("title"),
						jsonObj.getString("content"), 
						(int)Integer.valueOf(jsonObj.getString("minAge")), 
						(int)Integer.valueOf(jsonObj.getString("maxAge")), 
						jsonObj.getString("sendTime"),
						jsonObj.getString("targetGender"), 
						Boolean.getBoolean(jsonObj.getString("student")),
						(int)Integer.valueOf(jsonObj.getString("targetLat")),
						(int)Integer.valueOf(jsonObj.getString("targetLon")),
						jsonObj.getString("expDate"),
						Boolean.getBoolean(jsonObj.getString("accepted")),
						Boolean.getBoolean(jsonObj.getString("enabled")));								
			}
		} catch (Exception e) {
			Log.v(Deals.class.getName(), "Exception while saving to database:\n" +
					e.getMessage()); 
			CustomDialog cd3 = new CustomDialog(Deals.this); 
			cd3.showNotificationDialog("EXCEPTION while saving to database");
		}
	}
	
	/**
	 * Update deals list
	 */
	public void getDeals() {
		
		String username = CommonUtilities.getUsername(Deals.this); 
		JSONObject jsonIn = new JSONObject();
		
		try {
			jsonIn.put("userName", username);
		} catch (Exception e) {
			Log.v("Deals", "JSON Exception");
		}
		
		final JSONArray json = RestClient.connectToDatabase(
				app.utilities.CommonUtilities.GETDEALS_URL, jsonIn);
		
		if (json != null) {
			
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
											
					try {							
					    listContents = new ArrayList<String>(json.length());

						for (int i = 0; i < json.length(); i++) {
							listContents.add(json.getJSONObject(i).getString("title")); 								
						}
						
					} catch (JSONException e) {
						CustomDialog cd = new CustomDialog(Deals.this); 
						cd.showNotificationDialog(e.getMessage()); 
					}
					
					adapter = new ArrayAdapter<String>(Deals.this, 
							android.R.layout.simple_list_item_1, listContents); 
					adapter.setNotifyOnChange(true); 
					myListView.setAdapter(adapter); 
					
					// Each deal item can be clicked on and taken to another page 
					myListView.setOnItemClickListener(new OnItemClickListener() {
						
						@Override
						public void onItemClick(AdapterView<?> a, View v,
								int position, long id) {
							Intent dealDetailIntent = new Intent(Deals.this, DealDetail.class); 
							try {
								dealDetailIntent.putExtra("title", json.getJSONObject(position).getString("title"));
								dealDetailIntent.putExtra("content", json.getJSONObject(position).getString("content"));
								startActivity(dealDetailIntent); 
							} catch (JSONException e) {
								Log.d("Deals.java", "JSON EXCEPTION"); 
								e.printStackTrace();
							}
						}
					});
				}
			});
			
		} else {
			CustomDialog cd = new CustomDialog(Deals.this); 
			cd.showNotificationDialog("Deals list is empty.");
		}
	}
}
