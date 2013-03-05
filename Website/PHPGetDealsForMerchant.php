<?php

$databasehost = "acadgpl.ucc.nau.edu";
$databasename = "kd268";
$databaseusername ="kd268";
$databasepassword = "capstone";

// Connect to the database
$con = mysql_connect($databasehost,$databaseusername,$databasepassword) 
        or die(mysql_error());
mysql_select_db($databasename) or die(mysql_error());

$json = file_get_contents('php://input');
$obj = json_decode($json);
        
// Set paid and cancelled columns in userTransactions table 

$query = "Select title from kd268.deals " .
        "WHERE merchant = '" . $obj->{'merchant'} . "' " ;
 
//echo $query;
$res = mysql_query($query); 

if (mysql_errno()) { 
    header("HTTP/1.1 500 Internal Server Error");
    echo $query.'<br>';
    echo mysql_error(); 
}
?>