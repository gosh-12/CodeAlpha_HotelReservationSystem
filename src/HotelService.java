import java.io.*;
import java.util.*;

public class HotelService {

    private static final String DATA_FILE = "bookings.txt";

    private final List<Room> rooms = new ArrayList<>();
    private final Map<String, Reservation> reservations = new HashMap<>();

    public HotelService() {
        initializeRooms();
        loadReservationsFromFile();
    }

    private void initializeRooms() {
        rooms.add(new Room(101, RoomCategory.STANDARD));
        rooms.add(new Room(102, RoomCategory.STANDARD));
        rooms.add(new Room(201, RoomCategory.DELUXE));
        rooms.add(new Room(202, RoomCategory.DELUXE));
        rooms.add(new Room(301, RoomCategory.SUITE));
    }

    private void loadReservationsFromFile() {

        File file = new File(DATA_FILE);

        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {

                Reservation res = Reservation.fromCsvRecord(line.trim());

                if (res != null) {

                    reservations.put(res.getBookingId(), res);

                    for (Room r : rooms) {

                        if (r.getRoomNumber() == res.getRoomNumber()) {
                            r.setAvailable(false);
                            break;
                        }
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("File loading error: " + e.getMessage());
        }
    }

    private void saveReservationsToFile() {

        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(DATA_FILE))) {

            for (Reservation res : reservations.values()) {

                writer.write(res.toCsvRecord());
                writer.newLine();
            }

        } catch (IOException e) {
            System.err.println("File saving error: " + e.getMessage());
        }
    }

    public List<Room> getAllRooms() {
        return rooms;
    }

    public List<Room> getAvailableRooms(RoomCategory category) {

        List<Room> available = new ArrayList<>();

        for (Room r : rooms) {

            if (r.isAvailable() &&
                    (category == null || r.getCategory() == category)) {

                available.add(r);
            }
        }

        return available;
    }

    public Room findAvailableRoom(int roomNumber) {

        for (Room r : rooms) {

            if (r.getRoomNumber() == roomNumber && r.isAvailable()) {
                return r;
            }
        }

        return null;
    }

    public Reservation bookRoom(
            int roomNumber,
            String guestName,
            int nights) {

        Room room = findAvailableRoom(roomNumber);

        if (room == null) {
            return null;
        }

        double total =
                room.getCategory().getPricePerNight() * nights;

        String bookingId =
                "CA-" +
                UUID.randomUUID()
                        .toString()
                        .substring(0, 6)
                        .toUpperCase();

        Reservation res = new Reservation(
                bookingId,
                guestName,
                roomNumber,
                room.getCategory(),
                nights,
                total
        );

        room.setAvailable(false);

        reservations.put(bookingId, res);

        saveReservationsToFile();

        return res;
    }

    public boolean cancelReservation(String bookingId) {

        Reservation res = reservations.get(bookingId);

        if (res == null) {
            return false;
        }

        for (Room r : rooms) {

            if (r.getRoomNumber() == res.getRoomNumber()) {

                r.setAvailable(true);
                break;
            }
        }

        reservations.remove(bookingId);

        saveReservationsToFile();

        return true;
    }

    public Collection<Reservation> getAllReservations() {
        return reservations.values();
    }
}