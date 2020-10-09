import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MonsterGame {
    private static KeyStroke latestKeyStroke = null;
    private static Terminal terminal;
    private static boolean playerHitBomb = false;
    private static boolean playerHitPwUp = false;
    private static int score = 0;
    private static TextGraphics tg;
    private static int offsetX = 0;


    public static void main(String[] args) throws IOException {

        try {
            terminal = createTerminal();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        try {
            startGame();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        } finally {
            gameOverScreen(terminal);
        }

    }

    private static void startGame() throws IOException, InterruptedException {


       startMusic();

        Player player = createPlayer();

        List<Monster> monsters = createMonsters();

        List<Obstacle> maps = createObst();

        List<Bomb> bombs = createBombs();

        List<PwUp> pwUps = createPwUps();

        drawCharacters(terminal, player, monsters, maps, bombs, pwUps);

        //Test strings

        //tg.putString(2, 2, "JHELLO");


        int index = 0;
        boolean monsterMove = true;


        do {
            index++;
            scoreScreen(terminal);
            if (playerHitBomb) {
                if (index % 100 == 0) {
                    KeyStroke keyStroke = getUserKeyStroke(terminal, player);
                    movePlayer(player, keyStroke, terminal);
                    moveMonsters(player, monsters);
                }
            } else if (playerHitPwUp) {
                if (index % 30 == 0) {
                    KeyStroke keyStroke = getUserKeyStroke(terminal, player);
                    movePlayer(player, keyStroke, terminal);
                    if (monsterMove) {
                        moveMonsters(player, monsters);
                        monsterMove = false;

                    } else {

                        monsterMove = true;
                    }
                }
            } else if (index % 50 == 0) {
                KeyStroke keyStroke = getUserKeyStroke(terminal, player);

                movePlayer(player, keyStroke, terminal);

                if (monsterMove) {
                    moveMonsters(player, monsters);
                    monsterMove = false;
                } else {
                    monsterMove = true;
                }
            }
            Thread.sleep(5);


            drawCharacters(terminal, player, monsters, maps, bombs, pwUps);


        } while (isPlayerAlive(player, monsters));


        terminal.setForegroundColor(TextColor.ANSI.RED);
        terminal.setCursorPosition(player.getX(), player.getY());
        terminal.putCharacter(player.getSymbol());
        terminal.bell();
        terminal.flush();
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
        KeyStroke keyStroke = null;

        do {
            keyStroke = terminal.pollInput();
            if (keyStroke != null) {
                latestKeyStroke = keyStroke;
            }
        } while (latestKeyStroke == null);


        return latestKeyStroke;

    }

    private static Player createPlayer() {
        return new Player(28, 10, '\u263B');
    }

    private static List<Monster> createMonsters() {
        List<Monster> monsters = new ArrayList<>();
        Monster monster1 = new Monster(40,3, '\u26F9');
        Monster monster2 = new Monster(38,3, '\u26F7');
        Monster monster3 = new Monster(44,4, '\u26C4');
        Monster monster4 = new Monster(30,3, '\u2603');
        Monster monster5 = new Monster(35,3, '\u2622');
        Monster monster6 = new Monster(50,4, '\u2623');
        Monster monster7 = new Monster(52,3, '\u26F9');
        monsters.add(new Monster(monster1.getX(), monster1.getY(),monster1.getSymbol()));
        monsters.add(new Monster(monster2.getX(), monster2.getY(),monster2.getSymbol()));
        monsters.add(new Monster(monster3.getX(), monster3.getY(),monster3.getSymbol()));
        monsters.add(new Monster(monster4.getX(), monster4.getY(),monster4.getSymbol()));
        monsters.add(new Monster(monster5.getX(), monster5.getY(),monster5.getSymbol()));
        monsters.add(new Monster(monster6.getX(), monster6.getY(),monster6.getSymbol()));
        monsters.add(new Monster(monster7.getX(), monster7.getY(),monster7.getSymbol()));

        return monsters;
    }

    private static List<Bomb> createBombs() {
        List<Bomb> bombs = new ArrayList<>();
        for(int i=10; i<100; i+=5){
            int randomNum = ThreadLocalRandom.current().nextInt(5, 26);
            Random rBomb = new Random();
            Bomb bombPosition = new Bomb(rBomb.nextInt(i), rBomb.nextInt(randomNum), '\u26B0');
            bombs.add(new Bomb(bombPosition.getX(), bombPosition.getY(), bombPosition.getSymbol()));
        }
        return bombs;
    }

    private static List<PwUp> createPwUps() {
        List<PwUp> pwUps = new ArrayList<>();
        for(int i=10; i<200; i+=5){
            int randomNum = ThreadLocalRandom.current().nextInt(5, 26);
            Random randompwUp = new Random();
            PwUp pwUp1 = new PwUp(randompwUp.nextInt(i), randompwUp.nextInt(randomNum), '\u2604');
            pwUps.add(new PwUp(pwUp1.getX(), pwUp1.getY(), pwUp1.getSymbol()));
        }
        return pwUps;
    }

    private static Terminal createTerminal() throws IOException {
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        Terminal terminal = terminalFactory.createTerminal();
        terminal.getTerminalSize();
        TerminalSize.ONE.withColumns(1);
        tg = terminal.newTextGraphics();
        terminal.setCursorVisible(false);


        return terminal;
    }

    private static void drawCharacters(Terminal terminal, Player player, List<Monster> monsters, List<Obstacle> maps, List<Bomb> bombs, List<PwUp> pwUps) throws IOException {

        terminal.clearScreen();

        if (player.getX()+offsetX > 30) {
            offsetX -= 1;
        }

        for (Obstacle obstacle : maps) {
            if(obstacle.getxObst()+offsetX >= 0 && obstacle.getxObst()+offsetX < 80){
                terminal.setCursorPosition(obstacle.getxObst()+offsetX, obstacle.getyObst());
                terminal.putCharacter(obstacle.getSymbolObst());
            }


        }

        for (Bomb bomb : bombs) {
            terminal.setCursorPosition(bomb.getX()+offsetX, bomb.getY());
            terminal.putCharacter(bomb.getSymbol());

        }
        for (PwUp pwUp : pwUps) {
            terminal.setCursorPosition(pwUp.getX()+offsetX, pwUp.getY());
            terminal.putCharacter(pwUp.getSymbol());
        }

        // Detect if player tries to run into obstacle
        boolean playerMovedIntoObstacle = false;
        for (Obstacle map : maps) {
            if (map.getxObst() == player.getX() && map.getyObst() == player.getY()) {
                playerMovedIntoObstacle = true;

            }
        }

        // Detect if monster tries to run into obstacle
        //boolean monsterMovedIntoObstacle = false;
      /*  for (Obstacle map : maps) {
            for (Monster monster : monsters) {
                if (map.getxObst() == monster.getX() && map.getyObst() == monster.getY()) {
                    monster.setX(monster.getPreviousX());
                    monster.setY(monster.getPreviousY());

                }
            }
        }*/

        //Detect if Monster moved in to monster
        for (Monster monster : monsters) {
            for (Monster monster2 : monsters) {
                if (monster.getX() == monster2.getX() && monster.getY() == monster2.getY()) {
                    if(monster2.getSymbol()!=monster.getSymbol()) {
                        monster2.setX(monster2.getPreviousX());
                        monster2.setY(monster2.getPreviousY());
                    }
                }
            }
        }


            //Detect if player walked on "bomb"
            for (Bomb bombsOnMap: bombs) {
                if (player.getX() == bombsOnMap.getX() && player.getY() == bombsOnMap.getY()) {
                    playerHitPwUp = false;
                    playerHitBomb = true;
                }
            }
            for (PwUp pwUpsOnMap: pwUps) {
                if (player.getX() == pwUpsOnMap.getX() && player.getY() == pwUpsOnMap.getY()) {
                    if (playerHitPwUp){
                        score += 30;
                    }
                    else if (playerHitBomb){
                        score -= 2;
                    }
                    else if (playerHitPwUp = false) {
                    score += 10;
                }
                //This will be triggered after if's are done
                playerHitBomb = false;
                playerHitPwUp = true;

            }
        }


        if (playerMovedIntoObstacle) {
            // Restore player's position
            player.setX(player.getPreviousX());
            player.setY(player.getPreviousY());

        } else {
            // Move player


            terminal.setCursorPosition(player.getX()+offsetX, player.getY());
            terminal.putCharacter(player.getSymbol());
        }




                // Move monster
              for (Monster monster : monsters) {
            terminal.setCursorPosition(monster.getX()+offsetX, monster.getY());
            terminal.putCharacter(monster.getSymbol());
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

    private static void scoreScreen(Terminal terminal) throws IOException {
        String stringScore = "Score: " + Integer.toString(score);
        for (int i = 0; i < stringScore.length(); i++) {
            terminal.setCursorPosition(i, 30);
            terminal.putCharacter(stringScore.charAt(i));
            terminal.flush();
        }
    }


    private static void gameOverScreen(Terminal terminal) throws IOException {

        String line = "YOU GOT EATEN BY THE MONSTER! GAMEOVER!";
        for (int i = 0; i < (line.length()); i++) {
            if (i == 0) {
                terminal.setCursorPosition(i, 11);
                terminal.putCharacter(line.charAt(i));
            }
            terminal.setCursorPosition(i, 11);
            terminal.putCharacter(line.charAt(i));
        }
    }

    private static void startMusic() {
        // Exemple of playing background music in new thread, just use Music class and these 2 lines:
        Thread thread = new Thread(new Music());
        thread.start();
    }

    private static List<Obstacle> createObst() {
         List<Obstacle> obst = new ArrayList<>();

        for(int j=0; j<200; j+=20) {
            for (int i = 0; i < 6; i++) {
                obst.add(new Obstacle(j, i, '\u2588'));
            }
            for (int i = 10; i < 16; i++) {
                obst.add(new Obstacle(j, i, '\u2588'));
            }
            for (int i = 20; i < 26; i++) {
                obst.add(new Obstacle(j, i, '\u2588'));
            }
        }

        for(int j=10; j<200; j+=20) {
            for (int i = 6; i < 12; i++) {
                obst.add(new Obstacle(j, i, '\u2588'));
            }
            for (int i = 16; i < 24; i++) {
                obst.add(new Obstacle(j, i, '\u2588'));
            }
        }
        return obst;
    }

}
