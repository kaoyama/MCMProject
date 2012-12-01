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
            width: 20%; 
            float: left; 
            margin: 0px; 
            padding: 10px;
        }
        #couponSection
        {
            background: #FFFFFF;
            width: 20%; 
            float: left; 
            margin: 0px; 
            padding: 10px;
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

            <h2>Edit Account Settings</h2>
            
            <label for="locatlization">Localization?</label>
            <input type = 'checkbox' name = 'localization'/>
            
            <label for="ads">Ads?</label>
            <input type='radio' name='ads' id='noAds' value='none' onclick="return noAdsDisplay()"/>No Ads <br />
            <input type='radio' name='ads' id='someAds' value='some' onclick="return selectAds()"/>Select Ads <br />
            <input type='radio' name='ads' id='allAds' value='all' checked="checked" onclick="return allAds()" />All Ads
            
            <label for="coupons">Coupons?</label>
            <input type='radio' name='coupons' id='noCoupons' value='none' onclick="return noCouponsDisplay()"/>No Coupons <br />
            <input type='radio' name='coupons' id='someCoupons' value='some' onclick="return selectCoupons()"/>Select Coupons <br />
            <input type='radio' name='coupons' id='allCoupons' value='all' checked="checked" onclick="return allCoupons()" />All Coupons

            <div id="stuff"></div>
            <hr>
            <footer>
                <p>MoneyClip Mobile 2012</p>
            </footer>
        </div>
    </body>
</html>

