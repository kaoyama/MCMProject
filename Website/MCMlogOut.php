<?php
session_start();
$_SESSION['user'] = "guestOfKimi";
header("Location: index.php");
?>
