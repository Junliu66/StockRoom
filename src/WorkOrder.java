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

            System.out.println("=============================================================================");
            System.out.printf("||%-8s |%-40s |%-9s |%-10s||", "ORDER ID", "              PRODUCT NAME", " QUANTITY", "  STATUS");
            System.out.println("\n=============================================================================");

            while(orderID.next()){
                productName.next();
                quantity.next();
                status.next();

                System.out.printf("|%9d |%-40s |%9d |%11s|\n", orderID.getInt(1), productName.getString(1), quantity.getInt(1), status.getString(1));
            }
            System.out.println("-----------------------------------------------------------------------------");
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    private void createWorkOrder() {
        ResultSet productID = stockroomDB.select("stockroomdb.PRODUCTS", "product_id", new ArrayList<>());
        ResultSet productName = stockroomDB.select("stockroomdb.PRODUCTS", "product_name", new ArrayList<>());
        ResultSet productCreated = stockroomDB.select("stockroomdb.PRODUCTS", "date_created", new ArrayList<>());

        try{
            productID.beforeFirst();
            productName.beforeFirst();
            productCreated.beforeFirst();

            System.out.println("=============================================================================");
            System.out.printf("||%-10s |%-40s |%19s||", "Product ID", "              PRODUCT NAME", " Date Created  ");
            System.out.println("\n=============================================================================");

            while (productID.next()) {
                productName.next();
                productCreated.next();

                System.out.printf("|%11d |%-40s |%20tD|\n", productID.getInt(1), productName.getString(1), productCreated.getDate(1));
            }
            System.out.println("-----------------------------------------------------------------------------");

            System.out.println("Select the product ID that you want to order: ");
            int chosenProductID = reader.nextInt();

            productID.beforeFirst();
            boolean productFound = false;

            while(productID.next()){
                if (chosenProductID == productID.getInt(1)){
                    productFound = true;
                    System.out.println("Product found. Please select a quantity: ");
                    int chosenQuantity = reader.nextInt();
                    enterNewWorkOrder(chosenProductID, chosenQuantity);
                }
            }
            if (!productFound)
                System.out.println("Product ID not found.");
        }

        catch(SQLException e){

            e.printStackTrace();
        }

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

    private void enterNewWorkOrder(int chosenProductID, int chosenQuantity) {
        String productID =  "";
        productID += chosenProductID;

        String quantity = "";
        quantity += chosenQuantity;

        ArrayList<String> columns = new ArrayList<String>();
        ArrayList<String> values = new ArrayList<String>();

        columns.add("product_id");
        columns.add("quantity");
        columns.add("status");
        columns.add("date_created");

        values.add(productID);
        values.add(quantity);
        values.add("CREATED");
        values.add("NOW()");

        stockroomDB.updateQuery("INSERT INTO stockroomdb.WORKORDERS (product_id, quantity, status, date_created) " +
                        "VALUES (" + productID + ", " + quantity + ", 'CREATED', NOW())");

        ResultSet newOrderID = stockroomDB.query("SELECT LAST_INSERT_ID();");
        String orderID = "";
        try {
            newOrderID.first();
            orderID += newOrderID.getInt(1);
        }
        catch(SQLException e){
            e.printStackTrace();
        }

        int addingWorkOrder = stockroomDB.updateQuery("INSERT INTO stockroomdb.ORDER_ITEMS (parts_id, product_id, order_id, amount_needed) " +
                "SELECT parts_id, product_id, '" + orderID + "', (quantity * " + quantity + ") " +
                "FROM stockroomdb.PRODUCT_BOM " +
                "WHERE product_id = " + chosenProductID + ";");

        System.out.println(addingWorkOrder);

    }
}

