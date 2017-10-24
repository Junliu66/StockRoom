import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Created by zhangJunliu on 10/19/17.
 */
public class Overview {


    public static void overView() {
        System.out.println("=============================");
        System.out.println("======== Over View ========");
        System.out.println("=============================\n\n");
        System.out.println("What you want to do?");
        System.out.println("----------------------------------------");
        System.out.println("[1] View Completed Orders");
        System.out.println("[2] View Building Orders");
        System.out.println("[3] View Parts Are Missing From Work Orders");

        Scanner reader = new Scanner(System.in);
        int option = reader.nextInt();

        switch (option) {
            case 1:
                orderCompleted();
                break;
            case 2:
                buildingOrders();
                break;
            case 3:
                Purchasing.outOfStock();
        }
    }

    public static void orderCompleted() {

        DBHandler testDB = new DBHandler();
        ResultSet table1_order_id = testDB.query("SELECT order_id FROM stockroomdb.WORKORDERS WHERE status = 'COMPLETED';");
        ResultSet table1_product_name = testDB.query("SELECT p.product_name FROM stockroomdb.PRODUCTS AS p JOIN stockroomdb.WORKORDERS AS wo ON p.product_id = wo.product_id WHERE status = 'COMPLETED';");
        ResultSet table1_date_completed = testDB.query("SELECT date_completed FROm stockroomdb.WORKORDERS WHERE status = 'COMPLETED';");
        try {
            table1_order_id.beforeFirst();
            table1_product_name.beforeFirst();
            table1_date_completed.beforeFirst();

            System.out.println("=====================================================================================");
            System.out.printf("||%-10s |%-40s |%-19s||", "Order ID", "              PRODUCT NAME", "       Date Completed       ");
            System.out.println("\n=====================================================================================");
            while (table1_order_id.next() && table1_product_name.next() && table1_date_completed.next()) {

                System.out.printf("|%-11d |%-40s |%20tc|\n", table1_order_id.getInt(1), table1_product_name.getString(1), table1_date_completed.getTimestamp(1));
            }
            System.out.println("=====================================================================================");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        }


        public static void buildingOrders() {


            DBHandler testDB = new DBHandler();
            ResultSet table2_order_id = testDB.query("SELECT order_id FROM stockroomdb.WORKORDERS WHERE status = 'BUILDING';");
            ResultSet table2_product_name = testDB.query("SELECT p.product_name FROM stockroomdb.PRODUCTS AS p JOIN stockroomdb.WORKORDERS AS wo ON p.product_id = wo.product_id WHERE status = 'BUILDING';");
            ResultSet table2_date_building = testDB.query("SELECT date_building FROM stockroomdb.WORKORDERS WHERE status = 'BUILDING';");

            try {
                table2_order_id.beforeFirst();
                table2_product_name.beforeFirst();
                table2_date_building.beforeFirst();

                System.out.println("=====================================================================================");
                System.out.printf("||%-10s |%-40s |%-19s||", "Order ID", "              PRODUCT NAME", "       Date Building       ");
                System.out.println("\n=====================================================================================");

                while (table2_order_id.next() && table2_product_name.next() && table2_date_building.next()) {
                    System.out.printf("|%-11d |%-40s |%20tc|\n", table2_order_id.getInt(1), table2_product_name.getString(1), table2_date_building.getTimestamp(1));
                }
                System.out.println("=====================================================================================");

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
}


