import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by c on 10/28/17.
 */
public class ReceivingMenu {
    private DBHandler stockroomdb = new DBHandler();

    public void submit(int partIDNumber, int partQuantityReceived) {
        ArrayList<String> conditions = new ArrayList<>();
        conditions.add(partIDNumber + " = " + "parts_id");
        conditions.add("amount_needed > amount_filled");
        ResultSet orderID = stockroomdb.select("stockroomdb.ORDER_ITEMS", "id, order_id", conditions);

        ResultSet productName = stockroomdb.query("SELECT p.product_name FROM PRODUCTS AS p JOIN ORDER_ITEMS as oi ON p.product_id = oi.product_id WHERE " + partIDNumber + " = parts_id AND amount_needed > amount_filled;");
        ResultSet quantityNeeded = stockroomdb.query("SELECT (amount_needed - amount_filled) AS amount FROM ORDER_ITEMS WHERE " + partIDNumber + " = parts_id AND amount_needed > amount_filled;");

        try {
            orderID.beforeFirst();
            productName.beforeFirst();
            quantityNeeded.beforeFirst();

            while (orderID.next()) {
                productName.next();
                quantityNeeded.next();
                System.out.println("=============================================================================");
                System.out.printf("||%-10s |%-40s |%19s||", "Order ID", "              PRODUCT NAME", "Amount Needed ");
                System.out.println("\n=============================================================================");
                int uid = orderID.getInt(1);
                int orderId = orderID.getInt(2);
                int quantityNeededInt = quantityNeeded.getInt(1);
                System.out.printf("|%11d |%-40s |%20d|\n", orderId, productName.getString(1), quantityNeededInt);
                System.out.println("-----------------------------------------------------------------------------");
                System.out.println("Do you want to fill this kit? y or n");
                Scanner console = new Scanner(System.in);
                if (console.next().equalsIgnoreCase("y")) {
                    int amountLeftover = partQuantityReceived - quantityNeededInt;
                    if (amountLeftover <= 0) {
                        // receiving less than or equal amount of parts needed in current kit
                        HashMap<String, Object> updates = new HashMap<>();
                        updates.put("amount_filled", quantityNeededInt + partQuantityReceived);
                        ArrayList<Object[]> conds = new ArrayList<>();
                        Object[] cond = {"id", "=", uid};
                        conds.add(cond);
                        stockroomdb.update("stockroomdb.ORDER_ITEMS", updates, conds);
                        partQuantityReceived = 0;
                        // out of received parts so we break
                        break;
                    } else {
                        // receiving more parts than needed in current kit, get total quantity needed and set to quantity filled
                        ResultSet quantityNeededTotal = stockroomdb.query("SELECT amount_needed AS amount FROM ORDER_ITEMS WHERE id = " + uid);
                        quantityNeededTotal.next();
                        HashMap<String, Object> updates = new HashMap<>();
                        updates.put("amount_filled", quantityNeededTotal.getInt(1));
                        ArrayList<Object[]> conds = new ArrayList<>();
                        Object[] cond = {"id", "=", uid};
                        conds.add(cond);
                        stockroomdb.update("stockroomdb.ORDER_ITEMS", updates, conds);
                        // remove quantity stored in kit from partQuantityReceived
                        partQuantityReceived -= quantityNeededInt;
                    }
                }
            }
            // add leftovers to STOCKROOM
            if (partQuantityReceived > 0)
                stockroomdb.adjustPartQuantity(partIDNumber, partQuantityReceived);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
