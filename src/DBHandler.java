import java.sql.*;
import java.util.ArrayList;

public class DBHandler {


    //Putting this here for now; in the future it will be saved
    //under some config file or class
    String url = "jdbc:mysql://stockroomdb.crbhpfgmilql.us-west-2.rds.amazonaws.com:3306";
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

    private ResultSet select(String query){
        Statement stmt = null;
        ResultSet result = null;
        try{
            stmt = connection.createStatement();
            result = stmt.executeQuery(query);
        }
        catch(SQLException e){

        }

        return result;
    }

}
