<?php
 
/**
 * This class contains functions related to estabilishing connection with 
 * the database.
 */
class DB_Connect {
    
    /**
     * Constructor for databse connection 
     */
    function __construct() {
 
    }
 
    /**
     * Called when class instance is destructed 
     */
    function __destruct() {
        // $this->close();
    }
    
    /**
     * Connect to the database using information specified in config.php.  
     * Requres config.php to be initialized with speficic database information. 
     * @return type database handler 
     */
    public function connect() {
        require_once 'config.php';
        // connecting to mysql
        $con = mysql_connect(DB_HOST, DB_USER, DB_PASSWORD);
        // selecting database
        mysql_select_db(DB_DATABASE);
 
        // return database handler
        return $con;
    }
 
    /**
     * Close the current database conenction  
     */
    public function close() {
        mysql_close();
    }
 
} 
?>