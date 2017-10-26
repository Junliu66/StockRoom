import java.util.Scanner;

/**
 * Creates an object of Stockroom. User can:
 * Stockroom assistant can View Inventory;
 * Engineering can view/create work orders;
 * Purchaser can purchase;
 * Worker can receive orders of shipping;
 * People can check the Shipping
 * Supervisor can overview all above;
 * @author Chunlei
 */

public class StockroomApp {

    public static void main(String[] args) {
        Scanner console = new Scanner(System.in);
        boolean runProgram = true;
        System.out.print("Welcome to The Strockroom App main menu. ");
        while (runProgram == true) {
            runProgram = displayMenu(console);
        }
    }

    /**
     * Display the menu and calls the corresponding method.
     */

    public static boolean displayMenu(Scanner console) {
        System.out.println("What would you like to do?\n(Choose a number)");
        System.out.print("[1] View Inventory\n[2] Create Orders\n[3] Purchase\n[4] Receive Orders\n[5]Shipping\n[6]Overview\n[7] Quit\n> ");
        boolean runProgram = true;
        switch (console.nextLine()) {
            case "1":
                System.out.println("\n[1] View Inventory \n");
                viewInventory();
                break;
            case "2":
                System.out.println("\n[2] Create Orders\n");
                createOrders();
                break;
            case "3":
                System.out.println("\n[3] Purchase\n");
                purchase();
                break;
            case "4":
                System.out.println("\n[4] Receive Orders");
                receiveOrders();
                runProgram = false;
                break;
            case "5":
                System.out.println("\n[5] Shipping\n");
                shipping();
                break;
            case "6":
                System.out.println("\n[6] Overview\n");
                overview();
                break;
            case "7":
                System.out.println("\n[7] Thank you for using The Stockroom App.\n");
                runProgram = false;
                break;
            default:
                System.out.println("\nChoose a valid number.\n");
                break;
        }
        return runProgram;
    }

    // methods we need to work on
    private static void viewInventory() {
        System.out.print("Here is the Invetory of the Sotckroom: \n> ");
        Inventory.inventoryList();

    }

    private static void createOrders() {
        //System.out.println("which items you need to use: \n> ");
        //System.out.print("how many of it: \n> ");
        // TODO WorkOrder need to make createOrder method public
         WorkOrder.viewMenu();
    }

    private static void purchase() {
        System.out.print("Please fill in the table with the number of items you want to purchase: \n> ");
        Purchasing.purchasing();
    }

    private static void receiveOrders() {
        System.out.print("What items you received: \n> ");
        System.out.print("how many of it: \n> ");
    }

    private static void shipping() {
        System.out.print("Shipping items are: \n> ");
    }

    private static void overview() {
        System.out.print("Here is the Stockroom Overview: \n> ");
        Overview.overView();
    }
}