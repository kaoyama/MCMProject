<?php

class DBFunctions {

    /**
     * Constructor for database functions 
     */
    function __construct() {
        
    }

    /**
     * Destructor for class 
     */
    function __destruct() {
        // $this->close();
    }

    /**
     * Connect to the database
     * @return type database handler 
     */
    public function connect() {
        require_once './dbConfig/config.php';

        // connecting to mysql
        $con = mysql_connect(DB_HOST, DB_USER, DB_PASSWORD);

        // selecting database
        mysql_select_db(DB_DATABASE);

        // return database handler
        return $con;
    }

    /**
     * Close the database connection  
     */
    public function close() {
        mysql_close();
    }

    /**
     * Make a SQL query to the database 
     * @param type $queryStr SQL query 
     * @return type Result of the query 
     */
    public function query($queryStr) {
        $result = mysql_query($queryStr);

        if (mysql_errno()) {
            header("HTTP/1.1 500 Internal Server Error");
            echo $query . '<br>';
            echo mysql_error();
        }
        else
            return $result;
    }

    /**
     * Convert result from the database to a JSON array 
     * @param type $res Result of SQL query 
     * @return type JSON string of the result 
     */
    public function resultToJson($res) {
        $rows = array();
        while ($r = mysql_fetch_assoc($res)) {
            $rows[] = $r;
        }
        return json_encode($rows);
    }

}

?>