package battleship;

public class Player {
    Field field;

    Player() {
        this.field = new Field(this);
    }

    void printField() {
        Rows[] rows = Rows.values();
        System.out.println(field.getHeader());
        // for (Coordinate[] row : field.getField()) {
        for (int i = 0; i < field.getField().length; i++) {
            System.out.print(rows[i].name());
            for (int j = 0; j < field.getField()[i].length; j++) {
                String formattedSymbol = String.format(" %s", field.getField()[i][j].getStatusSymbol());
                System.out.print(formattedSymbol);
            }
            System.out.println();
        }
    }
}
