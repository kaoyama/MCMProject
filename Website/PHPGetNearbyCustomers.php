<?php

$databasehost = "acadgpl.ucc.nau.edu";
$databasename = "kd268";
$databaseusername ="kd268";
$databasepassword = "capstone";
$lat = 35170000;
$lon = -111600000;

// Connect to the database
$con = mysql_connect($databasehost,$databaseusername,$databasepassword) 
        or die(mysql_error());
mysql_select_db($databasename) or die(mysql_error());

$query = "SELECT `userName` FROM `kd268`.`customers` WHERE `currentLat` BETWEEN '" . 
        ($lat - 10000) . "' AND '" . ($lat + 10000) ."' AND `currentLon` BETWEEN '" . 
        ($lon - 100000) . "' AND '" . ($lon + 100000) . "'";
//echo $query;
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