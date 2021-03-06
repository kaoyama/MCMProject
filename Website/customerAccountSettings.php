<!DOCTYPE html>
<html>
    <script type="text/javascript" src="settingsOptions.js"></script>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="description" content="MoneyClip Mobile">
        <meta name="author" content="MCM">
        <title>MoneyClip Mobile Account Settings</title>
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
        #leftSidebar
        {
            background: #FFFFFF;
            width: 20%; 
            float: left; 
            margin: 0px; 
            padding: 10px;
        }
        #adSection
        {
            background: #FFFFFF;
            width: 37%; 
            float: left; 
            margin: 0px; 
            padding: 10px;
        }
        #couponSection
        {
            background: #FFFFFF;
            width: 38%; 
            float: left; 
            margin: 0px; 
            padding: 10px;
        }
        #main {
            overflow:auto;
            padding-bottom: 100px;
        }  /* must be same height as the footer */
        #footer {
            position: relative;
            margin-top: -100px; /* negative value of footer height */
            height: 100px;
            clear:both;
        } 
        #merchantList
        {
            background: forestgreen;
            border-width: 3px;
            border-style: solid;
            border-color: #000000;
            width: 30%; 
            float: left; 
            margin: 0px; 
            padding: 10px;
        }
    </style>
<?php
session_start();

if($_SESSION['user'] === "guestOfKimi" ||
        !$_SESSION['user'])
    header("Location: index.php");
            
$user = $_SESSION['user'];

$host = "acadgpl.ucc.nau.edu";
$dbuser = "kd268";                
$password = "capstone";    
$dbase = "kd268"; 

$cxn = mysql_connect($host, $dbuser, $password) or die ("No connection possible");
    $dbr = mysql_select_db($dbase,$cxn)or die(mysql_error());

   if ($dbr == FALSE) 
       echo "<h6>DB Error: ".mysql_error($cxn)."</h6>";
   
//get all merchants that have ads
   $getAllMerchants = "SELECT merchant FROM deals";
   $allMerchants = mysql_query($getAllMerchants, $cxn);
   $allMerchantList = '';
   //if there are none, no one has ads
   if (mysql_num_rows($allMerchants) < 1){
       $noMerchant = "No merchants have deals.";
   }
   else {
       //loop through to get array of  merchants
       while($allDealRow = mysql_fetch_row($allMerchants))
       {
           $merchantName = mysql_query("SELECT merchantName FROM merchants WHERE userName = '$allDealRow[0]';", $cxn);
           $currentMerchantName = mysql_fetch_row($merchantName);
           $allMerchantList = $allMerchantList.$currentMerchantName[0]."#";
       }
   }
  
   //get merchants that this user is subscribed to
   $getMerchants = "SELECT * FROM subscribedForDeals WHERE userName = '$user'";
   $dealMerchants = mysql_query($getMerchants, $cxn);
   $dealMerchantList = "";
   $nodealMerchant = "";
   
   //if there are none, no one has ads
   if (mysql_num_rows($dealMerchants) < 1){
       $nodealMerchant = "You are not subscribed to get any ads from any merchants.";
   }
   else {
       //loop through to get array of merchants
       $dealRow = mysql_fetch_row($dealMerchants);
       $i=0;
       for ($i=1;$i < sizeof($dealRow);$i++)
       {
           if ($dealRow[$i] != "" && $dealRow[$i] != null)
           {
               $getDealMerchantName = mysql_query("SELECT merchantName FROM merchants WHERE userName = '$dealRow[$i]';", $cxn);
               $currentDealMerchantName = mysql_fetch_row($getDealMerchantName);
               $dealMerchantList = $dealMerchantList.$currentDealMerchantName[0]."#";
           }
       }
   }

if (isset($_GET['run'])){
    //use post stuff
    //build sql query
    echo "Got here?";
    if ($_GET['run'] === "saveDealData")
    $saveQuery = "INSERT INTO subscribedForDeals";
    foreach ($_POST as $field => $value) {
        echo "$field";
    }
}
 ?>  
    <body>

        <div class="navbar navbar-fixed-top">
            <div class="navbar-inner">
                <div class="container">
                    <a class="btn btn-navbar" data-toggle="collapse" 
                       data-target=".nav-collapse">
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </a>
                    <a class="brand" href="/index.html">
                        <img src="./assets/img/MoneyclipMobile_Logo_36x197.png" 
                             height="36" width="197"></img></a>
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

            <h2>Edit Account Settings</h2>
            
            <div id="leftSidebar">
                <h3>Account Options</h3>
            <label for="locatlization">Localization?</label>
            <input type = 'checkbox' name = 'localization' id='localization'
                   onclick="return buttonState()" checked="checked"/>
            
            <label for="ads">Ads?</label>
            <input type='radio' name='ads' id='noAds' value='none' 
                   onclick="return noAdsDisplay()"/>No Ads <br />
            <input type='radio' name='ads' id='someAds' value='some' onclick=
                <?php 
                echo "'return selectAds(\"$adMerchantList\")'/>Select Ads 
                    <br />"
                ?>
            <input type='radio' name='ads' id='allAds' value='all' 
            checked="checked" onclick=  
                <?php
            echo "'return allAds(\"$allAdsMerchantList\")'/>All 
                Ads <br />"
            ?>
            <label for="coupons">Coupons?</label>
            <input type='radio' name='coupons' id='noCoupons' value='none' 
                   onclick="return noCouponsDisplay()"/>No Coupons <br />
            <input type='radio' name='coupons' id='someCoupons' value='some' 
                   onclick=
            <?php
            echo "'return selectCoupons(\"$couponMerchantList\")'/>Select 
                Coupons <br />"
            ?>
            <input type='radio' name='coupons' id='allCoupons' value='all' 
            checked="checked" onclick=
                <?php
            echo "'return allCoupons(\"$allCouponMerchantList\")'/>All 
                Coupons <br />"
            ?>
            </div>
            <div id="adSection"><h3>Advertisement Options</h3></div>
            <div id="couponSection"><h3>Coupon Options</h3></div>
            <hr>
            
            <br/>
            
            <input type='button' value='Save' onclick='return saveFunction()'/>
                    
            <div id="footer">
                <footer><p>MoneyClip Mobile 2012</p></footer>
            </div>
        </div>
    </body>
</html>

<?php
//the save button will store information in session variables or array.
//then it will trigger a refresh
//when the page is refreshed or left a php function will be called to store 
//the info from the variables in the database.
echo "
    <script>
    function saveFunction()
    {
        <html>
    <h3> stuff</h3></html>
    }
</script>";
?>
