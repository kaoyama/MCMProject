<?php

include_once './dbConfig/DBFunctions.php';
$db = new DBFunctions();     
$db->connect();
    
$json = file_get_contents('php://input');
$obj = json_decode($json);
$merchant = $obj->{'merchant'};
$customer = $obj->{'customer'};
$productIndex = $obj->{'productIndex'};
$cost = $obj->{'cost'};

// SQL query for android regId, if any 
$query = "SELECT android, regId FROM kd268.customers WHERE userName = '$customer'";
$result = $db->query($query); 
$android = mysql_result($result, 0, 'android'); 
$regId = mysql_result($result, 0, 'regId');

// Android cloud messaging
if ($android == 1) {
    $message = "Approval needed! $$cost charge from $merchant.";

    include_once './cloudMessaging/GCM.php';
    $gcm = new GCM();

    $registrationIds = array($regId);
    $message = array("price" => $message);

    // notify user that charge has been made via push notification 
    $gcm->send_notification($registrationIds, $message);

}

// Get boolean value from customers table 
$query = "INSERT INTO kd268.customerTransactions " . 
        " (merchant, customer, productIndex, cost, paid) VALUES " .
        " ('$merchant', '$customer', '$productIndex', '$cost' , '0')";
 
$result = $db->query($query); 
print $db->resultToJson($result); 
     
?>