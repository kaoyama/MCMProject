<?php
//This script should be called after the update location script
 
include_once './dbConfig/DBFunctions.php';
$db = new DBFunctions();     
$db->connect();
    
$json = file_get_contents('php://input');
$obj = json_decode($json);
$userName = $obj->{'userName'};

$mil = 1000000.0;
$R = 6371;          // radius of Earth (km)
$maxDist = 0.025; //0.01;    // 10 m 

$getUserInfo = "SELECT * FROM kd268.customers WHERE userName='" . $userName . "'";
$userInfo = mysql_query($getUserInfo);
$row = mysql_fetch_row($userInfo);

$bday = $row[7];//7 year-month-day
$studentStatus = $row[4];//4
$gender = $row[3];//3
$userLat = $row[5]/$mil;
$userLon = $row[6]/$mil;

//explode the date to get month, day and year
$birthDate = explode("-", $bday);

$month = date("n") - $birthDate[1];
$day = date("j") - $birthDate[2];
$age = date("Y") - $birthDate[0];

if ($month < 0) {
    $age = $age - 1;
}
if ($month == 0 && $day < 0) {
    $age = $age - 1;
}

// retrieve merchants within 10 m
$query = "SELECT dealIndex ," . 
        "($R * ACOS(COS(RADIANS($userLat)) * COS(RADIANS(targetLat/$mil)) * " . 
        "COS(RADIANS(targetLon/$mil) - RADIANS($userLon)) + " . 
        "SIN(RADIANS($userLat)) * SIN(RADIANS(targetLat/$mil)))) " . 
        "AS distance FROM kd268.deals WHERE targetGender='" . 
        $gender . "' OR 'B' AND student=" . $studentStatus . " AND minAge<=" . 
        $age . " AND maxAge>=" . $age . " AND accepted=TRUE AND enabled=TRUE" .
        " HAVING distance < $maxDist " . "ORDER BY distance";

$result = $db->query($query); 
    
//send deal index and username to new table
while($r = mysql_fetch_row($result)) {
    $dealIndex = $r[0];
    // query all ads/coupons from subscribed merchants 
    $query = "INSERT INTO kd268.customerDeals (dealIndex, userName) 
        VALUES('$dealIndex','$userName')";    
    $dealsRes = mysql_query($query); 
}

print $db->resultToJson($dealsRes); 
//does not return anything
?>
