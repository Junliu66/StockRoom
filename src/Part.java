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

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public void setMissingQuantity(int missingQuantity) {
        this.missingQuantity = missingQuantity;
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

    public int getOrderID() {
        return orderID;
    }

    public String getProductName() {
        return productName;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getVendor() {
        return vendor;
    }

    public int getMissingQuantity() {
        return missingQuantity;
    }

    public String displayInventoty() {
        return String.format("%5s%15d%15s", getPartID(), getPartNumber(), getQuantity());

    }


}