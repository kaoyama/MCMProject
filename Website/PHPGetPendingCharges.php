<?php

include_once './dbConfig/DBFunctions.php';
$db = new DBFunctions();     
$db->connect();
    
$json = file_get_contents('php://input');
$obj = json_decode($json);
$userName = $obj->{'userName'};
        
// Get boolean value from customers table 

$query = "SELECT * FROM kd268.customerTransactions " . 
        " WHERE merchant = '$merchantName' and paid = 0";
 
$result = $db->query($query); 
print $db->resultToJson($result); 
?>