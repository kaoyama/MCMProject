package app.localization;


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
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import app.utilities.CommonUtilities;
import app.utilities.CustomDialog;
import app.utilities.RestClient;

/**
 * Description of class here
 * @author Chihiro
 *
 */
public class HomeActivity extends Activity {
	protected static final int TIMEOUT_MILLISEC = 3000;
	static long MILLION = 1000000; 
	
	TextView username;
	String notificationMessage; 
	
	public static String name;
	public static String email; 

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		/*
		Button mapButton = (Button) findViewById(R.id.mapButton);

		//Listening to button event
		mapButton.setOnClickListener(new View.OnClickListener(){

			public void onClick(View arg0) {
				//Starting a new Intent
				Intent mapScreen = new Intent(getApplicationContext(), MerchantMap.class);

				startActivity(mapScreen);

			}
		});
	*/
		

		Button notificationButton = (Button) findViewById(R.id.notificationsButton);
		notificationButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
		        JSONArray json = RestClient.connectToDatabase(CommonUtilities.USERNOTIFICATION_URL, null); 
		        
		        if (json != null) {
		        	try {
			            String timestamp = json.getJSONObject(0).getString("timestamp"); 
			            
			            if (json.getJSONObject(0).getString("charged").equals("1")) {
			            	notificationMessage = "User has been seen by the merchant at time:\n" + timestamp + "!";
			            } else {
			            	notificationMessage = "User has not been seen by the merchant!\n" + 
			            			"User was last seen at time:\n" + timestamp; 
			            }			            
		        	} catch (Exception e) {
		        		notificationMessage = "Exception parsing JSON array.";
		        	}
		        } else {
		        	notificationMessage = "JSON array was null.";
		        }
		        
		        // Show dialog of results
		        CustomDialog cd = new CustomDialog(HomeActivity.this); 
		        cd.showNotificationDialog(notificationMessage);	      
			}
		});
		
		Button merchantButton = (Button) findViewById(R.id.merchantButton);
		merchantButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				// Starting a new intent
				Intent merchantScreen = new Intent(getApplicationContext(), Merchants.class);
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

				locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 1000, myLocationListener);
				Location currentLocation = locationManager.getLastKnownLocation(mlocProvider);
				locationManager.removeUpdates(myLocationListener);

				double currentLat = currentLocation.getLatitude();
				double currentLon = currentLocation.getLongitude();

				gpsButton.setText(currentLat + ", " + currentLon);
				
				// Send latitude and longitude to database 
				JSONObject json = new JSONObject();
				try {
					json.put("latitude", (int)(currentLat*MILLION)); 
					json.put("longitude", (int)(currentLon*MILLION));
					
					RestClient.connectToDatabase(
							CommonUtilities.UPDATEUSERLOCATION_URL, json);
					
					/*
					HttpParams httpParams = new BasicHttpParams();
			        HttpConnectionParams.setConnectionTimeout(httpParams,
			                TIMEOUT_MILLISEC);
			        HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
			        HttpClient client = new DefaultHttpClient(httpParams);
			        //
			        //String url = "http://10.0.2.2:8080/sample1/webservice2.php?" + 
			        //             "json={\"UserName\":1,\"FullName\":2}";
			       // String url = CommonUtilities.UPDATEUSERLOCATION_URL;
			        String url = "http://dana.ucc.nau.edu/~cs854/PHPUpdateUserLocation.php";
			        HttpPost request = new HttpPost(url);
					request.setEntity(new ByteArrayEntity(json.toString().getBytes(
						        "UTF8")));														
			        request.setHeader("json", json.toString());
			        HttpResponse response;
					response = client.execute(request);
			        HttpEntity entity = response.getEntity();
			       
			       */
				} catch (Exception e) {
					CustomDialog dialog = new CustomDialog(HomeActivity.this);
					dialog.showNotificationDialog("Error updating user latitude and longitude in database");
				}
				
			}
		});
		
		Button couponsButton = (Button) findViewById(R.id.couponsButton); 
		couponsButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// Starting a new intent
				Intent couponsScreen = new Intent(getApplicationContext(), Coupons.class);
				startActivity(couponsScreen); 
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