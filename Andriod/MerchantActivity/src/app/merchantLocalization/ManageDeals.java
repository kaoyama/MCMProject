package app.merchantLocalization;

import java.io.FileInputStream;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;
import app.utilities.CommonUtilities;
import app.utilities.CustomDialog;
import app.utilities.RestClient;



@SuppressLint("NewApi")
public class ManageDeals extends Activity {
	/** Called when the activity is first created. */

	String tempEnabled = null;
	
	LinearLayout dealsLayout;
	
	ManageDeals currentThis = this; 
	
	static int TIMEOUT_MILLISEC = 3000; 

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.managedeals);
		
		dealsLayout = (LinearLayout)findViewById(R.id.dealsLayout);
		
		//Sets up a title for each column
		TableRow tempTableRow=new TableRow(getBaseContext());
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		tempTableRow.setLayoutParams(lp);
		//Create the titles forthe columns	
		TextView titleView = new TextView(this);
		titleView.setText("Title:");
		TextView optionsView = new TextView(this);
		optionsView.setText("Options:");
		//Adds TextViews to columns
		tempTableRow.addView(titleView);
		tempTableRow.addView(optionsView);
		
		dealsLayout.addView(tempTableRow);
		
		getDeals(); 
	}

	/**
	 * Connect to web service (database) 
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
					CommonUtilities.MERCHANTDEALS_URL, json);
			
		} catch (Exception e) {
			CustomDialog cd3 = new CustomDialog(ManageDeals.this); 
			cd3.showNotificationDialog("Failed here");
		}
		
		
		try {
			for(int i = 0; i < jsonArray.length(); i++){
				final String dealIndex = jsonArray.getJSONObject(i).getString(
						"dealIndex");
				final String title = jsonArray.getJSONObject(i).getString(
						"title");
				final String enabled = jsonArray.getJSONObject(i).getString(
						"enabled");
				tempEnabled = enabled;
				
				TextView showDeal = new TextView(currentThis);
				showDeal.setText(title + "\n\n");
				LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				TableRow tempTableRow=new TableRow(getBaseContext());
				final ToggleButton tempButton = new ToggleButton(this);
				tempButton.setTextOn("Enabled");
				tempButton.setTextOff("Disabled");
				tempTableRow.setLayoutParams(lp);
				
			   if(tempEnabled.equals("0")){
					tempButton.setChecked(true);
					tempButton.setText("Enabled");
				}
			   else{
				   tempButton.setChecked(false);
				   tempButton.setText("Disabled");
			   }
				
				tempButton.setOnCheckedChangeListener(new OnCheckedChangeListener(){
						
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						JSONArray jsonArray2 = null;
						try {
			       			JSONObject json = new JSONObject();
			       			json.put("dealIndex", dealIndex);
			       			json.put("enabled", tempEnabled);
			       			if(tempEnabled.equals("0")) {
			       				json.put("enabled","1");
			       				tempEnabled = "1";
			       				jsonArray2 = RestClient.connectToDatabase(
			       						CommonUtilities.TOGGLEDEALSTATUS_URL, json);
			       				
			       			}
			       			else{
			       				json.put("enabled", "0");
			       				tempEnabled = "0";
			       				jsonArray2 = RestClient.connectToDatabase(
			       						CommonUtilities.TOGGLEDEALSTATUS_URL, json);
			       			}
			    
			       			
			       		} catch (Exception e) {
			       			CustomDialog cd3 = new CustomDialog(ManageDeals.this); 
			       			cd3.showNotificationDialog("Failed To Update Deal.");
			       		}
					}
						
				});	
				tempTableRow.addView(showDeal);
				tempTableRow.addView(tempButton);
				dealsLayout.addView(tempTableRow,lp);						
			}	
		}

        
		catch (Exception e) {
			CustomDialog cd3 = new CustomDialog(ManageDeals.this); 
			cd3.showNotificationDialog("Invalid: " + e.getMessage());
		}
		
		
	}
}

	

