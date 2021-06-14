package battleship;

public class Coordinate {
    enum Status {
        F("~"), O("O"), X("X"), M("M");

        private String symbol;

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

    Status getStatus() {
        return status;
    }

    String getStatusSymbol() {
        return status.symbol;
    }

    void setStatus(Status status) {
        this.status = status;
    }

}
