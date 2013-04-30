package app.utilities;

import static app.utilities.CommonUtilities.SERVER_URL;
import static app.utilities.CommonUtilities.TAG;

import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import app.localization.R;

import com.google.android.gcm.GCMRegistrar;

public final class ServerUtilities {
	private static final int MAX_ATTEMPTS = 5;
	private static final int BACKOFF_MILLI_SECONDS = 2000;
	private static final Random random = new Random();

	/**
	 * Register this account/device pair within the server.
	 * 
	 * @param context
	 *            Application context
	 * @param name
	 *            Name of the person to register
	 * @param email
	 *            Email of the person to register
	 * @param regId
	 *            The ID of the specific device of the person to register. This
	 *            ID is only for the Android.
	 * @return Success value of the registration. True if success, false
	 *         otherwise.
	 */
	public static boolean register(final Context context, String name,
			String email, final String regId) {

		Log.i(TAG, "registering device (regId = " + regId + ")");
		JSONArray jsonArray = null;
		JSONObject json = new JSONObject();

		try {
			json.put("name", name);
			json.put("email", email);
			json.put("regId", regId);
		} catch (Exception e) {
			Log.d(TAG, "exception in JSON");
		}

		Log.i(TAG, "JSON sent is: " + json.toString());

		long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
		// Once GCM returns a registration id, we need to register on our server
		// As the server might be down, we will retry it a few times
		for (int i = 1; i <= MAX_ATTEMPTS; i++) {
			Log.d(TAG, "Attempt #" + i + " to register");
			try {
				jsonArray = RestClient.connection(
						CommonUtilities.REGISTRATION_URL, json, null);

				String result = jsonArray.getJSONObject(0).getString("result");
				if (result.equals("1")) {
					// success
					Log.i(TAG, "Successful registration.");
					GCMRegistrar.setRegisteredOnServer(context, true);
					return true;

				} else {
					// here we are simplifying and retrying on any error; in a
					// real application, it should retry only on unrecoverable
					// errors
					// (like HTTP error code 503).
					Log.e(TAG, "Failed to register on attempt " + i);
					if (i == MAX_ATTEMPTS) {
						break;
					}
					try {
						Log.d(TAG, "Sleeping for " + backoff
								+ " ms before retry");
						Thread.sleep(backoff);
					} catch (InterruptedException e1) {
						// Activity finished before we complete - exit.
						Log.d(TAG,
								"Thread interrupted: abort remaining retries!");
						Thread.currentThread().interrupt();
						return false;
					}
					// increase back-off exponentially
					backoff *= 2;
				}
			} catch (Exception e) {
				Log.d(TAG, "Exception while trying to register.");
				Thread.currentThread().interrupt();
				return false;
			}
		}
		String message = context.getString(R.string.server_register_error,
				MAX_ATTEMPTS);
		Log.d(TAG, message);
		return false;
	}

	/**
	 * Unregister this account/device pair within the server.
	 * 
	 * @param context
	 *            Application context
	 * @param regId
	 *            Registration ID of the specific device
	 */
	public static void unregister(final Context context, final String regId) {
		Log.i(TAG, "unregistering device (regId = " + regId + ")");
		String serverUrl = SERVER_URL + "/unregister";

		JSONArray jsonArray = null;
		JSONObject json = new JSONObject();

		try {
			json.put("regId", regId);

			jsonArray = RestClient.connectToDatabase(
					CommonUtilities.SERVER_URL, json);

			jsonArray = RestClient.connectToDatabase(serverUrl, json);

			String result = jsonArray.getJSONObject(0).getString("result");
			if (result.equals("1")) {
				// success
				GCMRegistrar.setRegisteredOnServer(context, true);
				String message = context
						.getString(R.string.server_unregistered);
				Log.v(TAG, message);
				return;
			}
		} catch (Exception e) {

			String message = context.getString(
					R.string.server_unregister_error, e.getMessage());
			Log.v(TAG, message); 
		}
	}
}
