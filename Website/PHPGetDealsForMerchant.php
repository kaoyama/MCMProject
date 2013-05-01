<?php

/**
 * Retrieve all deals for the specific merchant from the "deals" table.  
 */
include_once './dbConfig/DBFunctions.php';
$db = new DBFunctions();     
$db->connect();
    
$json = file_get_contents('php://input');
$obj = json_decode($json);
$merchantName = $obj->{'merchant'};
        
// Get merchant their deals that they have created
$query = "SELECT * from kd268.deals " .
        "WHERE merchant = '$merchantName' " ;
 
$result = $db->query($query); 
print $db->resultToJson($result); 
?>