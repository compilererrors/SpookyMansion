public class Monster {
    private int x;
    private int y;
    private char symbol;
    private int previousX;
    private int previousY;
    private boolean monsterMovedIntoObstacle = false;

    public Monster(int x, int y, char symbol) {
        this.x = x;
        this.y = y;
        this.symbol = symbol;
        this.previousX = x;
        this.previousY = y;
    }

    public boolean hasMonsterMovedIntoObstacle() {
        return monsterMovedIntoObstacle;
    }

    public void setMonsterMovedIntoObstacle(boolean monsterMovedIntoObstacle) {
        this.monsterMovedIntoObstacle = monsterMovedIntoObstacle;
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

    public void moveTowards(Player player) {
        // a monster wants to minimize the distance between itself and the player


        previousX = x;
        previousY = y;

        int diffX = this.x - player.getX();
        int absDiffX = Math.abs(diffX);
        int diffY = this.y - player.getY();
        int absDiffY = Math.abs(diffY);

        if (absDiffX > absDiffY) {
            // Move horizontal! <--->
            if (diffX < 0) {
                this.x += 1;
            } else {
                this.x -= 1;
            }
        } else if (absDiffX < absDiffY) {
            // Move vertical! v / ^
            if (diffY < 0) {
                this.y += 1;
            } else {
                this.y -= 1;
            }
        } else {
            // Move diagonal! / or \
            if (diffX < 0) {
                this.x += 1;
            } else {
                this.x -= 1;
            }
            if (diffY < 0) {
                this.y += 1;
            } else {
                this.y -= 1;
            }
        }
    }

    @Override
    public String toString() {
        return "Monster{" +
                "x=" + x +
                ", y=" + y +
                ", symbol=" + symbol +
                ", previousX=" + previousX +
                ", previousY=" + previousY +
                '}';
    }
}