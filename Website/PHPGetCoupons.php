<?php

$databasehost = "acadgpl.ucc.nau.edu";
$databasename = "kd268";
$databaseusername ="kd268";
$databasepassword = "capstone";
$currentUser = "chihi";

// Connect to the database
$con = mysql_connect($databasehost,$databaseusername,$databasepassword) 
        or die(mysql_error());
mysql_select_db($databasename) or die(mysql_error());

// subscribed coupons 
$query = "SELECT * FROM kd268.merchantLocations WHERE " . 
        "(6367/100*SQRT(($pi2 - $currentLat)*($pi2 - $currentLat) + " .
        "($pi2 - latitude/$mil)*($pi2 - latitude/$mil) - " . 
        "2*($pi2 - $currentLat)*($pi2 - latitude/$mil) * " . 
        "COS(longitude/$mil - $currentLon))) < 10";

$sth = mysql_query($query); 

if (mysql_errno()) { 
    header("HTTP/1.1 500 Internal Server Error");
    echo $query.'<br>';
    echo mysql_error(); 
}
else
{
    $rows = array();
    while($r = mysql_fetch_assoc($sth)) {
        $rows[] = $r;
    }
    print json_encode($rows);
}
?>