import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class DBHandlerTest {

    public static void main(String[] args){
        DBHandler testDB = new DBHandler();
//        ResultSet result = testDB.select("stockroomdb.PARTS", "part_number", new ArrayList<String>());
//        try{
//            result.beforeFirst();
//            int i = 0;
//            while(result.next()){
//                System.out.println("Row " + i + " part_id: " + result.getInt(1));
//                i++;
//            }
//        }
//        catch(SQLException e){z
//            e.printStackTrace();
//        }
//
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

        String rownum = "NE AGAIN";
        HashMap testUpdate = new HashMap<String, Object>();
        testUpdate.put("description", "NE AGAIN");
        testUpdate.put("count", 5);
        ArrayList<Object[]> testConditions = new ArrayList<Object[]>();
        Object cond1[] = new Object[3]; // each condition is an array of Objects
        cond1[0] = "description"; // index 0 is the identifier
        cond1[1] = "="; // index 1 is the comparator (=, <, >, <>, etc)
        cond1[2] = rownum; // index 2 is the value to compare against
        // cond1 asks "if row is less than rownum"
        testConditions.add(cond1);
        int result3 = testDB.update("testTable", testUpdate, testConditions);

        System.out.println("Inserting row into testTable...");
        ArrayList<String> columns = new ArrayList<String>();
        columns.add("id");
        columns.add("description");
        columns.add("count");
        columns.add("weight");
        columns.add("row");
        columns.add("shelf");
        ArrayList<Object> values = new ArrayList<Object>();
        values.add(4);
        values.add("A diff item");
        values.add(12);
        values.add(2.13);
        values.add(2);
        values.add(116);
//        testDB.insert("testTable", columns, values);
        testDB.insert("testTable", null, values);
        testDB.adjustPartQuantity(11, -5);
    }
}
