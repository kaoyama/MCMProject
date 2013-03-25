<?php

$userName = 'chihi';

$databasehost = "acadgpl.ucc.nau.edu";
$databasename = "kd268";
$databaseusername ="kd268";
$databasepassword = "capstone";

$json = file_get_contents('php://input');


$obj = json_decode($json);

// connect to database 
$con = mysql_connect($databasehost,$databaseusername,$databasepassword) 
        or die(mysql_error());
mysql_select_db($databasename) or die(mysql_error());

// SQL query 
mysql_query("UPDATE `kd268`.`merchantLocations` SET latitude = '" . $obj->{'latitude'} . "' ," .
    "longitude = '" . $obj->{'longitude'} . "' WHERE merchantUserName = '" . $obj->{'userName'} . "'");
    
// close connection 
mysql_close($con);

$posts = array(1);
header('Content-type: application/json');
echo '[' . json_encode(array('posts' => $posts)) . ']';

?>
        