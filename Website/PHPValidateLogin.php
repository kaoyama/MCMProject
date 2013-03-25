<?php

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

// Get boolean value from customers table 
if ($obj->{'customer'} == '1') {
    $query = "SELECT * FROM kd268.customers WHERE userName = '" . $obj->{'userName'} .
            "' AND pwd = '". $obj->{'pwd'} . "'";
            //change query depending on phone type
} else {
    $query = "SELECT * FROM kd268.merchants WHERE userName = '" . $obj->{'userName'} .
            "' AND pwd = '". $obj->{'pwd'} . "'";
}

//echo $query;
$res = mysql_query($query); 

if (mysql_num_rows($res) < 1) {
    // error validating user 
    print "[". json_encode(array('result' => 0)) . "]";
} else {
    //update the user's device in the database
    $query = "UPDATE kd268.customers SET android =" . $obj->{'android'} . "WHERE userName = '" . $obj->{'userName'} . "'";
    $res = mysql_query($query); 
            
    print "[" . json_encode(array('result' => 1)) . "]";
}

// close connection 
mysql_close($con);

?>
        
