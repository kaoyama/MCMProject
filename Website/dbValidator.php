<?php

class dbValidator {

    /*
     * Variables
     */
    var $sitename; 
    var $randKey; 
    var $username; 
    var $password; 
    var $database; 
    var $tablename;
    var $tablename2; 
    var $tablename3; 
    var $connection; 
    var $dbHost; 
    var $sysop;
    var $error_message;
    
    /*
     * Constructor
     */
    function dbValidator() {
        $this->sitename = "dana.ucc.nau.edu/~cs854";
        $this->randKey = "0xerjklwed0cer";
    }
    
    /**
     * Initialize database
     * @param type $host name of database host
     * @param type $username username of database
     * @param type $password password of database
     * @param type $database name of database
     * @param type $tablename name of first table, user
     * @param type $tablename2 name of second table, useractivity
     */
    public function initDB($host, $username, $password, $database, $tablename,
            $tablename2, $tablename3) {
        $this->dbHost = $host; 
        $this->username = $username; 
        $this->password = $password; 
        $this->database = $database; 
        $this->tablename = $tablename; 
        $this->tablename2 = $tablename2; 
        $this->tablename3 = $tablename3; 
    }

    /**
     * Check for valid username and password in database
     * @param type $username username of user logging in
     * @param type $password password of user logging in
     * @return boolean returns true if succesful
     */
    public function login($username, $password) {
        // Check if session has been started 
        if (!isset($_SESSION)) {
            session_start(); 
        }
        // Check login info in database 
        if (!$this->checkLoginDB($username, $password)) {
            $this->handleError("Invalid login information.");
            $this->insertActivityIntoDB($username, "Failed login attempt");
            return false; 
        } 
        // Login successful
        $_SESSION[$this->getLoginSessionVar()] = $username;
        
        // Log user activity 
        $this->insertActivityIntoDB($username, "Successful login");
        return true; 
    }
    
    /**
     * Send message. 
     * @param type $to
     * @param type $from
     * @param type $subject
     * @param type $message
     * @param type $messageType
     * @return boolean 
     */
    public function sendMessage($to, $from, $subject, $message, $messageType) {
        // Check if session has been started 
        if (!isset($_SESSION)) {
            session_start(); 
        }
        
        // log in to database
        if (!$this->dbLogin()) {
            $this->handleError("Database login failed.");
            return false;
        }
        // make sure table exists
        if(!$this->ensureTable()) {
            $this->handleError("Error creating table.");
            return false; 
        }
        
        // Send message 
        if (!$this->sendMessageInDB($to, $from, $subject, $message, $messageType)) {
            $this->handleError("Error sending message");
            return false;
        }
        
        // Log user activity 
        $this->insertActivityIntoDB($from, "Message sent");
        
        return true; 
    }
    
    /**
     * Check login information (username, password) combination in database
     * @param type $username
     * @param type $password
     * @return boolean 
     */
    public function checkLoginDB($username, $password) {
        // log in to actual database
        if (!$this->dbLogin()) {
            $this->handleError("Login to database failed."); 
            return false;
        }
        
        // create query to choose valid user and password combination 
        $query = "SELECT * FROM $this->tablename".
                " WHERE username='$username' AND password = '$password' AND active = 1";
        
        $result = mysql_query($query, $this->connection);
        
        if(!$result || mysql_num_rows($result) <= 0) {
            $this->handleError("The username or password does not match.");
            return false;
        }
        
        $row = mysql_fetch_assoc($result); 
        
        // set session variables
        $_SESSION["username"] = $row["username"];
        $_SESSION["firstname"] = $row["firstname"];
        $_SESSION["lastname"] = $row["lastname"];
        $_SESSION["email"] = $row["email"];
        $_SESSION["password"] = $row["password"];
        $_SESSION["sysop"] = $row["sysop"];
        
        // free result set 
        mysql_free_result($result); 
        
        return true; 
    }     
    
