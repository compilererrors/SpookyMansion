public class Bomb {
    private int x;
    private int y;
    private char symbol;

    public Bomb(int x, int y, char symbol) {
        this.x = x;
        this.y = y;
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return "Monster{" +
                "x=" + x +
                ", y=" + y +
                ", symbol=" + symbol +
                '}';
    }


}
