package battleship;

public class Coordinate {
    enum Status {
        F("~"), O("O"), X("X"), M("M");
        String symbol;
        Status(String symbol) {
            this.symbol = symbol;
        }

        public String getSymbol() {
            return symbol;
        }
    }
    private int rowIndex;
    private int columnIndex;
    private Status status;

    Coordinate(int rowIndex, int columnIndex) {
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
        this.status = Status.F;
    }

    String getStatusSymbol() {
        return status.symbol;
    }

    void setStatus(Status status) {
        this.status = status;
    }

}
