<?php

$json = file_get_contents('php://input');
$obj = json_decode($json);

$databasehost = "acadgpl.ucc.nau.edu";
$databasename = "kd268";
$databaseusername ="kd268";
$databasepassword = "capstone";
//$currentUser = 'chihi';
$currentUser = $obj->{'userName'};

// Connect to the database
$con = mysql_connect($databasehost,$databaseusername,$databasepassword) 
        or die(mysql_error());
mysql_select_db($databasename) or die(mysql_error());

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

/*
if (mysql_errno()) { 
    header("HTTP/1.1 500 Internal Server Error");
    echo $query.'<br>';
    echo mysql_error(); 
}
else
{
    print json_encode($rows);
}
 * 
 */
?>