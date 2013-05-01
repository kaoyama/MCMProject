<?php

class DB_Functions {

    private $db;

    /**
     * Constructor for database functions  
     */
    function __construct() {
        include_once './db_connect.php';
        // connecting to database
        $this->db = new DB_Connect();
        $this->db->connect();
    }

    /**
     * Destructor - called when this class is destroyed 
     */
    function __destruct() {
        
    }

    /**
     * Stores new user 
     * @param type $name Name of new user 
     * @param type $email Email of new user
     * @param type $gcm_regid ID of specific device
     * @return boolean Stores the new user in the database.  Afterwards, queries
     *                 the database for the same user and returns the user's 
     *                 information.  If any errors occurred, returns False. 
     */
    public function storeUser($name, $email, $gcm_regid) {
        // insert user into database
        $result = mysql_query("INSERT INTO gcm_users(name, email, gcm_regid, created_at) VALUES('$name', '$email', '$gcm_regid', NOW())");

        // check for successful store
        if ($result) {
            // get user details
            $id = mysql_insert_id(); // last inserted id
            $result = mysql_query("SELECT * FROM gcm_users WHERE id = $id") or die(mysql_error());

            // return user details
            if (mysql_num_rows($result) > 0) {
                return mysql_fetch_array($result);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Get the user information based on the password
     * @param type $email Email of the person being queried 
     * @return type Returns the user's information 
     */
    public function getUserByEmail($email) {
        $result = mysql_query("SELECT * FROM gcm_users WHERE email = '$email' LIMIT 1");
        return $result;
    }

    /**
     * Retrieves all users in the table
     * @return type Returns all users
     */
    public function getAllUsers() {
        $result = mysql_query("select * FROM gcm_users");
        return $result;
    }

    /**
     * Check if the user exists or not based on their associated email. 
     * @param type $email Email of the person being queried 
     * @return boolean Returns true if the user exists in the table, and false 
     *                 otherwise. 
     */
    public function isUserExisted($email) {
        $result = mysql_query("SELECT email from gcm_users WHERE email = '$email'");
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            // user existed
            return true;
        } else {
            // user not existed
            return false;
        }
    }

}

?>