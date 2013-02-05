package app.localization;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class LocalizationActivity extends Activity {
    /** Called when the activity is first created. */
	protected static final int TIMEOUT_MILLISEC = 3000;
	
	EditText inputUserName;
    EditText inputPassword;
    
    TextView username;
    
    LocalizationActivity currentThis = this;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        inputUserName = (EditText) findViewById(R.id.username);
        inputPassword = (EditText) findViewById(R.id.password);
        Button loginButton = (Button) findViewById(R.id.loginButton);
 
        //Listening to button event
        loginButton.setOnClickListener(new View.OnClickListener(){
 
            public void onClick(View arg0) {
            	String userName = inputUserName.getText().toString();
            	String password = inputPassword.getText().toString();
            	
            	 try{
		        	   JSONObject json = new JSONObject();
						json.put("userName", userName);
						json.put("pwd", password);
						json.put("customer", 1);
						HttpParams httpParams = new BasicHttpParams();
				        HttpConnectionParams.setConnectionTimeout(httpParams,
				                TIMEOUT_MILLISEC);
				        HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
				        HttpClient client = new DefaultHttpClient(httpParams);
				        //
				        //String url = "http://10.0.2.2:8080/sample1/webservice2.php?" + 
				        //             "json={\"UserName\":1,\"FullName\":2}";
				        String url = "http://dana.ucc.nau.edu/~cs854/PHPValidateLogin.php";
				        HttpPost request = new HttpPost(url);
						request.setEntity(new ByteArrayEntity(json.toString().getBytes(
							        "UTF8")));														
				        request.setHeader("json", json.toString());
				        HttpResponse response;
						response = client.execute(request);
				        HttpEntity entity = response.getEntity();
				        if (entity != null) {
				            String result = app.utilities.RestClient.getASCIIContentFromEntity(entity);
				            JSONArray json2 = new JSONArray(result);
				            String validated = json2.getJSONObject(0).getString("result"); 
				            if (validated.equals("1")) {
				            	//Starting a new Intent
				                Intent homeScreen = new Intent(getApplicationContext(), HomeActivity.class);			 
				                startActivity(homeScreen);
				            } else {
					            Builder builder = new AlertDialog.Builder(currentThis); 
					    		builder.setMessage("Incorrent Username/Password combination.");
					    		builder.setCancelable(false); 
					    		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
							           public void onClick(DialogInterface dialog, int id) {
							               // User clicked OK button
							           }});  
					    		AlertDialog dialog = builder.create();
					    		dialog.show(); 
				            }
				        }
		        	   
		           } catch (Throwable t) {
				        //Toast.makeText(this, "Request failed: " + t.toString(),
				        //        Toast.LENGTH_LONG).show();
		        	   Builder builder = new AlertDialog.Builder(currentThis); 
			    		builder.setMessage("Error");
			    		builder.setCancelable(false); 
			    		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					           public void onClick(DialogInterface dialog, int id) {
					               // User clicked OK button
					           }});  
			    		AlertDialog dialog = builder.create();
			    		dialog.show(); 
				    }
            }
        });
 
    }
}