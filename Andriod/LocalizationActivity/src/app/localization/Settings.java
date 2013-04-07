package app.localization;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import app.utilities.CommonUtilities;
import app.utilities.CustomDialog;
import app.utilities.RestClient;

public class Settings extends Activity {
	TextView username;
	TextView result; 
	
	static int TIMEOUT_MILLISEC = 3000; 
	
	List<String> listContents; 
	ListView myListView; 
	ArrayAdapter<String> adapter; 
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		
		listContents = new ArrayList<String>();
		
		//myListView = (ListView)findViewById(R.id.dealsList);
		
		RadioGroup opt = (RadioGroup)findViewById(R.id.radioGroup_opt);
	}
	
	public void onCheckboxClicked(View view) {
		// Is the view now checked?
	    boolean checked = ((CheckBox) view).isChecked();
	    
	    // Check which checkbox was clicked
	    switch(view.getId()) {
	        case R.id.checkbox_localization:
	        	if (checked) { 
	        		// enable localization
	            } else {
	            	// disable localization
	            }
	        break;
	    }
	}
	
	public void onRadioButtonClicked(View view) {
	    // Is the button now checked?
	    boolean checked = ((RadioButton) view).isChecked();
	    
	    // Check which radio button was clicked
	    switch(view.getId()) {
	        case R.id.radio_optInAll:
	            if (checked) {
	            	optInOut(Settings.this, CommonUtilities.OPTINALL_URL); 
	            }
	            break;
	        case R.id.radio_optOutAll:
	            if (checked) {
	            	optInOut(Settings.this, CommonUtilities.OPTOUTALL_URL);
	            }
	            break;
	        case R.id.radio_optInSome:
	        	if (checked) {
	        		// Starting a new intent
					Intent settingsScreen = new Intent(getApplicationContext(), SettingsLocalization.class);
					startActivity(settingsScreen); 	      
	        	}
        		break;
	    }
	}

	/**
	 * Opt in or out, depending on URL
	 * @param url
	 */
	public static void optInOut(Activity context, String url) {
		String userName = CommonUtilities.getUsername(context); 
		JSONObject json = new JSONObject();
		try {
			json.put("userName", CommonUtilities.getUsername(context)); 
	
			RestClient.connectToDatabase(url, json);
	
		} catch (Exception e) {
			Log.v("Opt in/out all deals", "Exception while opting in/out all deals");
		}
	}
}

// To make it an actual Android settings page, uncomment below and comment above
/*
public class Settings extends PreferenceActivity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {        
        super.onCreate(savedInstanceState);        
        addPreferencesFromResource(R.xml.preferences);        
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, 0, 0, "Show current settings");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                startActivity(new Intent(this, SettingsLocalization.class));
                return true;
        }
        return false;
    }
    
}
*/