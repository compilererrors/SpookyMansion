import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import javax.swing.text.Position;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MonsterGame {
    private static KeyStroke latestKeyStroke = null;

    public static void main(String[] args) {
        private static Terminal terminal;

    static {
        try {
            terminal = createTerminal();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws IOException {
        Position player;

        try {
            startGame();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        } finally {
            gameOverScreen(terminal);
            System.out.println("Game over!");
        }

    }

    private static void startGame() throws IOException, InterruptedException {


        Player player = createPlayer();

        List<Monster> monsters = createMonsters();

        List<MapLevel> maps = createObst();

        List<Bomb> bombs = createBombs();

        drawCharacters(terminal, player, monsters, maps);

        int index = 0;
        do {
           index++;
            if (index % 100 == 0) {

            KeyStroke keyStroke = getUserKeyStroke(terminal,player);


            movePlayer(player, keyStroke, terminal);

            moveMonsters(player, monsters);
 		 } Thread.sleep(5);

            drawCharacters(terminal, player, monsters, maps);


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

    private static void movePlayer(Player player, KeyStroke keyStroke, Terminal terminal) {

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

    private static KeyStroke getUserKeyStroke(Terminal terminal, Player player) throws InterruptedException, IOException {
        KeyStroke keyStroke=null;

        do {
            keyStroke = terminal.pollInput();
            if(keyStroke != null) {
                latestKeyStroke = keyStroke;
            }
        } while (latestKeyStroke == null);


        return latestKeyStroke;

    }

    private static Player createPlayer() {
        return new Player(10, 10, '\u263a');
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

        return bombs;
    }

    private static Terminal createTerminal() throws IOException {
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        Terminal terminal = terminalFactory.createTerminal();
        TextGraphics tg = terminal.newTextGraphics();
        terminal.setCursorVisible(false);


        return terminal;
    }

    private static void drawCharacters(Terminal terminal, Player player, List<Monster> monsters, List<MapLevel> maps) throws IOException {
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

                player.setX(player.getPreviousX());
                player.setY(player.getPreviousY());
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
    private static void gameOverScreen(Terminal terminal) throws IOException {
      
        String line = "YOU GOT EATEN BY THE MONSTER!!! GAMEOVER!!";
        for (int i = 0; i < line.length(); i++) {
            terminal.setCursorPosition(i,20);
            terminal.putCharacter(line.charAt(i));

    }

}
}
