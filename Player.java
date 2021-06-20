package battleship;

public class Player {
    private final Field field;
    private int shipsSunk;
    private String number;

    Player(int number) {
        this.field = Field.newField();
        shipsSunk = 0;
        this.number = Integer.toString(number);
    }

    public int getShipsSunk() {
        return shipsSunk;
    }

    public String getNumber() {
        return number;
    }

    public void setShipsSunk(int shipsSunk) {
        this.shipsSunk = shipsSunk;
    }

    public Field getField() {
        return field;
    }

    public void printField(boolean includeFogOfWar) {
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

    public void takeShot(Player opponent, String coordinate) {
        opponent.getField().takeShot(coordinate);
    }
}
