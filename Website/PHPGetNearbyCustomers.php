<?php

include_once './dbConfig/DBFunctions.php';
$db = new DBFunctions();     
$db->connect();
    
$json = file_get_contents('php://input');
$obj = json_decode($json);

$merchantName = $obj->{'userName'};
$mil = 1000000.0;
$R = 6371;          // radius of Earth (km)
$maxDist = 0.01; //0.01;    // 10 m 

// retrieve location of merchant from database
$query = "SELECT latitude, longitude FROM kd268.merchantLocations WHERE ".
        "merchantUserName = '$merchantName'";
$res = $db->query($query); 
$merchantLat = mysql_result($res, 0, 'latitude')/$mil; 
$merchantLon = mysql_result($res, 0, 'longitude')/$mil;  

// retrieve merchants within 10 m
$query = "SELECT * ," . 
        "($R * ACOS(COS(RADIANS($merchantLat)) * COS(RADIANS(currentLat/$mil)) * " . 
        "COS(RADIANS(currentLon/$mil) - RADIANS($merchantLon)) + " . 
        "SIN(RADIANS($merchantLat)) * SIN(RADIANS(currentLat/$mil)))) " . 
        "AS distance FROM kd268.customers HAVING distance < $maxDist " . 
        "ORDER BY distance";

$result = $db->query($query); 
print $db->resultToJson($result); 
?>