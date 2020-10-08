import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MonsterGame {

    public static void main(String[] args) {

        try {
            startGame();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        } finally {
            System.out.println("Game over!");
        }

    }

    private static void startGame() throws IOException, InterruptedException {
        Terminal terminal = createTerminal();

        Player player = createPlayer();

        List<Monster> monsters = createMonsters();

        List<MapLevel> maps = createObst();

        List<Bomb> bombs = createBombs();

        //List<PwUp> pwUps = createPwUps();

        drawCharacters(terminal, player, monsters, maps, bombs);

        do {
            KeyStroke keyStroke = getUserKeyStroke(terminal);

            movePlayer(player, keyStroke);

            moveMonsters(player, monsters);

            drawCharacters(terminal, player, monsters, maps, bombs);


        } while (isPlayerAlive(player, monsters));


        terminal.setForegroundColor(TextColor.ANSI.RED);
        terminal.setCursorPosition(player.getX(), player.getY());
        terminal.putCharacter(player.getSymbol());
        terminal.bell();
        terminal.flush();
    }

    private static List<MapLevel> createObst() {
        List<MapLevel> obst = new ArrayList<>();
        obst.add(new MapLevel(4, 4, '\u2588'));
        obst.add(new MapLevel(5, 5, '\u2588'));

        return obst;
    }


    private static void moveMonsters(Player player, List<Monster> monsters) {
        for (Monster monster : monsters) {
            monster.moveTowards(player);
        }
    }

    private static void movePlayer(Player player, KeyStroke keyStroke) {
        switch (keyStroke.getKeyType()) {
            case ArrowUp:
                player.moveUp();
                break;
            case ArrowDown:
                player.moveDown();
                break;
            case ArrowLeft:
                player.moveLeft();
                break;
            case ArrowRight:
                player.moveRight();
                break;
        }
    }

    private static KeyStroke getUserKeyStroke(Terminal terminal) throws InterruptedException, IOException {
        KeyStroke keyStroke;
        do {
            Thread.sleep(5);
            keyStroke = terminal.pollInput();
        } while (keyStroke == null);
        return keyStroke;
    }

    private static Player createPlayer() {
        return new Player(10, 10, '\u263B');
    }

    private static List<Monster> createMonsters() {
        List<Monster> monsters = new ArrayList<>();
        monsters.add(new Monster(3, 3, 'X'));
        monsters.add(new Monster(23, 23, 'X'));
        monsters.add(new Monster(23, 3, 'C'));
        monsters.add(new Monster(3, 23, 'X'));
        return monsters;
    }

    private static List<Bomb> createBombs() {
        List<Bomb> bombs = new ArrayList<>();
        Random rBomb = new Random();
        Position bombPosition = new Position(rBomb.nextInt(80), rBomb.nextInt(24));
        bombs.add(new Bomb(bombPosition.x, bombPosition.y, 'Q'));
        bombs.add(new Bomb(bombPosition.x, bombPosition.y, 'Q'));
        bombs.add(new Bomb(bombPosition.x, bombPosition.y, '\u2665'));

        return bombs;
    }

    /*private static List<PwUp> createPwups() {
        List<PwUp> pwUps = new ArrayList<>();
        Random pwUp = new Random();
        Position pwUpPosition = new Position(rApple.nextInt(80), rApple.nextInt(24));
        Position bombPosition = new Position(rBomb.nextInt(80), rBomb.nextInt(24));
        pwUps.add(new PwUp(pwUp., bombPosition.y, 'Q'));
        bombs.add(new Bomb(bombPosition.x, bombPosition.y, 'Q'));
        bombs.add(new Bomb(bombPosition.x, bombPosition.y, '\u2665'));

        return bombs;
    }*/

    private static Terminal createTerminal() throws IOException {
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        Terminal terminal = terminalFactory.createTerminal();
        TextGraphics tg = terminal.newTextGraphics();
        terminal.setCursorVisible(false);


        return terminal;
    }

    private static void drawCharacters(Terminal terminal, Player player, List<Monster> monsters, List<MapLevel> maps, List<Bomb> bombs) throws IOException {
        for (Monster monster : monsters) {
            terminal.setCursorPosition(monster.getPreviousX(), monster.getPreviousY());
            terminal.putCharacter(' ');

            terminal.setCursorPosition(monster.getX(), monster.getY());
            terminal.putCharacter(monster.getSymbol());
        }

        for (MapLevel map : maps) {
            terminal.setCursorPosition(map.getxObst(), map.getyObst());
            terminal.putCharacter(map.getSymbolObst());
        }

        for (Bomb bomb : bombs) {
            terminal.setCursorPosition(bomb.getX(), bomb.getY());
            terminal.putCharacter(bomb.getSymbol());
        }

        // Detect if player tries to run into obstacle
        boolean playerMovedIntoObstacle = false;
        for (MapLevel map : maps) {
            if (map.getxObst() == player.getX() && map.getyObst() == player.getY()) {
                playerMovedIntoObstacle = true;
            }
        }

        // Detect if monster tries to run into obstacle
        boolean monsterMovedIntoObstacle = false;
        for (MapLevel map : maps) {
            for (Monster monster : monsters) {
                if (map.getxObst() == monster.getX() && map.getyObst() == monster.getY()) {
                    monsterMovedIntoObstacle = true;
                }
            }
        }

        if (playerMovedIntoObstacle) {
            // Restore player's position
            //terminal.setCursorPosition(player.getPreviousX(), player.getPreviousY());
            //terminal.putCharacter(player.getSymbol());
            player.setX(player.getPreviousX());
            player.setY(player.getPreviousY());

        } else {
            // Move player
            terminal.setCursorPosition(player.getPreviousX(), player.getPreviousY());
            terminal.putCharacter(' ');

            terminal.setCursorPosition(player.getX(), player.getY());
            terminal.putCharacter(player.getSymbol());
        }


        for (Monster monster : monsters) {
            if (monsterMovedIntoObstacle) {
                // Restore monster's position
                monster.setX(monster.getPreviousX());
                monster.setY(monster.getPreviousY());

            } /*else {
                // Move monster
                terminal.setCursorPosition(monster.getPreviousX(), monster.getPreviousY());
                terminal.putCharacter(' ');

                terminal.setCursorPosition(monster.getX(), monster.getY());
                terminal.putCharacter(monster.getSymbol());
            }*/
        }


        terminal.flush();

    }

    private static boolean isPlayerAlive(Player player, List<Monster> monsters) {
        for (Monster monster : monsters) {
            if (monster.getX() == player.getX() && monster.getY() == player.getY()) {
                return false;
            }
        }
        return true;
    }
}
