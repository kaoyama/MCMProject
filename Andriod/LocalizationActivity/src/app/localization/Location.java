package app.localization;

import org.json.JSONObject;

import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import app.utilities.CommonUtilities;
import app.utilities.CustomDialog;
import app.utilities.RestClient;

public class Location extends Activity {
	static long MILLION = 1000000; 
	static int NETWORK = 0; 
	static int GPS = 1; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location);
		
		Button networkButton = (Button) findViewById(R.id.updateLocationNetworkButton);
		networkButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// update location 
				getLocation(NETWORK); 
			}
		});
		
		Button gpsButton = (Button) findViewById(R.id.updateLocationGPSButton);
		gpsButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// update location 
				getLocation(GPS); 
			}
		});
	}

	/**
	 * 
	 * @param type Method to retrieve location.  Can be network or GPS.
	 */
	public void getLocation(int type) {

		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		LocationListener myLocationListener = new LocationListener() {
			
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

			@Override
			public void onLocationChanged(android.location.Location arg0) {
				// TODO: CHANGE THIS TO UPDATE EVERY LOCATION CHANGE
				//sets and displays the lat/long when a location is provided
				//String latlong = "Lat: " + loc.getLatitude() + " Long: " + loc.getLongitude();   
				//gpsButton.setText(latlong);
			}
		};

		String mlocProvider;
		Criteria hdCrit = new Criteria();

		hdCrit.setAccuracy(Criteria.ACCURACY_COARSE);

		mlocProvider = locationManager.getBestProvider(hdCrit, true);

		// When switching between network and GPS, change AndroidManifest.xml ACCESS_COARSE_LOCATION (network) and ACCESS_FINE_LOCATION (gps)
		if (type == NETWORK) {
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 1000, myLocationListener);
		} else if (type == GPS) {
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 1000, myLocationListener);
		}
		
		android.location.Location currentLocation = locationManager.getLastKnownLocation(mlocProvider);
		locationManager.removeUpdates(myLocationListener);

		double currentLat = currentLocation.getLatitude();
		double currentLon = currentLocation.getLongitude();

		// Send latitude and longitude to database 
		JSONObject json = new JSONObject();
		try {
			json.put("latitude", (int)(currentLat*MILLION)); 
			json.put("longitude", (int)(currentLon*MILLION));

			RestClient.connectToDatabase(
					CommonUtilities.UPDATEUSERLOCATION_URL, json);

		} catch (Exception e) {
			CustomDialog dialog = new CustomDialog(Location.this);
			dialog.showNotificationDialog("Error updating user latitude and longitude in database");
		}
		
		// Change TextView to current location 
		TextView text = null; 
		if (type == NETWORK) {
			text = (TextView) findViewById(R.id.updateLocationNetworkLabel);			
		} else if (type == GPS) {
			text = (TextView) findViewById(R.id.updateLocationGPSLabel);
		}
		text.setText("current Latitude: " + currentLat + "\ncurrent Longitude: " + currentLon); 
	}


}
