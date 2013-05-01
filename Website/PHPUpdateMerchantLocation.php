<?php

/**
 * Update the merchant location in the database.  This script can be used 
 * for debugging purposes.  Queries "merchantLocations" table. 
 */
include_once './dbConfig/DBFunctions.php';
$db = new DBFunctions();     
$db->connect();

$json = file_get_contents('php://input');
$obj = json_decode($json);

$lat = $obj->{'latitude'};
$lon = $obj->{'longitude'};
$username = $obj->{'userName'};

// SQL query 
mysql_query("UPDATE `kd268`.`merchantLocations` SET latitude = '$lat' ," .
    "longitude = '$lon' WHERE merchantUserName = '$username'");
    
// close connection 
mysql_close($con);

$posts = array(1);
header('Content-type: application/json');
echo '[' . json_encode(array('posts' => $posts)) . ']';

?>
        