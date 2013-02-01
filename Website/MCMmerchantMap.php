<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <title>MoneyClip Mobile</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="description" content="MoneyClip Mobile">
        <meta name="author" content="MCM">

        <!-- Le styles -->
        <link href="./assets/css/bootstrap.css" rel="stylesheet">
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
            footer {
                margin-left: 33%; 
            }
            #content {
                height: 100%;
                padding: 0px;
                margin: 0px;
            }
            #map_canvas {
                background: #46a546;
             /* border: 1px solid; */ 
                padding-top: 60px;
                float: right;
                width: 70%;  
                height: 90%;
                margin: 0px;
            }
            #leftSidebar
            {
                background: #FFFFFF;
                width: 27%; 
                float: left; 
                margin: 0px; 
                padding: 10px;
            }
            #merchantLink
            {
                background: #ffffff;
                padding: 5px;
                margin: 0px;
            }
            #merchantLink:hover
            {
                background: #dff0d8;
                padding: 5px;
                margin: 0px;
            }
        </style>

        <link href="./assets/css/bootstrap-responsive.css" rel="stylesheet">

        <!-- Google Maps API -->
        <script type="text/javascript"
                src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCPUwTRKp-Nc6Fe3CKttxQvKXg-yHBQrXI&sensor=true">
        </script>

        <!-- Google Maps Places API --> 
        <script type="text/javascript" 
                src="http://maps.googleapis.com/maps/api/js?libraries=places&sensor=true">
        </script>

        <!-- Initialize Google Maps --> 
        <!--<script type="text/javascript">
            function initialize() {
                var mapOptions = {
                    center: new google.maps.LatLng(35.199728,-111.648606),
                    zoom: 15,
                    mapTypeId: google.maps.MapTypeId.ROADMAP
                };
                var map = new google.maps.Map(document.getElementById("map_canvas"),
                mapOptions);
            }
        </script>-->

        <!-- Places API --> 
        <script>
                        
            // List of nearby merchants
            var map, placesList, service; 

            function initialize() {
                var flagstaff = new google.maps.LatLng(35.199728,-111.648606);

                map = new google.maps.Map(document.getElementById('map_canvas'), {
                    mapTypeId: google.maps.MapTypeId.ROADMAP,
                    center: flagstaff,
                    zoom: 17
                });

                var request = {
                    location: flagstaff,
                    radius: 500,
                    types: ['store']
                };

                placesList = document.getElementById('places');

                service = new google.maps.places.PlacesService(map);
                service.nearbySearch(request, callback);
            }

            function callback(results, status, pagination) {
                if (status != google.maps.places.PlacesServiceStatus.OK) {
                    return;
                } else {
                    createMarkers(results);

                    if (pagination.hasNextPage) {
                        var moreButton = document.getElementById('more');

                        moreButton.disabled = false;

                        google.maps.event.addDomListenerOnce(moreButton, 'click',
                        function() {
                            moreButton.disabled = true;
                            pagination.nextPage();
                        });
                    }
                }
            }

            function createMarkers(places) {
            
                var bounds = new google.maps.LatLngBounds();

                for (var i = 0, place; place = places[i]; i++) {                  
                   
                    // Make the list links to the merchants
                    var infowindow = new google.maps.InfoWindow(); 
                    var request = {
                        reference: place.reference
                    };
                    
                    service.getDetails(request, function(place, status) {
                        
                        if (status == google.maps.places.PlacesServiceStatus.OK) {
                            
                            var image = new google.maps.MarkerImage(
                            place.icon, new google.maps.Size(71, 71),
                            new google.maps.Point(0, 0), new google.maps.Point(17, 34),
                            new google.maps.Size(25, 25));

                            var marker = new google.maps.Marker({
                                map: map,
                                icon: image,
                                title: place.name,
                                position: place.geometry.location
                            });
                            google.maps.event.addListener(marker, 'click', function() {
                                infowindow.setContent(place.name);
                                infowindow.open(map, this);
                            });
                            
                            // parse address
                            var addr = place.formatted_address.split(",");
                            
                            placesList.innerHTML += '<div id="merchantLink"><a href=' + 
                                place.url + '><b>' + place.name + '</b></a><br>' + 
                                addr[0] + '<br>' + 
                                place.formatted_phone_number + '</div>'; 
                        }
                        
                    });

                    bounds.extend(place.geometry.location);
                }
                map.fitBounds(bounds);
            }           
            
            google.maps.event.addDomListener(window, 'load', initialize);
        </script>

        <!-- Le fav and touch icons -->
        <link rel="shortcut icon" href="/assets/img/MoneyclipMobile_Favicon_16x16.ico">
        <link rel="apple-touch-icon-precomposed" sizes="114x114" href="/assets/ico/apple-touch-icon-114-precomposed.png">
        <link rel="apple-touch-icon-precomposed" sizes="72x72" href="/assets/ico/apple-touch-icon-72-precomposed.png">
        <link rel="apple-touch-icon-precomposed" href="/assets/ico/apple-touch-icon-57-precomposed.png">
    </head>
    <body> <!--onload="initialize()">-->

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
<?php
session_start();
if($_SESSION['user'] === "guestOfKimi" ||
        !$_SESSION['user'])
    header("Location: index.php");
?>
        <div id="content">
            <div id="leftSidebar">
                <!-- List of merchants --> 
                <h3>List of Nearby Merchants</h3>
                <div id="places"></div>
                <button id="more">More results</button>
            </div>

            <!-- Google map -->
            <div id="map_canvas"></div>
            <!-- End Google map --> 

        </div>

        <hr>
        <footer>
            <p>MoneyClip Mobile 2012</p>
        </footer>

        <!-- Le javascript
        ================================================== -->
        <!-- Placed at the end of the document so the pages load faster -->
        <script src="/assets/js/jquery-min.js"></script>
        <script src="/assets/js/bootstrap-transition-min.js"></script>
        <script src="/assets/js/bootstrap-alert-min.js"></script>
        <script src="/assets/js/bootstrap-modal-min.js"></script>
        <script src="/assets/js/bootstrap-dropdown-min.js"></script>
        <script src="/assets/js/bootstrap-scrollspy-min.js"></script>
        <script src="/assets/js/bootstrap-tab-min.js"></script>
        <script src="/assets/js/bootstrap-tooltip-min.js"></script>
        <script src="/assets/js/bootstrap-popover-min.js"></script>
        <script src="/assets/js/bootstrap-button-min.js"></script>
        <script src="/assets/js/bootstrap-collapse-min.js"></script>
        <script src="/assets/js/bootstrap-carousel-min.js"></script>
        <script src="/assets/js/bootstrap-typeahead-min.js"></script>
    </body>
</html>

