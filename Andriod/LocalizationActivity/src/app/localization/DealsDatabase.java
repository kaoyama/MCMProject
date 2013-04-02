package app.localization;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import app.utilities.CommonUtilities;
import app.utilities.CustomDialog;
import app.utilities.MySQLiteHelper;
import app.utilities.RestClient;

public class DealsDatabase extends Activity {
	MySQLiteHelper db = new MySQLiteHelper(DealsDatabase.this);
	public Context DealsDatabaseThis = DealsDatabase.this; 

	public void saveDealsToSQLite() {
		
		// wipe existing database data
		db.onUpgrade(db.getReadableDatabase(), 1, 2); // wipe SQLite data
		
		String username = CommonUtilities.getUsername(DealsDatabase.this); 
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
						(int)Integer.valueOf(jsonObj.getString("dealIndex")),
						jsonObj.getString("merchant"),
						jsonObj.getString("title"),
						jsonObj.getString("content"));								
			}
		} catch (Exception e) {
			Log.v(Deals.class.getName(), "Exception while saving to database:\n" +
					e.getMessage()); 
			CustomDialog cd3 = new CustomDialog(DealsDatabase.this); 
			cd3.showNotificationDialog("EXCEPTION while saving to database");
		}
	}

}
