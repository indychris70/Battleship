package battleship;

public enum Rows {
    A(0), B(1), C(2), D(3), E(4), F(5), G(6), H(7), I(8);
    int index;
    Rows(int index) {
        this.index = index;
    }
    int getIndex() {
        return index;
    }
}
