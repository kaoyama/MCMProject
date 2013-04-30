package app.localization;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * View to display the detailed view of a specific deal.
 * 
 * @author Chihiro
 * 
 */
public class DealDetail extends Activity {

	/**
	 * Initialize the page upon startup of the deal details page.
	 * 
	 * @param savedInstanceState
	 *            Information of the deal passed on from the deals page, which
	 *            includes the title, content, and merchant information.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.deal_details);

		// retrieve the passed information from the bundle
		Intent startIntent = getIntent();
		String title = startIntent.getStringExtra("title");
		String content = startIntent.getStringExtra("content");
		String merchant = startIntent.getStringExtra("merchant");

		// change text depending on which deal was clicked
		TextView titleText = (TextView) findViewById(R.id.dealTitle);
		titleText.setText(title + " from " + merchant);

		TextView contentText = (TextView) findViewById(R.id.dealContent);
		contentText.setText(content);

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
	 * Initialize options menu.
	 * 
	 * @param menu
	 *            Menu item
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_deal_detail, menu);
		return true;
	}

}
