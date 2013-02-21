package app.utilities;

import android.content.Context;
import android.content.Intent;

public final class CommonUtilities {
	
	// give your server registration url here
    public static final String SERVER_URL = "http://dana.ucc.nau.edu/~cs854/cloudMessaging/register.php"; 
    
    public static final String REGISTRATION_URL = "http://dana.ucc.nau.edu/~cs854/cloudMessaging/register.php"; 
    public static final String USERNOTIFICATION_URL = "http://dana.ucc.nau.edu/~cs854/PHPRetrieveUserNotification.php";
    public static final String UPDATEUSERLOCATION_URL = "http://dana.ucc.nau.edu/~cs854/PHPUpdateUserLocation.php";
    public static final String NEARBYMERCHANTS_URL = "http://dana.ucc.nau.edu/~cs854/PHPGetNearbyMerchants.php";
    public static final String LOGIN_URL = "http://dana.ucc.nau.edu/~cs854/PHPValidateLogin.php";
    public static final String NOTIFICATION_URL = "http://dana.ucc.nau.edu/~cs854/PHPGetNotifications.php";
    
    // Google project id
    public static final String SENDER_ID = "901660126123"; 

    /**
     * Tag used on log messages.
     */
    public static final String TAG = "MCM Localization";

    public static final String DISPLAY_MESSAGE_ACTION =
            "app.localization.DISPLAY_MESSAGE";

    public static final String EXTRA_MESSAGE = "message";

    /**
     * Notifies UI to display a message.
     * <p>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    public static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }
}