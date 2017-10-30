/**
 * Created by zhangJunliu on 10/10/17.
 */
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by zhangJunliu on 10/10/17.
 */
public class Purchasing {

    public static void purchasing() {

        System.out.println("=============================");
        System.out.println("======== Purchasing ========");
        System.out.println("=============================\n\n");
        System.out.println("What you want to do?");
        System.out.println("----------------------------------------");
        System.out.println("[1] View Parts Are Missing From Work Orders");
        System.out.println("[2] Parts are in low quantity");

        Scanner reader = new Scanner(System.in);
        int option = reader.nextInt();

        switch (option) {
            case 1:
                outOfStock();
                break;
            case 2:
                lowQuantity();
        }
    }

    public static ArrayList<Part> outOfStock() {

        ArrayList<Part> outOfStockList = new ArrayList<>();
        DBHandler testDB = new DBHandler();

        ResultSet table1_parts_id = testDB.query("SELECT DISTINCT p.parts_id FROM stockroomdb.PARTS AS p JOIN stockroomdb.STOCKROOM AS s ON p.parts_id = s.parts_id JOIN stockroomdb.ORDER_ITEMS AS oi ON s.parts_id = oi.parts_id WHERE s.quantity < (oi.amount_needed - oi.amount_filled);");
        ResultSet table1_part_description = testDB.query("SELECT DISTINCT p.part_description FROM stockroomdb.PARTS AS p JOIN stockroomdb.STOCKROOM AS s ON p.parts_id = s.parts_id JOIN stockroomdb.ORDER_ITEMS AS oi ON s.parts_id = oi.parts_id WHERE s.quantity < (oi.amount_needed - oi.amount_filled);");
        ResultSet table1_part_vendor = testDB.query("SELECT DISTINCT p.vendor FROM stockroomdb.PARTS AS p JOIN stockroomdb.STOCKROOM AS s ON p.parts_id = s.parts_id JOIN stockroomdb.ORDER_ITEMS AS oi ON s.parts_id = oi.parts_id WHERE s.quantity < (oi.amount_needed - oi.amount_filled);");
        ResultSet table1_order_missing_quantity = testDB.query("SELECT s.quantity - SUM(oi.amount_needed - oi.amount_filled) FROM stockroomdb.PARTS AS p JOIN stockroomdb.STOCKROOM AS s ON p.parts_id = s.parts_id JOIN stockroomdb.ORDER_ITEMS AS oi ON s.parts_id = oi.parts_id WHERE s.quantity < (oi.amount_needed - oi.amount_filled);");

        try {
            table1_parts_id.beforeFirst();
            table1_part_description.beforeFirst();
            table1_part_vendor.beforeFirst();
            table1_order_missing_quantity.beforeFirst();

            System.out.println("=====================================================================================================================================================");
            System.out.printf("||%-10s |%-90s |%-20s |%-15s ||", "Part ID", "           Description", "      Vendor ", "  Missing Quantity");
            System.out.println("\n=====================================================================================================================================================");
            while (table1_parts_id.next() && table1_part_description.next() && table1_part_vendor.next() && table1_order_missing_quantity.next()) {

                Part outOfStock = new Part();
                outOfStock.setPartID(table1_parts_id.getInt(1));
                outOfStock.setDescription(table1_part_description.getString(1));
                outOfStock.setVendor(table1_part_vendor.getString(1));
                outOfStock.setMissingQuantity(table1_order_missing_quantity.getInt(1));
                outOfStockList.add(outOfStock);

                System.out.printf("||%-10d |%-90s |%-20s |%-15s    ||\n", table1_parts_id.getInt(1), table1_part_description.getString(1), table1_part_vendor.getString(1), table1_order_missing_quantity.getInt(1));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return outOfStockList;

    }

    public static void lowQuantity() {

        DBHandler testDB = new DBHandler();
        ResultSet table2_parts_id = testDB.query("SELECT p.parts_id FROM stockroomdb.PARTS AS p JOIN stockroomdb.STOCKROOM AS s ON p.parts_id = s.parts_id WHERE s.quantity < p.low_quantity;");
        ResultSet table2_part_description = testDB.query("SELECT p.part_description FROM stockroomdb.PARTS AS p JOIN stockroomdb.STOCKROOM AS s ON p.parts_id = s.parts_id WHERE s.quantity < p.low_quantity;");
        ResultSet table2_part_vendor = testDB.query("SELECT p.vendor FROM stockroomdb.PARTS AS p JOIN stockroomdb.STOCKROOM AS s ON p.parts_id = s.parts_id WHERE s.quantity < p.low_quantity;");
        ResultSet table2_stockroom_quantity = testDB.query("SELECT s.quantity FROM stockroomdb.PARTS AS p JOIN stockroomdb.STOCKROOM AS s ON p.parts_id = s.parts_id WHERE s.quantity < p.low_quantity;");
        ResultSet table2_part_low_quantity = testDB.query("SELECT p.low_quantity FROM stockroomdb.PARTS AS p JOIN stockroomdb.STOCKROOM AS s ON p.parts_id = s.parts_id WHERE s.quantity < p.low_quantity;");

        try {
            table2_parts_id.beforeFirst();
            table2_part_description.beforeFirst();
            table2_part_vendor.beforeFirst();
            table2_stockroom_quantity.beforeFirst();
            table2_part_low_quantity.beforeFirst();

            System.out.println("================================================================================================================================================================");
            System.out.printf("||%-10s |%-90s |%-20s |%-15s |%-15s ||", "Part ID", "           Description", "   Vendor    ", "  Quantity", "Low Quantity");
            System.out.println("\n================================================================================================================================================================");

            while (table2_parts_id.next() && table2_part_description.next() && table2_part_vendor.next() && table2_stockroom_quantity.next() && table2_part_low_quantity.next()) {
                System.out.printf("||%-10d |%-90s |%-20s |%-15s |%-15s ||\n", table2_parts_id.getInt(1), table2_part_description.getString(1), table2_part_vendor.getString(1), table2_stockroom_quantity.getInt(1), table2_part_low_quantity.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


