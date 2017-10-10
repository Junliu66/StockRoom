/**
 * Created by zhangJunliu on 10/8/17.
 */
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class Inventory {

    public static void main(String[] args){
        DBHandler testDB = new DBHandler();
        ResultSet result_part_id = testDB.select("stockroomdb.PARTS", "parts_id", new ArrayList<String>());

        ResultSet result1 = testDB.select("stockroomdb.PARTS", "part_number", new ArrayList<String>());
        //ResultSet result2 = testDB.select("stockroomdb.PARTS", "part_description", new ArrayList<String>());
        ResultSet result3 = testDB.select("stockroomdb.STOCKROOM", "quantity", new ArrayList<String>());
        try{
            result_part_id.beforeFirst();
            result1.beforeFirst();
            //result2.beforeFirst();
            result3.beforeFirst();
            ArrayList<Part> listOfInventories = new ArrayList<Part>();

            while(result_part_id.next() && result1.next() && result3.next()){
                Part part_id = new Part();
                part_id.setPartID(result_part_id.getInt(1));
                part_id.setPartNumber(result1.getInt(1));
                part_id.setQuantity(result3.getInt(1));
                listOfInventories.add(part_id);
            }

            System.out.println("part_id\t " + "part_number\t " + "quantity");

            for (int j = 0; j < listOfInventories.size(); j++) {
                System.out.println(listOfInventories.get(j).displayInventoty());
            }

            Scanner reader = new Scanner(System.in);
            System.out.println("You can:\n 1. Update part quantity\n 2. Search part list\n Enter 1 or 2 to choose");
            int option = reader.nextInt();
            if (option == 1) {
                System.out.println("part_id: ");
                int part_id = reader.nextInt();
                System.out.println("Enter a new quantity: ");
                int newQuantity = reader.nextInt();
                listOfInventories.get(part_id - 1).setQuantity(newQuantity);
                System.out.println("Quantity has been updated:\n" + "part_id\t " + "part_number\t " + "quantity\n" +
                        listOfInventories.get(part_id - 1).displayInventoty());
            }
            else if (option == 2) {
                System.out.println("Enter part_id to see the data: ");
                int part_id = reader.nextInt();
                System.out.println("part_id\t " + "part_number\t " + "quantity");
                System.out.println(listOfInventories.get(part_id - 1).displayInventoty());
            }

        }
        catch(SQLException e){
            e.printStackTrace();
        }

    }
}