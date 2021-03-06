<?php

/**
 * Updates the user's current location in the database table "customers." 
 */
include_once './dbConfig/DBFunctions.php';
$db = new DBFunctions();
$db->connect();

$json = file_get_contents('php://input');
$obj = json_decode($json);
$userName = $obj->{'userName'};
$latitude = $obj->{'latitude'};
$longitude = $obj->{'longitude'};

// SQL query 
$query = "UPDATE kd268.customers SET currentLat = '$latitude' ," .
        "currentLon = '$longitude', timestamp = now() WHERE userName = '$userName'";

$result = $db->query($query);
print $db->resultToJson($result);

// close connection 
mysql_close($con);

$posts = array(1);
header('Content-type: application/json');
echo '[' . json_encode(array('posts' => $posts)) . ']';
?>
        