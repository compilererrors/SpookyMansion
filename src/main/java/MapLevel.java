public class MapLevel {


    private int xObst;
    private int yObst;
    private char symbolObst;


    public MapLevel(int x, int y, char symbol) {
        this.xObst = x;
        this.yObst = y;
        this.symbolObst = symbol;

    }

    public int getxObst() {
        return xObst;
    }

    public int getyObst() {
        return yObst;
    }

    public char getSymbolObst() {
        return symbolObst;
    }

    @Override
    public String toString() {
        return "Monster{" +
                "x=" + xObst +
                ", y=" + yObst +
                ", symbol=" + symbolObst +
                '}';
    }


}
