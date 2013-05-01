<?php

/**
 * Retrieves a list of all merchants from the table "merchants."
 */
include_once './dbConfig/DBFunctions.php';
$db = new DBFunctions();
$db->connect();

$json = file_get_contents('php://input');
$obj = json_decode($json);

$userName = $obj->{'userName'};

$query = "SELECT userName, merchantName FROM kd268.merchants";

$result = $db->query($query);
print $db->resultToJson($result);
?>