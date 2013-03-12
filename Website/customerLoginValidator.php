
<!-- Validation goes before menues. -->
<?php
/**
 * Variable for database host.
 * 
 * @author Kimberly Oyama
 */
    //Database conncetion variables    
    $host = "acadgpl.ucc.nau.edu";
    $user = "kd268";                
    $password = "capstone";    
    $dbase = "kd268";                

    //Log in variables
    $fields = array("userName" => "Your User Name:",
                    "pwd" => "Your Password:");
    $logInName = 'kimi';
    $logInPwd = 'kimi';

    foreach ($_POST as $field => $value) {
        if (empty($value)) {
            //do other stuff
        }
        else {
            if ($field === "userName")
                $logInName = $_POST[$field];
            elseif ($field === "pwd")
                $logInPwd = $_POST[$field];
        }
    }
   // This code should connect.  If you get 'No connection possible', check your
   // userid and password.  Be sure you are using the password for your database,
   // which you specified when you requested the database.
   $cxn = mysql_connect($host, $user, $password) or die ("No connection possible");
   
   // This code should select your database.  I've given it two chances to report
   // the error.  If it fails, check the database name or call ITS for help.
   $dbr = mysql_select_db($dbase,$cxn)or die(mysql_error());

   if ($dbr == FALSE) 
       echo "<h6>DB Error: ".mysql_error($cxn)."</h6>";

   $sqlQuery = 'SHOW TABLES';
   $result = mysql_query($sqlQuery,$cxn);
   if($result == FALSE) {
       echo "<h4>Query Error: ".mysql_error($cxn)."</h4>
       <a href='index.php'>Try Again?</a>";
    }
    else {
        //echo "username: $logInName <br/> pwd: $logInPwd";
        
        // Check the database for this username/pwd combo
        $sqlQuery = "SELECT * FROM kd268.customers 
                    WHERE userName = '$logInName' 
                    AND pwd = '$logInPwd'";

        $result = mysql_query($sqlQuery,$cxn);

        // no users with that login info
        if(mysql_num_rows($result) < 1)
        {
            header("Location: customerLogin.php");
        }
        // otherwise let them into the site
        else
        {
            //Start session and keep track of the user
            $row = mysql_fetch_row($result);
            session_start();
            $_SESSION['user'] = $logInName;
            // keep track of user info 
            $_SESSION['name'] = $row[2];
            $_SESSION['gender'] = $row[3];
            $_SESSION['studentStatus'] = $row[4];
            $_SESSION['currentLat'] = $row[5];
            $_SESSION['currentLon'] = $row[6];
            $_SESSION['bday'] = $row[7];
            $_SESSION['email'] = $row[10];
            $_SESSION['userType'] = "customer";
            
            //Redirect to the homepage
            header("Location: customerHomePage.php");
        }
    }
?> 
       