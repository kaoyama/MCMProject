package app.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionDetector {

	/**
	 * Application context information
	 */
	private Context _context;

	/**
	 * Constructor for application context
	 * 
	 * @param context
	 *            Application context
	 */
	public ConnectionDetector(Context context) {
		this._context = context;
	}

	/**
	 * Checks for all possible Internet providers.
	 * 
	 * @return Boolean on whether there is an Internet connection
	 */
	public boolean isConnectingToInternet() {
		ConnectivityManager connectivity = (ConnectivityManager) _context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				// if any one of the Internet providers is connected, return
				// true
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
		}
		return false;
	}
}
