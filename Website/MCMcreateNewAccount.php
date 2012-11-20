<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=us-ascii" />
    <meta name="Robots" content="index,follow">
    <meta name="author" content="Kimi Oyama">
    <title>Create New Account</title>
    <link rel="stylesheet" type="text/css" href="style.css">
</head>

<body>
    <div id="container">
        <div id="top"></div>
        <div id="content">
            <div id="header">
                Hi. d(^_^)b <br> <br> <br> You are Awesome.
            </div>
            <div id="sideBar">
                Welcome Friend
            </div>
            <div id="stuff">
                <h2>Create a new user account.
                
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
                        }
                        elseif ($field === "gender"){
                           echo "<input type='radio' name='gender' id='$field'
                              value='female' />Female\n";
                           echo "<input type='radio' name='gender' id='$field'
                              value='male' />Male\n";
                        }
                        elseif ($field === "student"){
                            echo "<input type = 'checkbox' name = 'subscribe'/>";
                        }
                        elseif ($field === "bDayYear") {
                            echo "<input type='text' name='$field' id='$field'
                                  size='4' maxlength='4' />\n";
                        }
                        elseif ($field === "bDayMonth") {
                            
                            echo " <select name = '$field' id = '$field'>";
                            for ($i = 01; $i<13;$i++) {
                                echo"<option value = '$months[$i]'>$months[$i]</option>";
                            }
                            echo "</select>";
                        }
                        elseif ($field === "bDay") {
                            echo "<input type='text' name='$field' id='$field'
                                  size='2' maxlength='2' />";
                        }
                        else {
                            echo "<input type='text' name='$field' id='$field'
                                  size='55' maxlength='55' />\n";
                        }
                        /* echo the end of the field div */
                        echo "</div>\n";
                    }

                    /* Display the submit button */
                    echo "<div id='submit'>\n
                            <input type='submit' value='Create Account'>\n
                          </div>";
                  ?>
                </form>
            </div>
        </div>
    </div>
    <div id="footer">
    </div>
</body>
</html>