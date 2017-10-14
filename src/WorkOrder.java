/**
 * Created by zhangJunliu on 10/14/17.
 */
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import java.sql.*;

/**
 * Created by zhangJunliu on 10/10/17.
 */
public class WorkOrder {
    public static void workOrder(){

        DBHandler testDB = new DBHandler();

        ResultSet result_part_id = testDB.select("stockroomdb.PARTS", "parts_id", new ArrayList<String>());

        ResultSet result1 = testDB.select("stockroomdb.PARTS", "part_number", new ArrayList<String>());
        //ResultSet result2 = testDB.select("stockroomdb.PARTS", "part_description", new ArrayList<String>());
        ResultSet result3 = testDB.select("stockroomdb.STOCKROOM", "quantity", new ArrayList<String>());

        ResultSet result4 = testDB.select("SELECT TABLE_NAME FROM INFORMATION_SCHEMA. TABLES WHERE TABLE_TYPE = 'BASE TABLE' AND TABLE_SCHEMA='stockroomdb'");

        try {
            result_part_id.beforeFirst();
            result1.beforeFirst();
            //result2.beforeFirst();
            result3.beforeFirst();
            //result4.beforeFirst();
            while(result4.next()){
                System.out.println(result4.getString(1));
            }

            ArrayList<Part> listOfInventories = new ArrayList<Part>();

            while (result_part_id.next() && result1.next() && result3.next()) {
                Part part_id = new Part();
                part_id.setPartID(result_part_id.getInt(1));
                part_id.setPartNumber(result1.getInt(1));
                part_id.setQuantity(result3.getInt(1));
                listOfInventories.add(part_id);
            }
/**
 System.out.println("part_id\t " + "part_number\t " + "quantity");

 for (int j = 0; j < listOfInventories.size(); j++) {
 System.out.println(listOfInventories.get(j).displayInventoty());
 }
 **/
            ArrayList<Part> workOrder = new ArrayList<Part>();
            ArrayList<String> columns = new ArrayList<String>();



            Scanner intReader = new Scanner(System.in);
            String option = "";
            Scanner strReader = new Scanner(System.in);
            do {

                Part part  = new Part();

                System.out.println("Choose the part you need for kit: ");
                System.out.println("part_id: ");
                int part_id = intReader.nextInt();
                part.setPartID(part_id);

                int part_number = listOfInventories.get(part_id).getPartNumber();
                part.setPartNumber(part_number);

                System.out.println("quantity: ");
                int quantity = intReader.nextInt();
                part.setQuantity(quantity);

                workOrder.add(part);

                System.out.println("Do you want to add another part? Y or N");
                option = strReader.nextLine();

            } while (option.equals("Y"));

            System.out.println("part_id\t " + "part_number\t " + "quantity");

            for (int j = 0; j < workOrder.size(); j++) {
                System.out.println(workOrder.get(j).displayInventoty());
            }

            System.out.println("Please give your kit a name: ");
            String orderName = strReader.nextLine();
            ArrayList<String> rowForName = new ArrayList<String>();
            rowForName.add(orderName);
            rowForName.add("Y");
            rowForName.add("N");
            rowForName.add("N");
            testDB.insert("workOrders", null, rowForName);


            columns.add("parts_id INT");
            columns.add("part_number INT");
            columns.add("quantity INT");

            int workOrderTable = testDB.createTable(orderName, columns);

            for (int i = 0; i < workOrder.size(); i++) {
                ArrayList<String> rows = new ArrayList<String>();
                rows.add(String.valueOf(workOrder.get(i).getPartID()));
                rows.add(String.valueOf(workOrder.get(i).getPartNumber()));
                rows.add(String.valueOf(workOrder.get(i).getQuantity()));
                testDB.insert(orderName, null, rows);
            }

        }


        catch(SQLException e){
            e.printStackTrace();
        }

    }


}
