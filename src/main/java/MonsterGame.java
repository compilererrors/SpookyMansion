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
        Random rBomb = new Random();
        Bomb bombPosition = new Bomb(rBomb.nextInt(80), rBomb.nextInt(24), '\u256C');
        Bomb bombPosition1 = new Bomb(rBomb.nextInt(80), rBomb.nextInt(24), '\u256C');
        Bomb bombPosition2 = new Bomb(rBomb.nextInt(80), rBomb.nextInt(24), '\u26B0');
        bombs.add(new Bomb(bombPosition.getX(), bombPosition.getY(), bombPosition.getSymbol()));
        bombs.add(new Bomb(bombPosition1.getX(), bombPosition1.getY(), bombPosition1.getSymbol()));
        bombs.add(new Bomb(bombPosition2.getX(), bombPosition2.getY(), bombPosition2.getSymbol()));
        return bombs;
    }

    private static List<PwUp> createPwUps() {
        List<PwUp> pwUps = new ArrayList<>();
        Random pwUp = new Random();
        PwUp pwUp1 = new PwUp(pwUp.nextInt(80), pwUp.nextInt(24), '\u2604');
        PwUp pwUp2 = new PwUp(pwUp.nextInt(80), pwUp.nextInt(24), '\u26A1');
        PwUp pwUp3 = new PwUp(pwUp.nextInt(80), pwUp.nextInt(24), '\u2B50');
        PwUp pwUp4 = new PwUp(pwUp.nextInt(80), pwUp.nextInt(24), '\u2604');
        pwUps.add(new PwUp(pwUp1.getX(), pwUp1.getY(), pwUp1.getSymbol()));
        pwUps.add(new PwUp(pwUp2.getX(), pwUp2.getY(), pwUp2.getSymbol()));
        pwUps.add(new PwUp(pwUp3.getX(), pwUp3.getY(), pwUp3.getSymbol()));
        pwUps.add(new PwUp(pwUp4.getX(), pwUp4.getY(), pwUp4.getSymbol()));

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
        for (Obstacle map : maps) {
            for (Monster monster : monsters) {
                if (map.getxObst() == monster.getX() && map.getyObst() == monster.getY()) {
                    monster.setX(monster.getPreviousX());
                    monster.setY(monster.getPreviousY());

                }
            }
        }

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
        //högersida
        //rad1
        obst.add(new Obstacle(5, 0, '\u2588'));
        obst.add(new Obstacle(5, 1, '\u2588'));
        obst.add(new Obstacle(5, 2, '\u2588'));
        obst.add(new Obstacle(5, 3, '\u2588'));
        obst.add(new Obstacle(5, 4, '\u2588'));


        obst.add(new Obstacle(5, 10, '\u2588'));
        obst.add(new Obstacle(5, 11, '\u2588'));
        obst.add(new Obstacle(5, 12, '\u2588'));
        obst.add(new Obstacle(5, 13, '\u2588'));
        obst.add(new Obstacle(5, 14, '\u2588'));
        obst.add(new Obstacle(5, 15, '\u2588'));

        obst.add(new Obstacle(5, 20, '\u2588'));
        obst.add(new Obstacle(5, 21, '\u2588'));
        obst.add(new Obstacle(5, 22, '\u2588'));
        obst.add(new Obstacle(5, 23, '\u2588'));
        obst.add(new Obstacle(5, 24, '\u2588'));
        obst.add(new Obstacle(5, 25, '\u2588'));
//rad 2
        obst.add(new Obstacle(10, 4, '\u2588'));
        obst.add(new Obstacle(10, 5, '\u2588'));
        obst.add(new Obstacle(10, 6, '\u2588'));
        obst.add(new Obstacle(10, 7, '\u2588'));
        obst.add(new Obstacle(10, 8, '\u2588'));
        obst.add(new Obstacle(10, 9, '\u2588'));
        obst.add(new Obstacle(10, 10, '\u2588'));

        obst.add(new Obstacle(10, 13, '\u2588'));
        obst.add(new Obstacle(10, 14, '\u2588'));
        obst.add(new Obstacle(10, 15, '\u2588'));
        obst.add(new Obstacle(10, 16, '\u2588'));
        obst.add(new Obstacle(10, 17, '\u2588'));
        obst.add(new Obstacle(10, 18, '\u2588'));
        obst.add(new Obstacle(10, 19, '\u2588'));
        obst.add(new Obstacle(10, 20, '\u2588'));
        //rad 3

        obst.add(new Obstacle(15, 0, '\u2588'));
        obst.add(new Obstacle(15, 1, '\u2588'));
        obst.add(new Obstacle(15, 2, '\u2588'));
        obst.add(new Obstacle(15, 3, '\u2588'));
        obst.add(new Obstacle(15, 4, '\u2588'));

        obst.add(new Obstacle(15, 10, '\u2588'));
        obst.add(new Obstacle(15, 11, '\u2588'));
        obst.add(new Obstacle(15, 12, '\u2588'));
        obst.add(new Obstacle(15, 13, '\u2588'));
        obst.add(new Obstacle(15, 14, '\u2588'));
        obst.add(new Obstacle(15, 15, '\u2588'));

        obst.add(new Obstacle(15, 20, '\u2588'));
        obst.add(new Obstacle(15, 21, '\u2588'));
        obst.add(new Obstacle(15, 22, '\u2588'));
        obst.add(new Obstacle(15, 23, '\u2588'));
        obst.add(new Obstacle(15, 24, '\u2588'));
        obst.add(new Obstacle(15, 25, '\u2588'));
        //rad4
        obst.add(new Obstacle(20, 4, '\u2588'));
        obst.add(new Obstacle(20, 5, '\u2588'));
        obst.add(new Obstacle(20, 6, '\u2588'));
        obst.add(new Obstacle(20, 7, '\u2588'));
        obst.add(new Obstacle(20, 8, '\u2588'));
        obst.add(new Obstacle(20, 9, '\u2588'));
        obst.add(new Obstacle(20, 10, '\u2588'));

        obst.add(new Obstacle(20, 13, '\u2588'));
        obst.add(new Obstacle(20, 14, '\u2588'));
        obst.add(new Obstacle(20, 15, '\u2588'));
        obst.add(new Obstacle(20, 16, '\u2588'));
        obst.add(new Obstacle(20, 17, '\u2588'));
        obst.add(new Obstacle(20, 18, '\u2588'));
        obst.add(new Obstacle(20, 19, '\u2588'));
        obst.add(new Obstacle(20, 20, '\u2588'));
//rad5
        obst.add(new Obstacle(25, 0, '\u2588'));
        obst.add(new Obstacle(25, 1, '\u2588'));
        obst.add(new Obstacle(25, 2, '\u2588'));
        obst.add(new Obstacle(25, 3, '\u2588'));
        obst.add(new Obstacle(25, 4, '\u2588'));


        obst.add(new Obstacle(25, 10, '\u2588'));
        obst.add(new Obstacle(25, 11, '\u2588'));
        obst.add(new Obstacle(25, 12, '\u2588'));
        obst.add(new Obstacle(25, 13, '\u2588'));
        obst.add(new Obstacle(25, 14, '\u2588'));
        obst.add(new Obstacle(25, 15, '\u2588'));

        obst.add(new Obstacle(25, 20, '\u2588'));
        obst.add(new Obstacle(25, 21, '\u2588'));
        obst.add(new Obstacle(25, 22, '\u2588'));
        obst.add(new Obstacle(25, 23, '\u2588'));
        obst.add(new Obstacle(25, 24, '\u2588'));
        obst.add(new Obstacle(25, 25, '\u2588'));
//rad 6
        obst.add(new Obstacle(30, 4, '\u2588'));
        obst.add(new Obstacle(30, 5, '\u2588'));
        obst.add(new Obstacle(30, 6, '\u2588'));
        obst.add(new Obstacle(30, 7, '\u2588'));
        obst.add(new Obstacle(30, 8, '\u2588'));
        obst.add(new Obstacle(30, 9, '\u2588'));
        obst.add(new Obstacle(30, 10, '\u2588'));

        obst.add(new Obstacle(30, 13, '\u2588'));
        obst.add(new Obstacle(30, 14, '\u2588'));
        obst.add(new Obstacle(30, 15, '\u2588'));
        obst.add(new Obstacle(30, 16, '\u2588'));
        obst.add(new Obstacle(30, 17, '\u2588'));
        obst.add(new Obstacle(30, 18, '\u2588'));
        obst.add(new Obstacle(30, 19, '\u2588'));
        obst.add(new Obstacle(30, 20, '\u2588'));

        //vänstersida

        //rad7
        obst.add(new Obstacle(35, 0, '\u2588'));
        obst.add(new Obstacle(35, 1, '\u2588'));
        obst.add(new Obstacle(35, 2, '\u2588'));
        obst.add(new Obstacle(35, 3, '\u2588'));
        obst.add(new Obstacle(35, 4, '\u2588'));


        obst.add(new Obstacle(35, 10, '\u2588'));
        obst.add(new Obstacle(35, 11, '\u2588'));
        obst.add(new Obstacle(35, 12, '\u2588'));
        obst.add(new Obstacle(35, 13, '\u2588'));
        obst.add(new Obstacle(35, 14, '\u2588'));
        obst.add(new Obstacle(35, 15, '\u2588'));

        obst.add(new Obstacle(35, 20, '\u2588'));
        obst.add(new Obstacle(35, 21, '\u2588'));
        obst.add(new Obstacle(35, 22, '\u2588'));
        obst.add(new Obstacle(35, 23, '\u2588'));
        obst.add(new Obstacle(35, 24, '\u2588'));
        obst.add(new Obstacle(35, 25, '\u2588'));
//rad 8
        obst.add(new Obstacle(40, 4, '\u2588'));
        obst.add(new Obstacle(40, 5, '\u2588'));
        obst.add(new Obstacle(40, 6, '\u2588'));
        obst.add(new Obstacle(40, 7, '\u2588'));
        obst.add(new Obstacle(40, 8, '\u2588'));
        obst.add(new Obstacle(40, 9, '\u2588'));
        obst.add(new Obstacle(40, 10, '\u2588'));

        obst.add(new Obstacle(40, 13, '\u2588'));
        obst.add(new Obstacle(40, 14, '\u2588'));
        obst.add(new Obstacle(40, 15, '\u2588'));
        obst.add(new Obstacle(40, 16, '\u2588'));
        obst.add(new Obstacle(40, 17, '\u2588'));
        obst.add(new Obstacle(40, 18, '\u2588'));
        obst.add(new Obstacle(40, 19, '\u2588'));
        obst.add(new Obstacle(40, 20, '\u2588'));
        //rad 9

        obst.add(new Obstacle(45, 0, '\u2588'));
        obst.add(new Obstacle(45, 1, '\u2588'));
        obst.add(new Obstacle(45, 2, '\u2588'));
        obst.add(new Obstacle(45, 3, '\u2588'));
        obst.add(new Obstacle(45, 4, '\u2588'));

        obst.add(new Obstacle(45, 10, '\u2588'));
        obst.add(new Obstacle(45, 11, '\u2588'));
        obst.add(new Obstacle(45, 12, '\u2588'));
        obst.add(new Obstacle(45, 13, '\u2588'));
        obst.add(new Obstacle(45, 14, '\u2588'));
        obst.add(new Obstacle(45, 15, '\u2588'));

        obst.add(new Obstacle(45, 20, '\u2588'));
        obst.add(new Obstacle(45, 21, '\u2588'));
        obst.add(new Obstacle(45, 22, '\u2588'));
        obst.add(new Obstacle(45, 23, '\u2588'));
        obst.add(new Obstacle(45, 24, '\u2588'));
        obst.add(new Obstacle(45, 25, '\u2588'));
        //rad4
        obst.add(new Obstacle(50, 4, '\u2588'));
        obst.add(new Obstacle(50, 5, '\u2588'));
        obst.add(new Obstacle(50, 6, '\u2588'));
        obst.add(new Obstacle(50, 7, '\u2588'));
        obst.add(new Obstacle(50, 8, '\u2588'));
        obst.add(new Obstacle(50, 9, '\u2588'));
        obst.add(new Obstacle(50, 10, '\u2588'));


     
        return obst;
    }

}
