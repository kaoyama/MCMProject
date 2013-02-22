package app.utilities;

import static app.utilities.CommonUtilities.SERVER_URL;
import static app.utilities.CommonUtilities.TAG;

import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import app.localization.R;

import com.google.android.gcm.GCMRegistrar;


public final class ServerUtilities {
	private static final int MAX_ATTEMPTS = 5;
    private static final int BACKOFF_MILLI_SECONDS = 2000;
    private static final Random random = new Random();

    /**
     * Register this account/device pair within the server.
     *
     */
    public static boolean register(final Context context, String name, String email, final String regId) {
       
    	Log.i(TAG, "registering device (regId = " + regId + ")"); 
    	JSONArray jsonArray = null;
		JSONObject json = new JSONObject();

		try {
			json.put("name", name);
			json.put("email", email);
			json.put("regId", regId);
		} catch (Exception e) {
			Log.d(TAG, "exception in JSON"); 
		}
		
		Log.i(TAG, "JSON sent is: " + json.toString());
        
        long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
        // Once GCM returns a registration id, we need to register on our server
        // As the server might be down, we will retry it a couple
        // times.
        for (int i = 1; i <= MAX_ATTEMPTS; i++) {
            Log.d(TAG, "Attempt #" + i + " to register");
            try {
            	Log.d(TAG, "Try connecting!");
            	//jsonArray = RestClient.connectToDatabase(
    			//		CommonUtilities.REGISTRATION_URL, json);
                jsonArray = RestClient.connection(
                		CommonUtilities.REGISTRATION_URL, json, null);
            	
                Log.i(TAG, "JSON returned is: " + jsonArray.toString());
                String result = jsonArray.getJSONObject(0).getString("result"); 
                if (result.equals("1")) {
                	// success 
                	Log.i(TAG, "Successful registration.");
                	GCMRegistrar.setRegisteredOnServer(context, true);
                    return true;
                    
                } else {
                    // Here we are simplifying and retrying on any error; in a real
                	// application, it should retry only on unrecoverable errors
                	// (like HTTP error code 503).
	                Log.e(TAG, "Failed to register on attempt " + i);
	                if (i == MAX_ATTEMPTS) {
	                    break;
	                }
	                try {
	                    Log.d(TAG, "Sleeping for " + backoff + " ms before retry");
	                    Thread.sleep(backoff);
	                } catch (InterruptedException e1) {
	                    // Activity finished before we complete - exit.
	                    Log.d(TAG, "Thread interrupted: abort remaining retries!");
	                    Thread.currentThread().interrupt();
	                    return false;
	                }
	                // increase backoff exponentially
	                backoff *= 2;
                }
            } catch (Exception e) {
            	Log.d(TAG, "Exception while trying to register."); 
            	Thread.currentThread().interrupt();
            	return false;
            }
        }
        String message = context.getString(R.string.server_register_error,
                MAX_ATTEMPTS);
        //CommonUtilities.displayMessage(context, message);
		return false;
    }

    /**
     * Unregister this account/device pair within the server.
     */
    public static void unregister(final Context context, final String regId) {
        Log.i(TAG, "unregistering device (regId = " + regId + ")");
        String serverUrl = SERVER_URL + "/unregister";
        
        JSONArray jsonArray = null;
		JSONObject json = new JSONObject();

		try {
			json.put("regId", regId);

			jsonArray = RestClient.connectToDatabase(
					CommonUtilities.SERVER_URL, json);

		
			jsonArray = RestClient.connectToDatabase(serverUrl, json);
	        
	        String result = jsonArray.getJSONObject(0).getString("result"); 
	        if (result.equals("1")) {
	        	// success 
	        	GCMRegistrar.setRegisteredOnServer(context, true);
	            String message = context.getString(R.string.server_unregistered);
	            //CommonUtilities.displayMessage(context, message);
	            return;
	        }
		} catch (Exception e) {
       
        	String message = context.getString(R.string.server_unregister_error,
                    e.getMessage());
            //CommonUtilities.displayMessage(context, message);
        }
    }

    /**
     * Issue a POST request to the server.
     *
     * @param endpoint POST address.
     * @param params request parameters.
     *
     * @throws IOException propagated from POST.
     */
    /*
    private static void post(String endpoint, Map<String, String> params)
            throws IOException {   	
        
        URL url;
        try {
            url = new URL(endpoint);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid url: " + endpoint);
        }
        StringBuilder bodyBuilder = new StringBuilder();
        Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
        // constructs the POST body using the parameters
        while (iterator.hasNext()) {
            Entry<String, String> param = iterator.next();
            bodyBuilder.append(param.getKey()).append('=')
                    .append(param.getValue());
            if (iterator.hasNext()) {
                bodyBuilder.append('&');
            }
        }
        String body = bodyBuilder.toString();
        Log.v(TAG, "Posting '" + body + "' to " + url);
        byte[] bytes = body.getBytes();
        HttpURLConnection conn = null;
        try {
        	Log.e("URL", "> " + url);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setFixedLengthStreamingMode(bytes.length);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded;charset=UTF-8");
            // post the request
            OutputStream out = conn.getOutputStream();
            out.write(bytes);
            out.close();
            // handle the response
            int status = conn.getResponseCode();
            if (status != 200) {
              throw new IOException("Post failed with error code " + status);
            }
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
      }
      */
}
