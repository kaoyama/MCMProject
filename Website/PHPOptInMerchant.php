<?php

include_once './dbConfig/DBFunctions.php';
$db = new DBFunctions();     
$db->connect();
    
$json = file_get_contents('php://input');
$obj = json_decode($json);
$userName = $obj->{'userName'};
$merchant = $obj->{'merchant'};
$optIn = $obj->{'optIn'};

if ($optIn == '1') {
    $query = "INSERT INTO kd268.subscribeForDeals (customer, merchant) VALUES " . 
                "('$userName', '$merchant')";
    mysql_query($query);
} else if ($optIn == '0') {
    $query = "DELETE FROM kd268.subscribeForDeals WHERE customer = '$userName' " . 
            " AND merchant = '$merchant'";
    mysql_query($query);
}
    
?>