<?php

/**
 * For the specific user, pull information about all the user's subscribed 
 * merchants.  Then, for each subscribed merchant, put all of the merchant's 
 * deals into the "customerDeals" table.  
 */
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
while ($r = mysql_fetch_assoc($res)) {
    $merchant = $r['merchant'];

    // query all ads/coupons from subscribed merchants 
    $query = "SELECT dealIndex FROM kd268.deals WHERE merchant = '$merchant' " .
            " AND enabled = TRUE";
    $dealsRes = mysql_query($query);

    // for all deals that is from the specific merchant, add to table
    while ($deal = mysql_fetch_assoc($dealsRes)) {
        $dealIndex = $deal['dealIndex'];
        // add the deals from each subscribed merchants to the database
        $query = "INSERT INTO kd268.customerDeals (userName, dealIndex) VALUES " .
                "('$currentUser', '$dealIndex')";

        $allDeals = mysql_query($query);
    }
}
// does not return anything
?>