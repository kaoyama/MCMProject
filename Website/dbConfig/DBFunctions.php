<?php
 
class DBFunctions {
 
    // constructor
    function __construct() {
 
    }
 
    // destructor
    function __destruct() {
        // $this->close();
    }
 
    // Connecting to database
    public function connect() {
        require_once './dbConfig/config.php';
        // connecting to mysql
        $con = mysql_connect(DB_HOST, DB_USER, DB_PASSWORD);
        // selecting database
        mysql_select_db(DB_DATABASE);
 
        // return database handler
        return $con;
    }
 
    // Closing database connection
    public function close() {
        mysql_close();
    }
    
    // with error messages
    public function query($queryStr) {
        $result = mysql_query($queryStr);
        
        if (mysql_errno()) { 
            header("HTTP/1.1 500 Internal Server Error");
            echo $query.'<br>';
            echo mysql_error(); 
        }
        else
        {
            return $result;
        }
    }
    
    public function resultToJson($res) {
        $rows = array();
        while($r = mysql_fetch_assoc($res)) {
            $rows[] = $r;
        }
        return json_encode($rows);
    }
 
} 
?>