import javax.management.Query;
import java.sql.*;
import java.util.*;

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
     * helper function for selecting a table without conditions
     * @param table: name of table to select
     * @param selection: what to return
     * @return a ResultSet if the fetch was successful, else null
     */
    public ResultSet select(String table, String selection) {
        return select(table, selection, new ArrayList<String>());
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
            //TODO: handle SQLException
        }

        return result;
    }

    /**
     * Adjusts a part quantity by a given amount in the STOCKROOM database
     * @param partId: the ID of the part, integer
     * @param adjustment: the amount the quantity should be adjusted, signed integer
     * @return 0 if no error
     */
    public int adjustPartQuantity(int partId, int adjustment)
    {
        int currentQuantity;
        Statement stmt = null;
        ResultSet result = null;
        try {
            stmt = connection.createStatement();
            result = stmt.executeQuery("SELECT quantity FROM stockroomdb.STOCKROOM WHERE parts_id = " + partId);
            result.next();
            currentQuantity = result.getInt(1);
            return setPartQuantity(partId, currentQuantity + adjustment);
        }
        catch (SQLException e)
        {
            //TODO: handle SQLException
            e.printStackTrace();
            return -1;
        }
    }
    /**
     * set the quantity of a part by an amount
     * @param partId: id of the part in the STOCKROOM database
     * @param quantity: quantity to set
     * @return 0 if no error
     */
    public int setPartQuantity(int partId, int quantity)
    {
        Statement stmt = null;
        int result;
        try {
            stmt = connection.createStatement();
            return stmt.executeUpdate("UPDATE stockroomdb.STOCKROOM SET quantity = " + quantity + " WHERE parts_id = " + partId);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     *
     * @param tableName: name of new table
     * @param dataList: ArrayList of sql-formatted strings; ie {"FirstName CHAR(100)", "LastName CHAR(50)"}
     * @return either (1) the row count for SQL Data Manipulation Language (DML) statements
     *         or (2) 0 for SQL statements that return nothing
     */
    public int createTable(String tableName, ArrayList<String> dataList) {
        String query = "CREATE TABLE " + tableName + " ";
        if (!dataList.isEmpty()) {
            query += "(";
            for (String data : dataList) {
                query += data + ", ";
            }
            query = query.substring(0, query.length()-2);
            query += ")";
        }
        return update(query);
    }


    /**
     *
     * @param tableName: the name of the new table
     * @param updates: a HashMap of the updates to be performed, where the column in the key, and the new value is the value
     * @param searchConditions: an ArrayList of Object arrays of size 3. Each Object array's first two indices contain Strings representing first the column identifier to compare and second, the SQL comparator.
     *                        The third index contains the value to search for. This could be a String, a double, on an int.
     * @return
     */
    public int update(String tableName, HashMap<String, Object> updates, ArrayList<Object[]> searchConditions) {
        String query = "UPDATE " + tableName + " SET ";

        Set updateSet = updates.entrySet();
        Iterator it = updateSet.iterator();
        while (it.hasNext()) {
            Map.Entry me = (Map.Entry) it.next();
            query += me.getKey() + " = '" + me.getValue() + "', ";
        }
        query = query.substring(0, query.lastIndexOf(", "));

        if (!searchConditions.isEmpty()) {
            query += " WHERE ";
            for (int i = 0; i < searchConditions.size(); i++) {
                Object[] cond = searchConditions.get(i);
                query += cond[0] + " " + cond[1] + " ";
                // handles potential discrepancy with third value in condition arrays
                if (cond[2] instanceof String) {
                    query += "'" + cond[2] + "'";
                } else if (cond[2] instanceof Integer || cond[2] instanceof Double || cond[2] instanceof Float) {
                    query += cond[2];
                } else
                    return -1;
                if (i != searchConditions.size()-1)
                    query += " AND ";
            }
            if (searchConditions.size() > 1)
                query = query.substring(0, query.lastIndexOf(" AND "));
        }

        return update(query);
    }

    /**
     * update calls a properly-formatted update SQL query
     * @param query
     * @return
     */
    private int update(String query) {
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

//    public int insert(String tableName, ArrayList<String> values) {
//        return insert(tableName, new ArrayList<String>(), values);
//    }

    /**
     * insert: allows adding a row to a table
     * @param tableName: name of the table to add a row to
     * @param columns: columns to be added (optional)
     * @param values: values to add
     * @return either (1) the row count for SQL Data Manipulation Language (DML) statements
     *         or (2) 0 for SQL statements that return nothing
     */
    public int insert(String tableName, ArrayList<String> columns, ArrayList<Object> values) {
        String query = "INSERT INTO " + tableName;
        if (columns == null)
            columns = new ArrayList<String>();
        if (columns.size() == values.size()) {
        // using columns and values
        query += " (";
        for (String column : columns)
            query += column + ", ";
        query = query.substring(0, query.lastIndexOf(", "));
        query += ") VALUES (";
        for (Object value : values)
            if (value instanceof String)
                query += "'" + value + "'" + ", ";
            else
                query += value + ", ";
        query = query.substring(0, query.lastIndexOf(", "));
        query += ")";
        } else if(columns.size() == 0) {
            // just using values
            query += " VALUES (";
            for (Object value : values)
                if (value instanceof String && value != "NOW()")
                    query += "'" + value + "'" + ", ";
                else
                    query += value + ", ";
            query = query.substring(0, query.lastIndexOf(", "));
            query += ")";
        }
        else // number of columns and values don't match, return an error
            return -1;
        return insert(query);
    }

    /**
     * private helper method for public method insert
     * @param query: an SQL-formatted INSERT INTO string
     * @return either (1) the row count for SQL Data Manipulation Language (DML) statements
     *         or (2) 0 for SQL statements that return nothing
     */
    private int insert(String query) {
        Statement stmt = null;
        int result = -1;
        try {
            stmt = connection.createStatement();
            result = stmt.executeUpdate(query);
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

}
