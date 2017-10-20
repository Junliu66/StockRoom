/**
 * Created by zhangJunliu on 10/10/17.
 */
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;

/**
 * Created by zhangJunliu on 10/10/17.
 */
public class Purchasing {

    public static void purchasing() {

        DBHandler testDB = new DBHandler();
        Scanner scanner = new Scanner(System.in);

        //listOfLowQuantity();
        ArrayList<Part> listOfLowQuantity = listOfLowQuantity();
        //workOrderOutOfStock();
        System.out.println("\nParts not work order but are at risk:");
        listOfLowQuantityNotWorkOrder(listOfLowQuantity);
    }

    public static ArrayList<Part> listOfLowQuantity() {
        ArrayList<Part> listOfLowQuantity = new ArrayList<Part>();
        DBHandler testDB = new DBHandler();
        ResultSet result_part_id = testDB.select("stockroomdb.PARTS", "parts_id", new ArrayList<String>());

        ResultSet result_part_number = testDB.select("stockroomdb.PARTS", "part_number", new ArrayList<String>());
        //ResultSet result2 = testDB.select("stockroomdb.PARTS", "part_description", new ArrayList<String>());
        ResultSet result_stockroom_quantity = testDB.select("stockroomdb.STOCKROOM", "quantity", new ArrayList<String>());
        ResultSet result_part_low_quantity = testDB.select("stockroomdb.PARTS", "low_quantity", new ArrayList<String>());

        // ###################################################################################
        /* Stefano : These are queries I made up for your tables. I realized that you had the two tables switched. */
        // ###################################################################################
        /*

        // ###################################################################################
        // TABLE ONE - (This one shows all the parts that do not have enough inventory to fill the current work orders)
        // ###################################################################################
        ResultSet table1_parts_id = testDB.query("SELECT DISTINCT p.parts_id FROM stockroomdb.PARTS AS p JOIN stockroomdb.STOCKROOM AS s ON p.parts_id = s.parts_id JOIN stockroomdb.ORDER_ITEMS AS oi ON s.parts_id = oi.parts_id WHERE s.quantity < (oi.amount_needed - oi.amount_filled);");
        ResultSet table1_part_description = testDB.query("SELECT DISTINCT p.part_description FROM stockroomdb.PARTS AS p JOIN stockroomdb.STOCKROOM AS s ON p.parts_id = s.parts_id JOIN stockroomdb.ORDER_ITEMS AS oi ON s.parts_id = oi.parts_id WHERE s.quantity < (oi.amount_needed - oi.amount_filled);");
        ResultSet table1_part_vendor = testDB.query("SELECT DISTINCT p.vendor FROM stockroomdb.PARTS AS p JOIN stockroomdb.STOCKROOM AS s ON p.parts_id = s.parts_id JOIN stockroomdb.ORDER_ITEMS AS oi ON s.parts_id = oi.parts_id WHERE s.quantity < (oi.amount_needed - oi.amount_filled);");
        ResultSet table1_order_missing_quantity = testDB.query("SELECT s.quantity - SUM(oi.amount_needed - oi.amount_filled) FROM stockroomdb.PARTS AS p JOIN stockroomdb.STOCKROOM AS s ON p.parts_id = s.parts_id JOIN stockroomdb.ORDER_ITEMS AS oi ON s.parts_id = oi.parts_id WHERE s.quantity < (oi.amount_needed - oi.amount_filled);");

        // ###################################################################################
        //TABLE TWO - (This one is similar to your original first table, this one shows all the parts who's quantity in the stockroom is less than the part's "low_quantity")
        // ###################################################################################
        ResultSet table2_parts_id = testDB.query("SELECT p.parts_id FROM stockroomdb.PARTS AS p JOIN stockroomdb.STOCKROOM AS s ON p.parts_id = s.parts_id WHERE s.quantity < p.low_quantity;");
        ResultSet table2_part_description = testDB.query("SELECT p.part_description FROM stockroomdb.PARTS AS p JOIN stockroomdb.STOCKROOM AS s ON p.parts_id = s.parts_id WHERE s.quantity < p.low_quantity;");
        ResultSet table2_part_vendor = testDB.query("SELECT p.vendor FROM stockroomdb.PARTS AS p JOIN stockroomdb.STOCKROOM AS s ON p.parts_id = s.parts_id WHERE s.quantity < p.low_quantity;");
        ResultSet table2_stockroom_quantity = testDB.query("SELECT s.quantity FROM stockroomdb.PARTS AS p JOIN stockroomdb.STOCKROOM AS s ON p.parts_id = s.parts_id WHERE s.quantity < p.low_quantity;");
        ResultSet table2_part_low_quantity = testDB.query("SELECT p.low_quantity FROM stockroomdb.PARTS AS p JOIN stockroomdb.STOCKROOM AS s ON p.parts_id = s.parts_id WHERE s.quantity < p.low_quantity;");
        */

        try {
            result_part_id.beforeFirst();
            result_part_number.beforeFirst();
            result_stockroom_quantity.beforeFirst();
            result_part_low_quantity.beforeFirst();

            //Map order1 = new HashMap();
            //Map order2 = new HashMap();


            while(result_part_id.next() && result_part_number.next() && result_stockroom_quantity.next() && result_part_low_quantity.next()){
                Part part_id = new Part();
                if (result_stockroom_quantity.getInt(1) <= result_part_low_quantity.getInt(1)) {
                    part_id.setPartID(result_part_id.getInt(1));
                    part_id.setPartNumber(result_part_number.getInt(1));
                    part_id.setQuantity(result_stockroom_quantity.getInt(1));
                    part_id.setLowQuantity(result_part_low_quantity.getInt(1));

                    listOfLowQuantity.add(part_id);


                }
            }

            System.out.println("Parts are in low quantity:");
            System.out.println(String.format("%-15s%-15s%-15s%-15s", "part_id", "part_number", "quantity", "low_quantity"));

            for (int j = 0; j < listOfLowQuantity.size(); j++) {
                System.out.println(listOfLowQuantity.get(j).displayInventoryWithLowQuantity());

            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return listOfLowQuantity;

    }

    /**
     public static ArrayList<Part> workOrderOutOfStock() {
     ArrayList<Part> workOrderOutOfStock = new ArrayList<Part>();

     DBHandler testDB = new DBHandler();
     ResultSet result_part_id = testDB.select("stockroomdb.PARTS", "parts_id", new ArrayList<String>());

     ResultSet result1 = testDB.select("stockroomdb.PARTS", "part_number", new ArrayList<String>());
     //ResultSet result2 = testDB.select("stockroomdb.PARTS", "part_description", new ArrayList<String>());
     ResultSet result3 = testDB.select("stockroomdb.STOCKROOM", "quantity", new ArrayList<String>());

     ResultSet orderNames = testDB.select("stockroomdb.workOrders", "name", new ArrayList<String>());
     String orderName = "stockroom.";
     try {
     result_part_id.beforeFirst();
     result1.beforeFirst();
     result3.beforeFirst();
     orderNames.beforeFirst();
     ArrayList<Part> listOfInventories = new ArrayList<Part>();

     while (result_part_id.next() && result1.next() && result3.next()) {
     Part part_id = new Part();
     part_id.setPartID(result_part_id.getInt(1));
     part_id.setPartNumber(result1.getInt(1));
     part_id.setQuantity(result3.getInt(1));
     listOfInventories.add(part_id);
     }

     while (orderNames.next()) {
     orderName = orderName + orderNames.getString(1);
     ResultSet order = testDB.select(orderName, "part_id", new ArrayList<String>());
     order.beforeFirst();

     while(order.next()) {
     for (int j = 0; j < listOfInventories.size(); j++) {
     if (order.getInt(1) == listOfInventories.get(j).getPartID()) {
     workOrderOutOfStock.add(listOfInventories.get(j));
     }
     }
     }
     }
     }

     catch(SQLException e){
     e.printStackTrace();
     }

     return workOrderOutOfStock;
     }
     **/

    /**
     *
     * @param listOfLowQuantity
     * @return
     */

    public static ArrayList<Part> listOfLowQuantityNotWorkOrder(ArrayList<Part> listOfLowQuantity) {
        ArrayList<Part> listOfLowQuantityNotWorkOrder = listOfLowQuantity;

        DBHandler testDB = new DBHandler();

        ResultSet order = testDB.select("stockroomdb.ORDER_ITEMS", "id", new ArrayList<String>());

        // ###################################################################################
        /* Stefano : These are queries I made up for your second table */
        // ###################################################################################
        /*
        ResultSet result_parts_id = testDB.query("SELECT p.parts_id FROM stockroomdb.PARTS AS p JOIN stockroomdb.STOCKROOM AS s ON p.parts_id = s.parts_id WHERE s.quantity < p.low_quantity;");

        */

        try{
            order.beforeFirst();

            while (order.next()) {
                    //System.out.println(order.getInt(1));
                    for (int j = 0; j < listOfLowQuantityNotWorkOrder.size(); j++) {
                        if (order.getInt(1) == (listOfLowQuantityNotWorkOrder.get(j).getPartID())) {
                            listOfLowQuantityNotWorkOrder.remove(j);
                        }
                    }
                }

        }
        catch(SQLException e){
            e.printStackTrace();
        }

        System.out.println(String.format("%-15s%-15s%-15s%-15s", "part_id", "part_number", "quantity", "low_quantity"));

        for (int j = 0; j < listOfLowQuantityNotWorkOrder.size(); j++) {
            //if (listOfLowQuantity.get(j).getQuantity() == 0)
            System.out.println(listOfLowQuantityNotWorkOrder.get(j).displayInventoryWithLowQuantity());

        }

        return listOfLowQuantityNotWorkOrder;

    }
}
