import javafx.scene.paint.Color;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
import java.util.*;

/**
 *Handles the creation of new products, by logging the bill of materials of that product
 * to the MySQL database under the PRODUCTS and PRODUCT_BOM tables. It also handles the
 * creation of new work orders for those products by adding that product and desired
 * quantity to the WORKORDERS table and the amount of each part needed to create that
 * many products to the ORDER_ITEMS table along with how much of each of those parts that
 * are in the kit.
 *
 */
public class WorkOrder {

    DBHandler stockroomDB = new DBHandler();
    Scanner reader = new Scanner(System.in);
    public static String promptString = "";

    /**
     * The main display of the Work Order tab.
     *
     * @param root  The BorderPane layout for the program.
     * @param stage Where the layout is displayed.
     * @param table The object used to display each table.
     */
    public void viewGUI(BorderPane root, Stage stage, TableView table) {

        VBox rVBox = new VBox();
        MainMenu mainMenu = new MainMenu();
        mainMenu.getTable().setMaxWidth(Double.MAX_VALUE);
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
                createWorkOrderGUI(root, stage, new VBox());
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

            }
        });
        build.setPadding(new Insets(10, 10, 10, 10));
        build.setMinWidth(300);

        Button complete = new Button("Finish Building Work Orders");
        complete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                completeWorkOrderGUI(root, stage);

            }
        });
        complete.setPadding(new Insets(10, 10, 10, 10));
        complete.setMinWidth(300);

        Button product = new Button("Create New Product");
        product.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                newProductBOMGUI(root, stage, null, new HashMap<Integer, Integer>());
                //createWorkOrder();
            }
        });
        product.setPadding(new Insets(10, 10, 10, 10));
        product.setMinWidth(300);

        Label workOrderTitle = new Label("WORK ORDERS");
        workOrderTitle.setScaleX(2);
        workOrderTitle.setScaleY(2);
        workOrderTitle.setAlignment(Pos.CENTER);
        workOrderTitle.setPadding(new Insets(0, 0, 20, 0));
        HBox title = new HBox();
        title.getChildren().add(workOrderTitle);
        title.setMaxWidth(300);
        title.setAlignment(Pos.CENTER);

        rVBox.getChildren().addAll(title, view, create, kit, build, complete, product);
        rVBox.setAlignment(Pos.CENTER);
        rVBox.setPadding(new Insets(100));
        rVBox.setSpacing(10);
        root.setCenter(rVBox);
        stage.getScene().setRoot(root);
    }

    /**
     * Displays the contents of the WORKORDER table in the database.
     *
     * @param root  The BorderPane layout for the program.
     * @param stage Where the layout is displayed.
     */
    private void viewWorkOrdersGUI(BorderPane root, Stage stage) {

        VBox rVBox = new VBox();
        Label woTitle = new Label("VIEW WORK ORDERS");
        woTitle.setScaleY(2.0);
        woTitle.setScaleX(2.0);
        woTitle.setPadding(new Insets(30));
        rVBox.setAlignment(Pos.CENTER);

        MainMenu mainMenu = new MainMenu();
        ResultSet workOrders = stockroomDB.query("SELECT wo.order_id, p.product_name, wo.quantity, wo.status FROM stockroomdb.WORKORDERS AS wo JOIN stockroomdb.PRODUCTS AS p ON wo.product_id = p.product_id;");

        rVBox.setAlignment(Pos.CENTER);
        rVBox.getChildren().addAll(woTitle, mainMenu.displayTable(workOrders));

        root.setCenter(rVBox);
        stage.getScene().setRoot(root);
    }

    /**
     * Allows the user to create a new work order for a single product, by selecting the
     * product you want to build from a table and then enter the quantity that you want
     * to build.
     *
     * @param root   The BorderPane layout for the program.
     * @param stage  Where the layout is displayed.
     * @param window The VBox layout holding the Work Orders GUI
     */
    private void createWorkOrderGUI(BorderPane root, Stage stage, VBox window) {
        VBox rVBox = new VBox();
        Label woTitle = new Label("CREATE WORK ORDERS");
        woTitle.setScaleX(2.0);
        woTitle.setScaleY(2.0);
        woTitle.setPadding(new Insets(30));
        VBox titleBox = new VBox();
        titleBox.setPadding(new Insets(30));
        titleBox.setAlignment(Pos.CENTER);
        titleBox.getChildren().add(woTitle);
        MainMenu mainMenu = new MainMenu();
        ResultSet products = stockroomDB.query("SELECT product_id AS 'Product ID', product_name AS 'Product Name', date_created AS 'Date Created' FROM stockroomdb.PRODUCTS;");
        VBox productTable = mainMenu.displayTable(products);
        productTable.setAlignment(Pos.CENTER);
        rVBox.getChildren().addAll(titleBox, productTable);
        TableColumn createButtons = new TableColumn("Select One");
        createButtons.setMinWidth(60.0);
        rVBox.setAlignment(Pos.CENTER);

        createButtons.setCellFactory(new Callback<TableColumn<Object, Boolean>, TableCell<Object, Boolean>>() {
            @Override
            public TableCell<Object, Boolean> call(TableColumn<Object, Boolean> param) {
                return new addCell(root, stage, 0);
            }
        });

        mainMenu.getTable().getColumns().add(createButtons);

        rVBox.getChildren().add(window);
        root.setCenter(rVBox);

        stage.getScene().setRoot(root);
    }

    /**
     * Part of the Create Work Order GUI, confirms the selection of the product that you want
     * to create a work order for and select the quantity for that product.
     *
     * @param root         The BorderPane layout for the program.
     * @param stage        Where the layout is displayed.
     * @param productIDint The integer value of the product ID chosen.
     */
    private void confirmOrderSelection(BorderPane root, Stage stage, int productIDint) {

        VBox window = new VBox();
        window.setPadding(new Insets(30));
        window.setSpacing(10);
        window.setAlignment(Pos.CENTER);
        String text = "You have selected Product ID: [" + productIDint + "] ";
        Label prompt = new Label(text);
        Label prompt2 = new Label("Please select the quantity for the order: ");
        TextField quantityTF = new TextField();
        HBox quantityPrompt = new HBox();
        quantityPrompt.getChildren().addAll(prompt2, quantityTF);
        quantityPrompt.setAlignment(Pos.CENTER);

        Button confirm = new Button("CONFIRM ORDER");
        confirm.setOnAction(new EventHandler<ActionEvent>() {
            /**
             * Updates the WORKORDER table in the database by adding the product as a new line
             * in the WORKORDERS table, then updates the ORDER_ITEMS table with all the parts
             * for that product in the PRODUCT_BOM table along with the quantity for each part
             * multiplied with the amount of that product in the order.
             * @param event Updates the WORKORDER and ORDER_ITEMS table with the product and
             *              quantity selected.
             */
            @Override
            public void handle(ActionEvent event) {
                int quantity = Integer.parseInt(quantityTF.getText());
                stockroomDB.updateQuery("INSERT INTO stockroomdb.WORKORDERS (product_id, quantity, status, date_created) " +
                        "VALUES (" + productIDint + ", " + quantity + ", 'CREATED', NOW())");

                ResultSet newOrderID = stockroomDB.query("SELECT LAST_INSERT_ID();");
                String orderID = "";
                try {
                    newOrderID.first();
                    orderID += newOrderID.getInt(1);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                int addingWorkOrder = stockroomDB.updateQuery("INSERT INTO stockroomdb.ORDER_ITEMS (parts_id, product_id, order_id, amount_needed) " +
                        "SELECT parts_id, product_id, '" + orderID + "', (quantity * " + quantity + ") " +
                        "FROM stockroomdb.PRODUCT_BOM " +
                        "WHERE product_id = " + productIDint + ";");

                Label confirmation = new Label("NEW WORK ORDER ADDED TO DATABASE");
                confirmation.setScaleX(1.5);
                confirmation.setScaleY(1.5);

                VBox confirmationBox = new VBox();
                confirmationBox.setPadding(new Insets(40));
                confirmationBox.setAlignment(Pos.CENTER);
                confirmationBox.getChildren().add(confirmation);
                createWorkOrderGUI(root, stage, confirmationBox);
            }
        });

        window.getChildren().addAll(prompt, quantityPrompt, confirm);

        createWorkOrderGUI(root, stage, window);

    }

    /**
     * Allows user to log the amount of parts added to the kit in order to build a selected work order.
     *
     * @param root  The BorderPane layout for the program.
     * @param stage Where the layout is displayed.
     */
    private void kitWorkOrderGUI(BorderPane root, Stage stage) {
        VBox rVBox = new VBox();
        Label woTitle = new Label("KIT WORK ORDERS");
        woTitle.setScaleX(2.0);
        woTitle.setScaleY(2.0);
        rVBox.setAlignment(Pos.CENTER);
        woTitle.setPadding(new Insets(30));
        MainMenu mainMenu = new MainMenu();
        ResultSet workOrders = stockroomDB.query("SELECT wo.order_id, p.product_name, wo.quantity, wo.status FROM stockroomdb.WORKORDERS AS wo JOIN stockroomdb.PRODUCTS AS p ON wo.product_id = p.product_id WHERE status = 'CREATED';");
        VBox productTable = mainMenu.displayTable(workOrders);
        rVBox.getChildren().addAll(woTitle, productTable);
        TableColumn kitButtons = new TableColumn("Select One");
        kitButtons.setMinWidth(60.0);

        kitButtons.setCellFactory(new Callback<TableColumn<Object, Boolean>, TableCell<Object, Boolean>>() {
            @Override
            public TableCell<Object, Boolean> call(TableColumn<Object, Boolean> param) {
                return new addCell(root, stage, 1);
            }
        });

        mainMenu.getTable().getColumns().add(kitButtons);
        root.setCenter(rVBox);
        stage.getScene().setRoot(root);
    }

    /**
     * This GUI provides the method to update each of the line items of a work order's kit
     * by updating the ORDER_ITEMS table in the database to note how many of each needed item
     * has been added to the kit.
     *
     * @param root          The BorderPane layout for the program.
     * @param stage         Where the layout is displayed.
     * @param parts         The query result set of each of a specific work order ID's line items.
     * @param chosenOrderID The work order ID the user wants to kit.
     */
    private void kittingGUI(BorderPane root, Stage stage, ResultSet parts, int chosenOrderID) {
        VBox rVBox = new VBox();
        Label woTitle = new Label("KITTING WORK ORDERS");
        woTitle.setScaleX(2.0);
        woTitle.setScaleY(2.0);

        VBox titleBox = new VBox();
        titleBox.setPadding(new Insets(30));
        titleBox.setAlignment(Pos.CENTER);
        titleBox.getChildren().add(woTitle);

        try {
            parts.next();

            promptString = "Part Number : [" + parts.getInt(1) + "] \nPart Description : [" + parts.getString(2) + "] \nAmount Needed: [" + parts.getInt(3) + "] \nAmount Filled Already: [" + parts.getInt(4) + "]";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Label prompt = new Label(promptString);

        VBox currentPart = new VBox();
        currentPart.setAlignment(Pos.CENTER);
        currentPart.getChildren().add(prompt);

        Button fillFull = new Button("Fill Part");
        fillFull.setOnAction(new EventHandler<ActionEvent>() {
            /**
             * Updates the ORDER_ITEMS table in the database and changes the quantity kitted
             * to be exactly the quantity needed with one click. Also, if it's the last part
             * needed in the kitted, updates the WORKORDER table's status column to KITTED.
             * @param event Updates the ORDER_ITEMS table.
             */
            @Override
            public void handle(ActionEvent event) {
                int parts_id = 0;
                try {
                    parts_id = parts.getInt(1);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                stockroomDB.updateQuery("UPDATE stockroomdb.ORDER_ITEMS SET amount_filled = amount_needed WHERE order_id = " + chosenOrderID + " AND parts_id = " + parts_id + ";");

                try {
                    if (parts.isLast()) {
                        stockroomDB.updateQuery("UPDATE stockroomdb.WORKORDERS SET status = 'KITTED', date_kitted = NOW() WHERE order_id = " + chosenOrderID + ";");
                        kitWorkOrderGUI(root, stage);
                    } else {
                        kittingGUI(root, stage, parts, chosenOrderID);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        fillFull.setPadding(new Insets(10, 10, 10, 10));
        fillFull.setMinWidth(200);

        Button fillEmpty = new Button("Can't Fill At All");
        fillEmpty.setOnAction(new EventHandler<ActionEvent>() {
            /**
             * This button skips the current item if the kitter can't kit any amount of the
             * quantity needed to complete the work order. If it was the last part needed in
             * the kit, it changes the WORKORDER table's status column to KITTED
             * @param event Skips to the next item in ORDER_ITEMS table.
             */
            @Override
            public void handle(ActionEvent event) {
                try {
                    if (parts.isLast()) {
                        stockroomDB.updateQuery("UPDATE stockroomdb.WORKORDERS SET status = 'KITTED', date_kitted = NOW() WHERE order_id = " + chosenOrderID + ";");
                        kitWorkOrderGUI(root, stage);
                    } else {
                        kittingGUI(root, stage, parts, chosenOrderID);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        fillEmpty.setPadding(new Insets(10));
        fillEmpty.setMinWidth(200);

        TextField amountToFill = new TextField();
        amountToFill.setPadding(new Insets(10));
        amountToFill.setMinWidth(100);
        amountToFill.setMaxWidth(100);

        Button fillAmount = new Button("Add Custom Amount to Kit");
        fillAmount.setOnAction(new EventHandler<ActionEvent>() {
            /**
             * Updates the ORDER_ITEMS table in the database to change the quantity filled to
             * be the amount that was entered into the textfield above. If this part was the
             * last part needed to fill the kit, updates the WORKORDER table's status column
             * to KITTED.
             * @param event Updates the ORDER_ITEMS table with the custom amount.
             */
            @Override
            public void handle(ActionEvent event) {
                int parts_id = 0;
                try {
                    parts_id = parts.getInt(1);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                int amountToAdd = Integer.parseInt(amountToFill.getText());
                stockroomDB.updateQuery("UPDATE stockroomdb.ORDER_ITEMS SET amount_filled = " + amountToAdd + " WHERE order_id = " + chosenOrderID + " AND parts_id = " + parts_id + ";");
                try {
                    if (parts.isLast()) {
                        stockroomDB.updateQuery("UPDATE stockroomdb.WORKORDERS SET status = 'KITTED', date_kitted = NOW() WHERE order_id = " + chosenOrderID + ";");
                        kitWorkOrderGUI(root, stage);
                    } else {
                        kittingGUI(root, stage, parts, chosenOrderID);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        fillAmount.setPadding(new Insets(10, 10, 10, 10));
        fillAmount.setMinWidth(200);

        rVBox.setAlignment(Pos.TOP_CENTER);
        rVBox.setSpacing(10);
        rVBox.getChildren().addAll(titleBox, currentPart, fillFull, fillEmpty, amountToFill, fillAmount);
        root.setCenter(rVBox);
        stage.getScene().setRoot(root);
    }

    /**
     * Allows the user to change the status of a work order from KITTED to BUILDING on the
     * status column of the WORKORDERS table in the database to signify a manufacturer is
     * currently building a work order on the floor.
     *
     * @param root  The BorderPane layout for the program.
     * @param stage Where the layout is displayed.
     */
    private void buildWorkOrderGUI(BorderPane root, Stage stage) {
        VBox rVBox = new VBox();
        Label woTitle = new Label("BUILD WORK ORDERS");
        woTitle.setScaleX(2.0);
        woTitle.setScaleY(2.0);
        woTitle.setPadding(new Insets(30));
        MainMenu mainMenu = new MainMenu();
        ResultSet workOrders = stockroomDB.query("SELECT wo.order_id, p.product_name, wo.quantity, wo.status FROM stockroomdb.WORKORDERS AS wo JOIN stockroomdb.PRODUCTS AS p ON wo.product_id = p.product_id WHERE status = 'KITTED';");
        VBox orderTable = mainMenu.displayTable(workOrders);
        rVBox.setAlignment(Pos.CENTER);
        rVBox.getChildren().addAll(woTitle, orderTable);
        TableColumn buildButtons = new TableColumn("Select One");
        buildButtons.setMinWidth(60.0);

        buildButtons.setCellFactory(new Callback<TableColumn<Object, Boolean>, TableCell<Object, Boolean>>() {
            @Override
            public TableCell<Object, Boolean> call(TableColumn<Object, Boolean> param) {
                return new addCell(root, stage, 2);
            }
        });

        mainMenu.getTable().getColumns().add(buildButtons);
        root.setCenter(rVBox);
        stage.getScene().setRoot(root);
    }

    /**
     * Allows the user to change the status of a work order from BUILDING to COMPLETED on the
     * status column of the WORKORDERS table in the database to signify that a manufacturer
     * has currently finished all the products for the selected work order on the floor and
     * it's now ready for shipping.
     *
     * @param root  The BorderPane layout for the program.
     * @param stage Where the layout is displayed.
     */
    private void completeWorkOrderGUI(BorderPane root, Stage stage) {
        VBox rVBox = new VBox();
        Label woTitle = new Label("COMPLETE WORK ORDERS");
        woTitle.setScaleX(2.0);
        woTitle.setScaleY(2.0);
        woTitle.setPadding(new Insets(30));
        MainMenu mainMenu = new MainMenu();
        ResultSet workOrders = stockroomDB.query("SELECT wo.order_id, p.product_name, wo.quantity, wo.status FROM stockroomdb.WORKORDERS AS wo JOIN stockroomdb.PRODUCTS AS p ON wo.product_id = p.product_id WHERE status = 'BUILDING';");
        VBox orderTable = mainMenu.displayTable(workOrders);
        rVBox.setAlignment(Pos.CENTER);
        rVBox.getChildren().addAll(woTitle, orderTable);
        TableColumn completeButtons = new TableColumn("Select One");
        completeButtons.setMinWidth(60.0);

        completeButtons.setCellFactory(new Callback<TableColumn<Object, Boolean>, TableCell<Object, Boolean>>() {
            @Override
            public TableCell<Object, Boolean> call(TableColumn<Object, Boolean> param) {
                return new addCell(root, stage, 3);
            }
        });

        mainMenu.getTable().getColumns().add(completeButtons);
        root.setCenter(rVBox);
        stage.getScene().setRoot(root);
    }

    /**
     * Allows the creation of a new product to the database by adding the name of the product
     * and the date created to the PRODUCTS table and adding the amount of each part needed to
     * create the product to the PRODUCT_BOM table searchable by a unique product_id created from
     * the PRODUCTS table.
     *
     * @param root            The BorderPane layout for the program.
     * @param stage           Where the layout is displayed.
     * @param name            The name that the user chooses for their new product.
     * @param partAndQuantity Holds the bill of materials for the new product.
     */
    private void newProductBOMGUI(BorderPane root, Stage stage, String name, HashMap<Integer, Integer> partAndQuantity) {
        VBox rVBox = new VBox();
        Label newProductTitle = new Label("CREATE NEW PRODUCT");
        newProductTitle.setScaleX(2);
        newProductTitle.setScaleY(2);
        newProductTitle.setAlignment(Pos.CENTER);
        newProductTitle.setPadding(new Insets(0, 0, 20, 0));
        HBox title = new HBox();

        title.getChildren().add(newProductTitle);

        title.setAlignment(Pos.CENTER);
        VBox nameBox = new VBox();
        VBox createProductBOM = new VBox();
        HBox splitScreen = new HBox();
        splitScreen.setSpacing(20);

        if (name == null || name.isEmpty()) {
            Label productTitle = new Label("Enter your new product name:");
            TextField productNameTF = new TextField();
            Button enterNewName = new Button("Confirm Name");
            enterNewName.setOnAction(new EventHandler<ActionEvent>() {
                /**
                 * Confirms the name chosen for the new product.
                 */
                @Override
                public void handle(ActionEvent event) {
                    newProductBOMGUI(root, stage, productNameTF.getText(), new HashMap<Integer, Integer>());
                }
            });
            productTitle.setPrefWidth(300);
            productTitle.setAlignment(Pos.CENTER);
            productTitle.setPadding(new Insets(10));
            productNameTF.setMaxWidth(300);
            productNameTF.setAlignment(Pos.CENTER);
            productNameTF.setPadding(new Insets(10));
            enterNewName.setPrefWidth(300);
            enterNewName.setAlignment(Pos.CENTER);
            enterNewName.setPadding(new Insets(10));

            nameBox.setSpacing(10);
            nameBox.setAlignment(Pos.CENTER);

            nameBox.maxWidth(300);
            nameBox.getChildren().addAll(productTitle, productNameTF, enterNewName);
        } else {

            Label bomPrompt = new Label("Select the part ID and Quantity to add to your Product's Bill of Materials");
            Label bomPrompt2 = new Label("To add another part select ADD or select CREATE to add your current BOM to the server.\n");
            Label productName = new Label("Creating new BOM for [ " + name + " ]");
            productName.setPadding(new Insets(30));

            HBox promptBox = new HBox();
            VBox partBox = new VBox();
            VBox quantityBox = new VBox();

            Label partPrompt = new Label("Part ID:");
            TextField partTF = new TextField();
            partTF.setMaxWidth(100);
            Button addButton = new Button("ADD PART");
            Label spacer1 = new Label();
            spacer1.setPadding(new Insets(30));
            partBox.getChildren().addAll(partPrompt, partTF, spacer1, addButton);
            partBox.setAlignment(Pos.CENTER);
            partBox.setSpacing(10);

            Label quantityPrompt = new Label("Quantity:");
            TextField quantityTF = new TextField();
            quantityTF.setMaxWidth(100);
            Button createButton = new Button("CREATE BOM");
            Label spacer2 = new Label();
            spacer2.setPadding(new Insets(30));
            quantityBox.getChildren().addAll(quantityPrompt, quantityTF, spacer2, createButton);
            quantityBox.setAlignment(Pos.CENTER);
            quantityBox.setSpacing(10);

            addButton.setOnAction(new EventHandler<ActionEvent>() {
                /**
                 * Adds the parts_id and the quantity chosen to the new product's bill of
                 * material.
                 * @param event Add to bill of materials.
                 */
                @Override
                public void handle(ActionEvent event) {
                    partAndQuantity.put(Integer.parseInt(partTF.getText()), Integer.parseInt(quantityTF.getText()));
                    newProductBOMGUI(root, stage, name, partAndQuantity);
                }
            });

            createButton.setOnAction(new EventHandler<ActionEvent>() {
                /**
                 * Adds the new product to the database under the PRODUCTS table, along with date_added
                 * Adds the values from the product's partsAndQuantity hashmap to the the PRODUCT_BOM
                 * table organized by a unique id for each product from the PRODUCTS table.
                 * @param event Adds the product and bill of material to the database.
                 */
                @Override
                public void handle(ActionEvent event) {
                    stockroomDB.updateQuery("INSERT INTO stockroomdb.PRODUCTS (product_name, date_created) " +
                            "VALUES ('" + name + "', NOW());");

                    ResultSet newProductID = stockroomDB.query("SELECT LAST_INSERT_ID();");

                    String productID = "";
                    try {
                        newProductID.first();
                        productID += newProductID.getInt(1);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    Set set = partAndQuantity.entrySet();
                    Iterator iterator = set.iterator();

                    String query = "INSERT INTO stockroomdb.PRODUCT_BOM (product_id, parts_id, quantity) VALUES ";
                    while (iterator.hasNext()) {
                        Map.Entry mapEntry = (Map.Entry) iterator.next();
                        query += "(" + productID + ", " + mapEntry.getKey() + ", " + mapEntry.getValue() + ")";
                        if (iterator.hasNext()) {
                            query += ", ";
                        }
                    }
                    query += ";";

                    stockroomDB.updateQuery(query);
                    newProductBOMGUI(root, stage, null, new HashMap<Integer, Integer>());
                }
            });

            promptBox.getChildren().addAll(partBox, quantityBox);
            promptBox.setAlignment(Pos.CENTER);
            promptBox.setSpacing(40);

            VBox currentBOM = new VBox();
            currentBOM.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
            Set set = partAndQuantity.entrySet();
            Iterator iterator = set.iterator();
            Label bomTitle = new Label(name);
            bomTitle.setPadding(new Insets(40));
            bomTitle.setScaleX(1.5);
            bomTitle.setScaleY(1.5);
            bomTitle.setAlignment(Pos.CENTER);
            bomTitle.setPrefWidth(400);

            currentBOM.getChildren().add(bomTitle);

            while (iterator.hasNext()) {
                Map.Entry mapEntry = (Map.Entry) iterator.next();
                Label partQuantity = new Label("Part ID = [ " + mapEntry.getKey() + " ] with quantity of [ " + mapEntry.getValue() + " ]");
                partQuantity.setPadding(new Insets(5, 5, 5, 15));
                currentBOM.getChildren().add(partQuantity);
            }

            createProductBOM.getChildren().addAll(bomPrompt, bomPrompt2, productName, promptBox);
            createProductBOM.setAlignment(Pos.CENTER);

            splitScreen.getChildren().addAll(createProductBOM, currentBOM);

        }


        rVBox.setPadding(new Insets(100, 100, 100, 100));

        rVBox.setSpacing(10);
        rVBox.getChildren().addAll(title, nameBox, splitScreen);
        root.setCenter(rVBox);
        stage.getScene().setRoot(root);
    }

    /**
     * An inner class used to add a column to a table that is displaying the result set
     * of a database query.
     */
    private class addCell extends TableCell<Object, Boolean> {
        final Button newButton = new Button();
        final StackPane paddedButton = new StackPane();
        final DoubleProperty buttonY = new SimpleDoubleProperty();

        /**
         * Adds one column specific to the various methods that called them.
         *
         * @param root   The BorderPane layout for the program.t
         * @param stage  Where the layout is displayed.
         * @param choice Chooses which column to add to the table.
         */
        addCell(final BorderPane root, final Stage stage, final int choice) {
            MainMenu mainMenu = new MainMenu();
            paddedButton.setPadding(new Insets(3));

            switch (choice) {
                case 0:
                    paddedButton.getChildren().add(newButton);
                    newButton.setText("CREATE");
                    newButton.setOnMousePressed(new EventHandler<MouseEvent>() {
                        /**
                         * Returns the absolute Y position of the button pressed. Useful for
                         * finding which row was selected.
                         * @param event Capture Y position of button.
                         */
                        @Override
                        public void handle(MouseEvent event) {
                            buttonY.set(event.getScreenY());
                        }
                    });
                    newButton.setOnAction(new EventHandler<ActionEvent>() {
                        /**
                         * Action for creating work orders, it allows you to select the product
                         *  you want to build.
                         * @param event Select product for work order.
                         * */
                        @Override
                        public void handle(ActionEvent event) {
                            TableData data = (TableData) mainMenu.getTable().getItems().get(getTableRow().getIndex());
                            SimpleIntegerProperty productID = (SimpleIntegerProperty) data.getAt(1);
                            int productIDint = productID.getValue();
                            confirmOrderSelection(root, stage, productIDint);
                        }
                    });
                    break;
                case 1:
                    paddedButton.getChildren().add(newButton);
                    newButton.setText("KIT");
                    newButton.setOnMousePressed(new EventHandler<MouseEvent>() {
                        /**
                         * Returns the absolute Y position of the button pressed. Useful for
                         * finding which row was selected.
                         * @param event Capture Y position of button.
                         */
                        @Override
                        public void handle(MouseEvent event) {
                            buttonY.set(event.getScreenY());
                        }
                    });
                    newButton.setOnAction(new EventHandler<ActionEvent>() {
                        /**
                         * Action to start kitting a work order, allows the user to select which
                         * work order they want to start kitting.
                         * @param event Select work order for kitting.
                         */
                        @Override
                        public void handle(ActionEvent event) {
                            TableData data = (TableData) mainMenu.getTable().getItems().get(getTableRow().getIndex());
                            SimpleIntegerProperty orderID = (SimpleIntegerProperty) data.getAt(1);
                            int orderIDint = orderID.getValue();

                            ResultSet parts = stockroomDB.query("SELECT oi.parts_id, p.part_description, oi.amount_needed, amount_filled FROM stockroomdb.ORDER_ITEMS as oi JOIN stockroomdb.PARTS as p ON oi.parts_id = p.parts_id WHERE order_id = " + orderIDint + " AND amount_needed > amount_filled;");
                            try {
                                parts.beforeFirst();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            kittingGUI(root, stage, parts, orderIDint);
                        }
                    });
                    break;
                case 2:
                    paddedButton.getChildren().add(newButton);
                    newButton.setText("BUILD");
                    newButton.setOnMousePressed(new EventHandler<MouseEvent>() {
                        /**
                         * Returns the absolute Y position of the button
                         * @param event Captures Y position..
                         */
                        @Override
                        public void handle(MouseEvent event) {
                            buttonY.set(event.getScreenY());
                        }
                    });
                    newButton.setOnAction(new EventHandler<ActionEvent>() {
                        /**
                         * Action to change the status of a work order from KITTED To BUILDING
                         * by updating the WORKORDERS table in the database for that index.
                         * @param event Updates work order status.
                         */
                        @Override
                        public void handle(ActionEvent event) {
                            TableData data = (TableData) mainMenu.getTable().getItems().get(getTableRow().getIndex());
                            SimpleIntegerProperty orderID = (SimpleIntegerProperty) data.getAt(1);
                            int orderIDint = orderID.getValue();

                            stockroomDB.updateQuery("UPDATE stockroomdb.WORKORDERS SET status = 'BUILDING', date_building = NOW() WHERE order_id = " + orderIDint + ";");

                            buildWorkOrderGUI(root, stage);
                        }
                    });
                    break;
                case 3:
                    paddedButton.getChildren().add(newButton);
                    newButton.setText("COMPLETE");
                    newButton.setOnMousePressed(new EventHandler<MouseEvent>() {
                        /**
                         * Returns the absolute Y position of the button.
                         * @param event Captures Y position.
                         */
                        @Override
                        public void handle(MouseEvent event) {
                            buttonY.set(event.getScreenY());
                        }
                    });
                    newButton.setOnAction(new EventHandler<ActionEvent>() {
                        /**
                         * Action to change the status of a work order from the BUILDING to
                         * COMPLETED to the WORKORDERS table in the database for that index.
                         * @param event Update work order status.
                         */
                        @Override
                        public void handle(ActionEvent event) {
                            TableData data = (TableData) mainMenu.getTable().getItems().get(getTableRow().getIndex());
                            SimpleIntegerProperty orderID = (SimpleIntegerProperty) data.getAt(1);
                            int orderIDint = orderID.getValue();

                            stockroomDB.updateQuery("UPDATE stockroomdb.WORKORDERS SET status = 'COMPLETED', date_completed = NOW() WHERE order_id = " + orderIDint + ";");

                            completeWorkOrderGUI(root, stage);
                        }
                    });
                    break;
                case 4:

                    break;


            }


        }

        /**
         * Checks the table built to show the result set of your database query
         * and adds one of the cells above as long as that row is not empty.
         *
         * @param item  Boolean to check if the row has an item on it or not.
         * @param empty Boolean to check if the row is empty.
         */
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

/*
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

    }*/
