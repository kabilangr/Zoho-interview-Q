import java.util.List;

public class DeliveryExecutive {
    // Delivery Executive details
    static int count = 0;
    int id; // unique id
    boolean onRoute;
    int totalEarnings;
    List<Delivery> deliveryDetails;

    public DeliveryExecutive() { // constructor for initial values
        onRoute = false;
        count = count + 1;
        id = count;
        totalEarnings = 0;
        deliveryDetails = null;
    }
}