    /**
     * Get all log files stored in the useractivity database 
     * @return boolean|string 
     */
    function getLogFiles() {
        // Log in to actual database 
        if (!$this->dbLogin()) {
            $this->handleError("Login to database failed."); 
            return false;
        }
        
        // select all rows from useractivity table 
        $query = "SELECT * FROM $this->tablename2";
        
        $result = mysql_query($query, $this->connection);
        
        // put results in HTML table format 
        $str = '<table border ="1">';
        $str .= "<tr> <td><b>Username</b></td>";
        $str .= "<td><b>Status</b></td> <td><b>Timestamp</b></td> </tr>";
        while($row = mysql_fetch_assoc($result)) {
            $str .= "<tr>";
            $str .= "<td>" . $row['username'] . "</td>";
            $str .= "<td>" . $row['statuscode'] . "</td>";
            $str .= "<td>" . $row['timestamp'] . "</td>";
            $str .= "</tr>";
        }
        $str .= "</table>";
        return $str;
    }
    
    /**
     * Get messages from the database
     * @return boolean|string 
     */
    function getMessages() {
        // Log in to actual database 
        if (!$this->dbLogin()) {
            $this->handleError("Login to database failed."); 
            return false;
        }
        
        // select all rows from useractivity table 
        if ($this->isAdmin()) { 
            // get tables
            $query = "SELECT * FROM $this->tablename3 WHERE admindelete = 0";
        } else {
            $query = "SELECT * FROM $this->tablename3 WHERE fromuser = '" . 
                    $this->username() . "' AND userdelete = 0";
        }
        
        $result = mysql_query($query, $this->connection);
        
        // put results in HTML table format 
        $str = '<table border ="1">';
        $str .= "<tr>";
        $str .= "<td><b>Delete</b></td>";
        $str .= "<td><b>To</b></td>";
        $str .= "<td><b>From</b></td> ";
        $str .= "<td><b>Subject</b></td> ";
        $str .= "<td><b>Timestamp</b></td> ";
        $str .= "<td><b>Status</b></td>";
        $str .= "<td><b>Content</b></td></tr>";
        
        while($row = mysql_fetch_assoc($result)) {
            $str .= "<tr>";
            $str .= "<td><input type = 'checkbox' name = 'inbox[]' value = '" .
                    $row['messageid'] . "' /></td>";
            $str .= "<td>" . $row['touser'] . "</td>";
            $str .= "<td>" . $row['fromuser'] . "</td>";
            $str .= "<td>" . $row['subject'] . "</td>";
            $str .= "<td>" . $row['time'] . "</td>";
            $str .= "<td>" . $row['status'] . "</td>";
            $str .= "<td>" . $row['content'] . "</td>";
            $str .= "</tr>";
            
        }
        $str .= "</table>";
        return $str;
    }
    
    /**
     * "Delete" selected messages
     * @param type $messageid
     * @return boolean 
     */
    function deleteMessages($messageid) {
        // Log in to actual database 
        if (!$this->dbLogin()) {
            $this->handleError("Login to database failed."); 
            return false;
        }
        
        // Select all rows from database 
        if ($this->isAdmin()) {
            $query = "UPDATE " . $this->tablename3 . 
                    " SET admindelete = 1" . 
                    " WHERE messageid = $messageid";            
        } else {
            $query = "UPDATE " . $this->tablename3 . 
                    " SET userdelete = 1" . 
                    " WHERE messageid = $messageid";
        }
        
        if (!mysql_query($query, $this->connection)) {
            $this->handleErrors("Deleting user message failed.");
            return false; 
        }
        return true;
    }
    
    /**
     * Change status of selected message as "read"
     * @param type $messageid
     * @return boolean 
     */
    function markAsRead($messageid) {
        // Log in to actual database 
        if (!$this->dbLogin()) {
            $this->handleError("Login to database failed."); 
            return false;
        }
        // update status to read
        $query = "UPDATE $this->tablename3 SET status = 'read'" . 
                " WHERE messageid = $messageid";
            
        if (!mysql_query($query, $this->connection)) {
            $this->handleErrors("Mark as read failed.");
            return false; 
        }
        return true;
    }
    
