package app.merchantLocalization;

import java.io.FileOutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import app.utilities.CommonUtilities;
import app.utilities.CustomDialog;
import app.utilities.RestClient;

public class MerchantLocalizationActivity extends Activity {
    /** Called when the activity is first created. */
	EditText inputUserName;
    EditText inputPassword;
    
    protected static final int TIMEOUT_MILLISEC = 3000;
    
    TextView username;
    
    MerchantLocalizationActivity currentThis = this; 
    
    public void onBackPressed() {
    }
    
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        inputUserName = (EditText) findViewById(R.id.username);
        inputPassword = (EditText) findViewById(R.id.password);
        Button loginButton = (Button) findViewById(R.id.loginButton);

		// Listening to button event
		loginButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				login();
			}
		});
	}

	public void login() {
		
		String userName = inputUserName.getText().toString();
		String password = inputPassword.getText().toString();
		JSONArray jsonArray = null;

		try {
			JSONObject json = new JSONObject();
			json.put("userName", userName);
			json.put("pwd", password);
			json.put("customer", 0);

			jsonArray = RestClient.connectToDatabase(
					CommonUtilities.LOGIN_URL, json);

		} catch (Exception e) {
			// TODO: JSON EXCEPTION
		}
		
		try {							
			String validated = jsonArray.getJSONObject(0).getString(
					"result");
			
			if (validated.equals("1")) {
            	//Starting a new Intent
            	String fileName = "username_file";
            	String fileData = userName;

            	FileOutputStream fos = openFileOutput(fileName, currentThis.MODE_PRIVATE);
            	fos.write(fileData.getBytes());
            	fos.close();
            	
                Intent homeScreen = new Intent(getApplicationContext(), HomeActivity.class);			 
                startActivity(homeScreen);
            } else {
            	CustomDialog cd = new CustomDialog(MerchantLocalizationActivity.this); 
				cd.showNotificationDialog("Incorrect Username/Password combination."); 
            }
        
		} catch (Exception e) {
			CustomDialog cd = new CustomDialog(MerchantLocalizationActivity.this); 
			cd.showNotificationDialog("Invalid: " + e.getMessage());
		}
	}
    
}