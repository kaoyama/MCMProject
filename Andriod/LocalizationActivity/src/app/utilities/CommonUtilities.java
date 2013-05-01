package app.utilities;

import java.io.FileInputStream;

import android.app.Activity;
import android.content.Context;

public final class CommonUtilities {

	/**
	 * URL definitions - to be changed for different web services
	 */
	public static final String SERVER_URL = "http://dana.ucc.nau.edu/~cs854/cloudMessaging/register.php";
	public static final String REGISTRATION_URL = "http://dana.ucc.nau.edu/~cs854/PHPRegisterNewUser.php";
	public static final String UPDATEUSERLOCATION_URL = "http://dana.ucc.nau.edu/~cs854/PHPUpdateUserLocation.php";
	public static final String TARGETEDDEALS_URL = "http://dana.ucc.nau.edu/~cs854/PHPGetTargetedDealsForCustomers.php";
	public static final String UPDATEMERCHANTLOCATION_URL = "http://dana.ucc.nau.edu/~cs854/PHPUpdateMerchantLocation.php";
	public static final String NEARBYMERCHANTS_URL = "http://dana.ucc.nau.edu/~cs854/PHPGetNearbyMerchants.php";
	public static final String LOGIN_URL = "http://dana.ucc.nau.edu/~cs854/PHPValidateLogin.php";
	public static final String NOTIFICATION_URL = "http://dana.ucc.nau.edu/~cs854/PHPGetNotifications.php";
	public static final String PAYMENTS_URL = "http://dana.ucc.nau.edu/~cs854/PHPGetUserTransactions.php";
	public static final String UPDATEPAYMENT_URL = "http://dana.ucc.nau.edu/~cs854/PHPUpdatePayment.php";
	public static final String GETDEALS_URL = "http://dana.ucc.nau.edu/~cs854/PHPGetDealsForCustomers.php";
	public static final String OPTINALL_URL = "http://dana.ucc.nau.edu/~cs854/PHPOptInAllDeals.php";
	public static final String OPTOUTALL_URL = "http://dana.ucc.nau.edu/~cs854/PHPOptOutAllDeals.php";
	public static final String OPTINSOME_URL = "http://dana.ucc.nau.edu/~cs854/PHPOptInMerchant.php";
	public static final String ALLMERCHANTS_URL = "http://dana.ucc.nau.edu/~cs854/PHPGetAllMerchants.php";
	public static final String ALLSUBSCRIBEDMERCHANTS_URL = "http://dana.ucc.nau.edu/~cs854/PHPGetAllSubscribedMerchants.php";

	/**
	 * Google Project ID used for Google Cloud Messaging
	 */
	public static final String SENDER_ID = "901660126123";

	/**
	 * Tag used on log generic messages
	 */
	public static final String TAG = "MCM Localization";

	public static final String DISPLAY_MESSAGE_ACTION = "app.localization.DISPLAY_MESSAGE";
	public static final String EXTRA_MESSAGE = "message";

	/**
	 * Retrieves the username from the current session.
	 * 
	 * @param context
	 *            Application context
	 * @return Returns the username as a string
	 */
	public static String getUsername(Context context) {
		String tempUserName = "";

		try {
			// retrieve username from "file"
			FileInputStream fis = context.openFileInput("username_file");
			StringBuffer sb = new StringBuffer("");
			int ch;
			while ((ch = fis.read()) != -1) {
				sb.append((char) ch);
			}
			fis.close();
			tempUserName = sb.toString();
		} catch (Exception e) {
			CustomDialog cd = new CustomDialog((Activity) context);
			cd.showNotificationDialog("Could not get username.");
		}
		return tempUserName;
	}
}
