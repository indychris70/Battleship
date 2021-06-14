package battleship;

import java.util.Arrays;
import battleship.Coordinate.Status;

public class Field {
    private enum Messages {
        ERROR_SHIPS_OVERLAP("Error: May not overlap ships."),
        ERROR_WOULD_BE_ADJACENT("Error: May not place ship directly adjacent to another ship."),
        ERROR_WRONG_NUMBER_OF_COORDINATES("Did not receive %s coordinates"),
        ERROR_DIAGONAL_PLACEMENT("Ship must be placed horizontally (same row) or vertically (same column)."),
        ERROR_COORDINATES_DO_NOT_MATCH_SHIP_LENGTH("You entered a range of %s coordinates, which does not match the ship length of %s");

        String text;

        Messages(String text) {
            this.text = text;
        }

        private void print(String... subStrings) {
            System.out.println(String.format(text, subStrings));
        }
    }

    enum Ships {
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
            coordinates = new Coordinate[size];
        }
    }

    private final String[] rows = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
    private final String[] columns = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    private final Coordinate[][] field = new Coordinate[rows.length][columns.length];
    private final String header = "  " + String.join(" ", columns);
    private final int NUMBER_OF_COORDINATES = 2;

    private Field() {
        for (int i = 0; i < rows.length; i++) {
            for (int j = 0; j < 10; j++) {
                field[i][j] = new Coordinate(i, j);
            }
        }
    }

    static Field newField() {
        return new Field();
    }

    Coordinate[][] getField() {
        return field;
    }

    String getHeader() {
        return header;
    }

    String[] getRows() {
        return rows;
    }

    Ships[] getShips() {
        return Ships.values();
    }

    boolean placeShip(Ships ship, String coordinateString) {
        String[] coordinates = coordinateString.split(" ");
        if (isValid(coordinates, ship)) {
            int firstRow = Arrays.asList(rows).indexOf(coordinates[0].substring(0, 1));
            int firstColumn = Integer.parseInt(coordinates[0].substring(1));
            int secondRow = Arrays.asList(rows).indexOf(coordinates[1].substring(0, 1));
            int secondColumn = Integer.parseInt(coordinates[1].substring(1));

            if (wouldOverlap(firstRow, firstColumn, secondRow, secondColumn) || wouldBeAdjacent(firstRow, firstColumn, secondRow,secondColumn)) {
                return false;
            }

            for (int i = Math.min(firstRow, secondRow); i <= Math.max(firstRow, secondRow); i++) {
                for (int j = Math.min(firstColumn, secondColumn) - 1; j < Math.max(firstColumn, secondColumn); j++) {
                    field[i][j].setStatus(Status.O);
                }
            }
            return true;
        }
        return false;
    }
    
    private boolean wouldOverlap(int firstRow, int firstColumn, int secondRow, int secondColumn) {
        for (int i = Math.min(firstRow, secondRow); i <= Math.max(firstRow, secondRow); i++) {
            for (int j = Math.min(firstColumn, secondColumn) - 1; j < Math.max(firstColumn, secondColumn); j++) {
                if (field[i][j].getStatus() == Status.O) {
                    Messages.ERROR_SHIPS_OVERLAP.print();
                    return true;
                }
            }
        }
        return false;
    }

    private boolean indexInBounds(int row, int column) {
        return row >= 0 && row < rows.length && column >= 0 && column < columns.length;
    }

    private boolean coordinateOccupied(int row, int column) {
        return Status.O == field[row][column].getStatus();
    }
    
    private boolean wouldBeAdjacent(int firstRow, int firstColumn, int secondRow, int secondColumn) {
        int[][] adjacentCoordinateOffsets = new int[][] {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int i = Math.min(firstRow, secondRow); i <= Math.max(firstRow, secondRow); i++) {
            for (int j = Math.min(firstColumn, secondColumn) - 1; j < Math.max(firstColumn, secondColumn); j++) {
                for (int[] offset : adjacentCoordinateOffsets) {
                    if (indexInBounds(i + offset[0], j + offset[1]) && coordinateOccupied(i + offset[0], j + offset[1])) {
                        Messages.ERROR_WOULD_BE_ADJACENT.print();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isValid(String[] coordinates, Ships ship) {
        boolean isValid = true;
        if (coordinates.length != NUMBER_OF_COORDINATES) {
            Messages.ERROR_WRONG_NUMBER_OF_COORDINATES.print(Integer.toString(NUMBER_OF_COORDINATES));
            isValid = false;
        }
        String firstRow = coordinates[0].substring(0, 1);
        String firstColumn = coordinates[0].substring(1);
        String secondRow = coordinates[1].substring(0, 1);
        String secondColumn = coordinates[1].substring(1);
        boolean onSameRow = firstRow.equals(secondRow);
        boolean onSameColumn = firstColumn.equals(secondColumn);
        if (!(onSameRow || onSameColumn)) {
            Messages.ERROR_DIAGONAL_PLACEMENT.print();
            isValid = false;
        }
        int numberOfCoordinates = 0;
        if (onSameRow) {
            numberOfCoordinates = Math.abs(Integer.parseInt(firstColumn) - Integer.parseInt(secondColumn)) + 1;
        } else if (onSameColumn) {
            numberOfCoordinates = Math.abs(Arrays.asList(rows).indexOf(firstRow) - Arrays.asList(rows).indexOf(secondRow)) + 1;
        }
        if (numberOfCoordinates != ship.size) {
            Messages.ERROR_COORDINATES_DO_NOT_MATCH_SHIP_LENGTH.print(Integer.toString(numberOfCoordinates), Integer.toString(ship.size));
            isValid = false;
        }
        return isValid;
    }
}
