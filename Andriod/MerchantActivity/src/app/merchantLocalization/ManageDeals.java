package app.merchantLocalization;

import java.io.FileInputStream;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import app.utilities.CommonUtilities;
import app.utilities.CustomDialog;
import app.utilities.RestClient;



@SuppressLint("NewApi")
public class ManageDeals extends Activity {
	/** Called when the activity is first created. */

		
	LinearLayout dealsLayout;
	
	ManageDeals currentThis = this; 
	
	static int TIMEOUT_MILLISEC = 3000; 

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.managedeals);
		
		dealsLayout = (LinearLayout)findViewById(R.id.dealsLayout);
		
		getDeals(); 
	}

	/**
	 * Connect to webservice (database) 
	 */
	public void getDeals() {
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
			CustomDialog cd = new CustomDialog(ManageDeals.this); 
			cd.showNotificationDialog("Could not get username.");
		}
		  
		JSONArray jsonArray = null;
		try {
			JSONObject json = new JSONObject();
			json.put("merchant", userName);

			jsonArray = RestClient.connectToDatabase(
					CommonUtilities.MerchantDeals_URL, json);
			
		} catch (Exception e) {
			CustomDialog cd3 = new CustomDialog(ManageDeals.this); 
			cd3.showNotificationDialog("Failed here");
		}
		
		
		try {
			for(int i = 0; i < jsonArray.length(); i++){
				final String title = jsonArray.getJSONObject(i).getString(
						"title");
				TextView showDeal = new TextView(currentThis);
				showDeal.setText("Deal Title: " + title + "\n");
				LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				dealsLayout.addView(showDeal,lp);						
			}
		}

        
		catch (Exception e) {
			CustomDialog cd3 = new CustomDialog(ManageDeals.this); 
			cd3.showNotificationDialog("Invalid: " + e.getMessage());
		}
		
		
	}
}

	

