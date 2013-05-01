<?php

/**
 * Google Cloud Messaging 
 * Credits: Ravi Tamada 
 */
class GCM {

    /**
     * Constructor for GCM 
     */
    function __construct() {
        // empty
    }

    /**
     * Send push notification 
     * @param type $registrationIds ID of specific device 
     * @param type $message Message to be sent to the phone 
     */
    public function send_notification($registrationIds, $message) {
        // include config
        include_once './config.php';

        // Set POST variables - GCM is sent through Google 
        $url = 'https://android.googleapis.com/gcm/send';

        $fields = array(
            'registration_ids' => $registrationIds,
            'data' => $message,
        );

        $headers = array(
            'Authorization: key=' . GOOGLE_API_KEY,
            'Content-Type: application/json'
        );

        // Open connection
        $ch = curl_init();

        // Set the url, number of POST vars, POST data
        curl_setopt($ch, CURLOPT_URL, $url);

        curl_setopt($ch, CURLOPT_POST, true);
        curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

        // Disabling SSL Certificate support temporarly
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
        curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));

        // Execute post
        $result = curl_exec($ch);
        if ($result === FALSE) {
            die('Curl failed: ' . curl_error($ch));
        }

        // Close connection
        curl_close($ch);
    }

}

?>
