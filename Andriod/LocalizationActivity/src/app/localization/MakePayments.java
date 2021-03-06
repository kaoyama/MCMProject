package app.localization;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import app.utilities.CommonUtilities;
import app.utilities.RestClient;

/**
 * Make Payments page, which is a list of to-do items for the user. Pulls the
 * list of pending charges and displays them as buttons on the page. Need
 * service-oriented architecture and needs three elements: external database,
 * web-service, mobile web-service client.
 * 
 * @author Chihiro
 */
public class MakePayments extends Activity {

	MakePayments currentThis = this;
	LinearLayout paymentLayout;
	String tempUserName = "";
	TextView noChargesText;

	/**
	 * Called when the activity is first created.
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.payments);
		paymentLayout = (LinearLayout) findViewById(R.id.paymentLayout);

		// initialize no charges text
		noChargesText = new TextView(MakePayments.this);
		noChargesText.setText("There are no pending charges at this time.");

		// get list of notifications from database
		getData();
	}

	/**
	 * Get a list of pending charges from the database, and update the view with
	 * buttons for each pending charge.
	 */
	public void getData() {
		// get username from the current session
		final String userName = CommonUtilities.getUsername(MakePayments.this);

		JSONArray jsonArray = null;
		try {
			JSONObject json = new JSONObject();
			json.put("userName", userName);

			jsonArray = RestClient.connectToDatabase(
					CommonUtilities.PAYMENTS_URL, json);

		} catch (Exception e) {
			Log.e("Make Payments", "Failed here: " + e.getMessage());
		}

		int count = 0;
		try {

			if (jsonArray == null || jsonArray.length() == 0) {
				LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT);
				paymentLayout.addView(noChargesText, lp);
			}

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jo = jsonArray.getJSONObject(i);

				final String merchant = jo.getString("merchant");
				final String productIndex = jo.getString("productIndex");
				// final String purchaseTime = jo.getString("purchaseTime");
				final String cost = jo.getString("cost");
				final String paid = jo.getString("paid");
				final String cancelled = jo.getString("cancelled");
				final String transactionIndex = jo
						.getString("transactionIndex");

				if (paid.equals("0") && cancelled.equals("0")) {
					count++; // increment count to keep track of number of
								// buttons
					Button tempButton = new Button(currentThis);
					tempButton.setText(merchant + " requests payment for $"
							+ cost);
					tempButton.setOnClickListener(new View.OnClickListener() {

						public void onClick(View arg0) {
							AlertDialog.Builder builder = new AlertDialog.Builder(
									currentThis);
							builder.setMessage(
									"Would you like to purchase "
											+ productIndex + " for $" + cost
											+ "?").setTitle("Make Payment");
							builder.setNegativeButton("Pay Now",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											makePurchase(true, false,
													transactionIndex,
													"You have successfully paid "
															+ merchant + " $"
															+ cost + ".");
										}
									});
							builder.setPositiveButton("Cancel Payment",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											// User cancelled the transaction
											makePurchase(false, true,
													transactionIndex,
													"You have cancelled the payment for "
															+ merchant
															+ " for $" + cost
															+ ".");
										}
									});

							builder.setNeutralButton("Pay Later",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											// User cancelled the dialog
										}
									});

							// If the response does not enclose an entity, there
							// is no need
							AlertDialog dialog = builder.create();
							dialog.show();
						}
					});
					LayoutParams lp = new LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.WRAP_CONTENT);
					paymentLayout.addView(tempButton, lp);

				}
			}

			// if count is 0, there are no pending charges, so notify user
			if (count == 0) {
				LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT);
				paymentLayout.addView(noChargesText, lp);
			}
		}

		catch (Exception e) {
			Log.e("Make Payments", "Invalid: " + e.getMessage());
		}
	}

	/**
	 * Refresh the entire layout to delete the pending charges buttons that have
	 * been taken care of.
	 */
	public void refreshLayout() {
		paymentLayout.removeAllViews();
	}

	/**
	 * Called when the user clicks on the pending charge button.
	 * 
	 * @param paid
	 *            True if the action is to pay the charge
	 * @param cancelled
	 *            True if the action is to cancel the charge
	 * @param transactionIndex
	 *            Unique transaction index
	 * @param notification
	 *            Notification for the user
	 */
	public void makePurchase(boolean paid, boolean cancelled,
			String transactionIndex, String notification) {
		final String userName = CommonUtilities.getUsername(MakePayments.this);

		try {
			JSONObject json = new JSONObject();
			json.put("paid", paid);
			json.put("cancelled", cancelled);
			json.put("userName", userName);
			json.put("transactionIndex", transactionIndex);

			RestClient.connectToDatabase(CommonUtilities.UPDATEPAYMENT_URL,
					json);

			// show notification that payment has been made
			Toast toast = Toast.makeText(MakePayments.this, notification,
					Toast.LENGTH_LONG);
			toast.show();

		} catch (Exception e) {
			Log.e("Make Payments", "Failed here: " + e.getMessage());
		}

		// clear screen and show new set of buttons
		refreshLayout();
		getData();
	}
}
