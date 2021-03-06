package app.localization;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import app.utilities.CommonUtilities;
import app.utilities.CustomDialog;
import app.utilities.MySQLiteHelper;
import app.utilities.RestClient;

/**
 * View for the coupons list. Pulls subscribed coupons and ads from the
 * database. Need service-oriented architecture and needs three elements:
 * external database, web-service, mobile web-service client.
 * 
 * @author Chihiro
 * 
 */
public class Deals extends Activity {

	TextView username;
	TextView result;

	String dbResult;

	static int TIMEOUT_MILLISEC = 3000;

	List<String> listContents;
	ListView myListView;
	ArrayAdapter<String> adapter;
	MySQLiteHelper db = new MySQLiteHelper(Deals.this);

	public static int MERCHANT = 0;
	public static int TITLE = 1;
	public static int CONTENT = 2;
	public static int DEALINDEX = 3;

	SparseIntArray indexMap = new SparseIntArray();
	int index = 0;

	/**
	 * Initialize deals page upon startup of the page.
	 * 
	 * @param savedInstanceState
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.deals);

		listContents = new ArrayList<String>();

		myListView = (ListView) findViewById(R.id.dealsList);

		// get list of coupons from database and save to SQLite
		db.onUpgrade(db.getReadableDatabase(), 1, 2); // wipe SQLite data
		saveDealsToSQLite();
		Log.d(Deals.class.toString(), "Updating database from Deals page");

		// show list of deals as ListView on Deals page
		showSavedDeals();

		// showDeals();

		// home button
		Button settingsButton = (Button) findViewById(R.id.homeButton);
		settingsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Starting a new intent
				Intent homeScreen = new Intent(getApplicationContext(),
						HomeActivity.class);
				startActivity(homeScreen);
			}
		});
	}

	/**
	 * Pull a list of deals from the database at real-time and populate the list
	 * view. This function does not use the stored data in SQLite.
	 */
	public void showDeals() {
		// get current session's username
		String username = CommonUtilities.getUsername(Deals.this);
		JSONObject jsonIn = new JSONObject();

		try {
			jsonIn.put("userName", username);
		} catch (Exception e) {
			Log.v("Deals", "JSON Exception");
		}

		final JSONArray jsonArray = RestClient.connectToDatabase(
				CommonUtilities.GETDEALS_URL, jsonIn);

		if (jsonArray != null) {

			runOnUiThread(new Runnable() {

				@Override
				public void run() {

					// populate the listview with information from database
					try {
						listContents = new ArrayList<String>(jsonArray.length());

						for (int i = 0; i < jsonArray.length(); i++) {
							String title = jsonArray.getJSONObject(i)
									.getString("title");
							String merchant = jsonArray.getJSONObject(i)
									.getString("merchant");
							listContents.add(title + " from " + merchant);
						}

					} catch (JSONException e) {
						CustomDialog cd = new CustomDialog(Deals.this);
						cd.showNotificationDialog(e.getMessage());
					}

					adapter = new ArrayAdapter<String>(Deals.this,
							android.R.layout.simple_list_item_1, listContents);
					adapter.setNotifyOnChange(true);
					myListView.setAdapter(adapter);

					// Each deal item is clickable and takes you to another page
					myListView
							.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> a,
										View v, int position, long id) {
									Intent dealDetailIntent = new Intent(
											Deals.this, DealDetail.class);

									try {

										Log.d(Deals.class.toString(),
												"Requesting deals for position: "
														+ position);
										Log.d(Deals.class.toString(),
												"IndexMap at position is: "
														+ indexMap
																.get(position));

										// get title and content and put in
										// Android list
										dealDetailIntent.putExtra(
												"title",
												jsonArray.getJSONObject(
														position).getString(
														"title"));
										dealDetailIntent.putExtra(
												"content",
												jsonArray.getJSONObject(
														position).getString(
														"content"));
										dealDetailIntent.putExtra(
												"merchant",
												jsonArray.getJSONObject(
														position).getString(
														"merchant"));
										startActivity(dealDetailIntent);

									} catch (Exception e) {
										Log.e(Deals.class.toString(),
												"Exception showing specific deals\n"
														+ e.getMessage());
									}

								}
							});
				}
			});

		} else {
			CustomDialog cd = new CustomDialog(Deals.this);
			cd.showNotificationDialog("Merchant list is empty.");
		}
	}

	/**
	 * Show saved deals from the Android SQLite database
	 */
	public void showSavedDeals() {

		runOnUiThread(new Runnable() {

			@Override
			public void run() {

				listContents = new ArrayList<String>();

				Cursor cursor = db.all(Deals.this);
				String merchant;
				String title;
				int dealIndex;

				cursor.moveToFirst();

				// refresh index and mapping of position to dealIndex
				index = 0;
				indexMap = new SparseIntArray();

				try {

					while (cursor != null && cursor.isAfterLast() == false) {

						// warning: type (getString, getInt) must match column
						// type
						merchant = cursor.getString(0);
						title = cursor.getString(1);
						dealIndex = cursor.getInt(2);

						Log.d(Deals.class.toString(), "title: " + title
								+ " where dealIndex: " + dealIndex);

						listContents.add(title + " from " + merchant);

						// save listContents index to hashmap
						indexMap.put(index, dealIndex);
						index++;

						cursor.moveToNext();
					}
				} catch (Exception e) {
					Log.e(Deals.class.toString(),
							"Exception while showing saved deals:\n"
									+ e.getLocalizedMessage());
				}

				adapter = new ArrayAdapter<String>(Deals.this,
						android.R.layout.simple_list_item_1, listContents);
				adapter.setNotifyOnChange(true);
				myListView.setAdapter(adapter);

				// Each deal item can be clicked on and taken to another page
				myListView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> a, View v,
							int position, long id) {
						Intent dealDetailIntent = new Intent(Deals.this,
								DealDetail.class);

						try {

							Log.d(Deals.class.toString(),
									"Requesting deals for position: "
											+ position);
							Log.d(Deals.class.toString(),
									"IndexMap at position is: "
											+ indexMap.get(position));

							Cursor cursor = db.dealContent(Deals.this,
									indexMap.get(position));

							// get title and content and put in Android list
							cursor.moveToFirst();
							dealDetailIntent.putExtra("title",
									cursor.getString(TITLE));
							dealDetailIntent.putExtra("content",
									cursor.getString(CONTENT));
							dealDetailIntent.putExtra("merchant",
									cursor.getString(MERCHANT));
							startActivity(dealDetailIntent);

						} catch (Exception e) {
							Log.e(Deals.class.toString(),
									"Exception showing specific deals\n"
											+ e.getMessage());
						}

					}
				});
			}
		});

	}

	/**
	 * Save deals from database to SQLite, the database on the Android device
	 */
	public void saveDealsToSQLite() {
		String username = CommonUtilities.getUsername(Deals.this);
		JSONObject jsonIn = new JSONObject();

		try {
			jsonIn.put("userName", username);
		} catch (Exception e) {
			Log.v("Deals", "JSON Exception");
		}

		final JSONArray json = RestClient.connectToDatabase(
				app.utilities.CommonUtilities.GETDEALS_URL, jsonIn);

		try {
			for (int i = 0; i < json.length(); i++) {
				JSONObject jsonObj = json.getJSONObject(i);

				// insert the newly retrieved information to the device's SQLite
				db.insert(
						(int) Integer.valueOf(jsonObj.getString("dealIndex")),
						jsonObj.getString("merchant"),
						jsonObj.getString("title"),
						jsonObj.getString("content"));
			}

			db.close();
		} catch (Exception e) {
			Log.v(Deals.class.getName(),
					"Exception while saving to database:\n" + e.getMessage());
			CustomDialog cd3 = new CustomDialog(Deals.this);
			cd3.showNotificationDialog("EXCEPTION while saving to database");
		}
	}
}
