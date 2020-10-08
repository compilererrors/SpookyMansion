public class Player {
    private int x;
    private int y;
    private char symbol;
    private int previousX;
    private int previousY;

    public Player(int x, int y, char symbol) {
        this.x = x;
        this.y = y;
        this.symbol = symbol;
        this.previousX = x;
        this.previousY = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public char getSymbol() {
        return symbol;
    }

    public int getPreviousX() {
        return previousX;
    }

    public int getPreviousY() {
        return previousY;
    }

    public void moveUp(){
        previousX = x;
        previousY = y;
        y -= 2;
    }

    public void moveDown(){
        previousX = x;
        previousY = y;
        y += 2;
    }

    public void moveLeft(){
        previousX = x;
        previousY = y;
        x -= 2;
    }

    public void moveRight(){
        previousX = x;
        previousY = y;
        x += 2;
    }

    @Override
    public String toString() {
        return "Player{" +
                "x=" + x +
                ", y=" + y +
                ", symbol=" + symbol +
                ", previousX=" + previousX +
                ", previousY=" + previousY +
                '}';
    }
}