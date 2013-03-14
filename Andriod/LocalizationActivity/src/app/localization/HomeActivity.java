package app.localization;


import static app.utilities.CommonUtilities.EXTRA_MESSAGE;

import java.io.FileInputStream;

import org.json.JSONArray;
import org.json.JSONObject;

import app.utilities.WakeLocker;
import com.google.android.gcm.GCMRegistrar;

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

/**
 * Description of class here
 * @author Chihiro
 *
 */
public class HomeActivity extends Activity {
	protected static final int TIMEOUT_MILLISEC = 3000;
	static long MILLION = 1000000; 
	
	// Asyntask
	AsyncTask<Void, Void, Void> mRegisterTask;
	
	TextView username;
	String notificationMessage; 
	
	

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

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
				Intent merchantScreen = new Intent(getApplicationContext(), Merchants.class);
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
		

		final Button gpsButton = (Button) findViewById(R.id.gpsButton);
		gpsButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Starting a new intent
				Intent locationScreen = new Intent(getApplicationContext(), app.localization.Location.class); 
				startActivity(locationScreen); 				
			}
		});
		
		
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
			// TODO: DO WHAT WILL BE DONE WITH RECEIVED MESSAGE
		//	lblMessage.append(newMessage + "\n");			
			
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
}