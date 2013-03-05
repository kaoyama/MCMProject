package app.localization;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;

public class DealDetail extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.deal_details);
		
		Intent startIntent = getIntent(); 
		String title = startIntent.getStringExtra("title");
		String content = startIntent.getStringExtra("content"); 
		
		// change text depending on which deal was clicked
		TextView titleText = (TextView) findViewById(R.id.dealTitle);
		titleText.setText(title);
		
		TextView contentText = (TextView) findViewById(R.id.dealContent); 
		contentText.setText(content); 
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_deal_detail, menu);
		return true;
	}

}
