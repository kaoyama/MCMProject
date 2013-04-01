<?php

include_once './dbConfig/DBFunctions.php';
$db = new DBFunctions();     
$db->connect();
    
$json = file_get_contents('php://input');
$obj = json_decode($json);
$userName = $obj->{'userName'};

// subscribed merchants
$query = "SELECT dealIndex FROM kd268.customerDeals WHERE userName = '$userName' AND redeemed='0'";
$result = $db->query($query); 
$row = array();

// add deals from each subscribed merchants to the list
while($r = mysql_fetch_assoc($result)) {
    $dealIndex = $r['dealIndex'];

    // query all ads/coupons from subscribed merchants 
    $query = "SELECT title, content, merchant FROM kd268.deals 
        WHERE dealIndex = '$dealIndex' AND enabled = TRUE AND approved = TRUE";    
    $dealsRes = mysql_query($query); 
    while($d = mysql_fetch_assoc($dealsRes)) {
        $row[] = $d;
    }
}

print $db->resultToJson($row); 

?>