
<!-- Validation goes before menues. -->
<?php
/**
 * Variable for database host.
 * 
 * @author Kimberly Oyama
 */
    //Database conncetion variables    
    $host = "localhost";
    $user = "kd268";                
    $password = "capstone";    
    $dbase = "test";               

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
   $cxn = mysql_connect($host) or die ("No connection possible");

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
        echo "username: $logInName <br/> pwd: $logInPwd";
        
        // Check the database for this username/pwd combo
        $sqlQuery = "SELECT * FROM customers 
                    WHERE userName = '$logInName' 
                    AND pwd = '$logInPwd'";

        $result = mysql_query($sqlQuery,$cxn);

        // no users with that login info
        if(mysql_num_rows($result) < 1)
        {
            echo "<!DOCTYPE html>
                <html>
                <head>
                    <meta http-equiv='Content-Type' content='text/html; charset=us-ascii' />
                    <meta name='Robots' content='index,follow'>
                    <meta name='author' content='Kimi Oyama'>
                    <title>Log in</title>
                    <link rel='stylesheet' type='text/css' href='style.css'>
                </head>

                <body>
                    <div id='container'>
                        <div id='top'></div>
                        <div id='content'>
                            <div id='header'>
                                Hi. d(^_^)b <br> <br> <br> You are Awesome.
                            </div>
                             <p>Incorrect Login Info</p>
                             <div id='sideBar'>
                               Welcome Friend
                             </div>
                             <div id='stuff'>
                               <h2>Log In</h2>
                               <h3>Please enter your User Name and Password. 
                                    <br/> The grader will be able to see your Login information.</h3>
                               <form action='MCMloginValidator.php' method='POST'>";
                       
            /* Loop that displays the form fields */
            foreach ($fields as $field => $label) {
                /* echo the label */
                echo "<div class='field'>\n
                        <label for='$field'>$label</label>\n";

                /* echo the appropriate field */
                if ($field === "userName")
                {
                    echo "<input type='text' name='$field' id='$field'
                          size='65' maxlength='65' />\n";
                }
                elseif ($field === "pwd")
                {
                    echo "<input type='password' name='$field' id='$field'
                          size='65' maxlength='65' />\n";
                }
                /* echo the end of the field div */
                echo "</div>\n";
            }

            /* Display the submit button */
            echo "<div id='submit'>\n
                    <input type='submit' value='Login'>\n
                  </div>
                  </form>
                   </div>
                </div>
                <div id='footer'>
                </div>
            </body>
            </html>";
        }
        // otherwise let them into the site
        else
        {
            //Start session and keep track of the user
            $row = mysql_fetch_row($result);
            session_start();
            $_SESSION['user'] = $logInName;
            // keep track of user info for email, greeting, and 
            // account modification page
            $_SESSION['email'] = $row[4];
            $_SESSION['firstName'] = $row[2];
            $_SESSION['lastName'] = $row[3];
            
            //Redirect to the homepage
            header("Location: MCMhomePage.php");
        }
    }
?> 
       