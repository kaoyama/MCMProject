<?php

include_once './dbConfig/DBFunctions.php';
$db = new DBFunctions();     
$db->connect();
    
$json = file_get_contents('php://input');
$obj = json_decode($json);
$currentUser = $obj->{'userName'};

// subscribed merchants
$query = "SELECT * FROM kd268.subscribeForDeals WHERE customer = '$currentUser'";
$res = mysql_query($query); 

$rows = array(); 
while($r = mysql_fetch_assoc($res)) {
    $merchant = $r['merchant'];
    
    // query all ads/coupons from subscribed merchants 
    $query = "SELECT * FROM kd268.deals WHERE merchant = '$merchant' AND enabled = TRUE";
    $allDeals = mysql_query($query); 
    while($deal = mysql_fetch_assoc($allDeals)) {
        $rows[] = $deal;
    }
}

print json_encode($rows); 

?>