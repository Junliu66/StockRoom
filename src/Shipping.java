import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.*;

/*
Create a command line menu for Shipping that shows a list of the available completed work orders
They can type the name (or select the number) of the completed work order, then be prompted to ship? y/n"
*/
public class Shipping {
    public static void main(String[] args) {
        displayShipping();
    }

    public static void displayShipping() {
        DBHandler stockroomdb = new DBHandler();

        ArrayList<String> conditions = new ArrayList<>();
        conditions.add("status = 'COMPLETED'");
        ResultSet id_and_quantity = stockroomdb.select("stockroomdb.WORKORDERS", "order_id, quantity", conditions);
        // get product names from PRODUCTS table
        conditions = new ArrayList<>();
        ResultSet product_name = stockroomdb.query("SELECT p.product_name FROM PRODUCTS AS p JOIN WORKORDERS AS oi ON p.product_id = oi.product_id WHERE status = 'COMPLETED'");

        System.out.println("Completed work orders:");

        System.out.println("=============================================================================");
        System.out.printf("||%-10s |%-40s |%19s||", "Order ID", "PRODUCT NAME", "Quantity  ");
        System.out.println("\n=============================================================================");

        try {
            id_and_quantity.beforeFirst();
            product_name.beforeFirst();
            while (id_and_quantity.next()) {
                product_name.next();
                System.out.printf("||%-10d |%-40s |%19d||\n", id_and_quantity.getInt(1), product_name.getString(1), id_and_quantity.getInt(2));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Scanner user_input = new Scanner(System.in);
        System.out.println("Please enter your Order ID to be shipped: ");
        int orderId = user_input.nextInt();

        HashMap<String, Object> updates = new HashMap<>();
        updates.put("status", "SHIPPED");
        updates.put("date_shipped", "NOW()");
        ArrayList<Object[]> searchConditions = new ArrayList<>();
        Object[] cond1 = {"order_id", "=", orderId};
        searchConditions.add(cond1);
        stockroomdb.update("stockroomdb.WORKORDERS", updates, searchConditions);


    } //TODO: set order to "shipped" and add time-stamp.
}