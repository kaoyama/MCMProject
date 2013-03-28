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

// add deals from each subscribed merchants to the list
while($r = mysql_fetch_assoc($res)) {
    $merchant = $r['merchant'];

    // query all ads/coupons from subscribed merchants 
    $query = "SELECT dealIndex FROM kd268.deals WHERE merchant = '$merchant' AND enabled = TRUE";
    $dealsRes = mysql_query($query); 
    while ($deal = mysql_fetch_assoc($dealsRes)) {
        // add the deals from each subscribed merchants to the database
        $query = "INSERT INTO kd268.customerDeals (userName, dealIndex) VALUES ".
            "('$currentUser', '$dealIndex')";
                
                $allDeals = mysql_query($query); 
    
        $allDeals = mysql_query($query); 
    }
    
    
    while($deal = mysql_fetch_assoc($allDeals)) {
        $rows[] = $deal;
    }
    
}


 

$rows = array(); 
while($r = mysql_fetch_assoc($res)) {
    $merchant = $r['merchant'];
    
    // of these merchants, pull ones that are nearby based on customer location
    
    // query all ads/coupons from subscribed merchants 
    $query = "SELECT * FROM kd268.deals WHERE merchant = '$merchant' AND enabled = TRUE";
    $allDeals = mysql_query($query); 
    while($deal = mysql_fetch_assoc($allDeals)) {
        $rows[] = $deal;
    }
}

print json_encode($rows); 

?>