import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

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
        testArray.add("id INT ");
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

        HashMap testUpdate = new HashMap();
        testUpdate.put("id", "19");
        testUpdate.put("description", "new description");
        testUpdate.put("count", "20");
        ArrayList<String> testConditions = new ArrayList<String>();
        testConditions.add("row = '4'");
        int result3 = testDB.update("testTable", testUpdate, testConditions);

        System.out.println("Inserting row into testTable...");
        ArrayList<String> columns = new ArrayList<String>();
        columns.add("id");
        columns.add("description");
        columns.add("count");
        columns.add("weight");
        columns.add("row");
        columns.add("shelf");
        ArrayList<String> rows = new ArrayList<String>();
        rows.add("4");
        rows.add("A diff item");
        rows.add("12");
        rows.add("2.13");
        rows.add("2");
        rows.add("116");
//        testDB.insert("testTable", columns, rows);
        rows.set(0, "6");
        rows.set(1, "item three");
        testDB.insert("testTable", null, rows);
        }
}
