<?php

// Cloud messaging part *********************************
$regId = "APA91bFzZYTrcNsEhIM-7nj9gKRTkwQiKPLtoxwGCFNBbNtscgyA_YabUb_szUaKOmsdJlASlntW9N7nc8657Hnh_aK2V0ljKpDMpmPdEa2-r-C2-91_7bZ5tYCq7rjenWD3M99y0SgX";
$message = "New charge!";

include_once './cloudMessaging/GCM.php';

$gcm = new GCM();

$registrationIds = array($regId);
$message = array("price" => $message);

$result = $gcm->send_notification($registrationIds, $message);

//echo $result;

//********************************************************

$userName = 'chihi';

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

// SQL query 
mysql_query("UPDATE kd268.customers SET charged = TRUE " . 
        "WHERE userName = '" . $obj->{'userName'} . "'");
    
// close connection 
mysql_close($con);

$posts = array(1);
header('Content-type: application/json');
echo json_encode(array('posts' => $posts));

?>
        