/**
 * Created by zhangJunliu on 10/8/17.
 */
public class Part {
    int part_id;
    int part_number;
    int quantity;
    int lowQuantity;
    int order_id;


    public void setPartID(int part_id) {
        this.part_id = part_id;
    }

    public void setPartNumber(int part_number) {
        this.part_number = part_number;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setLowQuantity(int lowQuantity) {
        this.lowQuantity = lowQuantity;
    }

    public void setOrderID (int order_id) {
        this.order_id = order_id;
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

    public int getLowQuantity() {
        return lowQuantity;
    }

    public int getOrderID() {
        return order_id;
    }

    public String displayInventoty() {
        return String.format("%5s%15d%15s", getPartID(), getPartNumber(), getQuantity());

    }

    public String displayInventoryWithLowQuantity() {
        return String.format("%5s%15d%15s%15s", getPartID(), getPartNumber(), getQuantity(),getLowQuantity());
    }

    public String displayOrderStatus() {
        return String.format("%s", getOrderID());
    }


}