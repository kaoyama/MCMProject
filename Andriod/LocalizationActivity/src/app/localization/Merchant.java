package app.localization;


import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Merchant list.  Pulls subscribed or nearby merchant information from the database. 
 * Need service-oriented architecture and needs three elements: 
 * external database, web-service, mobile web-service client. 
 * @author Chihiro
 *
 */

public class Merchant extends Activity {
    /** Called when the activity is first created. */

    TextView username;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant);
        
        // Create label 
        TextView merchantTitle = new TextView(this); 
        merchantTitle.setTextSize(20); 
        merchantTitle.setText("List of Merchants");
        
        // Set the text view as the activity layout 
        setContentView(merchantTitle);
        
    }
}