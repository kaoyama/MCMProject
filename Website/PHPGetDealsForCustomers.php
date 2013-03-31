<?php

include_once './dbConfig/DBFunctions.php';
$db = new DBFunctions();     
$db->connect();
    
$json = file_get_contents('php://input');
$obj = json_decode($json);
$userName = $obj->{'userName'};

// subscribed merchants
$query = "SELECT dealIndex, redeemed FROM kd268.customerDeals WHERE userName = '$userName'";
$result = $db->query($query); 

print $db->resultToJson($result); 

?>