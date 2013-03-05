package app.merchantLocalization;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import app.utilities.CommonUtilities;
import app.utilities.CustomDialog;
import app.utilities.RestClient;



@SuppressLint("NewApi")
public class ManageDeals extends Activity {
	/** Called when the activity is first created. */

	TextView username;
	TextView result; 
	
	String dbResult; 
	
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
			json.put("userName", userName);

			jsonArray = RestClient.connectToDatabase(
					CommonUtilities.ADS_URL, json);
			
		} catch (Exception e) {
			CustomDialog cd3 = new CustomDialog(ManageDeals.this); 
			cd3.showNotificationDialog("Failed here");
		}
		
		try {
			for(int i = 0; i < jsonArray.length(); i++){
				final String merchant = jsonArray.getJSONObject(i).getString(
						"merchant");
				
				final String productIndex = jsonArray.getJSONObject(i).getString(
						"productIndex");
				
				final String purchaseTime = jsonArray.getJSONObject(i).getString(
						"purchaseTime");
				
				final String cost = jsonArray.getJSONObject(i).getString(
						"cost");
				
				final String paid = jsonArray.getJSONObject(i).getString(
						"paid");
				if(paid.equals("1")){
					TextView showAd = new TextView(currentThis);
					showAd.setText("Purchased: " + productIndex + "\nFrom: " + merchant + "\n" + purchaseTime);
					LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
					adsLayout.addView(showAd,lp);
							
				}
			}
		}

        
		catch (Exception e) {
			CustomDialog cd3 = new CustomDialog(ManageDeals.this); 
			cd3.showNotificationDialog("Invalid: " + e.getMessage());
		}
		
		
	}
}

	

