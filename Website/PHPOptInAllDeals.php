<?php

/** 
 * User subscribes to all merchants.  Queries 'merchant" table and 
 * "subscribeForDeals" table. 
 */
include_once './dbConfig/DBFunctions.php';
$db = new DBFunctions();     
$db->connect();
    
$json = file_get_contents('php://input');
$obj = json_decode($json);
$userName = $obj->{'userName'};
        
// get all merchants
$query = "SELECT * FROM kd268.merchants ";
$res = mysql_query($query); 

// subscribe user to all merchants 
while($r = mysql_fetch_assoc($res)) {
    $merchant = $r['userName'];
    
    $query = "INSERT INTO kd268.subscribeForDeals (customer, merchant) VALUES " . 
            "('$userName', '$merchant')";
    mysql_query($query);
}
?>