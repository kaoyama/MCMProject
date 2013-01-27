package app.localization;

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

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Merchant list.  Pulls subscribed or nearby merchant information from the database. 
 * Need service-oriented architecture and needs three elements: 
 * external database, web-service, mobile web-service client. 
 * @author Chihiro
 * 
 * 
 * Notes:
 * For connection to work, Apache server must be handled to start PHP. 
 * Also, make sure NAU Wi-Fi is connected on the device. 
 * 
 * IP address changes for each Wi-Fi access! 
 *
 */

public class Merchant extends Activity {
	/** Called when the activity is first created. */

	TextView username;
	TextView result; 
	
	String dbResult; 
	
	static int TIMEOUT_MILLISEC = 3000; 
	
	List<String> listContents; 
	ListView myListView; 
	ArrayAdapter<String> adapter; 
	Merchant currentThis = this; 
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.merchant);
		
		listContents = new ArrayList<String>(4);
		listContents.add("First item"); 
		
		myListView = (ListView)findViewById(R.id.merchantList);
		
		
		getData(); 
		
	}

	/**
	 * Connect to webservice (database) 
	 */
	public void getData() {
		new LongRunningGetIO().execute(); 
	}

	private class LongRunningGetIO extends AsyncTask <Void, Void, String> {

		protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
			InputStream in = entity.getContent();
			StringBuffer out = new StringBuffer();
			int n = 1;
			while (n>0) {
				byte[] b = new byte[4096];
				n =  in.read(b);
				if (n>0) out.append(new String(b, 0, n));
			}
			return out.toString();
		}

		@Override
		protected String doInBackground(Void... params) {
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();

			// can't use localhost, since localhost refers to the device itself 
			// (in this case the android device being tested). 
			// Use 10.0.2.2 for emulator 
			// Use own IP for device: 192.168.0.9
			// cd C:\Windows\System32
			// ipconfig 
			// Look at 10.1.64.169
			//HttpGet httpGet = new HttpGet("http://10.1.64.169/PHPQuery.php");
			HttpGet httpGet = new HttpGet("http://dana.ucc.nau.edu/~cs854/PHPGetNearbyMerchants.php");
			String text = null;
			try {

				HttpResponse response = httpClient.execute(httpGet, localContext);
				HttpEntity entity = response.getEntity();
				text = getASCIIContentFromEntity(entity);

			} catch (Exception e) {
				return e.getLocalizedMessage();
			}
			return text;
		}	

		protected void onPostExecute(final String results) {
			if (results!=null) {
				
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
					//	EditText et = (EditText)findViewById(R.id.databaseText);
					//	et.setText("Database connection worked!: " + results);
												
						try {							
							JSONArray jsonArray = new JSONArray(results);
						    listContents = new ArrayList<String>(jsonArray.length());

							for (int i = 0; i < jsonArray.length(); i++) {
								listContents.add(jsonArray.getJSONObject(i).getString("merchantUserName")); 								
							}
							
							// Save this information to be used in the Merchant Map page 
							Intent intent = new Intent(Merchant.this, MerchantMap.class); 
							Bundle b = new Bundle(); 
							b.putString("merchantInfo", jsonArray.toString()); 
							intent.putExtras(b); 
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						adapter = new ArrayAdapter<String>(currentThis, 
								android.R.layout.simple_list_item_1, listContents); 
						adapter.setNotifyOnChange(true); 
						myListView.setAdapter(adapter); 
					}
				});
				
			} else {
				//TODO: Error notification of some sort 
				//EditText et = (EditText)findViewById(R.id.databaseText);
				//et.setText("Database connection failed");
			}
		}
	}
}
