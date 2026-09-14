import java.util.List;
import java.util.Scanner;

public class HotelReservationSystem {
    private final HotelService service = new HotelService();
    private final Scanner scanner = new Scanner(System.in);

    public void start() {
        while (true) {
            System.out.println("\n==============================================");
            System.out.println("      CODEALPHA HOTEL RESERVATION SYSTEM      ");
            System.out.println("==============================================");
            System.out.println("1. View All Rooms");
            System.out.println("2. Search Available Rooms by Category");
            System.out.println("3. Book a Room");
            System.out.println("4. Cancel a Reservation");
            System.out.println("5. View All Active Bookings");
            System.out.println("6. Exit");

            int choice = getIntInput("Select an option (1-6): ");
            switch (choice) {
                case 1 -> displayRooms(service.getAllRooms());
                case 2 -> searchRooms();
                case 3 -> handleBooking();
                case 4 -> handleCancellation();
                case 5 -> displayActiveBookings();
                case 6 -> {
                    System.out.println("Thank you for using CodeAlpha Hotel System. Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice. Please select from 1 to 6.");
            }
        }
    }

    private void displayRooms(List<Room> rooms) {
        System.out.println("\n--- Room List ---");
        for (Room r : rooms) {
            System.out.println(r);
        }
    }

    private void searchRooms() {
        System.out.println("\nChoose Category to Filter:");
        System.out.println("1. Standard ($100/night)");
        System.out.println("2. Deluxe ($180/night)");
        System.out.println("3. Suite ($300/night)");
        System.out.println("4. Show All Available");

        int choice = getIntInput("Filter choice: ");
        RoomCategory cat = switch (choice) {
            case 1 -> RoomCategory.STANDARD;
            case 2 -> RoomCategory.DELUXE;
            case 3 -> RoomCategory.SUITE;
            default -> null;
        };

        List<Room> available = service.getAvailableRooms(cat);
        if (available.isEmpty()) {
            System.out.println("No rooms available for the selected criteria.");
        } else {
            displayRooms(available);
        }
    }

    private void handleBooking() {
        searchRooms();
        int roomNum = getIntInput("\nEnter Room Number to book: ");

        Room room = service.findAvailableRoom(roomNum);
        if (room == null) {
            System.out.println("Room #" + roomNum + " is either booked or does not exist.");
            return;
        }

        System.out.print("Enter Guest Full Name: ");
        String name = scanner.nextLine().trim();

        int nights = getIntInput("Enter Number of Nights: ");
        if (nights <= 0) {
            System.out.println("Duration must be at least 1 night.");
            return;
        }

        double total = room.getCategory().getPricePerNight() * nights;
        System.out.printf("Total charge: $%.2f\n", total);
        System.out.print("Proceed to payment simulation? (yes/no): ");
        String confirm = scanner.nextLine().trim();

        if (!confirm.equalsIgnoreCase("yes")) {
            System.out.println("Booking transaction aborted.");
            return;
        }

        System.out.println("\n[Payment Gateway Simulation] Contacting banking network...");
        System.out.println("[Payment Gateway Simulation] Verification successful. Total paid: $" + total);

        Reservation res = service.bookRoom(roomNum, name, nights);
        System.out.println("\n--- Booking Confirmed ---");
        System.out.println(res);
    }

    private void handleCancellation() {
        System.out.print("\nEnter Booking ID to cancel: ");
        String bookingId = scanner.nextLine().trim();

        boolean cancelled = service.cancelReservation(bookingId);
        if (cancelled) {
            System.out.println("Reservation " + bookingId + " has been cancelled. Room is now available.");
        } else {
            System.out.println("Booking ID not found.");
        }
    }

    private void displayActiveBookings() {
        System.out.println("\n--- Active Bookings ---");
        var list = service.getAllReservations();
        if (list.isEmpty()) {
            System.out.println("No active reservations recorded.");
            return;
        }
        for (Reservation r : list) {
            System.out.println(r);
        }
    }

    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format. Try again.");
            }
        }
    }

    public static void main(String[] args) {
        new HotelReservationSystem().start();
    }
}
