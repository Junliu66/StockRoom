import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.ResultSet;


/**
 * Created by zhangJunliu on 10/28/17.
 */
public class OverviewUI {
    DBHandler stockroomDB = new DBHandler();

    public void viewGUI(BorderPane root, Stage stage, TableView table) {

        VBox rVBox = new VBox();

        Button complatedOrders = new Button("Completed Orders");
        complatedOrders.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                displayCompletedOrders(root, stage);
                Overview.orderCompleted();
            }
        });
        complatedOrders.setPadding(new Insets(10, 10, 10, 10));
        complatedOrders.setMinWidth(300);

        Button buildingOrders = new Button("Building Orders");
        buildingOrders.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                displayBuildingOrders(root, stage);
                Overview.buildingOrders();
            }
        });
        buildingOrders.setPadding(new Insets(10, 10, 10, 10));
        buildingOrders.setMinWidth(300);

        Button missingQuantity = new Button("Parts Are Missing From Work Orders");
        missingQuantity.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                displayOutOfStock(root, stage);
                Purchasing.outOfStock();
            }
        });
        missingQuantity.setPadding(new Insets(10, 10, 10, 10));
        missingQuantity.setMinWidth(300);

        Label workOrderTitle = new Label("OVER VIEW");
        workOrderTitle.setScaleX(2);
        workOrderTitle.setScaleY(2);
        workOrderTitle.setAlignment(Pos.CENTER);
        workOrderTitle.setPadding(new Insets(0, 0, 20, 0));
        HBox title = new HBox();
        title.getChildren().add(workOrderTitle);
        title.setMaxWidth(300);
        title.setAlignment(Pos.CENTER);

        rVBox.getChildren().addAll(title, complatedOrders, buildingOrders, missingQuantity);
        //rVBox.setPadding(new Insets(100, 100, 100, 300));
        rVBox.setSpacing(10);
        root.setCenter(rVBox);
        stage.getScene().setRoot(root);

    }

    //display completed orders in Overview
    public void displayCompletedOrders(BorderPane root, Stage stage) {
        VBox rVBox = new VBox();
        Label woTitle = new Label("VIEW COMPLETED ORDERS");
        MainMenu mainMenu = new MainMenu();
        ResultSet completedOrders = stockroomDB.query("SELECT wo.order_id, p.product_name, wo.date_completed FROM stockroomdb.WORKORDERS AS wo JOIN stockroomdb.PRODUCTS AS p ON wo.product_id = p.product_id WHERE status = 'COMPLETED';");
        rVBox.getChildren().addAll(woTitle, mainMenu.displayTable(completedOrders));
        root.setCenter(rVBox);
        stage.getScene().setRoot(root);
    }

    //display building orders in Overview
    public void displayBuildingOrders(BorderPane root, Stage stage) {
        VBox rVBox = new VBox();
        Label woTitle = new Label("VIEW BUILDING ORDERS");
        MainMenu mainMenu = new MainMenu();
        ResultSet buildingOrders = stockroomDB.query("SELECT wo.order_id, p.product_name, wo.date_building FROM stockroomdb.WORKORDERS AS wo JOIN stockroomdb.PRODUCTS AS p ON wo.product_id = p.product_id WHERE status = 'BUILDING';");
        rVBox.getChildren().addAll(woTitle, mainMenu.displayTable(buildingOrders));
        root.setCenter(rVBox);
        stage.getScene().setRoot(root);

    }

    //display outofstock in Overview
    public void displayOutOfStock(BorderPane root, Stage stage) {
        VBox rVBox = new VBox();
        Label woTitle = new Label("VIEW PARTS ARE MISSING FROM WORK ORDERS");
        MainMenu mainMenu = new MainMenu();
        ResultSet outOfStock = stockroomDB.query("SELECT p.parts_id, p.part_description, p.vendor, s.quantity - SUM(oi.amount_needed - oi.amount_filled) FROM stockroomdb.PARTS AS p JOIN stockroomdb.STOCKROOM AS s ON p.parts_id = s.parts_id JOIN stockroomdb.ORDER_ITEMS AS oi ON s.parts_id = oi.parts_id WHERE s.quantity < (oi.amount_needed - oi.amount_filled);");
        rVBox.getChildren().addAll(woTitle, mainMenu.displayTable(outOfStock));
        root.setCenter(rVBox);
        stage.getScene().setRoot(root);
    }
}
