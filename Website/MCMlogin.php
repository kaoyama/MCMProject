<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=us-ascii" />
    <meta name="Robots" content="index,follow">
    <meta name="author" content="Kimi Oyama">
    <title>Log in</title>
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
                <a href='MCMcreateNewAccount.php'>Make New Account</a>
            </div>
            <div id="stuff">
                <h2>Log In</h2>
                
                <h3>Please enter your User Name and Password.
                    <br/> The grader will be able to see your Login information.</h3>
                <form action="MCMloginValidator.php" method="POST">
                <?php
                    $fields = array("userName" => "Your User Name:",
                                "pwd" => "Your Password:");
                    
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