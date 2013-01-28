<?php

$databasehost = "acadgpl.ucc.nau.edu";
$databasename = "kd268";
$databaseusername ="kd268";
$databasepassword = "capstone";
$currentUser = "chihi";
$currentLon = 0; 
$currentLat = 0; 
$pi2 = pi()/2.0; 
$mil = 1000000.0; 

// Connect to the database
$con = mysql_connect($databasehost,$databaseusername,$databasepassword) 
        or die(mysql_error());
mysql_select_db($databasename) or die(mysql_error());

// query 
$query = "SELECT currentLat, currentLon FROM kd268.customers WHERE ".
        "userName = '$currentUser'";
$res = mysql_query($query);
if(!$res) {
    die('No such username exists.');
} else {
    $currentLon = mysql_result($res, 0, 'currentLon')/$mil; 
    $currentLat = mysql_result($res, 0, 'currentLat')/$mil; 
}

// within 1 km radius...
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