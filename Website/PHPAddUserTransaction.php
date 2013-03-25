<?php

include_once './dbConfig/DBFunctions.php';
$db = new DBFunctions();     
$db->connect();
    
$json = file_get_contents('php://input');
$obj = json_decode($json);
$merchant = $obj->{'merchant'};
$customer = $obj->{'customer'};
$cost = $obj->{'cost'};
        
// Get boolean value from customers table 
$query = "INSERT INTO kd268.customerTransactions (merchant, customer, cost, paid)" . 
        " VALUES ('$merchant', '$customer' , '$cost' , FALSE)";
 
$result = $db->query($query); 
print $db->resultToJson($result); 
?>