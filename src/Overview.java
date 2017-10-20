import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.sql.Time;

/**
 * Created by zhangJunliu on 10/19/17.
 */
public class Overview {
    //ArrayList<Part> listOfFinishedOrders = new ArrayList<Part>();
    //ArrayList<Part> listOfOrdersInProgress = new ArrayList<Part>();
    ArrayList<Part> listOfPartsInShortage = new ArrayList<Part>();

    public static void overView() {
        orderStatus();

    }

    public static void orderStatus() {
        ArrayList<Part> listOfFinishedOrders = new ArrayList<Part>();
        ArrayList<Part> listOfOrdersInProgress = new ArrayList<Part>();

        DBHandler testDB = new DBHandler();
        ResultSet order_id = testDB.select("stockroomdb.WORKORDERS", "order_id", new ArrayList<String>());
        ResultSet status = testDB.select("stockroomdb.WORKORDERS", "status", new ArrayList<String>());
        ResultSet date_completed = testDB.select("stockroomdb.WORKORDERS", "date_completed", new ArrayList<String>());

        // ###################################################################################
        /* Stefano : These are queries I made up for the overview tables */
        // ###################################################################################
        /*

        // ###################################################################################
        // TABLE ONE - (This one shows all the Work Orders that are completed and ready to ship.)
        // ###################################################################################

        ResultSet table1_order_id = testDB.query("SELECT order_id FROM stockroomdb.WORKORDERS WHERE status = "COMPLETED";");
        ResultSet table1_product_name = testDB.query("SELECT p.product_name FROM stockroomdb.PRODUCTS AS p JOIN stockroomdb.WORKORDERS AS wo ON p.product_id = wo.product_id WHERE status = "COMPLETED";");
        ResultSet table1_date_completed = testDB.query("SELECT date_completed FROm stockroomdb.WORKORDERS WHERE status = "COMPLETED";");


        // ###################################################################################
        // TABLE TWO - (This one shows all the Work Orders that are currently being built.)
        // ###################################################################################

        ResultSet table2_order_id = testDB.query("SELECT order_id FROM stockroomdb.WORKORDERS WHERE status = "BUILDING";");
        ResultSet table2_product_name = testDB.query("SELECT p.product_name FROM stockroomdb.PRODUCTS AS p JOIN stockroomdb.WORKORDERS AS wo ON p.product_id = wo.product_id WHERE status = "BUILDING";");
        ResultSet table2_date_building = testDB.query("SELECT date_building FROM stockroomdb.WORKORDERS WHERE status = "BUILDING";");


        // ###################################################################################
        // TABLE THREE - (This one is pretty much the same as the Purchasing table, it says what parts are missing from WorkOrders.)
        // ###################################################################################

        ResultSet table3_parts_id = testDB.query("SELECT DISTINCT p.parts_id FROM stockroomdb.PARTS AS p JOIN stockroomdb.STOCKROOM AS s ON p.parts_id = s.parts_id JOIN stockroomdb.ORDER_ITEMS AS oi ON s.parts_id = oi.parts_id WHERE s.quantity < (oi.amount_needed - oi.amount_filled);");
        ResultSet table3_part_description = testDB.query("SELECT DISTINCT p.part_description FROM stockroomdb.PARTS AS p JOIN stockroomdb.STOCKROOM AS s ON p.parts_id = s.parts_id JOIN stockroomdb.ORDER_ITEMS AS oi ON s.parts_id = oi.parts_id WHERE s.quantity < (oi.amount_needed - oi.amount_filled);");
        ResultSet table3_part_vendor = testDB.query("SELECT DISTINCT p.vendor FROM stockroomdb.PARTS AS p JOIN stockroomdb.STOCKROOM AS s ON p.parts_id = s.parts_id JOIN stockroomdb.ORDER_ITEMS AS oi ON s.parts_id = oi.parts_id WHERE s.quantity < (oi.amount_needed - oi.amount_filled);");
        ResultSet table3_order_missing_quantity = testDB.query("SELECT s.quantity - SUM(oi.amount_needed - oi.amount_filled) FROM stockroomdb.PARTS AS p JOIN stockroomdb.STOCKROOM AS s ON p.parts_id = s.parts_id JOIN stockroomdb.ORDER_ITEMS AS oi ON s.parts_id = oi.parts_id WHERE s.quantity < (oi.amount_needed - oi.amount_filled);");

        */


        try {
            order_id.beforeFirst();
            status.beforeFirst();
            date_completed.beforeFirst();

            while (order_id.next() && status.next() && date_completed.next()) {
                if (status.getString(1).equals("COMPLETED")) {
                    Part order = new Part();
                    order.setOrderID(order_id.getInt(1));

                    //order.setDate(date_completed.getDate(1));

                    listOfFinishedOrders.add(order);

                }
                else if (status.getString(1).equals("KITTED") || status.getString(1).equals("CREATED")) {
                    Part order = new Part();
                    order.setOrderID((order_id.getInt(1)));
                    listOfOrdersInProgress.add(order);
                }


            }



        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println(String.format("\n%-10s%-15s", "kit#", "Date Done"));
        for (int i = 0; i < listOfFinishedOrders.size(); i++) {
            System.out.println(listOfFinishedOrders.get(i).displayOrderStatus());
        }

        System.out.println(String.format("%-10s%-15s", "kit#", "Date Started"));
        for (int i = 0; i < listOfOrdersInProgress.size(); i++) {
            System.out.println(listOfOrdersInProgress.get(i).displayOrderStatus());
        }

        }


}
