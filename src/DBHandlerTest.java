import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DBHandlerTest {

    public static void main(String[] args){
        DBHandler testDB = new DBHandler();
        ResultSet result = testDB.select("stockroomdb.PARTS", "*", new ArrayList<String>());
        try{
            result.beforeFirst();
            int i = 0;
            while(result.next()) {
                System.out.println("Row " + i + " part_id: " + result.getInt(1) + " " + result.getString(2));
                i++;
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }

        ArrayList<String> testArray = new ArrayList<String>();
        testArray.add("id int");
        testArray.add("description char(100)");
        testArray.add("count int");
        testArray.add("weight real");
        testArray.add("row int");
        testArray.add("shelf int");
        int result2 = testDB.createTable("testTable", testArray);
        System.out.println("Adding table with name 'testTable'");
        if (result2 >= 0)
            System.out.println("Success.");
        else
            System.out.println("Failed.");

    }
}
