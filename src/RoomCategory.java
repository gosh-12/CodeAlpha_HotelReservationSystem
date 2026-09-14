public enum RoomCategory {
    STANDARD(100.0),
    DELUXE(180.0),
    SUITE(300.0);

    private final double pricePerNight;

    RoomCategory(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }
}