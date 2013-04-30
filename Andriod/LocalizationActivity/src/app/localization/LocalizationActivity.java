package app.localization;

import java.io.FileOutputStream;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import app.utilities.CommonUtilities;
import app.utilities.CustomDialog;
import app.utilities.RestClient;

/**
 * Login page when the application is first opened.
 * 
 * @author Chihiro
 * 
 */
public class LocalizationActivity extends Activity {
	protected static final int TIMEOUT_MILLISEC = 3000;

	EditText inputUserName;
	EditText inputPassword;

	TextView username;

	LocalizationActivity currentThis = this;

	/**
	 * Initializes the login page when the application is first opened.
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		inputUserName = (EditText) findViewById(R.id.username);
		inputPassword = (EditText) findViewById(R.id.password);
		Button loginButton = (Button) findViewById(R.id.loginButton);

		// Listening to button event
		loginButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				login();
			}
		});
	}

	/**
	 * Perform the login operation upon correct input from the user. If the user
	 * inputs a in correct username/password combination, the user is notified
	 * and needs to try again.
	 */
	public void login() {

		String userName = inputUserName.getText().toString();
		String password = inputPassword.getText().toString();
		JSONArray jsonArray = null;

		try {
			JSONObject json = new JSONObject();
			json.put("userName", userName);
			json.put("pwd", password);
			json.put("customer", 1);
			json.put("android", 1);

			jsonArray = RestClient.connectToDatabase(CommonUtilities.LOGIN_URL,
					json);

		} catch (Exception e) {
			Log.v("Log in", "Exception while creating JSON object to send.");
		}

		try {
			String validated = jsonArray.getJSONObject(0).getString("result");

			if (validated.equals("1")) {
				// Starting a new Intent
				String fileName = "username_file";
				String fileData = userName;

				FileOutputStream fos = openFileOutput(fileName,
						Context.MODE_PRIVATE);
				fos.write(fileData.getBytes());
				fos.close();

				Intent homeScreen = new Intent(getApplicationContext(),
						HomeActivity.class);
				startActivity(homeScreen);
			} else {
				CustomDialog cd = new CustomDialog(LocalizationActivity.this);
				cd.showNotificationDialog("Incorrect Username/Password combination.");
			}

		} catch (Exception e) {
			Log.v("Log in", "Invalid: " + e.getMessage());
		}
	}
}