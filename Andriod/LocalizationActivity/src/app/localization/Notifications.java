package app.localization;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import app.utilities.*;
/**
 * Notifications list.  Pulls subscribed or nearby merchant information from the database. 
 * Need service-oriented architecture and needs three elements: 
 * external database, web-service, mobile web-service client. 
 * @author Chihiro
 */

public class Notifications extends Activity {
	/** Called when the activity is first created. */

	TextView username;
	TextView result; 
	
	String dbResult; 
	
	static int TIMEOUT_MILLISEC = 3000; 
	
	List<String> listContents; 
	ListView myListView; 
	ArrayAdapter<String> adapter; 
	Notifications currentThis = this; 
	Intent intent; 
	Bundle b; 
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.notifications);
		
		// Get list of notifications from database
		getData(); 
	}

	/**
	 * Connect to web service (database) 
	 */
	public void getData() {
		
		final JSONArray jsonArray = RestClient.connectToDatabase(CommonUtilities.NOTIFICATION_URL, null);
		
		if (jsonArray != null) {
			
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
											
					try {							
					    listContents = new ArrayList<String>(jsonArray.length());

						for (int i = 0; i < jsonArray.length(); i++) {
							listContents.add(jsonArray.getJSONObject(i).getString("merchantUserName")); 								
						}
						
						// Save this information to be used in the Merchant Map page 
						b.putString("merchantInfo", jsonArray.toString()); 
						intent.putExtras(b); 	
						
					} catch (JSONException e) {
						app.utilities.CustomDialog cd = new app.utilities.CustomDialog(Notifications.this); 
						cd.showNotificationDialog(e.getMessage()); 
					}
					
					adapter = new ArrayAdapter<String>(currentThis, 
							android.R.layout.simple_list_item_1, listContents); 
					adapter.setNotifyOnChange(true); 
					myListView.setAdapter(adapter); 
				}
			});
			
		} else {
			app.utilities.CustomDialog cd = new app.utilities.CustomDialog(Notifications.this); 
			cd.showNotificationDialog("Notification list is empty.");
		}
	}
}
