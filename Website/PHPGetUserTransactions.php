<?php

/**
 * Retrieve all transaction history for the specific user.  
 * Queries the table "customerTransactions."
 */
include_once './dbConfig/DBFunctions.php';
$db = new DBFunctions();     
$db->connect();
    
$json = file_get_contents('php://input');
$obj = json_decode($json);
$userName = $obj->{'userName'};
        
$query = "SELECT * FROM kd268.customerTransactions " . 
        " WHERE customer = '$userName' ORDER BY PurchaseTime DESC";
 
$result = $db->query($query); 
print $db->resultToJson($result); 
?>