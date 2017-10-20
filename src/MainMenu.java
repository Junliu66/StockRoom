import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;
import javafx.scene.image.Image;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.ArrayList;

public class MainMenu extends Application{
    public static void main(String[] args){
        launch(args);
    }
    private TableView table = new TableView();
    private VBox vBox = new VBox();
    private Stage stage = new Stage();
    private BorderPane root = new BorderPane();

    @Override
    public void start(Stage stage){
        //sets application title

        stage.setTitle("Stockroom Inventory App");

        //Inventory
        Button inventory = new Button();
        inventory.setText("View Inventory");
        /*
        Image inventoryImage = new Image(getClass().getResourceAsStream("\\Icons\\Stockroom.png"));
        inventory.setGraphic(new ImageView(inventoryImage));
        */
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
                displayReceiving();
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
        BorderPane borderPane = new BorderPane();

        HBox hBox = new HBox();
        hBox.getChildren().add(inventory);
        hBox.getChildren().add(orders);
        hBox.getChildren().add(purchase);
        hBox.getChildren().add(receiving);
        hBox.getChildren().add(shipping);
        hBox.getChildren().add(overview);

        borderPane.setTop(hBox);
        borderPane.setCenter(vBox);

        root = borderPane;

        stage.setScene(new Scene(root, 800, 600));
        stage.show();
        this.stage = stage;
    }

    public void displayInventory(){
        System.out.println("Inventory");
        DBHandler testDB = new DBHandler();
        ResultSet result_part_id = testDB.select("stockroomdb.PARTS", "*", new ArrayList<String>());
        vBox = displayTable(result_part_id);
        root.setCenter(vBox);
        stage.getScene().setRoot(root);
    }

    public void displayOrderForm(){

    }

    public void displayPurchaseForm(){

    }

    public void displayReceiving(){

    }

    public void displayShipped(){

    }

    public void displayOverview(){

    }

    public VBox displayTable(ResultSet queryResult){
        table.getColumns().clear();
        try{
            ResultSetMetaData dbData = queryResult.getMetaData();
            //Sets up the table columns: their names and data types
            queryResult.beforeFirst();
            System.out.println("Setup columns");
            for(int i = 1; i <= dbData.getColumnCount(); i++){
                final int index = i;
                String colName = dbData.getColumnName(i);
                int size = dbData.getColumnDisplaySize(i);
                TableColumn column = new TableColumn(colName);
                column.setMinWidth((double) size);
                int type = dbData.getColumnType(i);
                if(type == Types.INTEGER){
                    column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TableData, Integer>, ObservableValue<Integer>>() {
                        @Override
                        public ObservableValue<Integer> call(TableColumn.CellDataFeatures<TableData, Integer> param) {
                            return param.getValue().getAt(index);
                        }
                    });
                }
                else if(type == Types.VARCHAR){
                    column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TableData, String>, ObservableValue<String>>() {
                        @Override
                        public ObservableValue<String> call(TableColumn.CellDataFeatures<TableData, String> param) {
                            return param.getValue().getAt(index);
                        }
                    });
                }

                table.getColumns().add(column);
            }
            //Converts the ResultSet into usable data
            ObservableList<TableData> data = FXCollections.observableArrayList();
            queryResult.beforeFirst();
            System.out.println("Populate data");
            while(queryResult.next()){
                //Do a per row data grab and add each value based on the type value
                TableData tableData = new TableData();
                for(int i = 1; i <= dbData.getColumnCount(); i++){
                    int type = dbData.getColumnType(i);
                    if(type == Types.INTEGER){
                        tableData.add(queryResult.getInt(i));
                    }
                    else if(type == Types.VARCHAR){
                        tableData.add(queryResult.getString(i));
                    }
                }
                data.add(tableData);
            }

            table.setItems(data);

        }
        catch (java.sql.SQLException e){
            System.out.println(e);
        }

        VBox vBox = new VBox();
        vBox.setSpacing(5);
        vBox.setPadding(new Insets(10,0,0,10));

        vBox.getChildren().add(table);

        return vBox;
    }
}
