<?php

/**
 * Register a new user in the table "gcm_users." 
 */
include_once './dbConfig/DBFunctions.php';
$db = new DBFunctions();     
$db->connect();
    
$json = file_get_contents('php://input');
$obj = json_decode($json);
    
// response json
$json = file_get_contents('php://input'); 
$obj = json_decode($json); 

$name = $obj->{'name'};
$email = $obj->{'email'};
$regId = $obj->{'regId'};

$query = "INSERT INTO kd268.gcm_users(name, email, gcm_regid, created_at) " . 
        "VALUES('$name', '$email', '$regId', NOW())";
        
$result = mysql_query($query); 

if ($result) {
    echo "[". json_encode(array('result' => 1)) . "]";
} else {
    echo "[". json_encode(array('result' => 0)) . "]";
}
?>