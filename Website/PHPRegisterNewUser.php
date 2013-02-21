<?php


$databasehost = "acadgpl.ucc.nau.edu";
$databasename = "kd268";
$databaseusername ="kd268";
$databasepassword = "capstone";
$currentUser = "chihi";

// Connect to the database
$con = mysql_connect($databasehost,$databaseusername,$databasepassword) 
        or die(mysql_error());
mysql_select_db($databasename) or die(mysql_error());
    
// response json
$json = file_get_contents('php://input'); 
$obj = json_decode($json); 

$name = $obj->{'name'};
$email = $obj->{'email'};
$regId = $obj->{'regId'};

$query = "INSERT INTO gcm_users(name, email, gcm_regid, created_at) " . 
        "VALUES('$name', '$email', '$regId', NOW())";
        
$result = mysql_query($query); 

if ($result) {
    echo "[". json_encode(array('result' => 0)) . "]";
} else {
    echo "[". json_encode(array('result' => 1)) . "]";
}
?>