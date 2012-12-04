package app.localization;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.graphics.Color;
import android.location.*;


public class HomeActivity extends Activity {
    /** Called when the activity is first created. */

    
    TextView username;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        
        Button mapButton = (Button) findViewById(R.id.mapButton);
        
        //Listening to button event
        mapButton.setOnClickListener(new View.OnClickListener(){
 
            public void onClick(View arg0) {
                //Starting a new Intent
                Intent mapScreen = new Intent(getApplicationContext(), MerchantMap.class);
 
                startActivity(mapScreen);
 
            }
        });
        
        Button merchantButton = (Button) findViewById(R.id.merchantButton);
        
        //Listening to button event
        merchantButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View arg0) {
        		// Starting a new intent
        		Intent merchantScreen = new Intent(getApplicationContext(), Merchant.class);
        		
        		startActivity(merchantScreen); 
        	}
        });
        
        final Button gpsButton = (Button) findViewById(R.id.gpsButton);
        
        //Listening to button event
        gpsButton.setOnClickListener(new View.OnClickListener(){
 
            public void onClick(View arg0) {
            	LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            
            	LocationListener myLocationListener=new LocationListener() {
                    public void onLocationChanged(Location loc) {
                        //sets and displays the lat/long when a location is provided
                        String latlong = "Lat: " + loc.getLatitude() + " Long: " + loc.getLongitude();   
                        gpsButton.setText(latlong);
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
					
					String mlocProvider;
					Criteria hdCrit = new Criteria();
					
					hdCrit.setAccuracy(Criteria.ACCURACY_COARSE);
					
					mlocProvider = locationManager.getBestProvider(hdCrit, true);
					
					locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 1000, myLocationListener);
					Location currentLocation = locationManager.getLastKnownLocation(mlocProvider);
					locationManager.removeUpdates(myLocationListener);
 
					double currentLat = currentLocation.getLatitude();
					double currentLon = currentLocation.getLongitude();
					
					gpsButton.setText(currentLat + ", " + currentLon);
					
            }
        });
        
        Button logoutButton = (Button) findViewById(R.id.logoutButton);
        
        //Listening to button event
        logoutButton.setOnClickListener(new View.OnClickListener(){
 
            public void onClick(View arg0) {
                //Starting a new Intent
                Intent homeScreen = new Intent(getApplicationContext(), LocalizationActivity.class);
 
                startActivity(homeScreen);
 
            }
        });
        
    }
}