<?php
session_start();
    //Database conncetion variables    
    $host = "localhost";
    $user = "kd268";                
    $password = "capstone";    
    $dbase = "test";               

    //account variables
    $fields = array("userName" => "User Name: ",
                        "pwd" => "Password: ",
                        "pwdConfirmation" => "Confirm Password: ",
                        "firstName" => "First Name: ",
                        "lastName" => "Last Name: ",
                        "email" => "E-Mail: ",
                        "bDayYear" => "Year: ",
                        "bDayMonth" => "Month: ",
                        "bDay" => "Day: ",
                        "student" => "Student?",
                        "gender" => "Gender? ");
    $months = array("Jan" => "01",
                    "Feb" => "02",
                    "Mar" => "03",
                    "Apr" => "04",
                    "May" => "05",
                    "06" => "06",
                    "Jun" => "07",
                    "Jul" => "08",
                    "Sep" => "09",
                    "Oct" => "10",
                    "Nov" => "11",
                    "Dec" => "12");
    
    $studentStatus = "no";
    $logInName = 'boo';
    $logInPwd = 'boo';

   // Make connection
   $cxn = mysql_connect($host) or die ("No connection possible");

   // Test Connection
   $dbr = mysql_select_db($dbase,$cxn)or die(mysql_error());

   if ($dbr == FALSE) 
       echo "<h6>DB Error: ".mysql_error($cxn)."</h6>";

   //Test Query
   $sqlQuery = 'SHOW TABLES';
   $result = mysql_query($sqlQuery,$cxn);
   if($result == FALSE) {
       echo "<h4>Query Error: ".mysql_error($cxn)."</h4>";
    }
    else {
        $validInput = TRUE;
        foreach ($_POST as $field => $value) {
            if(empty($value))
            {
                $validInput = FALSE;
            }
            else {
                if ($field === "userName")
                    $logInName = $_POST[$field];
                elseif ($field === "pwd")
                    $logInPwd = $_POST[$field];
                elseif ($field === "pwdConfirmation")
                    $pwdConfirmation = $_POST[$field];
                elseif ($field === "firstName")
                    $fname = $_POST[$field];
                elseif ($field === "lastName")
                    $lname = $_POST[$field];
                elseif ($field === "email") {
                    if (!ereg("^[^0-9][a-zA-Z0-9_]+([.][a-zA-Z0-9_]+)*[@][a-zA-Z0-9_]+.(com|edu|org|gov)$",$value)){//"^.+@.+\.(com | edu | gov | org)$",$value))) {
                        $validInput = FALSE;
                        $invalidEmail = TRUE;
                    }
                    else
                        $email = $_POST[$field];
                }
                elseif ($field === "bDayYear") {
                    //check for number
                    $year = $_POST[$field];
                }
                elseif ($field === "bDayMonth") {
                    $month = $months[$_POST[$field]];
                }
                elseif ($field === "bDay") {
                    //check for number
                    $day = $_POST[$field];
                }
                elseif ($field === "subscribe") {
                    $studentStatus = "yes";
                }
                elseif ($field === "gender") {
                    $customerGender = $_POST[$field];
                }
            }
        }
        $birthday = $year."-".$month."-".$day;
        $name = $fname." ".$lname;
        
        if ($logInPwd != $pwdConfirmation) {
            $validInput = FALSE;
        }

        // No blanks and good email
        if($validInput) {

            //check for unique primary key
            $sqlQuery = "SELECT * FROM customers 
                    WHERE userName = '$logInName'";

            $result = mysql_query($sqlQuery,$cxn);

            // no users with that username
            if(mysql_num_rows($result) < 1)
            {
                //Insert new user
                $sqlQuery = "INSERT INTO customers 
                             (userName, pwd, customerName, birthday, gender, student) 
                             VALUES ('$logInName', '$logInPwd', '$name',
                                     '$birthday', '$customerGender',";
                if($studentStatus === "yes")
                    $sqlQuery = $sqlQuery." TRUE)";
                else
                    $sqlQuery = $sqlQuery." FALSE)";
                
                $result = mysql_query($sqlQuery,$cxn);
                
                header('Location: MCMhomePage.php');

            }
            //duplicate user name
            else {
                $validInput = FALSE;
                $badUserName = TRUE;
            }
        }
        //Blank field, bad email, or bad user name
        if(!$validInput) {
            echo "<!DOCTYPE html>
                    <html>
                    <head>
                        <meta http-equiv='Content-Type' content='text/html; charset=us-ascii' />
                        <meta name='Robots' content='index,follow'>
                        <meta name='author' content='Kimi Oyama'>
                        <title>Create New Account</title>
                        <link rel='stylesheet' type='text/css' href='style.css'>
                    </head>

                    <body>
                        <div id='container'>
                            <div id='top'></div>
                            <div id='content'>
                                <div id='header'>
                                    Hi. d(^_^)b <br> <br> <br> You are Awesome.
                                </div>
                                <div id='sideBar'>
                                    Welcome Friend
                                </div>
                                <div id='stuff'>
                                <h2>Create a new user account.</h2>
                                <h3>Please enter your information.</h3>
                                <form action='createNewAccountValidator.php' method='POST'>";
            if ($invalidEmail){
                echo "Email address must be in the form 
                            <i>username@hostname.(com|org|edu|gov)</i>.<br>\n";
            }
            if ($badUserName){
                echo "That user name is already taken. <br />";
            }
            echo "Please do not leave any fields blank.
                <br/> The grader will be able to see your Login information.";

            // Loop that displays the form fields 
            foreach ($fields as $field => $label) {
                // echo the label 
                echo "<div class='field'>\n
                        <label for='$field'>$label</label>\n";

                // echo the appropriate field 
                if ($field === "pwd" || $field === "pwdConfirmation") {
                    echo "<input type='password' name='$field' id='$field'
                          size='65' maxlength='65' />\n";
                }
                else {
                    echo "<input type='text' name='$field' id='$field'
                          size='65' maxlength='65' />\n";
                }
                // echo the end of the field div 
                echo "</div>\n";
            }

            // Display the submit button 
            echo "<div id='submit'>\n
                <input type='submit' value='Create Account'>\n
                </div>
                </div>
                </form>
            </div>
        </div>
        <div id='footer'>
        </div>
    </body>
</html>";
}
}
?> 
