<?php

/**
 * Enable or disable specific deals in the "deals" table.
 */
include_once './dbConfig/DBFunctions.php';
$db = new DBFunctions();
$db->connect();

$json = file_get_contents('php://input');
$obj = json_decode($json);

$enabled = $obj->{'enabled'};
$dealIndex = $obj->{'dealIndex'};

// SQL query 
mysql_query("UPDATE kd268.deals SET enabled = '$enabled'" .
        "WHERE dealIndex = '$dealIndex'");

// close connection 
mysql_close($con);

$posts = array(1);
header('Content-type: application/json');
echo json_encode(array('posts' => $posts));
?>
        