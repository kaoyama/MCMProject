package app.localization;


import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MerchantMap extends MapActivity {
	/** Called when the activity is first created. */
	
	double latitude; 
	double longitude; 
	static long MILLION = 1000000; 
	MapView mapView;
	MerchantMapItemOverlay itemizedOverlay;
	List<Overlay> mapOverlays;

	/**
	 * Returns if it is currently displaying the route information.
	 */
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	TextView username;

	public void onCreate(Bundle savedInstanceState) {
		// Loads layout file in res/layout/
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map); 

		// Allow zoom
		MapView mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);

		// Instantiate map overlay object
		List<Overlay> mapOverlays = mapView.getOverlays(); 
		Drawable drawable = this.getResources().getDrawable(R.drawable.androidmarker);
		MerchantMapItemOverlay itemizedOverlay = new MerchantMapItemOverlay(drawable, this); 

		// Create a GeoPoint to define specific map coordinates
		// Note: GeoPoint coordinate specified in microdegrees (degrees * 1e6)
		//GeoPoint point = new GeoPoint(35199728,-111648606);
		getLocation(); 
		GeoPoint point = new GeoPoint((int)(latitude*MILLION), (int)(longitude*MILLION));
		OverlayItem overlayitem = new OverlayItem(point, "Hi!", "You are here!");

		// Set default zoom
		MapController mapController = mapView.getController();
		mapController.setCenter(point); 
		mapController.setZoom(16); 

		// Add OverlayItem to the collection in MerchantMapItemOverlay instance
		itemizedOverlay.addOverlay(overlayitem); 
		mapOverlays.add(itemizedOverlay);        
	}

	//Listening to button event
	public void getLocation() {
		
		LocationListener myLocationListener = new LocationListener() {
			public void onLocationChanged(Location loc) {
				//sets and displays the lat/long when a location is provided
				getLocation(); 
				
				latitude = loc.getLatitude();
				longitude = loc.getLongitude();
				
				
		///		
				// Retrieve merchant info from Merchant.java
				Bundle b = getIntent().getExtras();
				String jsonArray = b.getString("merchantInfo");
		///		
				
				
				GeoPoint point = new GeoPoint((int)(latitude*MILLION), (int)(longitude*MILLION));
				OverlayItem overlayitem = new OverlayItem(point, "Hi!", jsonArray);

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

		/*String mlocProvider;
		
		Criteria hdCrit = new Criteria();

		hdCrit.setAccuracy(Criteria.ACCURACY_COARSE);

//		mlocProvider = locationManager.getBestProvider(hdCrit, true);
		mlocProvider = locationManager.NETWORK_PROVIDER;

		//locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 1000, myLocationListener);
		//Location currentLocation = locationManager.getLastKnownLocation(mlocProvider);
		Location currentLocation = locationManager.getProvider(NETWORK_PROVIDER);
		*/
		
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		//locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 1000, myLocationListener); 
		Location currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER); 
		latitude = currentLocation.getLatitude();
		longitude = currentLocation.getLongitude();
	}
}