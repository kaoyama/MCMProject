<?php

require_once("dbValidator.php");

$dbV = new dbValidator(); 

/*
 * For localhost: 
 */
//$dbV->initDB('localhost','root','nom','cs212','user','useractivity','message');

/*
 * For acadgpl NAU server 
 */
$host = "acadgpl.ucc.nau.edu";
$dbV->initDB($host, 'cs854', 'Kapibara14','cs854','user','useractivity','message');

?>
