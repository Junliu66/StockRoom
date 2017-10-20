import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.sql.Time;

/**
 * Created by zhangJunliu on 10/19/17.
 */
public class Overview {
    //ArrayList<Part> listOfFinishedOrders = new ArrayList<Part>();
    //ArrayList<Part> listOfOrdersInProgress = new ArrayList<Part>();
    ArrayList<Part> listOfPartsInShortage = new ArrayList<Part>();

    public static void overView() {
        orderStatus();

    }

    public static void orderStatus() {
        ArrayList<Part> listOfFinishedOrders = new ArrayList<Part>();
        ArrayList<Part> listOfOrdersInProgress = new ArrayList<Part>();

        DBHandler testDB = new DBHandler();
        ResultSet order_id = testDB.select("stockroomdb.WORKORDERS", "order_id", new ArrayList<String>());
        ResultSet status = testDB.select("stockroomdb.WORKORDERS", "status", new ArrayList<String>());
        ResultSet date_completed = testDB.select("stockroomdb.WORKORDERS", "date_completed", new ArrayList<String>());
        try {
            order_id.beforeFirst();
            status.beforeFirst();
            date_completed.beforeFirst();

            while (order_id.next() && status.next() && date_completed.next()) {
                if (status.getString(1).equals("COMPLETED")) {
                    Part order = new Part();
                    order.setOrderID(order_id.getInt(1));

                    //order.setDate(date_completed.getDate(1));

                    listOfFinishedOrders.add(order);

                }
                else if (status.getString(1).equals("KITTED") || status.getString(1).equals("CREATED")) {
                    Part order = new Part();
                    order.setOrderID((order_id.getInt(1)));
                    listOfOrdersInProgress.add(order);
                }


            }



        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println(String.format("\n%-10s%-15s", "kit#", "Date Done"));
        for (int i = 0; i < listOfFinishedOrders.size(); i++) {
            System.out.println(listOfFinishedOrders.get(i).displayOrderStatus());
        }

        System.out.println(String.format("%-10s%-15s", "kit#", "Date Started"));
        for (int i = 0; i < listOfOrdersInProgress.size(); i++) {
            System.out.println(listOfOrdersInProgress.get(i).displayOrderStatus());
        }

        }


}