    /**
     * Archive the log files.  This appends information to a CSV file and 
     * deletes the archived information from the database. 
     * @return boolean 
     */
    function archiveLogFiles() {
        // Log in to actual database 
        if (!$this->dbLogin()) {
            $this->handleError("Login to database failed."); 
            return false;
        }
        
        // Select all rows from database 
        $query = "SELECT * FROM $this->tablename2";
        
        $result = mysql_query($query, $this->connection);
        
        $filename = "logfiles.csv";
              
        // write new data to file, along with old data
        $fp = fopen($filename, "a");
        
        while($row = mysql_fetch_assoc($result)) {
            $time = $row['timestamp'];
            fputcsv($fp, array($row['username'],$row['statuscode'],$time));
            
            // delete from table 
            $query = "DELETE FROM " . $this->tablename2 . 
                    " WHERE username = '" . $row['username'] . 
                    "' AND timestamp = '" . $row['timestamp'] ."'";
            
            if (!mysql_query($query, $this->connection)) {
                $this->handleErrors("Deleting archived data failed.");
                fclose($fp);
                return false; 
            }
        }
        
        fclose($fp);
        return true;
    }
    
    /**
     * Return username. 
     * @return type 
     */
    function username() {
        return isset($_SESSION["username"]) ? 
            $_SESSION["username"] : '';
    }
    
    /**
     * Return full name of user. 
     * @return type 
     */
    function userFullName() {
        return isset($_SESSION["firstname"])? 
            ($_SESSION["firstname"] . " " . $_SESSION["lastname"]):'';
    }
    
    /**
     * Return first name of user. 
     * @return type 
     */
    function userFirstName() {
        return isset($_SESSION["firstname"])? $_SESSION["firstname"] : '';
    }
    
    /**
     * Return last name of user. 
     * @return type 
     */
    function userLastName() {
        return isset($_SESSION["lastname"])? $_SESSION["lastname"] : '';
    }
    
    /** 
     * Return email of user. 
     * @return type 
     */
    function userEmail() {
        return isset($_SESSION["email"])? $_SESSION["email"] : '';
    }
    
    /**
     * Return password of user.  Ideally, this should be encrypted. 
     * @return type 
     */
    function userPassword() {
        return isset($_SESSION["password"])? $_SESSION["password"] : '';
    }
    
    /**
     * Returns true if the user is an administrator. 
     * @return boolean 
     */
    function isAdmin() {
        if (isset($_SESSION["sysop"])) {
            if ($_SESSION["sysop"] == 1) {
                // is admin
                return true; 
            }
            else {
                // not admin
                return false; 
            }
        }
        return false;
    }
    
    /**
     * Log in to the actual database. 
     * @return boolean 
     */
    public function dbLogin() {
        $this->connection = mysql_connect($this->dbHost, $this->username, $this->password);
        mysql_select_db($this->database, $this->connection);
        
        // Error logging in to db
        if (!$this->connection) {
            $this->handleDBError("Database login failed");
            return false; 
        }
        return true; 
    } 
    
    /**
     * Append error messages. 
     * @param type $err 
     */
    function handleError($err) {
        $this->error_message .= $err."<br>";
    }
    
    /**
     * Append database error messages.
     * @param type $err 
     */
    function handleDBError($err) {
        $this->handleError($err."<br>mysqlerror: " . mysql_error());
    }
    
    /**
     * Get login session variable. 
     * @return string 
     */
    function getLoginSessionVar() {
        $retvar = md5($this->randKey); 
        $retvar = 'user_' . substr($retvar,0,10); 
        return $retvar; 
    }
    
    /**
     * Redirect the user to another URL. 
     * @param type $url 
     */
    function redirectToURL($url) {
        header("Location: $url");
        exit; 
    }
    
    /**
     * Check if the user is logged in to a session. 
     * @return boolean 
     */
    function checkLogin() {
        if(!isset($_SESSION)) {
            session_start(); 
        }
        $sessionvar = $this->getLoginSessionVar(); 
        
        if (empty($_SESSION[$sessionvar])) {
            return false; 
        }
        return true; 
    }
    
