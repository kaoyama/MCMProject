package app.utilities;

import java.io.IOException;
import java.io.InputStream;

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
import android.widget.Toast;

public class RestClient {
	
	protected static final int TIMEOUT_MILLISEC = 3000;
	
	/**
	 * Gets ASCII content from the HTTP response entity.
	 * @param entity HttpEntity object retrieved from HTTP response.
	 * @return string version of HTTP entity. 
	 */
	public static String getASCIIContentFromEntity(HttpEntity entity) {
		try {
			InputStream in = entity.getContent();
			StringBuffer out = new StringBuffer();
			int n = 1;
			while (n > 0) {
				byte[] b = new byte[4096];
				n =  in.read(b);
				if (n>0) out.append(new String(b, 0, n));
			}
			return out.toString();
		} catch (IllegalStateException e) {
			return "IllegalStateException thrown.";
		} catch (IOException e) {
			return "IOException thrown.";
		}
		
	}
	
	/**
	 * Connects to a specific URL, sending a JSON string and retrieving 
	 * a JSON string in return. 
	 * @param url URL address containing the PHP script.
	 * @param json JSON object to send to the PHP specified in the URL. If no input is required 
	 * for database connection, can be null. 
	 * @param activity  Activity object to display toast in case of failure. 
	 * @return JSON array returned from PHP call to the database. If nothing is returned 
	 * from the PHP query, return null.  On failed connection to the database, 
	 * returns null and displays a toast to the app with the exception. 
	 */
	public static JSONArray connectToDatabase(String url, JSONObject json, Activity activity) {
		try {        
	        // Establish an HTTP connection 
			HttpParams httpParams = new BasicHttpParams();
	        HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
	        HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
	        HttpClient client = new DefaultHttpClient(httpParams);
	        
	        HttpPost request = new HttpPost(url);
	        if (json != null) {
	        	request.setEntity(new ByteArrayEntity(json.toString().getBytes("UTF8")));
	        	request.setHeader("json", json.toString());
	        }
	        HttpResponse response = client.execute(request);
	        HttpEntity entity = response.getEntity();
	        
	        // If the response does not enclose an entity, there is no need
	        if (entity != null) {
	            String result = app.utilities.RestClient.getASCIIContentFromEntity(entity);
	            if (result != null) {
	            	JSONArray jsonResult = new JSONArray(result); 
	            	return jsonResult; 
	            } else return null; 
	        } else {
	        	return null;
	        }
	        
	    } catch (Throwable t) {
	    	// Display toast to notify exception occurrence
	        Toast.makeText(activity, "Request failed: " + t.toString(),
	        		Toast.LENGTH_LONG).show();
	        return null; 
	    }
	}
}
