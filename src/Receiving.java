import java.lang.String;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
/*
Create the command line menu for people who receives items.
bring up a prompt for user to enter in the part number the quantity.

*/

public class Receiving {


        public static void main(String[] args) {
            DBHandler stockroomdb = new DBHandler();
            Scanner user_input = new Scanner(System.in);
            System.out.println("How many items you received today, please type in the number: ");
            int totalItems;
            totalItems = user_input.nextInt();
            String partNumber=null;
            int partQuantity;
            String[] partID = new String[totalItems];
            int[] quantity = new int[totalItems];
            for (int i = 0; i < totalItems; i++) {

                System.out.println("Please enter your partNumber: ");
                partNumber = user_input.next();
                partID[i] = partNumber;

                System.out.println("Please enter how many you receive it?");
                partQuantity = user_input.nextInt();
                quantity[i] = partQuantity;

                //Do you want fill this in the kit? y/n
                //show detail of the items
                ResultSet orderID = stockroomdb.executeQuery ("SELECT order_id FROM stockroomdb.ORDER_ITEMS WHERE " + partNumber + " = " +
                        "parts_id AND amount_needed > amount_filled;");
                ResultSet productName = stockroomdb.executeQuery("SELECT p.product_name FROM PRODUCTS AS p JOIN ORDER_ITEMS as oi ON p.product_id = oi.product_id WHERE " + partNumber + " = parts_id AND amount_needed > amount_filled;");
                ResultSet quantityNeeded = stockroomdb.executeQuery("SELECT sum (amount_needed - amount_filled) AS amount FROM ORDER_ITEMS WHERE " + partNumber + " = parts_id AND amount_needed > amount_filled;");

                try {
                    orderID.beforeFirst();
                    productName.beforeFirst();
                    quantityNeeded.beforeFirst();

                    System.out.println(orderID.getInt(1) + "\t" + productName.getString(1) + "\t" + quantityNeeded.getInt(1));
                }
                catch(SQLException e){
                    e.printStackTrace();
                }
                //How many do you want fill in?
            }

            System.out.println("Here is list of the ordered you receive this time, please double check.\n");
            System.out.println("================================");

            System.out.printf("%5s %15s\n","partNumber", "Quantity");
            for (int i = 0; i < totalItems; i++) {
                System.out.printf("%5s %15s\n",partID[i], quantity[i]);
            }
            System.out.println("================================\n");
            System.out.println("No change enter 1.\nChange enter 2.");
        }
    }