    /**
     * Register a new user in the database. This occurs after user validation 
     * has occured in the acutal form. This function also checks for duplicate
     * username or email in the database.
     * @param type $firstname first name of the user
     * @param type $lastname last name of the user
     * @param type $username username of the new user
     * @param type $email email of the user
     * @param type $password password of the user
     * @return boolean 
     */
    function regsiterUser($firstname, $lastname, $username,
            $email, $password) {
        
        // log in to database
        if (!$this->dbLogin()) {
            $this->handleError("Database login failed.");
            return false;
        }
        // make sure table exists
        if(!$this->ensureTable()) {
            $this->handleError("Error creating table.");
            return false; 
        }
        // check duplicate username (primary key)
        if (!$this->isFieldUnique($username, 'username')) {
            $this->handleError("This username already exists. Please try another username.");
            return false;
        }
        // check duplicate email 
        if (!$this->isFieldUnique($email, 'email')) {
            $this->handleError("This email is already registered.");
            return false; 
        }
        
        // finally, insert the new user into the database
        if (!$this->insertIntoDB($firstname, $lastname, $username, 
                $email, $password)) {
            return false; 
        }
        
        $this->insertActivityIntoDB($username, "Registered user");
        return true;         
    }
    
    /**
     * Change the user information in the database. This function also checks
     * to see if the old username matches what is currently in the database. 
     * @param type $firstname new first name
     * @param type $lastname new last name 
     * @param type $username username of the user trying to change their 
     *                       information.  This does not get changed (primary)
     * @param type $email new email
     * @param type $oldPassword old password
     * @param type $password new password 
     * @return boolean 
     */
    function changeUserInfo($firstname, $lastname, $username,
            $email, $oldPassword, $password) {
        
        // log in to database
        if (!$this->dbLogin()) {
            $this->handleError("Database login failed.");
            return false;
        }
        // make sure table exists
        if(!$this->ensureTable()) {
            $this->handleError("Error creating table.");
            return false; 
        }        
        // old password matches? 
        if ($oldPassword != $this->userPassword()) {
            $this->handleError("Old password does not match current password.");
            return false; 
        }
        
        // finally, update in database 
        if (!$this->updateInDB($firstname, $lastname, $username, 
                $email, $password)) {
            return false; 
        }
        $this->insertActivityIntoDB($username, "Updated user info");
        return true;         
    }
    
    /**
     * Deletes the user account.  This function does not actually take out the 
     * user from the database; instead it disables the active flag for the user.
     * @param type $username
     * @return boolean 
     */
    function deleteAccount($username) {
        
        // log in to database
        if (!$this->dbLogin()) {
            $this->handleError("Database login failed.");
            return false;
        }        
        if (!$this->deleteUser($username)) {
            return false; 
        }
        $this->insertActivityIntoDB($username, "Deleted user");
        return true;         
    }
    
    /**
     * Makes sure that the field given is not a duplicate in the database. 
     * @param type $fieldval
     * @param type $field
     * @return boolean 
     */
    function isFieldUnique($fieldval, $field) {
        $query = "SELECT username from $this->tablename WHERE " .
                "$field='".$fieldval."'";
        
        $result = mysql_query($query, $this->connection); 
        
        if($result && mysql_num_rows($result) > 0) {
            return false; 
        }
        return true; 
    }
    
    /**
     * Insert into the database. 
     * @param type $firstname
     * @param type $lastname
     * @param type $username
     * @param type $email
     * @param type $password
     * @return boolean 
     */
    function insertIntoDB($firstname, $lastname, $username, 
                $email, $password) {
        $query = 'INSERT INTO ' . $this->tablename . ' (' . 
                'firstname, lastname, username, email, password, active)' .
                ' VALUES ('.
                '"' . $firstname . '",'.
                '"' . $lastname . '",'.
                '"' . $username . '",'.
                '"' . $email .'",'.
                '"' . $password . '", '.
                '"' . true . '"'.
                ')';
        if (!mysql_query($query, $this->connection)) {
            $this->handleError("Error inserting data into table"); 
            return false; 
        }
        return true; 
    }
    
