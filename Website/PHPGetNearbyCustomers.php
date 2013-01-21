<?php

$databasehost = "acadgpl.ucc.nau.edu";
$databasename = "kd268";
$databaseusername ="kd268";
$databasepassword = "capstone";
$lat = 35177800;
$lon = -111657101;

// Connect to the database
$con = mysql_connect($databasehost,$databaseusername,$databasepassword) 
        or die(mysql_error());
mysql_select_db($databasename) or die(mysql_error());

$query = "SELECT * FROM customers WHERE currentLat BETWEEN '" . 
        ($lat - 20) . "' AND '" . ($lat + 20) ."' AND currentLon BETWEEN '" . 
        ($lon - 20) . "' AND '" . ($lon + 20);
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