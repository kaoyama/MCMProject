package app.localization;


import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MerchantMap extends MapActivity {
    /** Called when the activity is first created. */

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
        GeoPoint point = new GeoPoint(35199728,-111648606);
        OverlayItem overlayitem = new OverlayItem(point, "Hi!", "This is downtown Flagstaff!");
        
        // Set default zoom
        MapController mapController = mapView.getController();
        mapController.setCenter(point); 
        mapController.setZoom(16); 
        
        // Add OverlayItem to the collection in MerchantMapItemOverlay instance
        itemizedOverlay.addOverlay(overlayitem); 
        mapOverlays.add(itemizedOverlay);        
    }
}