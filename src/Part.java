/**
 * Created by zhangJunliu on 10/8/17.
 */
public class Part {
    int partID;
    int part_number;
    int quantity;
    int orderID;
    String productName;
    String date;
    String description;
    String vendor;
    int missingQuantity;

    public void setPartID(int partID) {
        this.partID = partID;
    }

    public void setPartNumber(int part_number) {
        this.part_number = part_number;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPartID() {
        return partID;
    }

    public int getPartNumber() {
        return part_number;
    }

    public int getQuantity() {
        return quantity;
    }

    public String displayInventoty() {
        return String.format("%5s%15d%15s", getPartID(), getPartNumber(), getQuantity());

    }

}