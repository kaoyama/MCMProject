<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="description" content="MoneyClip Mobile">
        <meta name="author" content="MCM">
        <title>MoneyClip Mobile Recent Activities</title>
        <link href="./assets/css/bootstrap.css" rel="stylesheet">
    </head>

    <style type="text/css">
        body {
            padding-top: 60px; /* 60px to make the container go all the way to the bottom of the topbar */
            height: 100%; 
            margin: 0; 
            padding-bottom: 60px;
        }
        html { 
            height: 100%;
        }
        #container {
            padding: 0px;
            padding-left: 20px;
            margin: 10px;
        }
    </style>

    <body>

        <div class="navbar navbar-fixed-top">
            <div class="navbar-inner">
                <div class="container">
                    <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </a>
                    <a class="brand" href="/index.html"><img src="./assets/img/MoneyclipMobile_Logo_36x197.png" height="36" width="197"></img></a>
                    <div class="nav-collapse">
                        <ul class="nav">
                            <li><a href="/index.html">Home</a></li>
                            <li><a href="/login.jsp">Login</a></li>
                            <li><a href="/about.html">About</a></li>
                            <li><a href="/contact.html">Contact</a></li>
                        </ul>
                    </div><!--/.nav-collapse -->
                </div>
            </div>
        </div>

        <div id="container">

            <h2>Your Recent Activities</h2>
            <?php
            $host = "localhost";
            $user = "kd268";                
            $password = "capstone";    
            $dbase = "test";    
            
            session_start();
            include 'functions.php';
            checkUserSatus($_SESSION['user'], $_SESSION['userType']);
            
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
                 //echo "username: $logInName <br/> pwd: $logInPwd";

                 // Check the database for this username/pwd combo
                 $sqlQuery = "SELECT merchant, purchaseTime, cost FROM customerTransactions 
                             WHERE customer = '" . $_SESSION['user'] . "' 
                             AND paid = TRUE OR cancelled = TRUE";

                 $result = mysql_query($sqlQuery,$cxn);

                 // no users with that login info
                 if(mysql_num_rows($result) < 1)
                 {
                     echo "You have no prior transactions.";
                 }
                 // otherwise let them into the site
                 else
                 {
                     echo "<table>
                     <tr>
                     <td width='300'>
                     Merchant
                     </td>
                     <td width='300'>
                     Amount
                     </td>
                     <td width='300'>
                     Time
                     </td>
                     </tr>";
                     while($allTransactions = mysql_fetch_row($result))
                     {
                         echo "<tr><td> $allTransactions[0] </td>
                             <td> $allTransactions[1] </td>
                                 <td> $allTransactions[2] </td></tr>";
                     }
                     echo "</table>";
                 }
             }
            ?>

        <hr>
        <footer>
            <p>MoneyClip Mobile 2012</p>
        </footer>
        </div>
    </body>
</html>