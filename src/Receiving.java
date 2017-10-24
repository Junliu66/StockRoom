import java.lang.String;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
/*
Create the command line menu for people who receives items.
bring up a prompt for user to enter in the part number the quantity.


*/

public class Receiving {


    public static void main(String[] args) {
        displayReceiving();
    }

    public static void displayReceiving(){
        DBHandler stockroomdb = new DBHandler();
        Scanner user_input = new Scanner(System.in);
        System.out.println("How many items do you received? Please enter the number: ");
        int totalItems;
        totalItems = user_input.nextInt();
        int partIDNumber;
        int partQuantity;
        int[] partID = new int [totalItems];
        int[] quantity = new int[totalItems];
        for (int i = 0; i < totalItems; i++) {

            System.out.println("Please enter the received item Part ID: ");
            partIDNumber = Integer.parseInt(user_input.next());
            partID[i] = partIDNumber;

            System.out.println("Please enter how many you receive it?");
            partQuantity = user_input.nextInt();
            quantity[i] = partQuantity;

//            ResultSet orderID = stockroomdb.query ("SELECT id, order_id FROM stockroomdb.ORDER_ITEMS WHERE " + partIDNumber + " = " +
//                    "parts_id AND amount_needed > amount_filled;");
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

                while(orderID.next()){
                    productName.next();
                    quantityNeeded.next();
                    System.out.println("=============================================================================");
                    System.out.printf("||%-10s |%-40s |%19s||", "Order ID", "              PRODUCT NAME", "Amount Needed ");
                    System.out.println("\n=============================================================================");
                    int uid = orderID.getInt(1);
                    int orderId = orderID.getInt(2);
                    System.out.printf("|%11d |%-40s |%20d|\n", orderId, productName.getString(1), quantityNeeded.getInt(1));
                    System.out.println("-----------------------------------------------------------------------------");
                    System.out.println("Do you want to fill this kit? y or n");
                    Scanner console1= new Scanner(System.in);
                    String choice = "y";
                    if (console1.next().equalsIgnoreCase(choice)) {
                        int giveStock = 0;
                        if (partQuantity >= quantityNeeded.getInt(1)) {
                            giveStock = quantityNeeded.getInt(1);
                        } else {
                            giveStock = partQuantity;
                        }
                        HashMap<String, Object> updates = new HashMap<>();
                        // TODO it is not clear to me what should be the value of amount_filled here
                        updates.put("amount_filled", "190");
                        ArrayList<Object[]> conds = new ArrayList<>();
                        Object[] cond = {"id","=",uid};
                        conds.add(cond);
                        stockroomdb.update("stockroomdb.ORDER_ITEMS", updates, conds);
                        partQuantity -= quantityNeeded.getInt(1);
                    }
                }

            }
            catch(SQLException e){
                e.printStackTrace();
            }

        }
    }
}