package app.localization;

/**
 * Coupons list.  Pulls subscribed coupons and ads from the database.
 * Need service-oriented architecture and needs three elements: 
 * external database, web-service, mobile web-service client. 
 * @author Chihiro
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
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

	public static int MERCHANT = 0; 
	public static int TITLE = 1;
	public static int CONTENT = 2; 
	public static int LATITUDE = 3; 
	public static int LONGITUDE = 4; 
	public static int DEALINDEX = 5; 
	
	SparseIntArray indexMap = new SparseIntArray();
	int index = 0; 
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.deals);
		
		listContents = new ArrayList<String>();
		
		myListView = (ListView)findViewById(R.id.dealsList);
						
		// get list of coupons from database and save to SQLite 
		db.onUpgrade(db.getReadableDatabase(), 1, 2); // wipe SQLite data
		saveDealsToSQLite(); 
		
		// show list of deals as ListView on Deals page
		showSavedDeals(); 
		
		Log.d(Deals.class.toString(), "Updating database from Deals page"); 
	}
	
	/**
	 * Show saved deals from the Android SQLite database
	 */
	public void showSavedDeals() {
			
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				
				listContents = new ArrayList<String>();
				
				Cursor cursor = db.all(Deals.this); 
				String title;
				int dealIndex; 
				
				cursor.moveToFirst(); 
				
				// refresh index and mappping of position to dealIndex 
				index = 0; 
				indexMap = new SparseIntArray();
				
				try {
							
					while(cursor != null && cursor.isAfterLast() == false) {
						
						// warning: type (getString, getInt) must match column type 
						title = cursor.getString(1); 
						dealIndex = cursor.getInt(2); 
						
						Log.d(Deals.class.toString(), "title: " + title + " where dealIndex: " + dealIndex); 
						
						listContents.add(title); 
								
						// save listContents index to hashmap 
						indexMap.put(index,  dealIndex); 
						index++; 
											
						cursor.moveToNext(); 
					}
				} catch (Exception e) {
					Log.e(Deals.class.toString(), "Exception while showing saved deals:\n" + e.getLocalizedMessage()); 
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
							
							Log.d(Deals.class.toString(), "Requesting deals for position: " + position); 
							Log.d(Deals.class.toString(), "IndexMap at position is: " + indexMap.get(position)); 
							
							Cursor cursor = db.dealContent(Deals.this, indexMap.get(position)); 
							
							// get title and content and put in Android list
							cursor.moveToFirst(); 
							dealDetailIntent.putExtra("title", cursor.getString(TITLE));
							dealDetailIntent.putExtra("content", cursor.getString(CONTENT));
							startActivity(dealDetailIntent); 
							
						} catch (Exception e) {
							Log.e(Deals.class.toString(), "Exception showing specific deals\n" + e.getMessage());
						}
						
					}
				});
			}
		});
		
		
	}
	
	/**
	 * Save deals from database to SQLite, the database on the Android device 
	 */
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
				
				//TODO: Change to match what script returns
				db.insert(
						(int)Integer.valueOf(jsonObj.getString("dealIndex")),
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
			
			db.close(); 
		} catch (Exception e) {
			Log.v(Deals.class.getName(), "Exception while saving to database:\n" +
					e.getMessage()); 
			CustomDialog cd3 = new CustomDialog(Deals.this); 
			cd3.showNotificationDialog("EXCEPTION while saving to database");
		}
	}
	
}
