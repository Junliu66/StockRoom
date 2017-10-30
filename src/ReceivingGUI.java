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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ReceivingGUI {
    static DBHandler stockroomDB = new DBHandler();
    Scanner reader = new Scanner(System.in);

    public void viewGUI(BorderPane root, Stage stage, TableView table) {
        VBox rVBox = new VBox();

        System.out.println("title");
        Button start = new Button("Start");
        start.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                displayReceivingGUI(root, stage);
                //displayReceivingGUI();
            }
        });
        start.setPadding(new Insets(10, 10, 10, 10));
        start.setMinWidth(300);

        Label receivingTitle = new Label("RECORD THE RECEIVING");
        MainMenu mainMenu = new MainMenu();
        receivingTitle.setScaleX(2);
        receivingTitle.setScaleY(2);
        receivingTitle.setAlignment(Pos.CENTER);
        receivingTitle.setPadding(new Insets(0, 0, 20, 0));
        HBox title = new HBox();
        title.getChildren().add(receivingTitle);
        title.setMaxWidth(300);
        title.setAlignment(Pos.CENTER);

        rVBox.getChildren().addAll(title, start);//add all the new things in here
        rVBox.setPadding(new Insets(100, 100, 100, 300));
        rVBox.setSpacing(10);
        root.setCenter(rVBox);
        stage.getScene().setRoot(root);
    }

    private static void submitGUI(BorderPane root, Stage stage) {
        System.out.println("In the submitGui");
        VBox rVBox = new VBox();
        Label antTitle = new Label("VIEW AMOUNT NEEDED");
        MainMenu mainMenu = new MainMenu();
        ResultSet amountNeeded = stockroomDB.query("SELECT ant.order_id, p.product_name, ant.quantity, ant.status FROM stockroomdb.ORDER_ITEMS AS ant JOIN stockroomdb.PRODUCTS AS p ON ant.product_id = p.product_id;");
        rVBox.getChildren().addAll(antTitle, mainMenu.displayTable(amountNeeded));
        root.setCenter(rVBox);
        stage.getScene().setRoot(root);
    }

    private String getFillPageHeader(int partQuantityReceived, String partIDNumber) {
        return partQuantityReceived + " part " + partIDNumber + " to be filled";
    }

    public void getReceiveingAmount(BorderPane root, Stage stage) {
        System.out.println("Entering received parts");
        VBox rVBox = new VBox();

        Label label1 = new Label("Enter Part ID: ");
        TextField pid = new TextField();
        HBox hb1 = new HBox();
        hb1.getChildren().addAll(label1, pid);
        hb1.setSpacing(10);

        Label label2 = new Label("Enter Quantity: ");
        TextField qtt = new TextField();
        HBox hb2 = new HBox();
        hb2.getChildren().addAll(label2, qtt);
        hb2.setSpacing(10);

        Button submit = new Button("submit");
        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String partIDNumber = pid.getText();
                int partQuantityReceived = Integer.parseInt(qtt.getText());
                System.out.println("part id from user: " + partIDNumber);
                System.out.println("quantity from user: " + partQuantityReceived);

                ResultSet orderID = getOrders(partIDNumber);
                ResultSet productName = stockroomDB.query("SELECT p.product_name FROM PRODUCTS AS p JOIN ORDER_ITEMS as oi ON p.product_id = oi.product_id WHERE " + partIDNumber + " = parts_id AND amount_needed > amount_filled;");
                ResultSet quantityNeeded = stockroomDB.query("SELECT (amount_needed - amount_filled) AS amount FROM ORDER_ITEMS WHERE " + partIDNumber + " = parts_id AND amount_needed > amount_filled;");
                VBox all = new VBox();
                Label qttReceived = new Label(getFillPageHeader(partQuantityReceived, partIDNumber));
                all.getChildren().add(qttReceived);
                try {
                    orderID.beforeFirst();
                    productName.beforeFirst();
                    quantityNeeded.beforeFirst();
                    Label orderIdLabel = new Label("Order ID");
                    orderIdLabel.setMinWidth(200);
                    Label productNameLabel = new Label("PRODUCT NAME");
                    productNameLabel.setMinWidth(200);
                    Label amountLabel = new Label("Amount Needed");
                    amountLabel.setMinWidth(200);
                    HBox header = new HBox();
                    header.getChildren().addAll(orderIdLabel, productNameLabel, amountLabel);

                    all.getChildren().add(header);
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
                        Button fill = new Button("Fill");
                        fill.setOnAction(new EventHandler<ActionEvent>(){
                            @Override
                            public void handle(ActionEvent event){
                                try {
                                    int partLeft = fillKit(partQuantityReceived, quantityNeededInt, uid);
                                    qttReceived.setText(getFillPageHeader(partLeft, partIDNumber));
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        all.getChildren().add(generateVBox(Integer.toString(orderId), productName.getString(1), Integer.toString(quantityNeededInt), fill));
                        System.out.println("-----------------------------------------------------------------------------");
                    }
                    // add leftovers to STOCKROOM
                    if (partQuantityReceived > 0) {
                        stockroomDB.adjustPartQuantity(Integer.parseInt(partIDNumber), partQuantityReceived);
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }


                root.setCenter(all);
                stage.getScene().setRoot(root);
                //Ask to fill the kit
//                submitGUI(root, stage);
//                ReceivingMenu menu = new ReceivingMenu();
//                menu.submit(Integer.parseInt(partId), Integer.parseInt(quantity));
            }
        });
        rVBox.getChildren().addAll(hb1, hb2, submit);
        root.setCenter(rVBox);
        stage.getScene().setRoot(root);
    }

    private int fillKit(int partQuantityReceived, int quantityNeededInt, int uid) throws SQLException {
            int amountLeftover = partQuantityReceived - quantityNeededInt;
            int quantifyLeft;
            if (amountLeftover <= 0) {
                // receiving less than or equal amount of parts needed in current kit
                HashMap<String, Object> updates = new HashMap<>();
                updates.put("amount_filled", quantityNeededInt + partQuantityReceived);
                ArrayList<Object[]> conds = new ArrayList<>();
                Object[] cond = {"id", "=", uid};
                conds.add(cond);
                stockroomDB.update("stockroomdb.ORDER_ITEMS", updates, conds);
                quantifyLeft = 0;
                // out of received parts so we break
            } else {
                // receiving more parts than needed in current kit, get total quantity needed and set to quantity filled
                ResultSet quantityNeededTotal = stockroomDB.query("SELECT amount_needed AS amount FROM ORDER_ITEMS WHERE id = " + uid);
                quantityNeededTotal.next();
                HashMap<String, Object> updates = new HashMap<>();
                updates.put("amount_filled", quantityNeededTotal.getInt(1));
                ArrayList<Object[]> conds = new ArrayList<>();
                Object[] cond = {"id", "=", uid};
                conds.add(cond);
                stockroomDB.update("stockroomdb.ORDER_ITEMS", updates, conds);
                // remove quantity stored in kit from partQuantityReceived
                quantifyLeft = partQuantityReceived - quantityNeededInt;
            }
            return quantifyLeft;
    }

    private HBox generateVBox(String orderId,  String productName, String quantityNeeded, Button fill) {
        Label orderIdLabel = new Label(orderId);
        orderIdLabel.setMinWidth(200);
        Label productNameLabel = new Label(productName);
        productNameLabel.setMinWidth(200);
        Label amountLabel = new Label(quantityNeeded);
        amountLabel.setMinWidth(200);
        HBox ret = new HBox();
        ret.getChildren().addAll(orderIdLabel, productNameLabel, amountLabel, fill);
        return ret;
    }

    private static ResultSet getTable(String partId) {
        return stockroomDB.query("SELECT ORDER_ITEMS.order_id, PRODUCTS.product_name, (ORDER_ITEMS.amount_needed - ORDER_ITEMS.amount_filled) AS amount FROM stockroomdb.ORDER_ITEMS INNER JOIN stockroomdb.PRODUCTS ON ORDER_ITEMS.product_id = PRODUCTS.product_id WHERE ORDER_ITEMS.parts_id = " + partId + " AND ORDER_ITEMS.amount_needed > ORDER_ITEMS.amount_filled;");
    }


    private static ResultSet getOrders(String partId) {
        ArrayList<String> conditions = new ArrayList<>();
        conditions.add(Integer.parseInt(partId) + " = " + "parts_id");
        conditions.add("amount_needed > amount_filled");
        return stockroomDB.select("stockroomdb.ORDER_ITEMS", "id, order_id", conditions);

    }


    //    private static ResultSet getProductNames(String partId) {
//        return stockroomDB.query("SELECT p.product_name FROM PRODUCTS AS p JOIN ORDER_ITEMS as oi ON p.product_id = oi.product_id WHERE " + partId + " = parts_id AND amount_needed > amount_filled;");
//    }
//z
//    private static ResultSet getQuantities(String partId) {
//        return stockroomDB.query("SELECT (amount_needed - amount_filled) AS amount FROM ORDER_ITEMS WHERE " + partId + " = parts_id AND amount_needed > amount_filled;");
//    }

    private void displayReceivingGUI(BorderPane root, Stage stage) {
        System.out.println("record a receiving.");
        VBox rVBox = new VBox();

        Label t = new Label();
        t.setText("Do you want to record a receiving? ");
        t.setScaleX(1);
        t.setScaleY(1);
        t.setAlignment(Pos.CENTER);
        t.setPadding(new Insets(0, 0, 0, 0));
        t.setMinWidth(300);
        HBox title = new HBox();
        title.getChildren().add(t);
        title.setMaxWidth(300);
        title.setAlignment(Pos.CENTER);

        Button yes = new Button("YES");
        yes.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                getReceiveingAmount(root, stage);
            }
        });
        yes.setAlignment(Pos.CENTER);
        yes.setPadding(new Insets(10, 10, 10, 10));
        yes.setMinWidth(300);


        Button no = new Button("NO");
        no.setAlignment(Pos.CENTER);
        no.setPadding(new Insets(10, 10, 10, 10));
        no.setMinWidth(300);

        HBox buttons = new HBox();
        buttons.getChildren().addAll(yes, no);
        rVBox.getChildren().addAll(t, buttons);
        root.setCenter(rVBox);
        stage.getScene().setRoot(root);
    }


    private void submit() {
        int partIDNumber = 0;
        int partQuantityReceived = 0;
        ArrayList<String> conditions = new ArrayList<>();
        conditions.add(partIDNumber + " = " + "parts_id");
        conditions.add("amount_needed > amount_filled");
        ResultSet orderID = stockroomDB.select("stockroomdb.ORDER_ITEMS", "id, order_id", conditions);

        ResultSet productName = stockroomDB.query("SELECT p.product_name FROM PRODUCTS AS p JOIN ORDER_ITEMS as oi ON p.product_id = oi.product_id WHERE " + partIDNumber + " = parts_id AND amount_needed > amount_filled;");
        ResultSet quantityNeeded = stockroomDB.query("SELECT (amount_needed - amount_filled) AS amount FROM ORDER_ITEMS WHERE " + partIDNumber + " = parts_id AND amount_needed > amount_filled;");

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
                        stockroomDB.update("stockroomdb.ORDER_ITEMS", updates, conds);
                        partQuantityReceived = 0;
                        // out of received parts so we break
                        break;
                    } else {
                        // receiving more parts than needed in current kit, get total quantity needed and set to quantity filled
                        ResultSet quantityNeededTotal = stockroomDB.query("SELECT amount_needed AS amount FROM ORDER_ITEMS WHERE id = " + uid);
                        quantityNeededTotal.next();
                        HashMap<String, Object> updates = new HashMap<>();
                        updates.put("amount_filled", quantityNeededTotal.getInt(1));
                        ArrayList<Object[]> conds = new ArrayList<>();
                        Object[] cond = {"id", "=", uid};
                        conds.add(cond);
                        stockroomDB.update("stockroomdb.ORDER_ITEMS", updates, conds);
                        // remove quantity stored in kit from partQuantityReceived
                        partQuantityReceived -= quantityNeededInt;
                    }
                }
            }
            // add leftovers to STOCKROOM
            if (partQuantityReceived > 0)
                stockroomDB.adjustPartQuantity(partIDNumber, partQuantityReceived);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private class AddFillCell extends TableCell<Object, Boolean> {
        final Button shipButton = new Button();
        final StackPane paddedButton = new StackPane();
        final DoubleProperty buttonY = new SimpleDoubleProperty();

        AddFillCell(final Stage stage, final TableView tableView) {
            // paddedButton.setPadding(new Insets(3));
            paddedButton.getChildren().add(shipButton);
            shipButton.setText("Fill");
        }

        @Override
        protected void updateItem(Boolean item, boolean empty) {
            super.updateItem(item, empty);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            setGraphic(paddedButton);
        }
    }
}

