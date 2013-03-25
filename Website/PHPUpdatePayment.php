<?php

include_once './dbConfig/DBFunctions.php';
$db = new DBFunctions();     
$db->connect();
    
$json = file_get_contents('php://input');
$obj = json_decode($json);

$userName = $obj->{'userName'};
$paid = $obj->{'paid'};
$cancelled = $obj->{'cancelled'};
$transactionIndex = $obj->{'transactionIndex'};
        
// Set paid and cancelled columns in userTransactions table 

$query = "UPDATE kd268.customerTransactions " .
        "SET paid = '$paid' , cancelled = '$cancelled' " . 
        "WHERE customer = '$userName' AND transactionIndex = '$transactionIndex'";
 
$result = $db->query($query); 
print $db->resultToJson($result); 
?>