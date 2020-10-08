public class PwUp {
    private int x;
    private int y;
    private char symbol;

    public PwUp(int x, int y, char symbol) {
        this.x = x;
        this.y = y;
        this.symbol = symbol;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public char getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return "PwUp{" +
                "x=" + x +
                ", y=" + y +
                ", symbol=" + symbol +
                '}';
    }


}
