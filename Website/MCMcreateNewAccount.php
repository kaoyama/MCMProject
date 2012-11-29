<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="description" content="MoneyClip Mobile">
        <meta name="author" content="MCM">
        <title>MoneyClip Mobile Create New Account</title>
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
            padding: 0px;
            padding-left: 20px;
            margin: 0px;
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

        <div id="content">

            <h2>Create New User Account</h2>

                <form action="MCMcreateNewAccountValidator.php" method="POST">
                    <?php
                    $fields = array("userName" => "User Name: ",
                        "pwd" => "Password: ",
                        "pwdConfirmation" => "Confirm Password: ",
                        "firstName" => "First Name: ",
                        "lastName" => "Last Name: ",
                        "email" => "E-Mail: ",
                        "bDay" => "Birthday -> Day: ",
                        "bDayMonth" => "Month: ",
                        "bDayYear" => "Year: ",
                        "student" => "Student?",
                        "gender" => "Gender? ");

                    $months = array("1" => "Jan",
                        "2" => "Feb",
                        "3" => "Mar",
                        "4" => "Apr",
                        "5" => "May",
                        "6" => "Jun",
                        "7" => "Jul",
                        "8" => "Aug",
                        "9" => "Sep",
                        "10" => "Oct",
                        "11" => "Nov",
                        "12" => "Dec");

                    /* Loop that displays the form fields */
                    foreach ($fields as $field => $label) {
                        /* echo the label */
                        echo "<div class='field'>\n
                                <label for='$field'>$label</label>\n";


                        /* echo the appropriate field */
                        if ($field === "pwd" || $field === "pwdConfirmation") {
                            echo "<input type='password' name='$field' id='$field' />\n";
                        } elseif ($field === "gender") {
                            echo "<input type='radio' name='gender' id='$field'
                              value='f' />Female\n";
                            echo "<input type='radio' name='gender' id='$field'
                              value='m' />Male\n";
                        } elseif ($field === "student") {
                            echo "<input type = 'checkbox' name = 'subscribe'/>";
                        } elseif ($field === "bDayYear") {
                            echo "<input type='text' name='$field' id='$field'
                                  size='4' maxlength='4' />\n";
                        } elseif ($field === "bDayMonth") {

                            echo " <select name = '$field' id = '$field'>";
                            for ($i = 01; $i < 13; $i++) {
                                echo"<option value = '$months[$i]'>$months[$i]</option>";
                            }
                            echo "</select>";
                        } elseif ($field === "bDay") {
                            echo "<input type='text' name='$field' id='$field'
                                  size='2' maxlength='2' />";
                        } else {
                            echo "<input type='text' name='$field' id='$field'
                                  size='55' maxlength='55' />\n";
                        }
                        /* echo the end of the field div */
                        echo "</div>\n";
                    }

                    /* Display the submit button */
                    echo "<br><div id='submit'>\n
                            <input type='submit' value='Create Account'>\n
                          </div>";
                    ?>
                </form>
        </div>
        
        <hr>
        <footer>
            <p>MoneyClip Mobile 2012</p>
        </footer>
    </body>
</html>