<?php

/**
 * Retrieve nearby merchants within a distance of 50 m. This query uses 
 * customer location from "customers" table and "merchantLocations" table.
 */
include_once './dbConfig/DBFunctions.php';
$db = new DBFunctions();
$db->connect();

$json = file_get_contents('php://input');
$obj = json_decode($json);

$currentUser = $obj->{'userName'};
$mil = 1000000.0;
$R = 6371;      // radius of Earth (km)
$maxDist = 0.5; // 50 m, 0.5 km 

// retrieve current location of user from database
$query = "SELECT currentLat, currentLon FROM kd268.customers WHERE " .
        "userName = '$currentUser'";
$res = $db->query($query);
$currentLon = mysql_result($res, 0, 'currentLon') / $mil;
$currentLat = mysql_result($res, 0, 'currentLat') / $mil;

// retrieve merchants within 1 km
$query = "SELECT * ," .
        "($R * ACOS(COS(RADIANS($currentLat)) * COS(RADIANS(latitude/$mil)) * " .
        "COS(RADIANS(longitude/$mil) - RADIANS($currentLon)) + " .
        "SIN(RADIANS($currentLat)) * SIN(RADIANS(latitude/$mil)))) " .
        "AS distance FROM kd268.merchantLocations HAVING distance < $maxDist " .
        "ORDER BY distance";

$result = $db->query($query);
print $db->resultToJson($result);
?>