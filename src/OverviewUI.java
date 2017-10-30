import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 * Created by zhangJunliu on 10/28/17.
 */
public class OverviewUI extends Application {
    public static void main(String[] args){
        launch(args);
    }
    private TableView table = new TableView();
    private VBox vBox = new VBox();
    private Stage stage = new Stage();
    private BorderPane root = new BorderPane();

    private static double imageHeight = 50;
    private static double imageWidth = 50;

    @Override
    public void start(Stage stage) {
        //sets application title

        stage.setTitle("Overview");

        //Inventory
        Button completedOders = new Button("1.Completed Orders");
        completedOders.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                displayCompletedOrders();
            }
        });

        Button buildingOders = new Button("2.Building Orders");
        buildingOders.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                displayBuildingOrders();
            }
        });

        Button outOfStock = new Button("3.Out Of Stock");
        outOfStock.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                displayOutOfStock();
            }
        });

        BorderPane borderPane = new BorderPane();

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(20, 10, 20, 10));
        hBox.setSpacing(10);
        hBox.getChildren().addAll(completedOders, buildingOders, outOfStock);
        borderPane.setTop(hBox);
        borderPane.setCenter(vBox);

        root = borderPane;

        stage.setScene(new Scene(root, 1000, 1000));
        stage.show();
        this.stage = stage;
    }

    //display completed orders in Overview
    public void displayCompletedOrders() {

        TableColumn<Part, String> idColunm = new TableColumn<>("Order ID");
        idColunm.setMinWidth(100);
        idColunm.setCellValueFactory(new PropertyValueFactory<Part, String>("orderID"));

        TableColumn<Part, String> nameColumn = new TableColumn<>("Product Name");
        nameColumn.setMinWidth(300);
        nameColumn.setCellValueFactory(new PropertyValueFactory<Part, String>("productName"));

        TableColumn<Part, String> dateColumn = new TableColumn<>("Date Complated");
        dateColumn.setMinWidth(200);
        dateColumn.setCellValueFactory(new PropertyValueFactory<Part, String>("date"));

        table = new TableView();
        table.setItems(getCompletedOrder());
        table.getColumns().addAll(idColunm, nameColumn, dateColumn);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(table);
        root.setCenter(vBox);
        stage.getScene().setRoot(root);

    }

    //display building orders in Overview
    public void displayBuildingOrders() {
        TableColumn<Part, String> idColunm = new TableColumn<>("Order ID");
        idColunm.setMinWidth(100);
        idColunm.setCellValueFactory(new PropertyValueFactory<Part, String>("orderID"));

        TableColumn<Part, String> nameColumn = new TableColumn<>("Product Name");
        nameColumn.setMinWidth(300);
        nameColumn.setCellValueFactory(new PropertyValueFactory<Part, String>("productName"));

        TableColumn<Part, String> dateColumn = new TableColumn<>("Date Building");
        dateColumn.setMinWidth(200);
        dateColumn.setCellValueFactory(new PropertyValueFactory<Part, String>("date"));

        table = new TableView();
        table.setItems(getBuildingOrder());
        table.getColumns().addAll(idColunm, nameColumn, dateColumn);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(table);
        root.setCenter(vBox);
        stage.getScene().setRoot(root);
    }

    //display outofstock in Overview
    public void displayOutOfStock() {
        TableColumn<Part, String> idColumn = new TableColumn<>("Part ID");
        idColumn.setMinWidth(100);
        idColumn.setCellValueFactory(new PropertyValueFactory<Part, String>("partID"));

        TableColumn<Part, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setMinWidth(300);
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<Part, String>("description"));

        TableColumn<Part, String> vendorColumn = new TableColumn<>("Vendor");
        vendorColumn.setMinWidth(200);
        vendorColumn.setCellValueFactory(new PropertyValueFactory<Part, String>("vendor"));

        TableColumn<Part, String> missingQuantityColumn = new TableColumn<>("Missing Quantity");
        missingQuantityColumn.setMinWidth(200);
        missingQuantityColumn.setCellValueFactory(new PropertyValueFactory<Part, String>("missingQuantity"));

        table = new TableView();
        table.setItems(getOutOfStock());
        table.getColumns().addAll(idColumn, descriptionColumn, vendorColumn, missingQuantityColumn);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(table);
        root.setCenter(vBox);
        stage.getScene().setRoot(root);
    }

    //helper method for Overview
    public ObservableList<Part> getCompletedOrder() {
        ObservableList<Part> completedOrderList = FXCollections.observableArrayList();
        Overview status = new Overview();
        Part part = new Part();

        for (int i = 0; i < status.orderCompleted().size(); i++) {
            part.setOrderID(status.orderCompleted().get(i).getOrderID());
            part.setProductName(status.orderCompleted().get(i).getProductName());
            part.setDate(status.orderCompleted().get(i).getDate());
            completedOrderList.add(part);
        }
        return completedOrderList;
    }

    //helper method for Overview
    public ObservableList<Part> getBuildingOrder() {
        ObservableList<Part> BuildingOrderList = FXCollections.observableArrayList();
        Overview status = new Overview();
        Part part = new Part();

        for (int i = 0; i < status.buildingOrders().size(); i++) {
            part.setOrderID(status.buildingOrders().get(i).getOrderID());
            part.setProductName(status.buildingOrders().get(i).getProductName());
            part.setDate(status.buildingOrders().get(i).getDate());
            BuildingOrderList.add(part);
        }
        return BuildingOrderList;
    }

    //helper method for Overview
    public ObservableList<Part> getOutOfStock() {
        ObservableList<Part> outOfStockList = FXCollections.observableArrayList();
        Purchasing outOfStock = new Purchasing();
        Part part = new Part();

        for (int i = 0; i < outOfStock.outOfStock().size(); i++) {
            part.setPartID(outOfStock.outOfStock().get(i).getPartID());
            part.setDescription(outOfStock.outOfStock().get(i).getDescription());
            part.setVendor(outOfStock.outOfStock().get(i).getVendor());
            part.setMissingQuantity(outOfStock.outOfStock().get(i).getMissingQuantity());
            outOfStockList.add(part);
        }
        return outOfStockList;
    }
}
