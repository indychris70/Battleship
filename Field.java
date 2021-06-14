package battleship;

import java.util.Arrays;
import battleship.Coordinate.Status;

public class Field {
    private enum Messages {
        ERROR_SHIPS_OVERLAP("Error: May not overlap ships."),
        ERROR_WOULD_BE_ADJACENT("Error: May not place ship directly adjacent to another ship."),
        ERROR_WRONG_NUMBER_OF_COORDINATES("Error: Did not receive %s coordinates."),
        ERROR_DIAGONAL_PLACEMENT("Error: Ship must be placed horizontally (same row) or vertically (same column)."),
        ERROR_COORDINATES_DO_NOT_MATCH_SHIP_LENGTH("Error: You entered a range of %s coordinates, which does not match the ship length of %s"),
        ERROR_SHOT_COORDINATE_INVALID("Error: You entered the wrong coordinates. Try again:");

        String text;

        Messages(String text) {
            this.text = text;
        }

        private void print(String... subStrings) {
            System.out.println(String.format(text, subStrings));
        }
    }

    enum Ships {
        A("Aircraft Carrier", 5),
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

        void setCoordinate(int index, Coordinate coordinate) {
            coordinates[index] = coordinate;
        }

        int numberOfHits() {
            int hits = 0;
            for (Coordinate c : coordinates) {
                hits = c.getStatus() == Status.X ? hits + 1 : hits;
            }
            return hits;
        }

        boolean isSunk() {
            return numberOfHits() == size;
        }
    }

    private static final String[] rows = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
    private static final String[] columns = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    private final Coordinate[][] field = new Coordinate[rows.length][columns.length];
    private static final String header = "  " + String.join(" ", columns);
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

    static String getHeader() {
        return header;
    }

    String[] getRows() {
        return rows;
    }

    Ships[] getShips() {
        return Ships.values();
    }

    Coordinate getCoordinate(String coordinateString) {
        int rowIndex = getRowIndex(coordinateString);
        int columnIndex = getColumnIndex(coordinateString);
        return field[rowIndex][columnIndex];
    }

    boolean placeShip(Ships ship, String coordinateString) {
        if (isValidShipPlacement(coordinateString, ship)) {
            String[] coordinates = coordinateString.split(" ");
            int firstRow = getRowIndex(coordinates[0]);
            int firstColumn = getColumnIndex(coordinates[0]);
            int secondRow = getRowIndex(coordinates[1]);
            int secondColumn = getColumnIndex(coordinates[1]);
            int shipCoordinateIndex = 0;

            for (int i = Math.min(firstRow, secondRow); i <= Math.max(firstRow, secondRow); i++) {
                for (int j = Math.min(firstColumn, secondColumn); j <= Math.max(firstColumn, secondColumn); j++) {
                    field[i][j].setStatus(Status.O);
                    ship.setCoordinate(shipCoordinateIndex++, field[i][j]);
                }
            }
            return true;
        }
        return false;
    }

    void takeShot(String coordinateString) {
        int rowIndex = getRowIndex(coordinateString);
        int columnIndex = getColumnIndex(coordinateString);
        Coordinate coordinate = field[rowIndex][columnIndex];
        if (coordinate.getStatus() == Status.O || coordinate.getStatus() == Status.X) {
            coordinate.setStatus(Status.X);
        } else {
            coordinate.setStatus(Status.M);
        }
    }

    static boolean shotCoordinateValid(String coordinate) {
        int rowIndex = getRowIndex(coordinate);
        int columnIndex = getColumnIndex(coordinate);
        boolean shotValid = indexInBounds(rowIndex, columnIndex);
        if (!shotValid) {
            Messages.ERROR_SHOT_COORDINATE_INVALID.print();
        }
        return shotValid;
    }

    boolean isValidShipPlacement(String coordinateString, Ships ship) {
        String[] coordinates = coordinateString.split(" ");
        boolean isValid = true;
        // input should be able to be split into 2 coordinates
        if (coordinates.length != NUMBER_OF_COORDINATES) {
            Messages.ERROR_WRONG_NUMBER_OF_COORDINATES.print(Integer.toString(NUMBER_OF_COORDINATES));
            isValid = false;
        }
        int firstRow = getRowIndex(coordinates[0]);
        int firstColumn = getColumnIndex(coordinates[0]);
        int secondRow = getRowIndex(coordinates[1]);
        int secondColumn = getColumnIndex(coordinates[1]);
        boolean onSameRow = firstRow == secondRow;
        boolean onSameColumn = firstColumn == secondColumn;
        // Valid ship placement is horizontally on a single row or vertically in a single column
        if (!(onSameRow || onSameColumn)) {
            Messages.ERROR_DIAGONAL_PLACEMENT.print();
            isValid = false;
        }
        int numberOfCoordinates = 0;
        if (onSameRow) {
            numberOfCoordinates = Math.abs(firstColumn - secondColumn) + 1;
        } else if (onSameColumn) {
            numberOfCoordinates = Math.abs(firstRow - secondRow) + 1;
        }
        // The range of coordinates represented in the input should match the size of the ship
        if (numberOfCoordinates != ship.size) {
            Messages.ERROR_COORDINATES_DO_NOT_MATCH_SHIP_LENGTH.print(Integer.toString(numberOfCoordinates), Integer.toString(ship.size));
            isValid = false;
        }
        // A ship may not overlap or be placed directly adjacent to another ship
        if (wouldOverlap(firstRow, firstColumn, secondRow, secondColumn) || wouldBeAdjacent(firstRow, firstColumn, secondRow,secondColumn)) {
            isValid =  false;
        }

        return isValid;
    }
    
    private boolean wouldOverlap(int firstRow, int firstColumn, int secondRow, int secondColumn) {
        for (int i = Math.min(firstRow, secondRow); i <= Math.max(firstRow, secondRow); i++) {
            for (int j = Math.min(firstColumn, secondColumn); j <= Math.max(firstColumn, secondColumn); j++) {
                if (field[i][j].getStatus() == Status.O) {
                    Messages.ERROR_SHIPS_OVERLAP.print();
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean indexInBounds(int row, int column) {
        return row >= 0 && row < rows.length && column >= 0 && column < columns.length;
    }

    private static int getRowIndex(String coordinate) {
        return Arrays.asList(rows).indexOf(coordinate.substring(0, 1));
    }

    private static int getColumnIndex(String coordinate) {
        return Integer.parseInt(coordinate.substring(1)) - 1;
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
}
