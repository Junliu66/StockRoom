import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Used to test DBHandler.java during development
 */
public class DBHandlerTest {

    /**
     * main method, runs tests for the database
     * @param args not used
     */
    public static void main(String[] args){
        DBHandler testDB = new DBHandler();

//        ResultSet result = testDB.select("stockroomdb.PARTS", "part_number");
//        try{
//            result.beforeFirst();
//            int i = 0;
//            while(result.next()){
//                System.out.println("Row " + i + " part_id: " + result.getInt(1));
//                i++;
//            }
//        }
//        catch(SQLException e){
//            e.printStackTrace();
//        }

//        ArrayList<String> testArray = new ArrayList<String>();
//        testArray.add("id INT ");
//        testArray.add("description char(100)");
//        testArray.add("count int");
//        testArray.add("weight real");
//        testArray.add("row int");
//        testArray.add("shelf int");
//        int result2 = testDB.createTable("testTable", testArray);
//        System.out.println("Adding table with name 'testTable'");
//        if (result2 >= 0)
//            System.out.println("Success.");
//        else
//            System.out.println("Failed.");

//        String descr = "another \"test";
//        HashMap testUpdate = new HashMap<String, Object>();
//        testUpdate.put("description", "\"weird stuff 'huh'");
//        testUpdate.put("count", 23);
//        ArrayList<Object[]> testConditions = new ArrayList<>();
//        Object cond1[] = new Object[3]; // each condition is an array of Objects
//        cond1[0] = "description"; // index 0 is the identifier
//        cond1[1] = "="; // index 1 is the comparator (=, <, >, <>, etc)
//        cond1[2] = descr; // index 2 is the value to compare against
//        // cond1 asks "if description is equal to descr"
//        testConditions.add(cond1);
//        int result3 = testDB.update("testTable", testUpdate, testConditions);

//        System.out.println("Inserting row into testTable...");
//        ArrayList<String> columns = new ArrayList<>();
//        columns.add("id");
//        columns.add("name");
//        columns.add("time");
//        ArrayList<Object> values = new ArrayList<>();
//        values.add(5);
//        values.add("item with time");
//        values.add("NOW()");
////        testDB.insert("testTable", columns, values);
//        testDB.insert("testTable", null, values);
//        testDB.adjustPartQuantity(11, -5);

//        Receiving receivingTest = new Receiving();
//        receivingTest.displayReceiving();
    }
}
