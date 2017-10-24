import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javafx.geometry.Insets;

public class MainMenu extends Application{

    public static void main(String[] args){

        launch(args);
    }

    @Override
    public void start(Stage stage){
        //sets application title
        stage.setTitle("Stockroom Inventory App");

        //Inventory
        Button inventory = new Button();
        inventory.setText("View Inventory");
        inventory.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                displayInventory();
            }
        });

        Button orders = new Button();
        orders.setText("Create Order");
        orders.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                WorkOrder workorder = new WorkOrder();
                workorder.viewMenu();
                displayOrderForm();
            }
        });

        Button purchase = new Button();
        purchase.setText("Purchase");
        purchase.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                displayPurchaseForm();
            }
        });

        Button receiving = new Button();
        receiving.setText("Received Orders");
        receiving.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Receiving receiving = new Receiving();
                receiving.displayReceiving();
            }
        });

        Button shipping = new Button();
        shipping.setText("Shipped Orders");
        shipping.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                displayShipped();
            }
        });

        Button overview = new Button();
        overview.setText("Overview");
        overview.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                displayOverview();
            }
        });

        HBox root = new HBox();
        root.setPadding(new Insets(15, 12, 15, 12));
        root.setSpacing(10);
        root.setStyle("-fx-background-color: #336699;");
        root.getChildren().add(inventory);
        root.getChildren().add(orders);
        root.getChildren().add(purchase);
        root.getChildren().add(receiving);
        root.getChildren().add(shipping);
        root.getChildren().add(overview);

        stage.setScene(new Scene(root, 600, 50));
        stage.show();
    }

    public static void displayInventory(){
        System.out.println("Inventory");
        //Inventory.displayInventory();
    }

    public static void displayOrderForm(){

    }

    public static void displayPurchaseForm(){

    }

    public static void displayReceiving(){

    }

    public static void displayShipped(){

    }

    public static void displayOverview(){

    }
}