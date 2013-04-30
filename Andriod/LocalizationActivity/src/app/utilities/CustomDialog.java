package app.utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import app.localization.R;

public class CustomDialog {

	/**
	 * Current activity information
	 */
	Activity activity;

	/**
	 * Initialize dialog by setting its Activity.
	 * 
	 * @param activity
	 *            Activity to show dialog.
	 */
	public CustomDialog(Activity activity) {
		this.activity = activity;
	}

	/**
	 * Display notification dialog on the Activity.
	 * 
	 * @param notification
	 *            Notification message to show in dialog.
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
	 * Display alert dialog on the Activity.
	 * 
	 * @param title
	 *            Title of the alert dialog
	 * @param message
	 *            Alert message
	 * @param status
	 *            Success/failure used to set icon. Null can be passed for no
	 *            icon.
	 */
	public void showAlertDialog(String title, String message, Boolean status) {
		AlertDialog alertDialog = new AlertDialog.Builder(activity).create();

		// set the dialog title
		alertDialog.setTitle(title);

		// set the dialog message
		alertDialog.setMessage(message);

		if (status != null)
			// set alert dialog icon
			alertDialog
					.setIcon((status) ? R.drawable.success : R.drawable.fail);

		// set OK button
		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				});

		// show alert message
		alertDialog.show();
	}

}
