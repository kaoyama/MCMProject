package app.localization;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import app.utilities.CommonUtilities;
import app.utilities.CustomDialog;
import app.utilities.RestClient;

/**
 * Home page of the MCM customer application. The home page consists of the
 * Merchant Map, Deals, Make Payments, View History, Settings, and Log Out
 * buttons.
 * 
 * @author Chihiro
 * 
 */
public class HomeActivity extends Activity {
	protected static final int TIMEOUT_MILLISEC = 3000;
	static long MILLION = 1000000;
	static int NETWORK = 0;
	static int GPS = 1;

	public void onBackPressed() {
	}

	AsyncTask<Void, Void, Void> mRegisterTask;

	TextView username;
	String notificationMessage;

	// location update variables
	LocationManager locationManager;
	LocationListener myLocationListener;
	int CURRENT_TYPE = NETWORK;

	/**
	 * Initializes the page once it is loaded.
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		// send flag to login page to clear all activity stacks
		// so back button does not work upon logout
		boolean finish = getIntent().getBooleanExtra("finish", false);
		if (finish) {
			startActivity(new Intent(getApplicationContext(),
					LocalizationActivity.class));
			finish();
			return;
		}

		// Settings button
		Button settingsButton = (Button) findViewById(R.id.settingsButton);
		settingsButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent settingsScreen = new Intent(getApplicationContext(),
						Settings.class);
				startActivity(settingsScreen);
			}
		});

		// Make Payments button
		Button notificationButton = (Button) findViewById(R.id.notificationsButton);
		notificationButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent paymentScreen = new Intent(getApplicationContext(),
						MakePayments.class);
				startActivity(paymentScreen);
			}
		});

		// Merchant Map button
		Button merchantButton = (Button) findViewById(R.id.merchantButton);
		merchantButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent merchantScreen = new Intent(getApplicationContext(),
						MerchantMap.class);
				startActivity(merchantScreen);
			}
		});

		// History button
		Button viewHistoryButton = (Button) findViewById(R.id.searchButton);
		viewHistoryButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent paymentHistoryScreen = new Intent(
						getApplicationContext(), PaymentHistory.class);
				startActivity(paymentHistoryScreen);
			}
		});

		// GPS Button - For debug
		/*
		 * final Button gpsButton = (Button) findViewById(R.id.gpsButton);
		 * gpsButton.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // Starting a new intent
		 * Intent locationScreen = new Intent(getApplicationContext(),
		 * app.localization.LocationActivity.class);
		 * startActivity(locationScreen); } });
		 */

		// Deals button
		Button dealsButton = (Button) findViewById(R.id.dealsButton);
		dealsButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent dealsScreen = new Intent(getApplicationContext(),
						Deals.class);
				startActivity(dealsScreen);
			}
		});

		// Logout button
		Button logoutButton = (Button) findViewById(R.id.logoutButton);
		logoutButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				// clear activity stack so back button doesn't work
				Intent intent = new Intent(getApplicationContext(),
						HomeActivity.class);
				intent.putExtra("finish", true);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
			}
		});
	}

	/**
	 * Take care of instant polling for location upon startup
	 */
	@Override
	protected void onStart() {
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		myLocationListener = new LocationListener() {

			public void onProviderDisabled(String provider) {
			}

			public void onProviderEnabled(String provider) {
			}

			public void onStatusChanged(String provider, int status,
					Bundle extras) {
			}

			@Override
			public void onLocationChanged(Location arg0) {
				useNewLocation(arg0);
			}
		};

		// set up so location updates are performed
		getLocation(NETWORK);
		super.onStart();
	}

	/**
	 * Get current or newest location.
	 * 
	 * @param type
	 *            Method to retrieve location. Can be network or GPS.
	 */
	public void getLocation(int type) {

		String mlocProvider;
		Criteria hdCrit = new Criteria();

		hdCrit.setAccuracy(Criteria.ACCURACY_COARSE);

		mlocProvider = locationManager.getBestProvider(hdCrit, true);

		// When switching between network and GPS, change AndroidManifest.xml
		// ACCESS_COARSE_LOCATION (network) and ACCESS_FINE_LOCATION (gps)
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
		String userName = CommonUtilities.getUsername(HomeActivity.this);

		// Send latitude and longitude to database
		JSONObject json = new JSONObject();
		try {
			json.put("latitude", (int) (currentLat * MILLION));
			json.put("longitude", (int) (currentLon * MILLION));
			json.put("userName", userName);

			RestClient.connectToDatabase(
					CommonUtilities.UPDATEUSERLOCATION_URL, json);

		} catch (Exception e) {
			CustomDialog dialog = new CustomDialog(HomeActivity.this);
			dialog.showNotificationDialog("Error updating user latitude and longitude in database");
		}

		Log.d("Location", "Updating location: " + currentLat + ", "
				+ currentLon);

	}

	/**
	 * Remove location updates.
	 */
	@Override
	protected void onStop() {
		// remove location updates -- eats up battery power
		locationManager.removeUpdates(myLocationListener);
		super.onStop();
	}

}