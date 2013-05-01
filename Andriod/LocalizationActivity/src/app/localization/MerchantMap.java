package app.localization;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import app.utilities.CommonUtilities;
import app.utilities.RestClient;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

/**
 * Displays a map view of nearby merchants within a certain radius determined
 * by the web service.
 * 
 * @author Chihiro
 * 
 */
public class MerchantMap extends MapActivity {

	double latitude;
	double longitude;
	static long MILLION = 1000000;
	MapView mapView;
	MerchantMapItemOverlay itemizedOverlay;
	List<Overlay> mapOverlays;

	Bundle b;

	/**
	 * Returns if it is currently displaying the route information.
	 */
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	/**
	 * Initializes the page when the activity is first created.
	 */
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);

		// get merchants from database
		b = new Bundle();
		getMerchants();

		// Allow zoom
		MapView mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);

		// Create a GeoPoint to define specific map coordinates
		// Note: GeoPoint coordinate specified in microdegrees (degrees * 1e6)
		// GeoPoint point = new GeoPoint(35199728,-111648606);
		getLocation();

		// Retrieve merchant info from Merchant.java
		JSONArray jsonArray = null;

		// Create an instance of MyItemizedOverlay
		Drawable markerDefault = this.getResources().getDrawable(
				R.drawable.androidmarker);
		MyItemizedOverlay itemizedOverlay = new MyItemizedOverlay(markerDefault);

		// Add each of the merchants as an overlay item to the map
		try {
			if (b != null) {
				jsonArray = new JSONArray(b.getString("merchantInfo"));

				for (int i = 0; i < jsonArray.length(); i++) {
					int lat = Integer.parseInt(jsonArray.getJSONObject(i)
							.getString("latitude"));
					int lon = Integer.parseInt(jsonArray.getJSONObject(i)
							.getString("longitude"));
					String name = jsonArray.getJSONObject(i).getString(
							"merchantUserName");

					itemizedOverlay.addOverlayItem(lat, lon, name, this
							.getResources().getDrawable(R.drawable.beergarden));
				}
			}
		} catch (Exception e) {
			Log.v("Merchant Map",
					"Exception while adding merchants to merchant map. "
							+ e.getMessage());
		}

		// Your current location - doesn't change as you move around
		/*
		 * itemizedOverlay.addOverlayItem((int)(latitude*MILLION),
		 * (int)(longitude*MILLION), "You are here!",
		 * this.getResources().getDrawable(R.drawable.kangaroo2));
		 */

		// Your current location - changes as you move around
		MyLocationOverlay mylc = new MyLocationOverlay(this, mapView);
		mapView.getOverlays().add(mylc);
		mylc.enableMyLocation();

		// Set default zoom
		MapController mapController = mapView.getController();
		GeoPoint point = new GeoPoint((int) (latitude * MILLION),
				(int) (longitude * MILLION));
		mapController.setCenter(point);
		mapController.setZoom(19);

		if (itemizedOverlay != null) {
			mapView.getOverlays().add(itemizedOverlay);
		}

	}

	/**
	 * Get the current or newest location of the user.
	 */
	public void getLocation() {

		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		@SuppressWarnings("unused")
		LocationListener myLocationListener = new LocationListener() {
			public void onLocationChanged(Location loc) {

				latitude = loc.getLatitude();
				longitude = loc.getLongitude();

				GeoPoint point = new GeoPoint((int) (latitude * MILLION),
						(int) (longitude * MILLION));
				OverlayItem overlayitem = new OverlayItem(point, "Hi!",
						"You are here!");

				// Set default zoom
				MapController mapController = mapView.getController();
				mapController.setCenter(point);

				itemizedOverlay.addOverlay(overlayitem);
				mapOverlays.add(itemizedOverlay);
			}

			public void onProviderDisabled(String provider) {
				// required for interface, not used
			}

			public void onProviderEnabled(String provider) {
				// required for interface, not used
			}

			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// required for interface, not used
			}
		};

		Location currentLocation = locationManager
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		latitude = currentLocation.getLatitude();
		longitude = currentLocation.getLongitude();
	}

	/**
	 * Customized itemized overlay that displays the merchant name next to the
	 * icon displayed on the map. Credits:
	 * http://androidcookbook.com/Recipe.seam
	 * ;jsessionid=509EB1AD88ECB395598B6F1F5DC8361D?recipeId=2308
	 */
	private class MyItemizedOverlay extends ItemizedOverlay<OverlayItem> {

		private static final int FONT_SIZE = 12;
		private static final int TITLE_MARGIN = 3;
		private List<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
		private int markerHeight = 3;

		/**
		 * Constructor for the marker
		 * 
		 * @param defaultMarker
		 *            Default marker to be used on the map
		 */
		public MyItemizedOverlay(Drawable defaultMarker) {
			super(boundCenterBottom(defaultMarker));

			// call populate in itemized overlay constructor to avoid null
			// pointer exception
			populate();

			// set default marker height
			markerHeight = ((BitmapDrawable) MerchantMap.this.getResources()
					.getDrawable(R.drawable.beergarden)).getBitmap()
					.getHeight();
		}

		/**
		 * Return the item at index i
		 * 
		 * @param i
		 *            Index of the item to be returned
		 * @return OverlayItem
		 */
		@Override
		protected OverlayItem createItem(int i) {
			return mOverlays.get(i);
		}

		/**
		 * Returns the number of overlay items
		 * 
		 * @return Number of overlay items
		 */
		@Override
		public int size() {
			return mOverlays.size();
		}

		/**
		 * Add an existing overlay item
		 * 
		 * @param overlayItem
		 *            New item to be added
		 */
		public void addOverlayItem(OverlayItem overlayItem) {
			mOverlays.add(overlayItem);
			populate();
		}

		/**
		 * Add a new overlay item
		 * 
		 * @param lat
		 *            Latitude
		 * @param lon
		 *            Longitude
		 * @param title
		 *            Title of the item
		 * @param marker
		 *            Marker
		 */
		public void addOverlayItem(int lat, int lon, String title,
				Drawable marker) {
			GeoPoint point = new GeoPoint(lat, lon);
			OverlayItem overlayItem = new OverlayItem(point, title, null);
			overlayItem.setMarker(boundCenterBottom(marker));
			addOverlayItem(overlayItem);
		}

		/**
		 * Called when the overlay item (merchant icon) is clicked by the user.
		 * 
		 * @param index
		 *            Index of the tapped item.
		 * @param Returns
		 *            true upon success
		 */
		protected boolean onTap(int index) {

			Intent dealsScreen = new Intent(getApplicationContext(),
					Deals.class);
			startActivity(dealsScreen);
			return true;
		}

		/**
		 * Draw the overlay item on the map. This function specifically adds the
		 * functionality of adding merchant name information along with the
		 * marker. Credits:
		 * http://binwaheed.blogspot.com/2011/05/android-display
		 * -title-on-marker-in.html
		 * 
		 * @param canvas
		 *            Canvas to draw on
		 * @param mapView
		 *            Map that contains the overlay items
		 * @param shadow
		 *            True to request a shadow on the items
		 */
		@Override
		public void draw(android.graphics.Canvas canvas, MapView mapView,
				boolean shadow) {
			super.draw(canvas, mapView, shadow);

			// go through all OverlayItems and draw title for each of them
			for (OverlayItem item : mOverlays) {
				/*
				 * Converts latitude & longitude of this overlay item to
				 * coordinates on screen. As we have called boundCenterBottom()
				 * in constructor, so these coordinates will be of the bottom
				 * center position of the displayed marker.
				 */
				GeoPoint point = item.getPoint();
				Point markerBottomCenterCoords = new Point();
				mapView.getProjection().toPixels(point,
						markerBottomCenterCoords);

				/* Find the width and height of the title */
				TextPaint paintText = new TextPaint();
				Paint paintRect = new Paint();

				Rect rect = new Rect();
				paintText.setTextSize(FONT_SIZE);
				paintText.getTextBounds(item.getTitle(), 0, item.getTitle()
						.length(), rect);

				rect.inset(-TITLE_MARGIN, -TITLE_MARGIN);
				rect.offsetTo(
						markerBottomCenterCoords.x - rect.width() / 2,
						markerBottomCenterCoords.y - markerHeight
								- rect.height());

				paintText.setTextAlign(Paint.Align.CENTER);
				paintText.setTextSize(FONT_SIZE);
				paintText.setARGB(255, 255, 255, 255);
				paintRect.setARGB(130, 0, 0, 0);

				canvas.drawRoundRect(new RectF(rect), 2, 2, paintRect);
				canvas.drawText(item.getTitle(), rect.left + rect.width() / 2,
						rect.bottom - TITLE_MARGIN, paintText);
			}
		}
	}

	/**
	 * Get a list of nearby merchants from the database. The web service
	 * determines the radius containing the merchants.
	 */
	public void getMerchants() {

		String username = CommonUtilities.getUsername(MerchantMap.this);
		JSONObject jsonIn = new JSONObject();

		try {
			jsonIn.put("userName", username);
		} catch (Exception e) {
			Log.v("Merchants", "JSON Exception");
		}

		final JSONArray jsonArray = RestClient.connectToDatabase(
				CommonUtilities.NEARBYMERCHANTS_URL, jsonIn);

		if (jsonArray != null) {
			b.putString("merchantInfo", jsonArray.toString());
			Log.v("MerchantMap", "From database: " + jsonArray.toString());

		} else {
			Log.v("MerchantMap", "Merchant list is empty.");
		}
	}
}