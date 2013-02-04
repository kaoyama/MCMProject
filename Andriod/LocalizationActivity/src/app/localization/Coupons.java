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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Coupons extends Activity {
	
	TextView username;
	TextView result; 
	
	String dbResult; 
	
	static int TIMEOUT_MILLISEC = 3000; 
	
	List<String> listContents; 
	ListView myListView; 
	ArrayAdapter<String> adapter; 
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.coupons);
		
		listContents = new ArrayList<String>();
		
		myListView = (ListView)findViewById(R.id.couponsList);
						
		// Get list of coupons from database
		//getData(); 
	}

	/**
	 * Connect to webservice (database) 
	 */
	public void getData() {
		new LongRunningGetIO().execute(); 
	}

	private class LongRunningGetIO extends AsyncTask <Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			
			//TODO: Change PHP script 
			JSONArray json = RestClient.connectToDatabase(
					"http://dana.ucc.nau.edu/~cs854/PHPGetCoupons.php", 
					null, Coupons.this);
			
			if(json != null) {
				return json.toString();
			} 
			return null; 
		}	

		protected void onPostExecute(final String results) {
			if (results!=null) {
				
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
												
						try {							
							JSONArray jsonArray = new JSONArray(results);
						    listContents = new ArrayList<String>(jsonArray.length());

							for (int i = 0; i < jsonArray.length(); i++) {
								//TODO: Change merchants --> Coupons
								listContents.add(jsonArray.getJSONObject(i).getString("merchantUserName")); 								
							}
							
						} catch (JSONException e) {
							CustomDialog cd = new CustomDialog(Coupons.this); 
							cd.showNotificationDialog(e.getMessage()); 
						}
						
						adapter = new ArrayAdapter<String>(Coupons.this, 
								android.R.layout.simple_list_item_1, listContents); 
						adapter.setNotifyOnChange(true); 
						myListView.setAdapter(adapter); 
					}
				});
				
			} else {
				CustomDialog cd = new CustomDialog(Coupons.this); 
				cd.showNotificationDialog("Coupons list is empty.");
			}
		}
	}
}
