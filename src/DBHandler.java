import java.sql.*;
import java.util.ArrayList;

public class DBHandler {


    //TODO: Putting this here for now; in the future it will be saved under some config file or class
    String url = "jdbc:mysql://stockroomdb.crbhpfgmilql.us-west-2.rds.amazonaws.com:3306/stockroomdb";
    String username = "cs40a";
    String password = "DB5u5D4X5z6e";

    Connection connection;

    //Constructor
    public DBHandler(){
        //load the JDBC class
        try{
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch(ClassNotFoundException e){
            //TODO: Maybe raise an error in the actual program to repair
            System.out.println("Unable to load driver class");
            e.printStackTrace();
            System.exit(1);
        }
        try{
            connection = DriverManager.getConnection(url, username, password);
        }
        catch(SQLException e){
            //Handle database connection failures here
            e.printStackTrace();
            System.exit(1);
        }
        catch(Exception e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * @param table: name of the table
     * @param selection: what to return
     * @param conditions: conditionals (i.e. 'id=1')
     * @return a ResultSet if the fetch was successful, else null
     */
    public ResultSet select(String table, String selection, ArrayList<String> conditions){
        String query = "select " + selection + " from " + table;
        if(!conditions.isEmpty()){
            query += " where ";
            for (String condition: conditions) {
                query += condition + " and ";
            }
        }

        return select(query);
    }

    /**
     * Function for making selections of the database
     * @param query an SQL-formatted selection string
     * @return an SQL ResultSet
     */
    private ResultSet select(String query){
        Statement stmt = null;
        ResultSet result = null;
        try{
            stmt = connection.createStatement();
            result = stmt.executeQuery(query);
        }
        catch(SQLException e){
            //TODO: handle SQLExceptions
        }

        return result;
    }

    /**
     *
     * @param tableName: name of new table
     * @param dataList: ArrayList of sql-formatted strings; ie {"FirstName CHAR(100)", "LastName CHAR(50)"}
     * @return either (1) the row count for SQL Data Manipulation Language (DML) statements
     *         or (2) 0 for SQL statements that return nothing
     */
    public int createTable(String tableName, ArrayList<String> dataList) {
        String query = "create table " + tableName + " ";
        if (!dataList.isEmpty()) {
            query += "(";
            for (String data : dataList) {
                query += data + ", ";
            }
            query = query.substring(0, query.length()-2);
            query += ")";
        }
        return createTable(query);
    }

    private int createTable(String query) {
        Statement stmt = null;
        int result = -1;
        try {
            stmt = connection.createStatement();
            result = stmt.executeUpdate(query);
        } catch(SQLException e) {
            e.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }



}