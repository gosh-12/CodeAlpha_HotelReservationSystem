
    public class Reservation {
    private final String bookingId;
    private final String guestName;
    private final int roomNumber;
    private final RoomCategory category;
    private final int nights;
    private final double totalAmount;

    public Reservation(String bookingId, String guestName, int roomNumber,
                       RoomCategory category, int nights, double totalAmount) {
        this.bookingId = bookingId;
        this.guestName = guestName;
        this.roomNumber = roomNumber;
        this.category = category;
        this.nights = nights;
        this.totalAmount = totalAmount;
    }

    public String getBookingId() {
        return bookingId;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public String toCsvRecord() {
        return String.join(",",
                bookingId,
                guestName,
                String.valueOf(roomNumber),
                category.name(),
                String.valueOf(nights),
                String.valueOf(totalAmount));
    }

    public static Reservation fromCsvRecord(String csvLine) {
        String[] parts = csvLine.split(",");

        if (parts.length != 6) {
            return null;
        }

        return new Reservation(
                parts[0],
                parts[1],
                Integer.parseInt(parts[2]),
                RoomCategory.valueOf(parts[3]),
                Integer.parseInt(parts[4]),
                Double.parseDouble(parts[5])
        );
    }

    @Override
    public String toString() {
        return String.format(
                "Booking ID: %s | Guest: %s | Room: #%d (%s) | Duration: %d nights | Paid: $%.2f",
                bookingId,
                guestName,
                roomNumber,
                category,
                nights,
                totalAmount
        );
    }
}