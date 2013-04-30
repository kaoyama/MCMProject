package app.utilities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.PowerManager;

/**
 * Wakes up the device from sleep upon receipt of a Google Cloud Messaging
 * message.
 * 
 */
public abstract class WakeLocker {
	private static PowerManager.WakeLock wakeLock;

	/**
	 * Acquire the attention of the device.
	 * 
	 * @param context
	 *            Application context
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("Wakelock")
	public static void acquire(Context context) {
		if (wakeLock != null)
			wakeLock.release();

		PowerManager pm = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);
		wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
				| PowerManager.ACQUIRE_CAUSES_WAKEUP
				| PowerManager.ON_AFTER_RELEASE, "WakeLock");
		wakeLock.acquire();
	}

	/**
	 * Release the device.
	 */
	public static void release() {
		if (wakeLock != null)
			wakeLock.release();
		wakeLock = null;
	}
}
