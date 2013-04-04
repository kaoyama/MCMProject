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
            $host = "acadgpl.ucc.nau.edu";
            $user = "kd268";                
            $password = "capstone";    
            $dbase = "kd268";   
            
            session_start();
            include 'functions.php';
            checkUserSatus($_SESSION['user'], $_SESSION['userType']);
            
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
                 $getRecentActivities = "SELECT merchant, purchaseTime, cost, 
                             paid, cancelled FROM kd268.customerTransactions 
                             WHERE customer='" . $_SESSION['user'] . "'";
                 
                 $recentActivities = mysql_query($getRecentActivities, $cxn);
                 
                 if(mysql_num_rows($recentActivities) < 1)
                 {
                     echo "You have no prior transactions.";
                 }
                 // otherwise let them into the site
                 else
                 {
                     echo "<h3>Pending Transactions</h3>
                         <table>
                         <tr>
                         <td width='300'>
                         <b>Merchant</b>
                         </td>
                         <td width='300'>
                         <b>Amount</b>
                         </td>
                         <td width='300'>
                         <b>Time</b>
                         </td>
                         </tr>";
                     $others = array();
                     $i = 0;
                     while($transaction = mysql_fetch_row($recentActivities))
                     {
                         if(!$transaction[4] && !$transaction[3]) {
                            echo "<tr><td> $transaction[0] </td>
                                <td> $transaction[2] </td>
                                    <td> $transaction[1] </td></tr>";
                         }
                         //echo mysql_num_rows($recentActivities);
                         else {
                             $others[$i] = array();
                             $others[$i][0] = $transaction[0];
                             $others[$i][2] = $transaction[2];
                             $others[$i][1] = $transaction[1];
                             $i++;
                         }
                     }
                     
                     echo "</table>";

                    echo "<br/> 
                        <h3>Completed Transactions</h3>
                        <table>
                        <tr>
                        <td width='300'>
                        <b>Merchant</b>
                        </td>
                        <td width='300'>
                        <b>Amount</b>
                        </td>
                        <td width='300'>
                        <b>Time</b>
                        </td>
                        </tr>";
                    for($i = 0; $i < mysql_num_rows($recentActivities); $i++) {
                        echo "<tr>
                            <td> " . $others[$i][0] . "</td>
                                <td>" .  $others[$i][2] . "</td>
                                    <td>" . $others[$i][1] . "</td></tr>";
                    }
                    echo "</table>";
                 }
             }
            ?>
        <footer>
            <p>MoneyClip Mobile 2012</p>
        </footer>
        </div>
    </body>
</html>