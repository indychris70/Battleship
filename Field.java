package battleship;

import java.util.Arrays;
import battleship.Coordinate.Status;

public class Field {

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

    Ships[] getShips() {
        return Ships.values();
    }

    private Player player;
    String[] rows = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
    Coordinate[][] field = new Coordinate[rows.length][10];
    String header = "  1 2 3 4 5 6 7 8 9 10";

    Field(Player player) {
        this.player = player;
        for (int i = 0; i < rows.length; i++) {
            for (int j = 0; j < 10; j++) {
                field[i][j] = new Coordinate(i, j);
            }
        }
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

    boolean placeShip(Ships ship, String coordinateString) {
        System.out.println(coordinateString);
        String[] coordinates = coordinateString.split(" ");
        System.out.println(Arrays.toString(coordinates));
        if (isValid(coordinates, ship)) {
            int firstCoordinateRow = Arrays.asList(rows).indexOf(coordinates[0].substring(0, 1));
            int firstCoordinateColumn = Integer.parseInt(coordinates[0].substring(1));
            int secondCoordinateRow = Arrays.asList(rows).indexOf(coordinates[1].substring(0, 1));
            int secondCoordinateColumn = Integer.parseInt(coordinates[1].substring(1));

            for (int i = Math.min(firstCoordinateRow, secondCoordinateRow); i <= Math.max(firstCoordinateRow, secondCoordinateRow); i++) {
                for (int j = Math.min(firstCoordinateColumn, secondCoordinateColumn); j <= Math.max(firstCoordinateColumn, secondCoordinateColumn); j++) {
                    if (field[i][j - 1].getStatusSymbol() == "O") {
                        System.out.println("Error: May not overlap ships.");
                        return false;
                    }
                }
            }

            for (int i = Math.min(firstCoordinateRow, secondCoordinateRow); i <= Math.max(firstCoordinateRow, secondCoordinateRow); i++) {
                for (int j = Math.min(firstCoordinateColumn, secondCoordinateColumn); j <= Math.max(firstCoordinateColumn, secondCoordinateColumn); j++) {
                    int indexOfRowAbove = i - 1;
                    int indexOfRowBelow = i + 1;
                    int indexOfColumnOnRight = j;
                    int indexOfColumnOnLeft = j - 2;
                    if (indexOfRowAbove >= 0 && field[indexOfRowAbove][j - 1].getStatusSymbol() == "O") {
                        System.out.println("Error 1: May not place ship directly adjacent to another ship.");
                        return false;
                    }
                    if (indexOfRowBelow <= 9 && field[indexOfRowBelow][j - 1].getStatusSymbol() == "O") {
                        System.out.println("Error 2: May not place ship directly adjacent to another ship.");
                        return false;
                    }
                    if (indexOfColumnOnRight <= 9 && field[i][j].getStatusSymbol() == "O") {
                        System.out.println("Error 3: May not place ship directly adjacent to another ship.");
                        return false;
                    }
                    if (indexOfColumnOnLeft >= 0 && field[i][j - 2].getStatusSymbol() == "O") {
                        System.out.println("Error 4: May not place ship directly adjacent to another ship.");
                        return false;
                    }
                }
            }

            for (int i = Math.min(firstCoordinateRow, secondCoordinateRow); i <= Math.max(firstCoordinateRow, secondCoordinateRow); i++) {
                for (int j = Math.min(firstCoordinateColumn, secondCoordinateColumn); j <= Math.max(firstCoordinateColumn, secondCoordinateColumn); j++) {
                    field[i][j - 1].setStatus(Status.O);
                }
            }
            return true;
        }
        return false;

    }

    boolean isValid(String[] coordinates, Ships ship) {
        boolean isValid = true;
        if (coordinates.length != 2) {
            System.out.println("Coordinates length not 4");
            isValid = false;
        }
        String firstCoordinateRow = coordinates[0].substring(0, 1);
        String firstCoordinateColumn = coordinates[0].substring(1);
        String secondCoordinateRow = coordinates[1].substring(0, 1);
        String secondCoordinateColumn = coordinates[1].substring(1);
        boolean onSameRow = firstCoordinateRow.equals(secondCoordinateRow);
        boolean onSameColumn = firstCoordinateColumn.equals(secondCoordinateColumn);
        if (!(onSameRow || onSameColumn)) {
            System.out.println("Coordinates must be on the same column or the same row.");
            isValid = false;
        }
        int numberOfCoordinates = 0;
        if (onSameRow) {
            numberOfCoordinates = Math.abs(Integer.parseInt(firstCoordinateColumn) - Integer.parseInt(secondCoordinateColumn)) + 1;
        } else if (onSameColumn) {
            numberOfCoordinates = Math.abs(Arrays.asList(rows).indexOf(firstCoordinateRow) - Arrays.asList(rows).indexOf(secondCoordinateRow)) + 1;
        }
        if (numberOfCoordinates != ship.size) {
            System.out.println(String.format("You entered a range of %d coordinates, which does not match the ship length of %d", numberOfCoordinates, ship.size));
            isValid = false;
        }
        return isValid;
    }
}
