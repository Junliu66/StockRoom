import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.image.Image;

import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;

public class MainMenu extends Application{
    public static void main(String[] args){
        launch(args);
    }
    public static TableView table = new TableView();
    private VBox vBox = new VBox();
    private Stage stage = new Stage();
    private BorderPane root = new BorderPane();

    private static double imageHeight = 50;
    private static double imageWidth = 50;

    @Override
    public void start(Stage stage){
        //sets application title
        stage.setTitle("Stockroom Inventory App");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setSelectionModel(null);

        //Inventory
        Button inventory = createButton("View Inventory", Paths.get("Icons", "Stockroom.png").toString());
        inventory.setMinWidth(125);
        inventory.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                displayInventory();
            }
        });

        Button orders = createButton("Work Orders", Paths.get("Icons", "Bill Materials.png").toString());
        orders.setMinWidth(125);
        orders.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                WorkOrder workOrder = new WorkOrder();
                workOrder.viewGUI(root, stage, table);
            }
        });

        Button purchase = createButton("Purchase", Paths.get("Icons", "purchasing.png").toString());
        purchase.setMinWidth(125);
        purchase.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                PurchasingUI purchasingUI = new PurchasingUI();
                purchasingUI.viewGUI(root, stage, table);
            }
        });

        Button receiving = createButton("Received Parts", Paths.get("Icons", "receving.png").toString());
        receiving.setMinWidth(125);
        receiving.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
               ReceivingGUI Receiving = new ReceivingGUI();
                Receiving.viewGUI(root, stage);
            }

        });

        Button shipping = createButton("Ship Order", Paths.get("Icons", "shipping.png").toString());
        shipping.setMinWidth(125);
        shipping.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                displayShipped();
            }
        });

        Button overview = createButton("Overview", Paths.get("Icons", "Customer.png").toString());
        overview.setMinWidth(125);
        overview.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                OverviewUI overView = new OverviewUI();
                overView.viewGUI(root, stage, table);
            }
        });

        Button quit = createButton("Quit", "");
        quit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
            }
        });

        BorderPane borderPane = new BorderPane();

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.TOP_CENTER);
        hBox.getChildren().addAll(inventory, orders, purchase, receiving, shipping, overview, quit);
        borderPane.setTop(hBox);
        borderPane.setCenter(vBox);
        vBox.setAlignment(Pos.CENTER);

        showSplash(vBox);

        borderPane.getStylesheets().clear();
        borderPane.getStylesheets().add("main.css");
        root = borderPane;

        stage.setScene(new Scene(root, 1000, 800));
        stage.show();
        this.stage = stage;
    }

    private void showSplash(VBox vBox) {
        ImageView logo = new ImageView(new Image("Icons/stockroom-app.png"));
//        vBox.setBackground(new Background(new BackgroundImage(logo, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, new BackgroundPosition(Side.LEFT, 0.5, true, Side.TOP, 0.0, true), null)));
        Text credits = new Text();
        credits.setTextAlignment(TextAlignment.CENTER);
//        credits.setFont(Font.font(20.0));
        credits.setText("Created by:\nChunlei Li\nStefano Mauri\nChristian Wookey\nJunliu Zhang\nAndre Zhu");
        GridPane creditsGrid = new GridPane();
//        creditsGrid.setGridLinesVisible(true);
        GridPane.setHalignment(credits, HPos.CENTER);
        creditsGrid.setAlignment(Pos.CENTER);
        creditsGrid.add(logo, 0, 1, 1, 1);
        creditsGrid.add(credits, 0, 3, 1, 1);
        creditsGrid.setVgap(25.0);
        vBox.getChildren().add(creditsGrid);
    }

    public Button createButton(String text, String fileName ){
        Button newButton = new Button();
        newButton.setText(text);
        Image buttonImage = new Image(getClass().getClassLoader().getResourceAsStream(fileName));
        ImageView scaledImage = new ImageView(buttonImage);
        //Adjusting the image size to fit the button
        scaledImage.setFitWidth(imageWidth);
        scaledImage.setFitHeight(imageHeight);
        newButton.setContentDisplay(ContentDisplay.TOP);
        newButton.setGraphic(scaledImage);

        return newButton;
    }

    public Button createButton(String text, ArrayList<String> path){
        //ArrayList path should have
        String first = path.remove(0);
        String filePath = Paths.get(first, (String[])path.toArray()).toString();
        return createButton(text, filePath);
    }

    public void displayInventory(){
        DBHandler testDB = new DBHandler();
        ResultSet result_part_id = testDB.query("SELECT p.parts_id, p.part_number, p.part_description, p.vendor, s.quantity " +
                "FROM stockroomdb.PARTS AS p JOIN stockroomdb.STOCKROOM AS s ON p.parts_id = s.parts_id;");
        vBox = displayTable(result_part_id);
        root.setCenter(vBox);
        stage.getScene().setRoot(root);
    }

    public void displayOrderForm(){

    }

    public void displayPurchaseForm(){

    }


    public void displayShipped(){
        Shipping shipping = new Shipping();
        shipping.viewGUI(root, stage, table, this);
    }


    public void displayOverview() {

    }

    public static TableView getTable() {
        return table;
    }

    public void setMiddle(Node newDisplay){
        root.setCenter(newDisplay);
        stage.getScene().setRoot(root);
    }

    public static VBox displayTable(ResultSet queryResult){
        table.getColumns().clear();
        try{
            ResultSetMetaData dbData = queryResult.getMetaData();
            //Sets up the table columns: their names and data types
            queryResult.beforeFirst();
            System.out.println("Setup columns");
            for(int i = 1; i <= dbData.getColumnCount(); i++){
                final int index = i;
                String colName = dbData.getColumnName(i);
                TableColumn column = new TableColumn(colName);
                int type = dbData.getColumnType(i);
                if(type == Types.INTEGER || type == Types.BIGINT || type == Types.DECIMAL){
                    column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TableData, Integer>, ObservableValue<Integer>>() {
                        @Override
                        public ObservableValue<Integer> call(TableColumn.CellDataFeatures<TableData, Integer> param) {
                            return param.getValue().getAt(index);
                        }
                    });
                }
                else if(type == Types.VARCHAR ){
                    column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TableData, String>, ObservableValue<String>>() {
                        @Override
                        public ObservableValue<String> call(TableColumn.CellDataFeatures<TableData, String> param) {
                            return param.getValue().getAt(index);
                        }
                    });
                }
                else if(type == Types.TIMESTAMP){
                    column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TableData, Timestamp>, ObservableValue<Timestamp>>() {
                        @Override
                        public ObservableValue call(TableColumn.CellDataFeatures<TableData, Timestamp> param) {
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
                    if(type == Types.INTEGER || type == Types.BIGINT ){
                        tableData.add(queryResult.getInt(i));
                    }
                    else if(type == Types.VARCHAR){
                        tableData.add(queryResult.getString(i));
                    }
                    else if(type == Types.TIMESTAMP){
                        tableData.add(queryResult.getString(i));
                    }
                    else if(type == Types.DECIMAL){
                        System.out.println("decimal");
                        System.out.println(queryResult.getBigDecimal(i));
                        tableData.add(queryResult.getBigDecimal(i).intValue());
                    }
                    else {
                        System.out.println("does not find type " + type);
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
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(10,10,0,10));
        vBox.setVgrow(table, Priority.ALWAYS);
        vBox.getChildren().add(table);

        return vBox;
    }
}
