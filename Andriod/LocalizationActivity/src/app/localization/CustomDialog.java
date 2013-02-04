package app.localization;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;

public class CustomDialog {
	
	Activity activity; 
	
	public CustomDialog(Activity activity) {
		this.activity = activity; 
	}
	
	public void showNotificationDialog(String notification) {
		
        Builder builder = new AlertDialog.Builder(activity); 
		builder.setMessage(notification);
		builder.setCancelable(false); 
		builder.setPositiveButton("Ok", new OkOnClickListener()); 
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	private final class OkOnClickListener implements 
	DialogInterface.OnClickListener {
	public void onClick(DialogInterface dialog, int which) {
		dialog.dismiss();
	}
}
}
