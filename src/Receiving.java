import java.lang.String;
import java.util.Scanner;

public class Receiving {
        public static void main(String[] args) {
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
            }

            System.out.println("Here is list of the ordered you receive this time, please double check.\n");
            System.out.println("================================");

            System.out.printf("%3s %15s\n","partNumber", "Quantity");
            for (int i = 0; i < totalItems; i++) {
                System.out.printf("%3s %15s\n",partID[i], quantity[i]);
            }
            System.out.println("================================\n");
            System.out.println("No change enter 1.\nChange enter 2.");
        }
    }