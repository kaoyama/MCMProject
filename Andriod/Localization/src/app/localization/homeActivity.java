package app.localization;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.graphics.Color;

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
                Intent mapScreen = new Intent(getApplicationContext(), HelloGoogleMaps.class);
 
                startActivity(mapScreen);
 
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