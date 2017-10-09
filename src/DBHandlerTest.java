import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DBHandlerTest {

    public static void main(String[] args){
        DBHandler testDB = new DBHandler();
        ResultSet result = testDB.select("stockroomdb.PARTS", "part_number", new ArrayList<String>());
        try{
            result.beforeFirst();
            int i = 0;
            while(result.next()){
                System.out.println("Row " + i + " part_id: " + result.getInt(1));
                i++;
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }

    }
}
