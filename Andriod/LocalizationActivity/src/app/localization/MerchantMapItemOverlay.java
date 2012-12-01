package app.localization;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

/**
 * Manage individual overlay items placed on the map. 
 */
public class MerchantMapItemOverlay extends ItemizedOverlay {
	
	// List to hold each of OverlayItem objects
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	
	// Application context 
	Context mContext; 
	
	/**
	 * Constructor for itemized overlay.
	 * Define the bounds of the default marker object. 
	 * Commonly, the center-point at the bottom of the image is placed 
	 * at the point attached to the map coordinates. 
	 * @param defaultMarker
	 */
	public MerchantMapItemOverlay(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker)); 
		mContext = context; 
	}

	/**
	 * Add overlay items to the ArrayList. 
	 * The populate() method reads each of the OverlayItem objects and 
	 * prepares them to be drawn on the map. 
	 * @param overlay
	 */
	public void addOverlay(OverlayItem overlay) {
		mOverlays.add(overlay); 
		populate(); 
	}

	/**
	 * Create each OverlayItem. 
	 */
	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	/**
	 * Get current number of items in ArrayList.
	 */
	@Override
	public int size() {
		return mOverlays.size();
	}

	/**
	 * Callback method for user tap on the marker. 
	 */
	protected boolean onTap(int index) {
		OverlayItem item = mOverlays.get(index); 
		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext); 
		dialog.setTitle(item.getTitle()); 
		dialog.setMessage(item.getSnippet()); 
		dialog.show(); 
		return true; 
	}
}