package app.localization;

import static app.utilities.CommonUtilities.SENDER_ID;
import static app.utilities.CommonUtilities.SERVER_URL;
import static app.utilities.CommonUtilities.TAG;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import app.utilities.ConnectionDetector;
import app.utilities.CustomDialog;
import app.utilities.ServerUtilities;

import com.google.android.gcm.GCMRegistrar;

/**
 * Page to register the user and device in the database. Upon registration, the
 * device ID is pulled and stored in the database, which in turn enables the
 * Google Cloud Message notification service to push notification to the phone
 * when a charge is made. This page can be extended to include other information
 * for the user, such as new password, birthday, and other demographic
 * information.
 * 
 */
public class RegisterActivity extends Activity {
	// Alert dialog manager
	CustomDialog alert = new CustomDialog(this);

	// Asynctask for server connection
	AsyncTask<Void, Void, Boolean> mRegisterTask;

	// Internet detector
	ConnectionDetector cd;

	// UI elements
	EditText txtName;
	EditText txtEmail;

	public static String name;
	public static String email;

	// Register button
	Button btnRegister;

	/**
	 * Initializes the page when it is first started.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

		prepareForRegistration();
	}

	/**
	 * Prepare the page for the actual registration in the database.
	 */
	public void prepareForRegistration() {
		cd = new ConnectionDetector(getApplicationContext());

		// Check if Internet present
		if (!cd.isConnectingToInternet()) {
			// Internet Connection is not present
			alert.showAlertDialog("Internet Connection Error",
					"Please connect to working Internet connection", false);
			// stop executing code by return
			return;
		}

		// Check if GCM configuration is set
		if (SERVER_URL == null || SENDER_ID == null || SERVER_URL.length() == 0
				|| SENDER_ID.length() == 0) {
			// GCM sernder id / server url is missing
			alert.showAlertDialog("Configuration Error!",
					"Please set your Server URL and GCM Sender ID", false);
			// stop executing code by return
			return;
		}

		txtName = (EditText) findViewById(R.id.txtName);
		txtEmail = (EditText) findViewById(R.id.txtEmail);
		btnRegister = (Button) findViewById(R.id.btnRegister);

		/*
		 * Click event on Register button
		 */
		btnRegister.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				name = txtName.getText().toString();
				email = txtEmail.getText().toString();

				// Check if user filled the form
				if (name.trim().length() > 0 && email.trim().length() > 0) {

					register(name, email);

				} else {
					// user didn't fill data; show alert message
					alert.showAlertDialog("Registration Error!",
							"Please enter your details", false);
				}
			}
		});
	}

	/**
	 * Register the username, email, and device ID information in the database.
	 * 
	 * @param name
	 *            Name of the user
	 * @param email
	 *            Email of the user
	 */
	void register(final String name, final String email) {

		cd = new ConnectionDetector(getApplicationContext());

		// Check if Internet present
		if (!cd.isConnectingToInternet()) {
			// Internet Connection is not present
			alert.showAlertDialog("Internet Connection Error",
					"Please connect to working Internet connection", false);
			// stop executing code by return
			// return;
		}

		// Make sure the device has the proper dependencies.
		GCMRegistrar.checkDevice(this);

		// Make sure the manifest was properly set - comment out this line
		// while developing the app, then uncomment it when it's ready.
		GCMRegistrar.checkManifest(this);

		// lblMessage = (TextView) findViewById(R.id.lblMessage);

		// registerReceiver(mHandleMessageReceiver, new IntentFilter(
		// DISPLAY_MESSAGE_ACTION));

		// Get GCM registration id
		final String regId = GCMRegistrar.getRegistrationId(this);

		// Check if regid already presents
		if (regId.equals("")) {
			// Registration is not present, register now with GCM
			Log.i(TAG, "regId is empty");
			GCMRegistrar.register(this, SENDER_ID);
		} else {
			// Device is already registered on GCM
			if (GCMRegistrar.isRegisteredOnServer(this)) {
				// Skips registration.
				Toast.makeText(getApplicationContext(),
						"Already registered with GCM", Toast.LENGTH_LONG)
						.show();
			} else {
				// Try to register again, but not in the UI thread.
				// It's also necessary to cancel the thread onDestroy(),
				// hence the use of AsyncTask instead of a raw thread.
				final Context context = this;
				mRegisterTask = new AsyncTask<Void, Void, Boolean>() {
					@Override
					protected Boolean doInBackground(Void... params) {
						// Register on our server
						// On server creates a new user
						Log.i(TAG,
								"Calling register from AsyncTask in RegisterActivity");
						boolean res = ServerUtilities.register(context, name,
								email, regId);

						Log.d(TAG, "Got here");
						if (res) {
							Log.d(TAG, "successful registration");
							Intent homeScreen = new Intent(
									getApplicationContext(),
									LocalizationActivity.class);
							startActivity(homeScreen);

							return true;
						} else {
							Log.d(TAG, "unsuccessful registration");
						}
						return false;
					}

					@Override
					protected void onPostExecute(Boolean result) {
						mRegisterTask = null;
					}

				};
				mRegisterTask.execute(null, null, null);
			}
		}
	}
}
