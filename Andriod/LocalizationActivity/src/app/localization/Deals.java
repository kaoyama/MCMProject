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

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import app.utilities.CustomDialog;
import app.utilities.RestClient;

import app.utilities.CommonUtilities;

public class Deals extends Activity {
	
	TextView username;
	TextView result; 
	
	String dbResult; 
	
	static int TIMEOUT_MILLISEC = 3000; 
	
	List<String> listContents; 
	ListView myListView; 
	ArrayAdapter<String> adapter; 
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.deals);
		
		listContents = new ArrayList<String>();
		
		myListView = (ListView)findViewById(R.id.dealsList);
						
		// Get list of coupons from database
		getDeals(); 
	}
	
	/**
	 * Update deals list
	 */
	public void getDeals() {
		final JSONArray json = RestClient.connectToDatabase(
				app.utilities.CommonUtilities.GETDEALS_URL, null);
		
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
