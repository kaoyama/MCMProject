<?php

/**
 * Unsubscribe from all merchants. Queries "subscribeForDeals" table. 
 */
include_once './dbConfig/DBFunctions.php';
$db = new DBFunctions();     
$db->connect();
    
$json = file_get_contents('php://input');
$obj = json_decode($json);
$userName = $obj->{'userName'};
        
// remove all rows for customer
$query = "DELETE FROM kd268.subscribeForDeals WHERE customer = '$userName'";
$res = mysql_query($query); 

?>