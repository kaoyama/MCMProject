<?php

/**
 * Validates login for the user based on the username/password combination.  
 * Queries "customers" or "merchants" table.
 */
include_once './dbConfig/DBFunctions.php';
$db = new DBFunctions();
$db->connect();

$json = file_get_contents('php://input');
$obj = json_decode($json);
$userName = $obj->{'userName'};
$pwd = $obj->{'pwd'};
$android = $obj->{'android'};

// Get boolean value from customers table 
if ($obj->{'customer'} == '1') {
    //change query depending on phone type
    $query = "SELECT * FROM kd268.customers WHERE userName = '$userName' " .
            " AND pwd = '$pwd'";
} else {
    $query = "SELECT * FROM kd268.merchants WHERE userName = '$userName' " .
            " AND pwd = '$pwd'";
}

//echo $query;
$res = mysql_query($query);

if (mysql_num_rows($res) < 1) {
    // error validating user 
    print "[" . json_encode(array('result' => 0)) . "]";
} else {
    //update the user's device in the database
    $query = "UPDATE kd268.customers SET android = '$android' WHERE ".
            " userName = '$userName'";
    $res = mysql_query($query);

    print "[" . json_encode(array('result' => 1)) . "]";
}

// close connection 
mysql_close($con);
?>
        
