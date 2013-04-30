package app.localization;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import app.utilities.CommonUtilities;
import app.utilities.CustomDialog;
import app.utilities.RestClient;

public class LocationActivity extends Activity {
	static long MILLION = 1000000;
	static int NETWORK = 0;
	static int GPS = 1;

	LocationManager locationManager;
	LocationListener myLocationListener;

	int CURRENT_TYPE = NETWORK;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location);

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		myLocationListener = new LocationListener() {

			public void onProviderDisabled(String provider) {
			}

			public void onProviderEnabled(String provider) {
			}

			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// Toast t = Toast.makeText(LocationActivity.this,
				// "Status changed", Toast.LENGTH_SHORT);
				// t.show();
			}

			@Override
			public void onLocationChanged(Location arg0) {
				// TODO: CHANGE THIS TO UPDATE EVERY LOCATION CHANGE
				// sets and displays the lat/long when a location is provided
				// String latlong = "Lat: " + loc.getLatitude() + " Long: " +
				// loc.getLongitude();
				// gpsButton.setText(latlong);
				Toast t = Toast.makeText(LocationActivity.this,
						"Updating location", Toast.LENGTH_SHORT);
				t.show();
				Log.d("Location", "Updating location: " + arg0.getLatitude()
						+ ", " + arg0.getLongitude());

				useNewLocation(arg0);
			}
		};

		// Update location via Network service -- cell tower and Wi-Fi
		Button networkButton = (Button) findViewById(R.id.updateLocationNetworkButton);
		networkButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// update location
				CURRENT_TYPE = NETWORK;
				getLocation(NETWORK);
			}
		});

		// Update location via GPS
		Button gpsButton = (Button) findViewById(R.id.updateLocationGPSButton);
		gpsButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// update location
				CURRENT_TYPE = GPS;
				getLocation(GPS);
			}
		});

		// Stop updating
		Button stopUpdateButton = (Button) findViewById(R.id.stopUpdatingLocationButton);
		stopUpdateButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// stop updating location

				locationManager.removeUpdates(myLocationListener);
				Toast t = Toast.makeText(LocationActivity.this,
						"Stop location updates", Toast.LENGTH_SHORT);
				t.show();
			}
		});

		// Go away -- for testing purposes. Take out for final.
		Button goAwayButton = (Button) findViewById(R.id.goingAwayButton);
		goAwayButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// stop updating location

				Toast t = Toast.makeText(LocationActivity.this,
						"Going away...and away...", Toast.LENGTH_LONG);
				t.show();

				// Update database with zero values
				JSONObject json = new JSONObject();
				try {
					json.put("latitude", 0);
					json.put("longitude", 0);
					json.put("userName",
							CommonUtilities.getUsername(LocationActivity.this));

					RestClient.connectToDatabase(
							CommonUtilities.UPDATEUSERLOCATION_URL, json);

				} catch (Exception e) {
					CustomDialog dialog = new CustomDialog(
							LocationActivity.this);
					dialog.showNotificationDialog("Error updating user latitude and longitude in database");
				}
			}
		});
	}

	public void updateTargetedDeals() {
		String userName = CommonUtilities.getUsername(LocationActivity.this);

		// Send latitude and longitude to database
		JSONObject json = new JSONObject();
		try {
			json.put("userName", userName);

			RestClient.connectToDatabase(CommonUtilities.TARGETEDDEALS_URL,
					json);

		} catch (Exception e) {
			CustomDialog dialog = new CustomDialog(LocationActivity.this);
			dialog.showNotificationDialog("Error updating user latitude and longitude in database");
		}
	}

	/**
	 * Get the current/newest location of the user. When switching between
	 * network and GPS, change AndroidManifest.xml ACCESS_COARSE_LOCATION
	 * (network) and ACCESS_FINE_LOCATION (GPS)
	 * 
	 * @param type
	 *            Method to retrieve location. Can be network or GPS.
	 */
	public void getLocation(int type) {

		String mlocProvider;
		Criteria hdCrit = new Criteria();

		hdCrit.setAccuracy(Criteria.ACCURACY_COARSE);
		mlocProvider = locationManager.getBestProvider(hdCrit, true);

		if (type == NETWORK) {
			locationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, // provider
					3000, // minimum time interval between location updates (ms)
					0, // minimum distance between location updates (m)
					myLocationListener);
		} else if (type == GPS) {
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 3000, 0, myLocationListener);
		}

		// Remove to update automatically
		Location currentLocation = locationManager
				.getLastKnownLocation(mlocProvider);
		locationManager.removeUpdates(myLocationListener);

		useNewLocation(currentLocation);
	}

	/**
	 * Use the new location and update the location information in the database.
	 * 
	 * @param location
	 *            Location of the user
	 */
	public void useNewLocation(Location location) {

		double currentLat = location.getLatitude();
		double currentLon = location.getLongitude();
		String userName = CommonUtilities.getUsername(LocationActivity.this);

		// Send latitude and longitude to database
		JSONObject json = new JSONObject();
		try {
			json.put("latitude", (int) (currentLat * MILLION));
			json.put("longitude", (int) (currentLon * MILLION));
			json.put("userName", userName);

			RestClient.connectToDatabase(
					CommonUtilities.UPDATEUSERLOCATION_URL, json);

		} catch (Exception e) {
			CustomDialog dialog = new CustomDialog(LocationActivity.this);
			dialog.showNotificationDialog("Error updating user latitude and longitude in database");
		}

		// Change TextView to current location
		TextView text = null;
		if (CURRENT_TYPE == NETWORK) {
			text = (TextView) findViewById(R.id.updateLocationNetworkLabel);
		} else if (CURRENT_TYPE == GPS) {
			text = (TextView) findViewById(R.id.updateLocationGPSLabel);
		}
		text.setText("current Latitude: " + currentLat
				+ "\ncurrent Longitude: " + currentLon);
	}
}
