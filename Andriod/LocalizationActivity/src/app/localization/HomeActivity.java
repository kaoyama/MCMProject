package app.localization;


import static app.utilities.CommonUtilities.EXTRA_MESSAGE;

import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
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
import android.widget.Toast;
import app.utilities.CommonUtilities;
import app.utilities.CustomDialog;
import app.utilities.RestClient;
import app.utilities.WakeLocker;

import com.google.android.gcm.GCMRegistrar;

/**
 * Description of class here
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
	
	// Asyntask
	AsyncTask<Void, Void, Void> mRegisterTask;
	
	TextView username;
	String notificationMessage; 
	
	// location update variables
	LocationManager locationManager;
	LocationListener myLocationListener;
	int CURRENT_TYPE = NETWORK; 

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		// send flag to login page to clear all activity stacks 
		// so back button does not work upon logout
		boolean finish = getIntent().getBooleanExtra("finish", false);
		if (finish) {
			startActivity(new Intent(getApplicationContext(), LocalizationActivity.class));
			finish(); 
			return ;
		}
		
		Button settingsButton = (Button) findViewById(R.id.settingsButton);
		settingsButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				// Starting a new intent
				Intent settingsScreen = new Intent(getApplicationContext(), Settings.class);
				startActivity(settingsScreen); 	      
			}
		});
		
		Button notificationButton = (Button) findViewById(R.id.notificationsButton);
		notificationButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				// Starting a new intent
				Intent paymentScreen = new Intent(getApplicationContext(), MakePayments.class);
				startActivity(paymentScreen); 	      
			}
		});
		
		Button merchantButton = (Button) findViewById(R.id.merchantButton);
		merchantButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				
				// Starting a new intent
				Intent merchantScreen = new Intent(getApplicationContext(), MerchantMap.class);
				startActivity(merchantScreen); 
			}
		});
		
		Button viewHistoryButton = (Button) findViewById(R.id.searchButton);
		viewHistoryButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				// Starting a new intent
				Intent paymentHistoryScreen = new Intent(getApplicationContext(), PaymentHistory.class);
				startActivity(paymentHistoryScreen); 	      
			}
		});
		

		// GPS Button - For debug
		/*
		final Button gpsButton = (Button) findViewById(R.id.gpsButton);
		gpsButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Starting a new intent
				Intent locationScreen = new Intent(getApplicationContext(), app.localization.LocationActivity.class); 
				startActivity(locationScreen); 				
			}
		});
		*/
		
		
		Button dealsButton = (Button) findViewById(R.id.dealsButton); 
		dealsButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// Starting a new intent
				Intent dealsScreen = new Intent(getApplicationContext(), Deals.class);
				startActivity(dealsScreen); 
			}
		});

		Button logoutButton = (Button) findViewById(R.id.logoutButton);

		//Listening to button event
		logoutButton.setOnClickListener(new View.OnClickListener(){

			public void onClick(View arg0) {
				// clear activity stack so back button doesn't work 
				Intent intent = new Intent(getApplicationContext(), HomeActivity.class); 
				intent.putExtra("finish", true);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent); 
				finish(); 
			}
		});
		
	}
	
	/**
	 * From GCM
	 * Receiving push messages
	 * */
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
			// Waking up mobile if it is sleeping
			WakeLocker.acquire(getApplicationContext());
			
			/**
			 * Take appropriate action on this message
			 * depending upon your app requirement
			 * For now i am just displaying it on the screen
			 * */
			
			// Showing received message	
			Toast.makeText(getApplicationContext(), "New Message: " + newMessage, Toast.LENGTH_LONG).show();
			
			// Releasing wake lock
			WakeLocker.release();
		}
	};
	
	/**
	 * From GCM
	 */
	@Override
	protected void onDestroy() {
		if (mRegisterTask != null) {
			mRegisterTask.cancel(true);
		}
		try {
			unregisterReceiver(mHandleMessageReceiver);
			GCMRegistrar.onDestroy(this);
		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
		super.onDestroy();
	}
	
	/**
	 * Take care of instant polling 
	 */
	@Override
	protected void onStart() {
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		myLocationListener = new LocationListener() {
			
			public void onProviderDisabled(String provider) {}
			public void onProviderEnabled(String provider) {}
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
	 * 
	 * @param type Method to retrieve location.  Can be network or GPS.
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
					0,    // minimum distance between location updates (m)
					myLocationListener);
		} else if (type == GPS) {
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 
					3000, // minimum time interval between location updates (ms)
					0, 	  // minimum distance between location updates (m)
					myLocationListener);
		}
		
		
		// TODO: Remove for update automatically 
		Location currentLocation = locationManager.getLastKnownLocation(mlocProvider);
		locationManager.removeUpdates(myLocationListener);
		
		useNewLocation(currentLocation); 
		
	}
	
	public void useNewLocation(Location location) {
		
		double currentLat = location.getLatitude();
		double currentLon = location.getLongitude();
		String userName = CommonUtilities.getUsername(HomeActivity.this); 
				
		// Send latitude and longitude to database 
		JSONObject json = new JSONObject();
		try {
			json.put("latitude", (int)(currentLat*MILLION)); 
			json.put("longitude", (int)(currentLon*MILLION));
			json.put("userName", userName); 

			RestClient.connectToDatabase(
					CommonUtilities.UPDATEUSERLOCATION_URL, json);

		} catch (Exception e) {
			CustomDialog dialog = new CustomDialog(HomeActivity.this);
			dialog.showNotificationDialog("Error updating user latitude and longitude in database");
		}
		
		Log.d("Location", "Updating location: " + currentLat + ", " + currentLon); 
		
	}
	
	@Override
	protected void onStop() {
		// remove location updates -- eats up battery power
		locationManager.removeUpdates(myLocationListener);
		super.onStop();
	}
	
}