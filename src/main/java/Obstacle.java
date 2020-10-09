public class Obstacle {


    private int xObst;
    private int yObst;
    private char symbolObst;


    public Obstacle(int x, int y, char symbol) {
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
        return "MapObstacle{" +
                "x=" + xObst +
                ", y=" + yObst +
                ", symbol=" + symbolObst +
                '}';
    }


}
