public class Delivery {
    // Each Delivery details
    static int bookingId = 0;
    int id; // unique id
    int combineOrders;
    double time;
    double startTime;
    char destination;
    char restaurant;
    int charge;

    public Delivery() { // constructor for initial values
        bookingId = bookingId + 1;
        id = bookingId;
        restaurant = '\0';
        destination = '\0';
        combineOrders = 0;
        time = 0;
        startTime = 0;
        charge = 0;
    }
}
