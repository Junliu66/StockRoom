import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

import javax.xml.transform.Result;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.*;

/*
Create a command line menu for Shipping that shows a list of the available completed work orders
They can type the name (or select the number) of the completed work order, then be prompted to ship? y/n"
*/
public class Shipping {
    public static void main(String[] args) {
        displayShipping();
    }

    public static void displayShipping() {
        DBHandler stockroomdb = new DBHandler();

        ArrayList<String> conditions = new ArrayList<>();
        conditions.add("status = 'COMPLETED'");
        ResultSet id_and_quantity = stockroomdb.select("stockroomdb.WORKORDERS", "order_id, quantity", conditions);
        // get product names from PRODUCTS table
        conditions = new ArrayList<>();
        ResultSet product_name = stockroomdb.query("SELECT p.product_name FROM PRODUCTS AS p JOIN WORKORDERS AS oi ON p.product_id = oi.product_id WHERE status = 'COMPLETED'");

        System.out.println("Completed work orders:");

        System.out.println("=============================================================================");
        System.out.printf("||%-10s |%-40s |%19s||", "Order ID", "PRODUCT NAME", "Quantity  ");
        System.out.println("\n=============================================================================");

        try {
            id_and_quantity.beforeFirst();
            product_name.beforeFirst();
            while (id_and_quantity.next()) {
                product_name.next();
                System.out.printf("||%-10d |%-40s |%19d||\n", id_and_quantity.getInt(1), product_name.getString(1), id_and_quantity.getInt(2));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Scanner user_input = new Scanner(System.in);
        System.out.println("Please enter your Order ID to be shipped: ");
        int orderId = user_input.nextInt();

        HashMap<String, Object> updates = new HashMap<>();
        updates.put("status", "SHIPPED");
        updates.put("date_shipped", "NOW()");
        ArrayList<Object[]> searchConditions = new ArrayList<>();
        Object[] cond1 = {"order_id", "=", orderId};
        searchConditions.add(cond1);
        stockroomdb.update("stockroomdb.WORKORDERS", updates, searchConditions);
    }

    public static ResultSet getCompletedWorkOrders() {
        DBHandler stockroomdb = new DBHandler();

        ArrayList<String> conditions = new ArrayList<>();
        conditions.add("status = 'COMPLETED' OR status = 'SHIPPED'");
        ResultSet id_and_quantity = stockroomdb.select("stockroomdb.WORKORDERS", "*", conditions);
        return id_and_quantity;
    }

    public void shipOrder(int orderId) {
        DBHandler stockroomdb = new DBHandler();
        HashMap<String, Object> updates = new HashMap<>();
        updates.put("status", "SHIPPED");
        updates.put("date_shipped", "NOW()");
        ArrayList<Object[]> searchConditions = new ArrayList<>();
        Object[] cond1 = {"order_id", "=", orderId};
        searchConditions.add(cond1);
        stockroomdb.update("stockroomdb.WORKORDERS", updates, searchConditions);
    }

    public void viewGUI(BorderPane root, Stage stage, TableView table, MainMenu mainMenu) {
        {
            ResultSet rs = getCompletedWorkOrders();
            VBox shipVBox = mainMenu.displayTable(rs);

            TableColumn shipButtons = new TableColumn("Ship");
            shipButtons.setSortable(false);
            shipButtons.setCellFactory(new Callback<TableColumn<Object, Boolean>, TableCell<Object, Boolean>>() {
                @Override
                public TableCell<Object, Boolean> call(TableColumn<Object, Boolean> param) {
                    return new AddShipCell(mainMenu, table);
                }
            });

            table.getColumns().add(shipButtons);

            root.setCenter(shipVBox);
            stage.getScene().setRoot(root);
        }
    }


    private class AddShipCell extends TableCell<Object, Boolean> {
        final Button shipButton = new Button();
        final StackPane paddedButton = new StackPane();
        final DoubleProperty buttonY = new SimpleDoubleProperty();

        AddShipCell(final MainMenu mainMenu, final TableView tableView) {
            paddedButton.setPadding(new Insets(0))  ;
            paddedButton.getChildren().add(shipButton);
            shipButton.setText("Ship");
            // change text color
//            shipButton.setTextFill(Color.WHITE);
            // change button color
//            shipButton.setBackground(new Background(new BackgroundFill(Color.DARKRED, new CornerRadii(3.0), new Insets(0.0))));
            shipButton.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    buttonY.set(event.getScreenY());
                }
            });
            shipButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    TableData data = (TableData) tableView.getItems().get(getTableRow().getIndex());
                    SimpleIntegerProperty orderID = (SimpleIntegerProperty) data.getAt(1);
                    Shipping shipping = new Shipping();
                    shipping.shipOrder(orderID.intValue());
                    tableView.getSelectionModel().select(getTableRow().getIndex());
                    mainMenu.displayShipped();
                }
            });
        }

        @Override
        protected void updateItem(Boolean item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty) {
                if (getTableRow() != null) {
                    TableData data = (TableData) getTableView().getItems().get(getTableRow().getIndex());
                    SimpleStringProperty shipDate = (SimpleStringProperty) data.getAt(9);
                    if (shipDate.getValue() == null) {
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