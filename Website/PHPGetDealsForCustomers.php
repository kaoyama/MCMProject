<?php

/**
 * Retrieve all deals from subscribed merchants for a specific user.  
 */
include_once './dbConfig/DBFunctions.php';
$db = new DBFunctions();     
$db->connect();
    
$json = file_get_contents('php://input');
$obj = json_decode($json);
$userName = $obj->{'userName'};

// subscribed merchants
$query = "SELECT dealIndex FROM kd268.customerDeals WHERE " . 
        " userName = '$userName' AND redeemed='0' AND sent='0'";
$result = mysql_query($query);

$row = array();
// add deals from each subscribed merchants to the list
while($r = mysql_fetch_assoc($result)) {
    $dealIndex = $r['dealIndex'];

    // query all ads/coupons from subscribed merchants 
    $query = "SELECT * FROM kd268.deals 
        WHERE dealIndex = '$dealIndex' AND enabled = TRUE AND " .
            " accepted = TRUE ORDER BY merchant DESC";    
    $dealsRes = mysql_query($query); 
    while($d = mysql_fetch_assoc($dealsRes)) {
        $row[] = $d;
    }
}

print json_encode($row); 

?>