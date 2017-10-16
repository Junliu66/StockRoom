import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class WorkOrder {

    DBHandler stockroomDB = new DBHandler();
    Scanner reader = new Scanner(System.in);

    public void viewMenu() {
        System.out.println("=============================");
        System.out.println("======== WORK ORDERS ========");
        System.out.println("=============================\n\n");
        System.out.println("What would you like to do?");
        System.out.println("----------------------------------------");
        System.out.println("[1] View Existing Work Orders");
        System.out.println("[2] Create New Work Order");
        System.out.println("[3] Kit Work Order");
        System.out.println("[4] Start Building Work Order");
        System.out.println("[5] Finish Building Work Order");
        System.out.println("[6] Create Bill of Materials for New Product");
        System.out.print("\n Enter number: ");
        int option = reader.nextInt();

        switch (option) {
            case 1: viewWorkOrders();
                    break;
            case 2: createWorkOrder();
                    break;
            case 3: kitWorkOrder();
                    break;
            case 4: buildWorkOrder();
                    break;
            case 5: completeWorkOrder();
                    break;
            case 6: newProductBOM();
                    break;
            default: System.out.println("Invalid answer.");
                    break;
        }
    }

    private void viewWorkOrders() {
        ResultSet orderID = stockroomDB.select("stockroomdb.WORKORDERS", "order_id", new ArrayList<>());
        ResultSet productName = stockroomDB.select("stockroomdb.PRODUCTS AS p JOIN stockroomdb.WORKORDERS AS wo ON p.product_id = wo.product_id", "product_name", new ArrayList<>());
        ResultSet quantity = stockroomDB.select( "stockroomdb.WORKORDERS", "quantity", new ArrayList<>());
        ResultSet status = stockroomDB.select( "stockroomdb.WORKORDERS", "status", new ArrayList<>());
        try{
            orderID.beforeFirst();
            productName.beforeFirst();
            quantity.beforeFirst();
            status.beforeFirst();

            System.out.println("=======================================================================================");
            System.out.println("|\tORDER ID\t|\tPRODUCT NAME\t\t\t|\tQUANTITY\t|\tSTATUS\t|");
            System.out.println("=======================================================================================");
            while(orderID.next()){
                productName.next();
                quantity.next();
                status.next();

                System.out.println("|\t\t" + orderID.getInt(1) + "\t\t|\t" + productName.getString(1) + "\t\t\t|\t" +  quantity.getInt(1) +  "\t|\t" + status.getString(1) +  "\t|");
            }
            System.out.println("---------------------------------------------------------------------------------------");
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    private void createWorkOrder() {
        System.out.println(" create ");
    }

    private void kitWorkOrder() {
        System.out.println(" kit ");
    }

    private void buildWorkOrder() {
        System.out.println(" build ");
    }

    private void completeWorkOrder() {
        System.out.println(" finish ");
    }

    private void newProductBOM() {
        System.out.println(" new ");
    }
}

