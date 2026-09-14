public class Room {
    private final int roomNumber;
    private final RoomCategory category;
    private boolean isAvailable;

    public Room(int roomNumber, RoomCategory category) {
        this.roomNumber = roomNumber;
        this.category = category;
        this.isAvailable = true;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public RoomCategory getCategory() {
        return category;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }

    @Override
    public String toString() {
        return String.format("Room #%-3d | Type: %-8s | Rate: $%.2f/night | Status: %s",
                roomNumber, category, category.getPricePerNight(), isAvailable ? "Available" : "Booked");
    }
}