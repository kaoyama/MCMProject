package app.localization;

// TODO: Look at this one: 
// http://www.codeproject.com/Articles/112044/GPSLocator-App-to-Find-Current-Nearest-Location-us

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
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
		//MerchantMapItemOverlay itemizedOverlay = new MerchantMapItemOverlay(drawable, this); 

		// Create a GeoPoint to define specific map coordinates
		// Note: GeoPoint coordinate specified in microdegrees (degrees * 1e6)
		//GeoPoint point = new GeoPoint(35199728,-111648606);
		getLocation(); 
		
		// Retrieve merchant info from Merchant.java
		Bundle b = getIntent().getExtras();
		JSONArray jsonArray = null; 
		
		// Create an instance of MyItemizedOverlay
		Drawable markerDefault = this.getResources().getDrawable(R.drawable.androidmarker); 
		MyItemizedOverlay itemizedOverlay = new MyItemizedOverlay(markerDefault);

		try {
			if (b != null) {
				jsonArray = new JSONArray(b.getString("merchantInfo"));
				
				for (int i = 0; i < jsonArray.length(); i++) {	
					int lat = Integer.parseInt(jsonArray.getJSONObject(i).getString("latitude")); 
					int lon = Integer.parseInt(jsonArray.getJSONObject(i).getString("longitude")); 
					String name = jsonArray.getJSONObject(i).getString("merchantUserName");
					
					itemizedOverlay.addOverlayItem(lat, lon, name, 
							this.getResources().getDrawable(R.drawable.beergarden)); 
				}
			}
		} catch (Exception e) {
			// TODO: Handle errors 
		}
		
		// Your current location 
		itemizedOverlay.addOverlayItem((int)(latitude*MILLION), (int)(longitude*MILLION),
				"You are here!", this.getResources().getDrawable(R.drawable.kangaroo2)); 
		
	
		// Set default zoom
		MapController mapController = mapView.getController();
		GeoPoint point = new GeoPoint((int)(latitude*MILLION), (int)(longitude*MILLION));
		mapController.setCenter(point); 
		mapController.setZoom(18); 

		// Add OverlayItem to the collection in MerchantMapItemOverlay instance
		//itemizedOverlay.addOverlay(overlayitem); 
		//mapOverlays.add(itemizedOverlay);
		
		mapView.getOverlays().add(itemizedOverlay); 
		    
	}

	//Listening to button event
	public void getLocation() {
		
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
	
		LocationListener myLocationListener = new LocationListener() {
			public void onLocationChanged(Location loc) {
				//sets and displays the lat/long when a location is provided
				//getLocation(); 
				
				latitude = loc.getLatitude();
				longitude = loc.getLongitude();
				
				GeoPoint point = new GeoPoint((int)(latitude*MILLION), (int)(longitude*MILLION));
				OverlayItem overlayitem = new OverlayItem(point, "Hi!", "You are here!");

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
		
		//locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, myLocationListener); 
		Location currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER); 
		latitude = currentLocation.getLatitude();
		longitude = currentLocation.getLongitude();
		
	}
	
	// http://androidcookbook.com/Recipe.seam;jsessionid=509EB1AD88ECB395598B6F1F5DC8361D?recipeId=2308
	private class MyItemizedOverlay extends ItemizedOverlay<OverlayItem> {
		
		private List<OverlayItem> mOverlays = new ArrayList<OverlayItem>(); 
		
		public MyItemizedOverlay(Drawable defaultMarker) {
			super(boundCenterBottom(defaultMarker)); 
		}

		@Override
		protected OverlayItem createItem(int i) {
			return mOverlays.get(i);
		}

		@Override
		public int size() {
			return mOverlays.size(); 
		}
		
		public void addOverlayItem(OverlayItem overlayItem) {
			mOverlays.add(overlayItem); 
			populate(); 
		}
		
		public void addOverlayItem(int lat, int lon, String title, Drawable marker) {
			GeoPoint point = new GeoPoint(lat, lon); 
			OverlayItem overlayItem = new OverlayItem(point, title, null);
			overlayItem.setMarker(boundCenterBottom(marker)); 
			addOverlayItem(overlayItem); 
		}
	
		protected boolean onTap(int index) {
			Toast.makeText(MerchantMap.this, getItem(index).getTitle(), 
					Toast.LENGTH_SHORT).show();
			return true; 
		}
	}
}