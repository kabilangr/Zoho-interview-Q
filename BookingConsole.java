import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class BookingConsole {
    static int id = 1;

    // creating Executives
    public static List<DeliveryExecutive> createExecutives(int n) {
        List<DeliveryExecutive> deliveryExecutives = new ArrayList<DeliveryExecutive>();
        for (int i = 0; i < n; i++) {
            DeliveryExecutive newExecutive = new DeliveryExecutive();
            deliveryExecutives.add(newExecutive);
        }
        return deliveryExecutives;
    }

    // handling booking events
    public static List<DeliveryExecutive> handleBooking(char restaurant, char destination, double pickupTime,
            List<DeliveryExecutive> deliveryExecutives) {
        Collections.sort(deliveryExecutives, (a, b) -> a.totalEarnings - b.totalEarnings);
        deliveryExecutives = assignDeliveryExecutive(restaurant, destination, pickupTime,
                deliveryExecutives);
        return deliveryExecutives;
    }

    // assigning delivery to an delivery executive
    public static List<DeliveryExecutive> assignDeliveryExecutive(
            char restaurant,
            char destination,
            double pickupTime,
            List<DeliveryExecutive> deliveryExecutive) {
        boolean flag = false;
        int allottedDelivery = 0;
        List<DeliveryExecutive> newList = new ArrayList<DeliveryExecutive>();
        // combine order posibility checking and combining if posible
        for (DeliveryExecutive de : deliveryExecutive) {
            List<Delivery> temp = new ArrayList<Delivery>();
            if (de.deliveryDetails != null)
                for (Delivery delivery : de.deliveryDetails) {
                    if (delivery.restaurant == restaurant
                            && delivery.destination == destination
                            && (delivery.startTime + 0.25) > pickupTime
                            && delivery.startTime <= pickupTime
                            && !flag && delivery.combineOrders < 5) {
                        delivery.combineOrders = delivery.combineOrders + 1;
                        delivery.id = id++;
                        if (delivery.time < pickupTime)
                            delivery.time = pickupTime;
                        delivery.charge = delivery.charge + 5;
                        de.totalEarnings = de.totalEarnings + 5;
                        flag = true;
                        allottedDelivery = de.id;
                    }
                    temp.add(delivery);
                }
            de.deliveryDetails = temp;
            newList.add(de);
        }
        if (!flag) {
            newList = new ArrayList<DeliveryExecutive>();
            for (DeliveryExecutive de : deliveryExecutive) {
                if (allottedDelivery == 0) {
                    // creating new delivery for delivery executive
                    if (!de.onRoute) {
                        Delivery newDelivery = new Delivery();
                        List<Delivery> listDelivery = new ArrayList<Delivery>();
                        newDelivery.charge = 50;
                        de.totalEarnings = de.totalEarnings + newDelivery.charge;
                        de.onRoute = true;
                        newDelivery.combineOrders = 1;
                        newDelivery.destination = destination;
                        newDelivery.restaurant = restaurant;
                        newDelivery.time = pickupTime;
                        newDelivery.startTime = pickupTime;
                        newDelivery.id = id++;
                        listDelivery.add(newDelivery);
                        de.deliveryDetails = listDelivery;
                        allottedDelivery = de.id;
                    } else {
                        // adding delivery to delivery executive
                        if (de.deliveryDetails != null)
                            for (Delivery delivery : de.deliveryDetails) {
                                if (delivery.time + 1 < pickupTime) {
                                    Delivery newDelivery = new Delivery();
                                    List<Delivery> listDelivery = new ArrayList<Delivery>();
                                    newDelivery.charge = 50;
                                    de.totalEarnings = de.totalEarnings + newDelivery.charge;
                                    de.onRoute = true;
                                    newDelivery.combineOrders = 1;
                                    newDelivery.destination = destination;
                                    newDelivery.restaurant = restaurant;
                                    newDelivery.startTime = pickupTime;
                                    newDelivery.time = pickupTime;
                                    newDelivery.id = id++;
                                    listDelivery.add(newDelivery);
                                    de.deliveryDetails = listDelivery;
                                    allottedDelivery = de.id;
                                }
                            }
                    }
                }
                newList.add(de);
            }
        }
        Collections.sort(newList, (a, b) -> a.id - b.id);

        // check if slots are available
        if (allottedDelivery == 0) {
            System.out.println("\n\nNo Slots available");
            System.out.println("----------------------xxxxxxxxxxxxxxxxxxx--------------------");
        } else {
            // displaying executives and earnings
            System.out.println("--------------------------");
            System.out.println("Booking ID : " + id);
            System.out.println("Available Executives :");
            System.out.println("-------------------------------------------------------------");
            System.out.println("Executive\t\tDelivery Charge Earned");
            System.out.println("-------------------------------------------------------------");

            for (DeliveryExecutive listdelivery : newList) {
                System.out.println("DE" + listdelivery.id + "\t\t" + listdelivery.totalEarnings);
            }
            System.out.println("-------------------------------------------------------------");
            System.out.println("Allotted Delivery Executive: DE" + allottedDelivery
                    + (flag ? " (because same location within 15mins)" : ""));
            System.out.println("----------------------xxxxxxxxxxxxxxxxxxx--------------------");

        }
        return newList;
    }

    // changing time format from double to string
    public static String timeFormatChange(double time) {
        int min = (int) ((time - Math.floor(time)) * 60);
        String strTime = time > 12 ? (int) time - 12 + ":" + min : (int) Math.floor(time) + ":" + min;
        strTime = strTime + ((time >= 12) ? " PM" : " AM");
        return strTime;
    }

    // changing time format from string to double
    public static double timeFormatChange(String time) {
        char ch = ' ';
        double hr = 0;
        String str = "";
        time = time + " ";
        for (int i = 0; i < time.length(); i++) {
            ch = time.charAt(i);
            if (ch == '.' || ch == ':') {
                hr = Double.parseDouble(str);
                str = "";
                continue;
            }
            if (ch == ' ' || (Character.isLetter(ch) && str != "")) {
                hr = hr + ((double) Integer.parseInt(str) / 60);
                break;
            }
            str = str + ch;
        }
        if (hr != 12 && (time.contains("PM") || time.contains("pm") || time.contains("p") || time.contains("Pm")))
            hr = hr + 12;
        return hr;
    }

    // display details about delivery and delivery executives
    public static void displayDeliveryExecutive(List<DeliveryExecutive> deliveryExecutives) {
        int i = 1;
        System.out.println("Delivery History");
        System.out.println(
                "---------------------------------------------------------------------------------------------------------------------");
        System.out.println(
                "Trip\tDelivery_Time\tExecutive\tDelivery Charge\tResturant\tDestination\torders\tPick-up_Time");
        System.out.println(
                "---------------------------------------------------------------------------------------------------------------------");
        // displaying each delivery and the delivery executive
        for (DeliveryExecutive de : deliveryExecutives) {
            if (de.deliveryDetails != null)
                for (Delivery delivery : de.deliveryDetails) {
                    System.out.println(i++ + "\t" + timeFormatChange(delivery.time + 0.5) + "\tDE" + de.id
                            + "\t" + delivery.charge + "\t" + delivery.restaurant + "\t" + delivery.destination + "\t" +
                            +delivery.combineOrders + "\t" + timeFormatChange(delivery.time));
                }
        }
        System.out.println(
                "---------------------------------------------------------------------------------------------------------------------\n\n");
        System.out.println("Total earned");
        System.out.println("-------------------------------------------------------------------------------");
        System.out.println("Executive\tAllowance\tDeliver Charge\tTotal");
        System.out.println("-------------------------------------------------------------------------------");

        // printing delivery executive and total earning
        for (DeliveryExecutive de : deliveryExecutives) {
            int allowance = de.deliveryDetails.size() * 10;
            System.out.println(
                    "DE" + de.id + "\t" + allowance + "\t" + de.totalEarnings + "\t" + (de.totalEarnings + allowance));
        }
        System.out.println("-------------------------------------------------------------------------------");
        System.out.println(
                "----------------------------------------------xxxxxxxxxxxxxxxxxxx-------------------------------------------");
    }

    // main
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter the number of delivery executives");
        int n = in.nextInt(); // no. of DeliveryExecutives
        List<DeliveryExecutive> deliveryExecutives = createExecutives(n);
        while (true) {
            System.out.println("-------------------------------------------------------------");
            System.out.println("1 => Assign delivery executive");
            System.out.println("2 => Display delivery executive's activity");
            System.out.println("3 => Exit");
            System.out.println("Enter the choice");
            System.out.println("-------------------------------------------------------------");
            int choice = in.nextInt();
            switch (choice) {
                case 1: {
                    // input delivery details
                    System.out.println("Enter Restaurant");
                    char restaurant = in.next().charAt(0);
                    System.out.println("Enter Destination");
                    char destination = in.next().charAt(0);
                    System.out.println("Enter Time in \"HH:mm a\" format");
                    String strTime = in.next();
                    if (strTime.contains(":") || strTime.contains(".")) { // time format check
                        Double pickupTime = timeFormatChange(strTime);
                        deliveryExecutives = handleBooking(restaurant, destination, pickupTime, deliveryExecutives);
                    } else {
                        System.out.println("\n\nWrong time format");
                        System.out.println("----------------------xxxxxxxxxxxxxxxxxxx--------------------");
                    }
                    break;
                }
                case 2: {
                    // display details
                    displayDeliveryExecutive(deliveryExecutives);
                    break;
                }
                default: // Exiting
                    return;
            }
        }
    }
}
