import java.util.*;
import java.io.*;

class Room implements Serializable { // ✅ Make Room serializable for file saving
    int roomNumber;
    boolean isBooked;
    String customerName;
    int nights;
    static final double PRICE_PER_NIGHT = 1000.0;

    Room(int number) {
        this.roomNumber = number;
        this.isBooked = false;
    }

    double calculateBill() {
        double base = nights * PRICE_PER_NIGHT;
        double gst = base * 0.18;
        return base + gst;
    }

    public String toString() {
        return "Room No: " + roomNumber + " | " +
               (isBooked ? "Booked by " + customerName : "Available");
    }
}

public class HotelBookingSystem {
    static Scanner sc = new Scanner(System.in);
    static ArrayList<Room> rooms = new ArrayList<>();

    public static void main(String[] args) {
        loadRooms();
        System.out.println("==== Welcome to Priya Hotel Booking System ====");
        System.out.print("Enter admin password to continue: ");
        String pwd = sc.nextLine();
        if (!pwd.equals("admin123")) {
            System.out.println("Access Denied. Exiting...");
            return;
        }

        while (true) {
            System.out.println("\n1. View Rooms\n2. Book Room\n3. Cancel Booking\n4. Add Room\n5. Exit");
            System.out.print("Enter your choice: ");
            int ch = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (ch) {
                case 1: viewRooms(); break;
                case 2: bookRoom(); break;
                case 3: cancelBooking(); break;
                case 4: addRoom(); break;
                case 5: saveRooms(); System.out.println("Thank you!"); return;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    static void viewRooms() {
        if (rooms.isEmpty()) {
            System.out.println("No rooms available.");
            return;
        }
        for (Room r : rooms) {
            System.out.println(r);
        }
    }

    static void bookRoom() {
        System.out.print("Enter room number to book: ");
        int number = sc.nextInt(); sc.nextLine();
        for (Room r : rooms) {
            if (r.roomNumber == number) {
                if (r.isBooked) {
                    System.out.println("Room already booked.");
                    return;
                }
                System.out.print("Enter customer name: ");
                r.customerName = sc.nextLine();
                System.out.print("Enter number of nights: ");
                r.nights = sc.nextInt(); sc.nextLine();
                r.isBooked = true;
                System.out.printf("Room booked! Bill (with 18%% GST): ₹%.2f\n", r.calculateBill());
                return;
            }
        }
        System.out.println("Room not found.");
    }

    static void cancelBooking() {
        System.out.print("Enter room number to cancel: ");
        int number = sc.nextInt(); sc.nextLine();
        for (Room r : rooms) {
            if (r.roomNumber == number && r.isBooked) {
                r.isBooked = false;
                r.customerName = null;
                r.nights = 0;
                System.out.println("Booking canceled.");
                return;
            }
        }
        System.out.println("Room not found or not booked.");
    }

    static void addRoom() {
        System.out.print("Enter new room number: ");
        int number = sc.nextInt(); sc.nextLine();
        for (Room r : rooms) {
            if (r.roomNumber == number) {
                System.out.println("Room already exists.");
                return;
            }
        }
        rooms.add(new Room(number));
        System.out.println("Room added.");
    }

    static void saveRooms() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("rooms.dat"))) {
            out.writeObject(rooms);
        } catch (IOException e) {
            System.out.println("Error saving data.");
        }
    }

    @SuppressWarnings("unchecked")
    static void loadRooms() {
        File file = new File("rooms.dat");
        if (file.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                rooms = (ArrayList<Room>) in.readObject();
            } catch (Exception e) {
                System.out.println("Error loading data.");
            }
        }
    }
}
