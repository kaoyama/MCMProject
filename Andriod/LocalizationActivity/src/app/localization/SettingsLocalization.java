package app.localization;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

public class SettingsLocalization extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_localization);

		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

		StringBuilder builder = new StringBuilder();

		builder.append("\nEnable localization: " + sharedPrefs.getBoolean("perform_localization", false));
		builder.append("\nOpt out of all targeted deals: " + sharedPrefs.getBoolean("opt_out_all", false));
		builder.append("\nSpecific merchants: " + sharedPrefs.getString("specific_merchants", "NULL"));

		TextView settingsTextView = (TextView) findViewById(R.id.settings_text_view);
		settingsTextView.setText(builder.toString());

	}

}

