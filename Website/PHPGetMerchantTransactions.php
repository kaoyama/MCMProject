<?php

include_once './dbConfig/DBFunctions.php';
$db = new DBFunctions();     
$db->connect();
    
$json = file_get_contents('php://input');
$obj = json_decode($json);
        
$query = "SELECT * FROM kd268.customerTransactions " . 
        " WHERE merchant = '" . $obj->{'merchant'} . "'"; 
$result = $db->query($query); 

print $db->resultToJson($result); 

?>