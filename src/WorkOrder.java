import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class WorkOrder {

    DBHandler stockroomDB = new DBHandler();
    Scanner reader = new Scanner(System.in);

    public void viewGUI(BorderPane root, Stage stage, TableView table) {

        VBox rVBox = new VBox();

        Button view = new Button("View Existing Work Orders");
        view.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                viewWorkOrdersGUI(root, stage);
                //viewWorkOrders();
            }
        });
        view.setPadding(new Insets(10, 10, 10, 10));
        view.setMinWidth(300);

        Button create = new Button("Create New Work Orders");
        create.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                createWorkOrderGUI(root, stage, table);
                //createWorkOrder();
            }
        });
        create.setPadding(new Insets(10, 10, 10, 10));
        create.setMinWidth(300);

        Button kit = new Button("Kit Work Orders");
        kit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                kitWorkOrderGUI(root, stage);
                //kitWorkOrder();
            }
        });
        kit.setPadding(new Insets(10, 10, 10, 10));
        kit.setMinWidth(300);

        Button build = new Button("Start Building Work Orders");
        build.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                buildWorkOrderGUI(root, stage);
                buildWorkOrder();
            }
        });
        build.setPadding(new Insets(10, 10, 10, 10));
        build.setMinWidth(300);

        Button complete = new Button("Finish Building Work Orders");
        complete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                completeWorkOrderGUI(root, stage);
                completeWorkOrder();
            }
        });
        complete.setPadding(new Insets(10, 10, 10, 10));
        complete.setMinWidth(300);

        Button product = new Button("Create New Product");
        product.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                newProductBOMGUI(root, stage);
                //createWorkOrder();
            }
        });
        product.setPadding(new Insets(10, 10, 10, 10));
        product.setMinWidth(300);

        Label workOrderTitle = new Label ("WORK ORDERS");
        workOrderTitle.setScaleX(2);
        workOrderTitle.setScaleY(2);
        workOrderTitle.setAlignment(Pos.CENTER);
        workOrderTitle.setPadding(new Insets(0,0, 20, 0));
        HBox title = new HBox();
        title.getChildren().add(workOrderTitle);
        title.setMaxWidth(300);
        title.setAlignment(Pos.CENTER);

        rVBox.getChildren().addAll(title, view, create, kit, build, complete, product);
        rVBox.setPadding(new Insets(100, 100, 100, 300));
        rVBox.setSpacing(10);
        root.setCenter(rVBox);
        stage.getScene().setRoot(root);
    }

    private void viewWorkOrdersGUI(BorderPane root, Stage stage) {

        VBox rVBox = new VBox();
        Label woTitle = new Label ("VIEW WORK ORDERS");
        MainMenu mainMenu = new MainMenu();
        ResultSet workOrders = stockroomDB.query("SELECT wo.order_id, p.product_name, wo.quantity, wo.status FROM stockroomdb.WORKORDERS AS wo JOIN stockroomdb.PRODUCTS AS p ON wo.product_id = p.product_id;");

        rVBox.getChildren().addAll(woTitle, mainMenu.displayTable(workOrders));
        root.setCenter(rVBox);
        stage.getScene().setRoot(root);
    }

    private void createWorkOrderGUI(BorderPane root, Stage stage, TableView table) {
        VBox rVBox = new VBox();
        Label woTitle = new Label ("CREATE WORK ORDERS");
        MainMenu mainMenu = new MainMenu();
        ResultSet products = stockroomDB.query("SELECT product_id AS 'Product ID', product_name AS 'Product Name', date_created AS 'Date Created' FROM stockroomdb.PRODUCTS;");
        VBox productTable = mainMenu.displayTable(products);
        rVBox.getChildren().addAll(woTitle, productTable);
        TableColumn numQuantity = new TableColumn("Quantity");
        TableColumn createButtons = new TableColumn("Select One");
        numQuantity.setMinWidth(60.0);
        createButtons.setMinWidth(60.0);

        numQuantity.setCellFactory(new Callback<TableColumn<Object, Boolean>, TableCell<Object, Boolean>>() {
            @Override public TableCell<Object, Boolean> call(TableColumn<Object, Boolean> param) {
                return new addCell(stage, mainMenu.getTable(), 1);
            }
        });

        createButtons.setCellFactory(new Callback<TableColumn<Object, Boolean>, TableCell<Object, Boolean>>() {
            @Override public TableCell<Object, Boolean> call(TableColumn<Object, Boolean> param) {
                return new addCell(stage, mainMenu.getTable(), 0);
            }
        });

        mainMenu.getTable().getColumns().add(numQuantity);
        mainMenu.getTable().getColumns().add(createButtons);
        root.setCenter(rVBox);
        stage.getScene().setRoot(root);
    }
    private void kitWorkOrderGUI(BorderPane root, Stage stage) {
        VBox rVBox = new VBox();
        Label woTitle = new Label ("KIT WORK ORDERS");
        MainMenu mainMenu = new MainMenu();
        ResultSet workOrders = stockroomDB.query("SELECT wo.order_id, p.product_name, wo.quantity, wo.status FROM stockroomdb.WORKORDERS AS wo JOIN stockroomdb.PRODUCTS AS p ON wo.product_id = p.product_id WHERE status = 'CREATED';");

        rVBox.getChildren().addAll(woTitle, mainMenu.displayTable(workOrders));
        root.setCenter(rVBox);
        stage.getScene().setRoot(root);
    }

    private void buildWorkOrderGUI(BorderPane root, Stage stage) {
        VBox rVBox = new VBox();
        Label woTitle = new Label ("BUILD WORK ORDERS");
        MainMenu mainMenu = new MainMenu();
        ResultSet workOrders = stockroomDB.query("SELECT wo.order_id, p.product_name, wo.quantity, wo.status FROM stockroomdb.WORKORDERS AS wo JOIN stockroomdb.PRODUCTS AS p ON wo.product_id = p.product_id WHERE status = 'KITTED';");

        rVBox.getChildren().addAll(woTitle, mainMenu.displayTable(workOrders));
        root.setCenter(rVBox);
        stage.getScene().setRoot(root);
    }

    private void completeWorkOrderGUI(BorderPane root, Stage stage) {
        VBox rVBox = new VBox();
        Label woTitle = new Label ("COMPLETE WORK ORDERS");
        MainMenu mainMenu = new MainMenu();
        ResultSet workOrders = stockroomDB.query("SELECT wo.order_id, p.product_name, wo.quantity, wo.status FROM stockroomdb.WORKORDERS AS wo JOIN stockroomdb.PRODUCTS AS p ON wo.product_id = p.product_id WHERE status = 'BUILDING';");

        rVBox.getChildren().addAll(woTitle, mainMenu.displayTable(workOrders));
        root.setCenter(rVBox);
        stage.getScene().setRoot(root);
    }

    private void newProductBOMGUI(BorderPane root, Stage stage) {
        VBox rVBox = new VBox();
        Label woTitle = new Label ("CREATE NEW PRODUCT");
        MainMenu mainMenu = new MainMenu();
        ResultSet workOrders = stockroomDB.query("SELECT wo.order_id, p.product_name, wo.quantity, wo.status FROM stockroomdb.WORKORDERS AS wo JOIN stockroomdb.PRODUCTS AS p ON wo.product_id = p.product_id;");

        rVBox.getChildren().addAll(woTitle, mainMenu.displayTable(workOrders));
        root.setCenter(rVBox);
        stage.getScene().setRoot(root);
    }


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
            case 2: //createWorkOrder();
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
        ResultSet orderID = stockroomDB.query("SELECT order_id FROM stockroomdb.WORKORDERS WHERE status = 'CREATED';");
        ResultSet productName = stockroomDB.query("SELECT p.product_name FROM stockroomdb.WORKORDERS as wo JOIN stockroomdb.PRODUCTS as p ON wo.product_id = p.product_id WHERE status = 'CREATED';");
        ResultSet quantity = stockroomDB.query("SELECT quantity FROM stockroomdb.WORKORDERS WHERE status = 'CREATED';");

        try {
            orderID.beforeFirst();
            productName.beforeFirst();
            quantity.beforeFirst();

            System.out.println("LIST OF KITS STILL IN NEED OF KITTING:\n");
            System.out.println("=================================================================");
            System.out.printf("||%-8s |%-40s |%-9s||", "ORDER ID", "              PRODUCT NAME", " QUANTITY");
            System.out.println("\n=================================================================");

            while(orderID.next()){
                productName.next();
                quantity.next();

                System.out.printf("|%9d |%-40s |%10d|\n", orderID.getInt(1), productName.getString(1), quantity.getInt(1));
            }
            System.out.println("-----------------------------------------------------------------\n");

            System.out.println("Select the order ID that you want to kit: ");
            int chosenOrderID = reader.nextInt();

            orderID.beforeFirst();
            boolean orderFound = false;

            while(orderID.next()){
                if (chosenOrderID == orderID.getInt(1)){
                    orderFound = true;
                    System.out.println("Order found. Type QUIT to stop kitting. ");
                    kitting(chosenOrderID);
                }
            }
            if (!orderFound)
                System.out.println("Order ID not found.");
        }
        catch(SQLException e) {
            e.printStackTrace();
        }


        ResultSet amount_needed = stockroomDB.select( "stockroomdb.ORDER_ITEMS", "amount_needed", new ArrayList<>());
        ResultSet amount_filled = stockroomDB.select( "stockroomdb.ORDER_ITEMS", "amount_filled", new ArrayList<>());


    }

    private void kitting(int chosenOrderID) {
        ResultSet parts_ID = stockroomDB.query("SELECT parts_id FROM stockroomdb.ORDER_ITEMS WHERE order_id = " + chosenOrderID + " AND amount_needed > amount_filled;");
        ResultSet part_description = stockroomDB.query("SELECT p.part_description FROM stockroomdb.ORDER_ITEMS as oi JOIN stockroomdb.PARTS as p ON oi.parts_id = p.parts_id WHERE order_id = " + chosenOrderID + " AND amount_needed > amount_filled;");
        ResultSet amount_needed = stockroomDB.query("SELECT amount_needed FROM stockroomdb.ORDER_ITEMS WHERE order_id = " + chosenOrderID + " AND amount_needed > amount_filled;");
        ResultSet amount_filled = stockroomDB.query("SELECT amount_filled FROM stockroomdb.ORDER_ITEMS WHERE order_id = " + chosenOrderID + " AND amount_needed > amount_filled;");
        try {
            parts_ID.beforeFirst();
            part_description.beforeFirst();
            amount_needed.beforeFirst();
            amount_filled.beforeFirst();

            System.out.println("LIST OF KITS STILL IN NEED OF KITTING:\n");


            while (parts_ID.next()) {
                part_description.next();
                amount_needed.next();
                amount_filled.next();

                System.out.println("==================================================================================================");
                System.out.printf("||%-8s |%-50s |%-15s |%-15s||", "PARTS ID", "          PART DESCRIPTION", " AMOUNT NEEDED", " AMOUNT FILLED");
                System.out.println("\n==================================================================================================");
                System.out.printf("|%9d |%-50s |%15d |%16d|\n", parts_ID.getInt(1), part_description.getString(1), amount_needed.getInt(1), amount_filled.getInt(1));
                System.out.println("--------------------------------------------------------------------------------------------------\n");
                int kitfill = fillKit(chosenOrderID, parts_ID.getInt(1));
            }
            if (!parts_ID.next()) {
                System.out.println("KITTING COMPLETE!");
                int workOrderStatusChange = stockroomDB.updateQuery("UPDATE stockroomdb.WORKORDERS SET status = 'KITTED', date_kitted = NOW() WHERE order_id = " + chosenOrderID + ";");
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }

    }

    private int fillKit(int chosenOrderID, int parts_ID) {
        System.out.println("FILL COMPLETELY? Y OR N");
        String choice = reader.next();
        boolean finished = false;

        System.out.println(choice);
        while (!finished)
        {
            if (choice.equals("Y")) {
                System.out.println("adding to kit...");
                int fill_part = stockroomDB.updateQuery("UPDATE stockroomdb.ORDER_ITEMS SET amount_filled = amount_needed WHERE order_id = " + chosenOrderID + " AND parts_id = " + parts_ID + ";");
                return 1;
            } else if (choice.equals("N")) {
                System.out.println("PARTIAL FILL? Y OR N");
                String secondChoice = reader.next();
                if (secondChoice.equals("Y")) {
                    System.out.println("HOW MANY TO FILL? : ");
                    int amountToFill = reader.nextInt();
                    int partial_fill = stockroomDB.updateQuery("UPDATE stockroomdb.ORDER_ITEMS SET amount_filled = " + amountToFill + " WHERE order_id = " + chosenOrderID + " AND parts_id = " + parts_ID + ";");

                    return 2;
                }
                else if (choice.equals("N")){
                    finished = true;
                    return 3;
                }
                else {
                    System.out.println("Could not read choice. Choose just Y or N or type QUIT to leave\n New choice:");
                    choice = reader.next();
                }

            } else if (choice.equals("QUIT")) {

                return 0;
            } else {
                System.out.println("Could not read choice. Choose just Y or N or type QUIT to leave\n New choice:");
                choice = reader.next();
            }
        }
        return 0;
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

    private class addCell extends TableCell<Object, Boolean> {
        final Button createButton = new Button();
        final TextField createdTextField = new TextField();
        final StackPane paddedButton = new StackPane();
        final DoubleProperty buttonY = new SimpleDoubleProperty();

        TableView table = new TableView();

        addCell(final Stage stage, final TableView tableView, final int choice) {
            paddedButton.setPadding(new Insets(3));

            switch (choice) {
                case 0:

                    paddedButton.getChildren().add(createButton);
                    createButton.setText("CREATE");
                    createButton.setOnMousePressed(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            buttonY.set(event.getScreenY());
                        }
                    });
                    createButton.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            TableData data = (TableData) tableView.getItems().get(getTableRow().getIndex());
                            SimpleIntegerProperty orderID = (SimpleIntegerProperty) data.getAt(1);
                            //Shipping shipping = new Shipping();
                            //shipping.shipOrder(orderID.intValue());
                            tableView.getSelectionModel().select(getTableRow().getIndex());
                            //displayShipped();
                        }
                    });
                    break;
                case 1:
                    paddedButton.getChildren().add(createdTextField);
                    break;
                case 2:
                    //createWorkOrder();
                    break;
                case 3:
                    kitWorkOrder();
                    break;
                case 4:
                    buildWorkOrder();
                    break;
                case 5:
                    completeWorkOrder();
            }


        }

        @Override
        protected void updateItem(Boolean item, boolean empty) {
            MainMenu mainMenu = new MainMenu();
            super.updateItem(item, empty);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            setGraphic(paddedButton);
            if (!empty) {
                if (getTableRow() != null) {
                    TableData data = (TableData) mainMenu.getTable().getItems().get(getTableRow().getIndex());
                    SimpleIntegerProperty productName = (SimpleIntegerProperty) data.getAt(1);
                    if (productName.getValue() != null) {
                        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                        setGraphic(paddedButton);
                    } else {
                        setGraphic(null);
                    }
                } else {
                    setGraphic(null);
                }
            } else {
                setGraphic(null);
            }
        }

    }

}

