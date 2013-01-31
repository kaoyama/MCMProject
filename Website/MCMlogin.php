<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="description" content="MoneyClip Mobile">
        <meta name="author" content="MCM">
        <title>MoneyClip Mobile Log In</title>
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
        #content {
            height: 100%;
            padding: 0px;
            padding-left: 20px;
            margin: 0px;
        }
    </style>

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

        <div id="content">

            <h2>Log In</h2>

            <h3>Please enter your User Name and Password.</h3>
            <form action="MCMloginValidator.php" method="POST">
                <?php
                session_start();
                
                $fields = array("userName" => "Your User Name:",
                    "pwd" => "Your Password:");

                /* Loop that displays the form fields */
                foreach ($fields as $field => $label) {
                    /* echo the label */
                    echo "<div class='field'>\n
                                <label for='$field'>$label</label>\n";

                    /* echo the appropriate field */
                    if ($field === "userName") {
                        echo "<input type='text' name='$field' id='$field'
                                  size='65' maxlength='65' />\n";
                    } elseif ($field === "pwd") {
                        echo "<input type='password' name='$field' id='$field'
                                  size='65' maxlength='65' />\n";
                    }

                    /* echo the end of the field div */
                    echo "</div>\n";
                }

                /* Display the submit button */
                echo "<div id='submit'>\n
                            <input type='submit' value='Login'>\n
                          </div>";
                ?>
            </form>

            <a href='MCMcreateNewAccount.php'>Create New Account</a>
        </div>
        
        <hr>
        <footer>
            <p>MoneyClip Mobile 2012</p>
        </footer>
    </body>
</html>