    /**
     * Put message into DB
     * @param type $to
     * @param type $from
     * @param type $subject
     * @param type $message
     * @param type $messageType
     * @return boolean 
     */
    function sendMessageInDB($to, $from, $subject, $message, $messageType) {
        $query = 'INSERT INTO ' . $this->tablename3 . ' (' . 
                'touser, fromuser, subject, content, status, type)' . 
                ' VALUES (' . 
                '"' . $to . '",' . 
                '"' . $from . '",' . 
                '"' . $subject . '",' . 
                '"' . $message . '",' . 
                '"unread",' . 
                '"' . $messageType . '")';
        if (!mysql_query($query, $this->connection)) {
            $this->handleError("Error inserting data into table $query"); 
            return false; 
        }
        return true;
    } 
    
    /**
     * Insert activity into the database. 
     * @param type $username
     * @param type $activity
     * @return boolean 
     */
    function insertActivityIntoDB($username, $activity) {
        // log in to database
        if (!$this->dbLogin()) {
            $this->handleError("Database login failed.");
            return false;
        }
        
        $query = 'INSERT INTO ' . $this->tablename2 . ' (' .
                'username, statuscode)' .
                ' VALUES (' .
                '"' . $username . '",' .
                '"' . $activity . '")';
        if (!mysql_query($query, $this->connection)) {
            $this->handleError("Error logging user activity.");
            return false;
        }
        return true; 
    }
    
    function updateInDB($firstname, $lastname, $username, $email, $password) {
        $query = "UPDATE $this->tablename" .
                " SET firstname = '" . $firstname . "'," .
                " lastname = '" . $lastname . "'," .
                " email = '" . $email . "'," .
                " password = '" . $password . "'" .                
                " WHERE username = '" . $username . "';";
        
        if (!mysql_query($query, $this->connection)) {
            $this->handleDBError("Error updating user info");
            return false;
        }
        return true; 
    }
    
    /**
     * Sets the active flag to false in the database for the particular user.
     * @param type $username
     * @return boolean 
     */
    function deleteUser($username) {
        $query = "UPDATE $this->tablename " .
                "SET active = 0 " . 
                "WHERE username = '" . $username . "';";
        if (!mysql_query($query, $this->connection)) {
            $this->handleDBError("Error deactivating user");
            return false;
        }
        return true; 
    }
    
    /**
     * Ensure that the table exists. 
     * @return boolean 
     */
    function ensureTable() {
        $result = mysql_query("SHOW COLUMNS FROM $this->tablename", $this->connection); 
        if (!$result || mysql_num_rows($result) <= 0) {
            return $this->createTable(); 
        }
        return true; 
    }
    
    /**
     * Creates a new table in the database. 
     * @return boolean 
     */
    function createTable() {
        $query = "CREATE TABLE $this->tablename(".
                "username VARCHAR(75) NOT NULL ," .
                "firstname VARCHAR(75) NOT NULL ,".
                "lastname VARCHAR(75) NOT NULL ,".
                "email VARCHAR(75) NOT NULL ,".
                "password VARCHAR(75) NOT NULL,".
                "active BOOLEAN NOT NULL,".
                "sysop BOOLEAN NOT NULL,".
                "PRIMARY KEY (username)".
                ")";
        
        if (!mysql_query($query, $this->connection)) {
            $this->handleDBError("Error creating database.");
            return false;
        }
        return true; 
    }
    
    /**
     * Gets the error messages produced while accessing the database. 
     * @return type 
     */
    public function getErrorMessage() {
        return $this->error_message . "<br><br>"; 
    }
    
    /**
     * Log the user out of the website. Clear the session variable. 
     * @param type $username 
     */
    public function logOut($username) {
        $this->insertActivityIntoDB($username, "Log out");
        
        $sessionvar = $this->getLoginSessionVar();
        
        $_SESSION[$sessionvar] = NULL; 
        unset($_SESSION[$sessionvar]);
        
    }
}
?>
