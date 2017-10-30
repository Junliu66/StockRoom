import javafx.application.Application;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.image.Image;
import javafx.scene.*;

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
    private static TableView table = new TableView();
    private VBox vBox = new VBox();
    private Stage stage = new Stage();
    private BorderPane root = new BorderPane();

    private static double imageHeight = 50;
    private static double imageWidth = 50;

    @Override
    public void start(Stage stage){
        //sets application title

        stage.setTitle("Stockroom Inventory App");

        //Inventory
        Button inventory = createButton("View Inventory", Paths.get("Icons", "Stockroom.png").toString());
        inventory.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                displayInventory();
            }
        });

        Button orders = createButton("Work Orders", Paths.get("Icons", "Bill Materials.png").toString());
        orders.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                WorkOrder workOrder = new WorkOrder();
                workOrder.viewGUI(root, stage, table);
            }
        });

        Button purchase = createButton("Purchase", Paths.get("Icons", "purchasing.png").toString());
        purchase.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                displayPurchaseForm();
            }
        });

        Button receiving = createButton("Received Orders", Paths.get("Icons", "receving.png").toString());
        receiving.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                displayReceivingOption();
            }
        });

        Button shipping = createButton("Shipped Orders", Paths.get("Icons", "shipping.png").toString());
        shipping.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                displayShipped();
            }
        });

        Button overview = createButton("Overview", Paths.get("Icons", "Customer.png").toString());
        overview.setText("Overview");
        overview.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                displayOverview();
            }
        });
        BorderPane borderPane = new BorderPane();

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.TOP_CENTER);
        hBox.getChildren().addAll(inventory, orders, purchase, receiving, shipping, overview);
        borderPane.setTop(hBox);
        borderPane.setCenter(vBox);

        showSplash(vBox);

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
        credits.setFont(Font.font(20.0));
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

    public void submitReceived() {

    }


    public void getReceiveingAmount() {
        System.out.println("Entering received parts");
        VBox rVBox = new VBox();

        Label label1 = new Label("Enter Part ID: ");
        TextField pid = new TextField ();
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
                String partId = pid.getText();
                String quantity = qtt.getText();
                System.out.println("part id from user: " + partId);
                System.out.println("quantity from user: " + quantity);
                // call Receiving class here
                ReceivingMenu menu = new ReceivingMenu();
                menu.submit(Integer.parseInt(partId), Integer.parseInt(quantity));
            }
        });
        rVBox.getChildren().addAll(hb1, hb2, submit);
        root.setCenter(rVBox);
        stage.getScene().setRoot(root);
    }

    public void displayReceivingOption(){
        System.out.println("Do you want to record?");
        VBox rVBox = new VBox();
        Label t = new Label();
        t.setText("Do you want to record?");
        Button yes = new Button("YES");
        yes.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                getReceiveingAmount();
            }
        });
        Button no = new Button("NO");
        HBox buttons = new HBox();
        buttons.getChildren().addAll(yes, no);
        rVBox.getChildren().addAll(t, buttons);
        root.setCenter(rVBox);
        stage.getScene().setRoot(root);
    }

    public void displayShipped(){
        Shipping shipping = new Shipping();
        shipping.viewGUI(root, stage, table, this);
    }


    /*****See OverviewUI ********/
    public void displayOverview() {
        //displayCompletedOrders();
        //displayBuildingOrders();
        //displayOutOfStock();

    }

    public TableView getTable() {
        return table;
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
                    if(type == Types.INTEGER){
                        tableData.add(queryResult.getInt(i));
                    }
                    else if(type == Types.VARCHAR){
                        tableData.add(queryResult.getString(i));
                    }
                    else if(type == Types.TIMESTAMP){
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
