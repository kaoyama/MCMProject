<?php

$json = file_get_contents('php://input');
$obj = json_decode($json);

$databasehost = "acadgpl.ucc.nau.edu";
$databasename = "kd268";
$databaseusername ="kd268";
$databasepassword = "capstone";
//$merchantName = "store";
$merchantName = $obj->{'merchant'};

// Connect to the database
$con = mysql_connect($databasehost,$databaseusername,$databasepassword) 
        or die(mysql_error());
mysql_select_db($databasename) or die(mysql_error());
        
// Set paid and cancelled columns in userTransactions table 

$query = "Select * from kd268.deals " .
        "WHERE merchant = '$merchantName' " ;
 
//echo $query;
$res = mysql_query($query); 

if (mysql_errno()) { 
    header("HTTP/1.1 500 Internal Server Error");
    echo $query.'<br>';
    echo mysql_error(); 
} else {
    $rows = array();
    while($r = mysql_fetch_assoc($res)) {
        $rows[] = $r;
    }
    print json_encode($rows);
}
?>