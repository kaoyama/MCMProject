package app.localization;

import static app.utilities.CommonUtilities.SENDER_ID;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import app.utilities.ServerUtilities;

import com.google.android.gcm.GCMBaseIntentService;

/**
 * Google Cloud Messaging functions.
 * 
 * @author Chihiro
 * 
 */
public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "GCMIntentService";

	/**
	 * Constructor for GCM
	 */
	public GCMIntentService() {
		super(SENDER_ID);
	}

	/**
	 * Method called when the device is registered.
	 * 
	 * @param context
	 *            Application context
	 * @param registrationId
	 *            Registration ID of specific device
	 */
	@Override
	protected void onRegistered(Context context, String registrationId) {
		Log.i(TAG, "Device registered: regId = " + registrationId);
		Log.d("NAME", RegisterActivity.name);
		ServerUtilities.register(context, RegisterActivity.name,
				RegisterActivity.email, registrationId);
	}

	/**
	 * Method called when the device is unregistered
	 * 
	 * @param context
	 *            Application context
	 * @param registrationId
	 *            Registration ID of specific device
	 */
	@Override
	protected void onUnregistered(Context context, String registrationId) {
		Log.i(TAG, "Device unregistered");
		ServerUtilities.unregister(context, registrationId);
	}

	/**
	 * Method called on receiving a new message
	 * 
	 * @param context
	 *            Application context
	 * @param intent
	 *            Intent containing the string passed in from the message
	 */
	@Override
	protected void onMessage(Context context, Intent intent) {
		Log.i(TAG, "Received message");
		String message = intent.getExtras().getString("price");

		// notifies user
		generateNotification(context, message);
	}

	/**
	 * Method called on receiving a deleted message
	 * 
	 * @param context
	 *            Application message
	 * @param total
	 *            Total of deleted messages
	 */
	@Override
	protected void onDeletedMessages(Context context, int total) {
		Log.i(TAG, "Received deleted messages notification");
		String message = getString(R.string.gcm_deleted, total);

		generateNotification(context, message);
	}

	/**
	 * Method called on Error
	 * 
	 * @param context
	 *            Application context
	 * @param errorId
	 *            ID of the error
	 */
	@Override
	public void onError(Context context, String errorId) {
		Log.i(TAG, "Received error: " + errorId);
	}

	/**
	 * Called when there is a recoverable error.
	 * 
	 * @param context
	 *            Application context
	 * @param errorId
	 *            ID of the error
	 */
	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		// log message
		Log.i(TAG, "Received recoverable error: " + errorId);
		return super.onRecoverableError(context, errorId);
	}
	
	/**
	 * Issues a notification to inform the user that server has sent a message.
	 * @param context Application context 
	 * @param message Message to inform the user 
	 */
	@SuppressWarnings("deprecation")
	private static void generateNotification(Context context, String message) {
		int icon = R.drawable.ic_launcher;
		long when = System.currentTimeMillis();
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		
		Notification notification = new Notification(icon, message, when);

		String title = context.getString(R.string.app_name);

		// start new intent -- take the user to the make payments page 
		Intent notificationIntent = new Intent(context, MakePayments.class);
		
		// set intent so it does not start a new activity
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent intent = PendingIntent.getActivity(context, 0,
				notificationIntent, 0);
		notification.setLatestEventInfo(context, title, message, intent);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		// Play default notification sound
		notification.defaults |= Notification.DEFAULT_SOUND;

		// Vibrate if vibrate is enabled
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		notificationManager.notify(0, notification);
	}
}
