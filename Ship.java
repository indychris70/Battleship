package battleship;

public class Ship {
    enum ShipTypes {
        A("Aircraft Carrier", 5),
        B("Battleship", 4),
        S("Submarine", 3),
        C("Cruiser", 3),
        D("Destroyer", 2);

        private String type;
        private int size;


        ShipTypes(String type, int size) {
            this.type = type;
            this.size = size;
        }

        public int getSize() {
            return size;
        }

        public String getType() {
            return type;
        }
    }

    private Coordinate[] coordinates;
    private ShipTypes type;

    Ship(ShipTypes type) {
        this.type = type;
        coordinates = new Coordinate[type.getSize()];
    }

    public Coordinate[] getCoordinates() {
        return coordinates;
    }

    public ShipTypes getType() {
        return type;
    }

    public void setCoordinate(int index, Coordinate coordinate) {
        coordinates[index] = coordinate;
    }

    public int numberOfHits() {
        int hits = 0;
        for (Coordinate c : coordinates) {
            hits = c.getStatus() == Coordinate.Status.X ? hits + 1 : hits;
        }
        return hits;
    }

    public boolean isSunk() {
        return numberOfHits() == type.getSize();
    }
}
