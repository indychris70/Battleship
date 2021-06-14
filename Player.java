package battleship;

public class Player {
    private final Field field;

    Player() {
        this.field = Field.newField();
    }

    Field getField() {
        return field;
    }

    void printField() {
        String[] rows = field.getRows();
        System.out.println(field.getHeader());
        // for (Coordinate[] row : field.getField()) {
        for (int i = 0; i < field.getField().length; i++) {
            System.out.print(rows[i]);
            for (int j = 0; j < field.getField()[i].length; j++) {
                String formattedSymbol = String.format(" %s", field.getField()[i][j].getStatusSymbol());
                System.out.print(formattedSymbol);
            }
            System.out.println();
        }
    }
}
