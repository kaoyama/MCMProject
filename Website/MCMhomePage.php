<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=us-ascii" />
    <meta name="Robots" content="index,follow">
    <meta name="author" content="Kimi Oyama">
    <title>Kimi's Page</title>
    <link rel="stylesheet" type="text/css" href="style.css">
</head>

    <?php
    session_start();
    if($_SESSION['user'] === "GuestOfKimi")
        header("Location: createNewAccount.php");
    else {
        echo "<body>
                <div id='container'>
                <div id='top'></div>
                <div id='content'>
                    <div id='header'>
                        Hi. d(^_^)b <br> <br> <br> Life is Awesome.
                    </div>
                    <div id='menu'>
                        <ul>
                            <li><a href='MCMhomePage.php'>Home</a></li>
                            <li><a href='MCMcreateNewAccount.php'>Modify Account</a></li>
                            <li><a href='MCMsettings.php'>Settings</a></li>
                            <li><a href='MCMlogin.php'>Log out</a></li>
                        </ul>
                    </div>
                    <div id='sideBar'>
                        hi there.
                    </div>
                    <div id='stuff'>
                        	
                    </div>
                </div>
                <div id='bottom'></div>
            </div>";
    }
    ?>
    <div id="footer">
    </div>
</body>
</html>