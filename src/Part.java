/**
 * Created by zhangJunliu on 10/8/17.
 */
public class Part {
    int part_id;
    int part_number;
    int quantity;

    public void setPartID(int part_id) {
        this.part_id = part_id;
    }

    public void setPartNumber(int part_number) {
        this.part_number = part_number;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public int getPartID() {
        return part_id;
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