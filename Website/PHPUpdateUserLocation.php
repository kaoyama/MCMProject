<?php

//$json=$_GET ['json'];
$userName = 'Chihiro';
$json = file_get_contents('php://input');
$obj = json_decode($json);
//echo $json;
//Save
$con = mysql_connect('acadgpl.ucc.nau.edu', 'kd268', 'capstone')
        or die('Cannot connect to the DB');
mysql_select_db('TEST', $con);
/* grab the posts from the db */
//$query = "SELECT post_title, guid FROM wp_posts WHERE 
//  post_author = $user_id AND post_status = 'publish'
// ORDER BY ID DESC LIMIT $number_of_posts";
mysql_query("UPDATE `kd268`.`location` SET latitude = '" . $obj->{'latitude'} . "' ," .
    "longitude = '" . $obj->{'longitude'} . "' WHERE userName = '" . $userName . "'");
    
mysql_close($con);
//
//$posts = array($json);
$posts = array(1);
header('Content-type: application/json');
echo json_encode(array('posts' => $posts));

?>
        