package battleship;

public class Coordinate {
    enum Status {
        F("~", "~"), O("~", "O"), X("X", "X"), M("M", "M");

        private String privatesSymbol;
        private String publicSymbol;

        Status(String privateSymbol, String publicSymbol) {
            this.privatesSymbol = privateSymbol;
            this.publicSymbol = publicSymbol;
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

    String getPrivateStatusSymbol() {
        return status.privatesSymbol;
    }

    String getPublicStatusSymbol() {
        return status.publicSymbol;
    }

    void setStatus(Status status) {
        this.status = status;
    }

}
