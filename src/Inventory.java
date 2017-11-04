/**
 * Created by zhangJunliu on 10/8/17.
 */
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * display inventory list, handle the quantity update and
 * search the part in the inventory
 */
public class Inventory {

    /**
     * Prints a command-line menu that allows the user to view the list of inventory
     * and update the quantity
     */
    public static void inventory() {

        System.out.println("=============================");
        System.out.println("======== Inventory ========");
        System.out.println("=============================\n");
        ArrayList<Part> listOfInventory = inventoryList();
        System.out.println("What you want to do?");
        System.out.println("----------------------------------------");
        System.out.println("[1] Update part quantity");
        System.out.println("[2] Parts are in low quantity");
        Scanner reader = new Scanner(System.in);
        int option = reader.nextInt();

        switch (option) {
            case 1:
                updateQuantity(listOfInventory);
                break;
            case 2:
                searchList(listOfInventory);
        }
    }

    /**
     * get the data of part id, part number and quantity and display them in a table
     * @return  table of inventory
     */
    public static ArrayList<Part> inventoryList() {
        ArrayList<Part> listOfInventories = new ArrayList<Part>();
        DBHandler testDB = new DBHandler();
        ResultSet result_part_id = testDB.select("stockroomdb.PARTS", "parts_id", new ArrayList<String>());
        ResultSet result1 = testDB.select("stockroomdb.PARTS", "part_number", new ArrayList<String>());
        ResultSet result3 = testDB.select("stockroomdb.STOCKROOM", "quantity", new ArrayList<String>());
        try{
            result_part_id.beforeFirst();
            result1.beforeFirst();
            result3.beforeFirst();

            while(result_part_id.next() && result1.next() && result3.next()){
                Part part_id = new Part();
                part_id.setPartID(result_part_id.getInt(1));
                part_id.setPartNumber(result1.getInt(1));
                part_id.setQuantity(result3.getInt(1));
                listOfInventories.add(part_id);
            }

            System.out.println(String.format("%-10s%-15s%-15s", "part_id", "part_number", "quantity"));


            for (int j = 0; j < listOfInventories.size(); j++) {
                System.out.println(listOfInventories.get(j).displayInventoty());
            }
        }catch(SQLException e){
                e.printStackTrace();
        }
        return listOfInventories;
    }

    /**
     * update the quantity of chosen part id
     * @param listOfInventories    list of inventory
     */
    public static void updateQuantity(ArrayList<Part> listOfInventories) {
        DBHandler testDB = new DBHandler();
        Scanner reader = new Scanner(System.in);
        System.out.println("part_id: ");
        int part_id = reader.nextInt();
        System.out.println("Enter a new quantity: ");
        int newQuantity = reader.nextInt();
        listOfInventories.get(part_id - 1).setQuantity(newQuantity);

        HashMap testUpdate = new HashMap();
        testUpdate.put("quantity", newQuantity);
        ArrayList<Object[]> testConditions = new ArrayList<Object[]>();
        Object cond1[] = new Object[3];
        cond1[0] = "parts_id";
        cond1[1] = "=";
        cond1[2] = part_id;

        testConditions.add(cond1);
        int result5 = testDB.update("STOCKROOM", testUpdate, testConditions);

        System.out.println("Quantity has been updated:\n" + "part_id\t " + "part_number\t " + "quantity\n" +
                listOfInventories.get(part_id - 1).displayInventoty());
    }

    /**
     * search the inventory according to the part id
     * @param listOfInventories    list of inventory
     */
    public static void searchList(ArrayList<Part> listOfInventories) {
        Scanner reader = new Scanner(System.in);
        System.out.println("Enter part_id to see the data: ");
        int part_id = reader.nextInt();
        System.out.println("part_id\t " + "part_number\t " + "quantity");
        System.out.println(listOfInventories.get(part_id - 1).displayInventoty());
    }
}

/**
 * setter and getters for the part id, part number and quantity
 */
class Part {
    int partID;
    int part_number;
    int quantity;

    /**
     * set part id
     * @param partID  part_id
     */
    public void setPartID(int partID) {
        this.partID = partID;
    }

    /**
     * set part number
     * @param part_number   part_number
     */
    public void setPartNumber(int part_number) {
        this.part_number = part_number;
    }

    /**
     * set quantity in the inventory
     * @param quantity    part quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * get part id
     * @return    part_id
     */
    public int getPartID() {
        return partID;
    }

    /**
     * get part number
     * @return    part_number
     */
    public int getPartNumber() {
        return part_number;
    }

    /**
     * get quantity in the inventory
     * @return    part quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * display the inventory table
     * @return
     */
    public String displayInventoty() {
        return String.format("%5s%15d%15s", getPartID(), getPartNumber(), getQuantity());
    }
}



