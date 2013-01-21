package app.localization;


import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color;
import android.location.*;


public class HomeActivity extends Activity {
	protected static final int TIMEOUT_MILLISEC = 3000;
	static long MILLION = 1000000; 
	
	/** Called when the activity is first created. */


	TextView username;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		Button mapButton = (Button) findViewById(R.id.mapButton);

		//Listening to button event
		mapButton.setOnClickListener(new View.OnClickListener(){

			public void onClick(View arg0) {
				//Starting a new Intent
				Intent mapScreen = new Intent(getApplicationContext(), MerchantMap.class);

				startActivity(mapScreen);

			}
		});

		Button merchantButton = (Button) findViewById(R.id.merchantButton);

		//Listening to button event
		merchantButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				// Starting a new intent
				Intent merchantScreen = new Intent(getApplicationContext(), Merchant.class);

				startActivity(merchantScreen); 
			}
		});

		final Button gpsButton = (Button) findViewById(R.id.gpsButton);

		//Listening to button event
		gpsButton.setOnClickListener(new View.OnClickListener(){

			public void onClick(View arg0) {
				LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

				LocationListener myLocationListener=new LocationListener() {
					public void onLocationChanged(Location loc) {
						//sets and displays the lat/long when a location is provided
						String latlong = "Lat: " + loc.getLatitude() + " Long: " + loc.getLongitude();   
						gpsButton.setText(latlong);
					}

					public void onProviderDisabled(String provider) {
						// required for interface, not used
					}

					public void onProviderEnabled(String provider) {
						// required for interface, not used
					}

					public void onStatusChanged(String provider, int status,
							Bundle extras) {
						// required for interface, not used
					}
				};

				String mlocProvider;
				Criteria hdCrit = new Criteria();

				hdCrit.setAccuracy(Criteria.ACCURACY_COARSE);

				mlocProvider = locationManager.getBestProvider(hdCrit, true);

				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 1000, myLocationListener);
				Location currentLocation = locationManager.getLastKnownLocation(mlocProvider);
				locationManager.removeUpdates(myLocationListener);

				double currentLat = currentLocation.getLatitude();
				double currentLon = currentLocation.getLongitude();

				gpsButton.setText(currentLat + ", " + currentLon);
				
				// ***************************************************************
				// try to send lat long
				
				try {
					JSONObject json = new JSONObject();
					json.put("latitude", (int)(currentLat*MILLION)); 
					json.put("longitude", (int)(currentLon*MILLION));
					HttpParams httpParams = new BasicHttpParams();
			        HttpConnectionParams.setConnectionTimeout(httpParams,
			                TIMEOUT_MILLISEC);
			        HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
			        HttpClient client = new DefaultHttpClient(httpParams);
			        //
			        //String url = "http://10.0.2.2:8080/sample1/webservice2.php?" + 
			        //             "json={\"UserName\":1,\"FullName\":2}";
			        String url = "http://dana.ucc.nau.edu/~cs854/PHPPut.php";

			        HttpPost request = new HttpPost(url);
			        request.setEntity(new ByteArrayEntity(json.toString().getBytes(
			                "UTF8")));
			        request.setHeader("json", json.toString());
			        HttpResponse response = client.execute(request);
			        HttpEntity entity = response.getEntity();
			        // If the response does not enclose an entity, there is no need
			        if (entity != null) {
			            InputStream instream = entity.getContent();

			           //String result = RestClient.convertStreamToString(instream);
			           // Log.i("Read from server", result);
			           // Toast.makeText(this,  result, Toast.LENGTH_LONG).show();
			        }
			    } catch (Throwable t) {
			        //Toast.makeText(this, "Request failed: " + t.toString(),
			        //        Toast.LENGTH_LONG).show();
			    }
				// ***************************************************************
			}
		});

		Button logoutButton = (Button) findViewById(R.id.logoutButton);

		//Listening to button event
		logoutButton.setOnClickListener(new View.OnClickListener(){

			public void onClick(View arg0) {
				//Starting a new Intent
				Intent homeScreen = new Intent(getApplicationContext(), LocalizationActivity.class);

				startActivity(homeScreen);

			}
		});

	}
}