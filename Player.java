package battleship;

public class Player {
    private final Field field;
    private int shipsSunk;

    Player() {
        this.field = Field.newField();
        shipsSunk = 0;
    }

    int getShipsSunk() {
        return shipsSunk;
    }

    void setShipsSunk(int shipsSunk) {
        this.shipsSunk = shipsSunk;
    }

    Field getField() {
        return field;
    }

    void printField(boolean includeFogOfWar) {
        String[] rows = getField().getRows();
        System.out.println(Field.getHeader());

        for (int i = 0; i < rows.length; i++) {
            System.out.print(rows[i]);
            for (int j = 0; j < getField().getField()[i].length; j++) {
                String symbol;
                if (includeFogOfWar) {
                    symbol = getField().getField()[i][j].getPublicStatusSymbol();
                } else {
                    symbol = getField().getField()[i][j].getPrivateStatusSymbol();
                }
                System.out.print(String.format(" %s", symbol));
            }
            System.out.println();
        }
    }

    void takeShot(Player opponent, String coordinate) {
        opponent.getField().takeShot(coordinate);
    }
}
