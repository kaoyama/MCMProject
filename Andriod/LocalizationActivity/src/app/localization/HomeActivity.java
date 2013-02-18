package app.localization;


import java.io.FileInputStream;

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

		Button notificationButton = (Button) findViewById(R.id.notificationsButton);
		notificationButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
		        JSONArray json = RestClient.connectToDatabase(CommonUtilities.USERNOTIFICATION_URL, null);
		  		        
				try {
					FileInputStream fis = openFileInput("username_file");
					StringBuffer sb =new StringBuffer("");
					int ch;
					while((ch = fis.read())!= -1){
						sb.append((char)ch);
					}
					fis.close();
					notificationMessage = sb.toString();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
		
		Button registerButton = (Button) findViewById(R.id.registrationButton);
		registerButton.setOnClickListener(new View.OnClickListener(){

			public void onClick(View arg0) {
				//Starting a new Intent
				Intent registerScreen = new Intent(getApplicationContext(), RegisterActivity.class);
				startActivity(registerScreen);
			}
		});		
	}
}