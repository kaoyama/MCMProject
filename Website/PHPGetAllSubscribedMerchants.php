<?php

include_once './dbConfig/DBFunctions.php';
$db = new DBFunctions();     
$db->connect();
    
$json = file_get_contents('php://input');
$obj = json_decode($json);
$userName = $obj->{'userName'};

$query = "SELECT merchant FROM kd268.subscribeForDeals WHERE " . 
        " customer = '$userName'";

$result = $db->query($query); 
print $db->resultToJson($result); 
?>