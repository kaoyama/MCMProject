<?php

include_once './dbConfig/DBFunctions.php';
$db = new DBFunctions();     
$db->connect();
    
$json = file_get_contents('php://input');
$obj = json_decode($json);
$currentUser = $obj->{'userName'};
        
// Get boolean value from customers table 
$query = "SELECT charged, timestamp FROM kd268.customers WHERE " . 
        "userName = '$currentUser'";

$result = $db->query($query); 
print $db->resultToJson($result); 
?>