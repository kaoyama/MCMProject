package app.utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import app.localization.R;

public class CustomDialog {
	
	Activity activity; 
	
	/**
	 * Initialize dialog by setting its Activity.
	 * @param activity Activity to show dialog. 
	 */
	public CustomDialog(Activity activity) {
		this.activity = activity; 
	}
	
	/**
	 * Display notification dialog on the Activity.
	 * @param notification Notification message to show in dialog.
	 */
	public void showNotificationDialog(String notification) {
		
        Builder builder = new AlertDialog.Builder(activity); 
		builder.setMessage(notification);
		builder.setCancelable(false); 
		builder.setPositiveButton("Ok", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}); 
		
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	/**
	 * Function to display simple Alert Dialog
	 * @param context - application context
	 * @param title - alert dialog title
	 * @param message - alert message
	 * @param status - success/failure (used to set icon)
	 * 				 - pass null if you don't want icon
	 * */
	
	
	/**
	 * Display alert dialog on the Activity.
	 * @param title Title of the alert dialog
	 * @param message Alert message
	 * @param status Success/failure used to set icon.  Null can be passed for 
	 * no icon.
	 */
	public void showAlertDialog(String title, String message, Boolean status) {
		AlertDialog alertDialog = new AlertDialog.Builder(activity).create();

		// Setting Dialog Title
		alertDialog.setTitle(title);

		// Setting Dialog Message
		alertDialog.setMessage(message);

		if(status != null)
			// Setting alert dialog icon
			alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

		// Setting OK Button
		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});

		// Showing Alert Message
		alertDialog.show();
	}

}
