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
 * purchasing user interface shows the parts are out of stock
 * and parts are in low quantity so that user can handle purchasing
 * of these items
 */
public class PurchasingUI {
    DBHandler stockroomDB = new DBHandler();

    public void viewGUI(BorderPane root, Stage stage, TableView table) {

        VBox rVBox = new VBox();

        Button missingQauntity = new Button("Parts Are Missing From Work Orders");
        missingQauntity.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                outOfStock(root, stage);
                Purchasing.outOfStock();
            }
        });
        missingQauntity.setPadding(new Insets(10, 10, 10, 10));
        missingQauntity.setMinWidth(300);

        Button lowQuantity = new Button("Parts are in low quantity");
        lowQuantity.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                lowQuantity(root, stage);
                Purchasing.lowQuantity();
            }
        });
        lowQuantity.setPadding(new Insets(10, 10, 10, 10));
        lowQuantity.setMinWidth(300);

        Label workOrderTitle = new Label("PURCHASING");
        workOrderTitle.setScaleX(2);
        workOrderTitle.setScaleY(2);
        workOrderTitle.setAlignment(Pos.CENTER);
        workOrderTitle.setPadding(new Insets(0, 0, 20, 0));
        HBox title = new HBox();
        title.getChildren().add(workOrderTitle);
        title.setMaxWidth(300);
        title.setAlignment(Pos.CENTER);

        rVBox.getChildren().addAll(title, missingQauntity, lowQuantity);
        rVBox.setPadding(new Insets(100));
        rVBox.setAlignment(Pos.CENTER);
        rVBox.setSpacing(10);
        root.setCenter(rVBox);
        stage.getScene().setRoot(root);

    }

    /**
     * display parts that are out of stock according to the amount filed and needed
     * @param root the root BorderPane for the program
     * @param stage the active Stage in the program
     */
    public void outOfStock(BorderPane root, Stage stage) {
        VBox rVBox = new VBox();
        Label woTitle = new Label("VIEW PARTS ARE MISSING FROM WORK ORDERS");
        MainMenu mainMenu = new MainMenu();
        ResultSet missingQuantity = stockroomDB.query("SELECT oi.order_id, p.parts_id, p.part_description, p.vendor, s.quantity - (oi.amount_needed - oi.amount_filled) FROM stockroomdb.PARTS AS p JOIN stockroomdb.STOCKROOM AS s ON p.parts_id = s.parts_id JOIN stockroomdb.ORDER_ITEMS AS oi ON s.parts_id = oi.parts_id WHERE s.quantity < (oi.amount_needed - oi.amount_filled);");
        rVBox.getChildren().addAll(woTitle, mainMenu.displayTable(missingQuantity));
        root.setCenter(rVBox);
        stage.getScene().setRoot(root);
    }

    /**
     * display parts that are in low quantity according to the low quantity settings
     * @param root the root BorderPane for the program
     * @param stage the active Stage in the program
     */
    public void lowQuantity(BorderPane root, Stage stage) {
        VBox rVBox = new VBox();
        Label woTitle = new Label("VIEW PARTS ARE IN LOW QUANTITY");
        MainMenu mainMenu = new MainMenu();
        ResultSet lowQuantiy = stockroomDB.query("SELECT p.parts_id, p.part_description, p.vendor, s.quantity, p.low_quantity FROM stockroomdb.PARTS AS p JOIN stockroomdb.STOCKROOM AS s ON p.parts_id = s.parts_id WHERE s.quantity < p.low_quantity;");
        rVBox.getChildren().addAll(woTitle, mainMenu.displayTable(lowQuantiy));
        root.setCenter(rVBox);
        stage.getScene().setRoot(root);

    }
}


