package app.localization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import app.utilities.CommonUtilities;
import app.utilities.CustomDialog;
import app.utilities.RestClient;

public class SettingsLocalization extends Activity {

	List<String> listContents; 
	ListView myListView; 
	ArrayAdapter<String> adapter;
	List<Boolean> checkBoxList;
	
	private Button checkAllButton, clearAllButton; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_localization);
		
		// how to get shared preferences in case settings is an actual Android setting
		/*
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

		StringBuilder builder = new StringBuilder();

		builder.append("\nEnable localization: " + sharedPrefs.getBoolean("perform_localization", false));
		builder.append("\nOpt out of all targeted deals: " + sharedPrefs.getBoolean("opt_out_all", false));
		builder.append("\nSpecific merchants: " + sharedPrefs.getString("specific_merchants", "NULL"));

		TextView settingsTextView = (TextView) findViewById(R.id.settings_text_view);
		settingsTextView.setText(builder.toString());
		*/
		
		checkAllButton = (Button)findViewById(R.id.btnCheckAll);
        clearAllButton = (Button)findViewById(R.id.btnClearAll);
		
		listContents = new ArrayList<String>();
		myListView = (ListView)findViewById(R.id.allMerchantList);
		checkBoxList = new ArrayList<Boolean>();
		
		// Get list of merchants from database
		getMerchants(); 
		
		checkAllButton.setOnClickListener(new OnClickListener() 
        {           
            @Override
            public void onClick(View arg0) {
                for(int i=0 ; i < myListView.getAdapter().getCount(); i++) {
                    myListView.setItemChecked(i, true);      
                    Settings.optInOut(SettingsLocalization.this, CommonUtilities.OPTINALL_URL); 
                    
                    checkBoxList.set(i, true); 
                }
            }
        });
		
		clearAllButton.setOnClickListener(new OnClickListener() 
        {           
            @Override
            public void onClick(View v) 
            {
                for(int i=0 ; i < myListView.getAdapter().getCount(); i++)
                {
                    myListView.setItemChecked(i, false);
                    Settings.optInOut(SettingsLocalization.this, CommonUtilities.OPTOUTALL_URL);

                    checkBoxList.set(i, false); 
                }
            }
        });
	}
	
	public void getMerchants() {
		
		String username = CommonUtilities.getUsername(SettingsLocalization.this); 
		JSONObject jsonIn = new JSONObject();
		
		try {
			jsonIn.put("userName", username);
		} catch (Exception e) {
			Log.v("Deals", "JSON Exception");
		}
		
		final JSONArray jsonArray = RestClient.connectToDatabase(CommonUtilities.ALLMERCHANTS_URL, null);
		final JSONArray subscribedJsonArray = RestClient.connectToDatabase(CommonUtilities.ALLSUBSCRIBEDMERCHANTS_URL, jsonIn);
		
		if (jsonArray != null) {
			
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
											
					try {							
					    listContents = new ArrayList<String>(jsonArray.length());

						for (int i = 0; i < jsonArray.length(); i++) {
							String merchantName = jsonArray.getJSONObject(i).getString("userName");
							listContents.add(merchantName); 
						}
						
					} catch (JSONException e) {
						CustomDialog cd = new CustomDialog(SettingsLocalization.this); 
						cd.showNotificationDialog(e.getMessage()); 
					}
					
					adapter = new ArrayAdapter<String>(SettingsLocalization.this, 
							android.R.layout.simple_list_item_multiple_choice, listContents); 
					adapter.setNotifyOnChange(true); 
					
					myListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
					myListView.setAdapter(adapter); 
					
					Log.i("TAG", "GOT HERE"); 
					try {
						for (int i = 0; i < jsonArray.length(); i++) {
							String merchantName = jsonArray.getJSONObject(i).getString("userName");
							Log.i("TAG", "merchant Name = " + merchantName); 
							
							Log.i("TAG", "SUBSCRIBED JSON = " + subscribedJsonArray.toString()); 
							Log.i("TAG", "subscribedJsonArray length = " + subscribedJsonArray.length()); 
							boolean found = false;
							for (int j = 0; j < subscribedJsonArray.length(); j++) {
								Log.i("TAG", "subscribed merchant = " + subscribedJsonArray.getJSONObject(j).getString("merchant"));
								if (merchantName.equals(subscribedJsonArray.getJSONObject(j).getString("merchant"))) {
									found = true; 
								}
							}
							if (found == true) {
								checkBoxList.add(true); 
								myListView.setItemChecked(i, true); 
								Log.i("TAG", "Inserted checkbox");
							} else {
								checkBoxList.add(false); 
								myListView.setItemChecked(i, false);
								Log.i("TAG", "Inserted clear checkbox");
							}
						}
					} catch (Exception e) {
						Log.i("TAG", "Exception while adding checkbox default values");
						Log.i("TAG", e.toString());
					}
					
					// Each deal item can be clicked on and taken to another page 
					myListView.setOnItemClickListener(new OnItemClickListener() {
						
						@Override
						public void onItemClick(AdapterView<?> a, View v,
								int position, long id) {
							String merchantName = adapter.getItem(position); 
							
							checkBoxList.set(position, !checkBoxList.get(position)); 
							
							Log.i("TAG", merchantName + " is = " + checkBoxList.get(position)); 
							
							// save in database if checked
							
							String userName = CommonUtilities.getUsername(SettingsLocalization.this); 
							JSONObject json = new JSONObject();
							try {
								String bool;
								if (checkBoxList.get(position)) {
									bool = "1";
								} else {
									bool = "0";
								}
								json.put("userName", CommonUtilities.getUsername(SettingsLocalization.this)); 
								json.put("merchant", merchantName); 
								json.put("optIn", bool);
						
								RestClient.connectToDatabase(CommonUtilities.OPTINSOME_URL, json);
						
							} catch (Exception e) {
								Log.v("Opt in/out all deals", "Exception while opting in/out all deals");
							}
						}
					});
				}
			});
			
		} else {
			CustomDialog cd = new CustomDialog(SettingsLocalization.this); 
			cd.showNotificationDialog("Merchant list is empty.");
		}
}

}

