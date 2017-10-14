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
    int atRisk = 10;

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
                    listOfLowQuantity.add(part_id);

                }
            }

            System.out.println("Parts are in low quantity:");
            System.out.println("part_id\t " + "part_number\t " + "quantity");

            for (int j = 0; j < listOfLowQuantity.size(); j++) {
                System.out.println(listOfLowQuantity.get(j).displayInventoty());

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

    public static ArrayList<Part> listOfLowQuantityNotWorkOrder(ArrayList<Part> listOfLowQuantity) {
        ArrayList<Part> listOfLowQuantityNotWorkOrder = listOfLowQuantity;

        DBHandler testDB = new DBHandler();

        ResultSet orderNames = testDB.select("stockroomdb.workOrders", "kitname", new ArrayList<String>());
        try {
            orderNames.beforeFirst();

            while (orderNames.next()) {
                String orderName = "stockroomdb.";
                orderName = orderName + orderNames.getString(1);
                ResultSet order = testDB.select(orderName, "parts_id", new ArrayList<String>());
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
        }
        catch(SQLException e){
            e.printStackTrace();
        }

        System.out.println("part_id\t " + "part_number\t " + "quantity");

        for (int j = 0; j < listOfLowQuantityNotWorkOrder.size(); j++) {
            //if (listOfLowQuantity.get(j).getQuantity() == 0)
            System.out.println(listOfLowQuantityNotWorkOrder.get(j).displayInventoty());

        }

        return listOfLowQuantityNotWorkOrder;

    }
}
