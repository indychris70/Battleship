package battleship;

public class Field {

    private enum Ships {
        AC("Aircraft Carrier", 5),
        B("Battleship", 4),
        S("Submarine", 3),
        C("Cruiser", 3),
        D("Destroyer", 2);

        String type;
        int size;
        Coordinate[] coordinates;

        Ships(String type, int size) {
            this.type = type;
            this.size = size;
        }
    }

    private Player player;
    Coordinate[][] field = new Coordinate[Rows.values().length][10];
    String header = "  1 2 3 4 5 6 7 8 9 10";

    Field(Player player) {
        this.player = player;
        for (Rows row : Rows.values()) {
            for (int i = 0; i < 10; i++) {
                field[row.getIndex()][i] = new Coordinate(row.getIndex(), i);
            }
        }
    }

    Coordinate[][] getField() {
        return field;
    }

    String getHeader() {
        return header;
    }
}
