/* Drew Schuster */
package com.thoughtworks.pacman.ui;

import com.thoughtworks.pacman.core.GameMap;
import com.thoughtworks.pacman.core.Ghost;
import com.thoughtworks.pacman.core.Player;
import com.thoughtworks.pacman.sound.GameSounds;
import com.thoughtworks.pacman.sound.NoSounds;
import com.thoughtworks.pacman.sound.Sounds;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.JPanel;

/*This board class contains the player, ghosts, pellets, and most of the game logic.*/
public class Board extends JPanel {
    public static final Image PACMAN_IMAGE = Toolkit.getDefaultToolkit().getImage(Board.class.getResource("pacman.jpg"));
    private static final Image PACMAN_UP_IMAGE = Toolkit.getDefaultToolkit().getImage(Board.class.getResource("pacmanup.jpg"));
    private static final Image PACMAN_DOWN_IMAGE = Toolkit.getDefaultToolkit().getImage(Board.class.getResource("pacmandown.jpg"));
    private static final Image PACMAN_LEFT_IMAGE = Toolkit.getDefaultToolkit().getImage(Board.class.getResource("pacmanleft.jpg"));
    private static final Image PACMAN_RIGHT_IMAGE = Toolkit.getDefaultToolkit().getImage(Board.class.getResource("pacmanright.jpg"));
    private static final Image GHOST_10_IMAGE = Toolkit.getDefaultToolkit().getImage(Board.class.getResource("ghost10.jpg"));
    private static final Image GHOST_20_IMAGE = Toolkit.getDefaultToolkit().getImage(Board.class.getResource("ghost20.jpg"));
    private static final Image GHOST_30_IMAGE = Toolkit.getDefaultToolkit().getImage(Board.class.getResource("ghost30.jpg"));
    private static final Image GHOST_40_IMAGE = Toolkit.getDefaultToolkit().getImage(Board.class.getResource("ghost40.jpg"));
    private static final Image GHOST_11_IMAGE = Toolkit.getDefaultToolkit().getImage(Board.class.getResource("ghost11.jpg"));
    private static final Image GHOST_21_IMAGE = Toolkit.getDefaultToolkit().getImage(Board.class.getResource("ghost21.jpg"));
    private static final Image GHOST_41_IMAGE = Toolkit.getDefaultToolkit().getImage(Board.class.getResource("ghost41.jpg"));
    private static final Image GHOST_31_IMAGE = Toolkit.getDefaultToolkit().getImage(Board.class.getResource("ghost31.jpg"));

    private static final Font FONT = new Font("Monospaced", Font.BOLD, 12);

    /* Initialize the player and ghosts */
    Player player = new Player(200, 300);
    Ghost ghost1 = new Ghost(180, 180);
    Ghost ghost2 = new Ghost(200, 180);
    Ghost ghost3 = new Ghost(220, 180);
    Ghost ghost4 = new Ghost(220, 180);

    /* Timer is used for playing sound effects and animations */
    private long timer = System.currentTimeMillis();

    /* Dying is used to count frames in the dying animation.  If it's non-zero, pacman is in the process of dying */
    int dying = 0;

    /* Score information */
    private int currScore;
    private int highScore;

    /* if the high scores have been cleared, we have to update the top of the screen to reflect that */
    private boolean clearHighScores = false;

    private int numLives = 2;

    private GameMap map;

    /* State flags*/
    boolean stopped;

    boolean titleScreenB = true;
    boolean winScreenB = false;
    boolean overScreenB = false;
    boolean demo = false;

    private TitleScreen titleScreen;
    private WinScreen winScreen;
    private OverScreen overScreen;
    private DyingScreen dyingScreen;
    int gameFrame;

    Sounds sounds;

    private int lastPelletEatenX = 0;
    private int lastPelletEatenY = 0;

    /* Constructor initializes state flags etc.*/
    public Board() {
        initHighScores();
        try {
        	sounds = new GameSounds();
        } catch (Exception e) {
        	sounds = new NoSounds();
        }
        currScore = 0;
        stopped = false;
        gameFrame = 0;
        titleScreen = new TitleScreen(sounds, this);
        winScreen = new WinScreen(sounds, this);
        overScreen = new OverScreen(sounds, this);
        dyingScreen = new DyingScreen(sounds, this);
    }

    /* Reads the high scores file and saves it */
    public void initHighScores() {
        File file = new File("highScores.txt");
        Scanner sc;
        try {
            sc = new Scanner(file);
            highScore = sc.nextInt();
            sc.close();
        } catch (Exception e) {
        }
    }

    /* Writes the new high score to a file and sets flag to update it on screen */
    public void updateScore(int score) {
        PrintWriter out;
        try {
            out = new PrintWriter("highScores.txt");
            out.println(score);
            out.close();
        } catch (Exception e) {
        }
        highScore = score;
        clearHighScores = true;
    }

    public void clearHighScores() {
        updateScore(0);
    }

    private void reset() {
        numLives = 2;
        currScore = 0;
        map = new GameMap();
    }

    private void resetEntities() {
        player = new Player(200, 300, map);
        ghost1 = new Ghost(180, 180, map);
        ghost2 = new Ghost(200, 180, map);
        ghost3 = new Ghost(220, 180, map);
        ghost4 = new Ghost(220, 180, map);
    }

    private void drawLives(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, Pacman.MAX + 5, Pacman.WINDOW_WIDTH, Pacman.TILE_SIZE);

        g.setColor(Color.YELLOW);
        g.setFont(FONT);
        for (int i = 0; i < numLives; i++) {
            g.fillOval(Pacman.TILE_SIZE * (i + 1), Pacman.MAX + 5, Pacman.TILE_SIZE, Pacman.TILE_SIZE);
        }
        g.drawString("Reset", 100, Pacman.MAX + 5 + Pacman.TILE_SIZE);
        g.drawString("Clear High Scores", 180, Pacman.MAX + 5 + Pacman.TILE_SIZE);
        g.drawString("Exit", 350, Pacman.MAX + 5 + Pacman.TILE_SIZE);
    }


    /*  This function draws the board.  The pacman board is really complicated and can only feasibly be done
manually.  Whenever I draw a wall, I call fillWall to invalidate those coordinates.  This way the pacman
and ghosts know that they can't traverse this area */
    private void drawBoard(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Pacman.WINDOW_WIDTH, Pacman.WINDOW_HEIGHT);

        drawBorder(g);
        drawWalls(g);
        drawLives(g);
    }

    private void drawScore(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Pacman.WINDOW_WIDTH, 20);
        g.setColor(Color.YELLOW);
        g.setFont(FONT);
        if (demo)
            g.drawString("DEMO MODE PRESS ANY KEY TO START A GAME\t High Score: " + highScore, 20, 10);
        else
            g.drawString("Score: " + currScore + "\t High Score: " + highScore, 20, 10);
    }

    private void drawBorder(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawRect(19, 19, 382, 382);
    }

    private void drawWalls(Graphics g) {
        g.setColor(Color.BLUE);
        for (int i = 0; i < Pacman.GRID_SIZE; i++) {
            for (int j = 0; j < Pacman.GRID_SIZE; j++) {
                if (map.hasWall(i, j))
                    g.fillRect((i + 1) * Pacman.TILE_SIZE, (j + 1) * Pacman.TILE_SIZE, Pacman.TILE_SIZE, Pacman.TILE_SIZE);
            }
        }
    }

    private void drawPellets(Graphics g) {
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                if (map.hasPellet(i, j))
                    fillPellet(i, j, g);
            }
        }
    }

    private void fillPellet(int x, int y, Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillOval((x+1) * Pacman.TILE_SIZE + 8, (y+1) * Pacman.TILE_SIZE + 8, 4, 4);
    }

    /* This is the main function that draws one entire frame of the game */
    public void paint(Graphics g) {
        if (dying > 0) {
        	dyingScreen.paint(g);
            return;
        }

        /* If this is the title screen, draw the title screen and return */
        if (titleScreenB) {
        	titleScreen.paint(g);
            return;
        }

        /* If this is the win screen, draw the win screen and return */
        else if (winScreenB) {
            winScreen.paint(g);
            return;
        }

        /* If this is the game over screen, draw the game over screen and return */
        else if (overScreenB) {
        	overScreen.paint(g);
            return;
        }

        /* If need to update the high scores, redraw the top menu bar */
        if (clearHighScores) {
            clearHighScores = false;
            drawScore(g);
        }

        /* oops is set to true when pacman has lost a life */
        boolean oops = false;

        /* Game initialization */
        if (gameFrame == 1) {
            reset();
            resetEntities();
            drawBoard(g);
            drawPellets(g);
            drawLives(g);
            /* TODO: Easter Egg! Don't let the player go in the ghost box*/
            // player.state[9][7] = false;

            drawScore(g);
            gameFrame++;
        }
        /* Second frame of new game */
        else if (gameFrame == 2) {
            gameFrame++;
        }
        /* Third frame of new game */
        else if (gameFrame == 3) {
            gameFrame++;
            /* Play the newGame sound effect */
            sounds.newGame();
            timer = System.currentTimeMillis();
            return;
        }
        /* Fourth frame of new game */
        else if (gameFrame == 4) {
            /* Stay in this state until the sound effect is over */
            long currTime = System.currentTimeMillis();
            if (currTime - timer >= 5000) {
                gameFrame = 0;
            } else
                return;
        }

        /* Drawing optimization */
        g.copyArea(player.x - 20, player.y - 20, 80, 80, 0, 0);
        g.copyArea(ghost1.x - 20, ghost1.y - 20, 80, 80, 0, 0);
        g.copyArea(ghost2.x - 20, ghost2.y - 20, 80, 80, 0, 0);
        g.copyArea(ghost3.x - 20, ghost3.y - 20, 80, 80, 0, 0);
        g.copyArea(ghost4.x - 20, ghost4.y - 20, 80, 80, 0, 0);


        /* Detect collisions */
        if (player.x == ghost1.x && Math.abs(player.y - ghost1.y) < 10)
            oops = true;
        else if (player.x == ghost2.x && Math.abs(player.y - ghost2.y) < 10)
            oops = true;
        else if (player.x == ghost3.x && Math.abs(player.y - ghost3.y) < 10)
            oops = true;
        else if (player.x == ghost4.x && Math.abs(player.y - ghost4.y) < 10)
            oops = true;
        else if (player.y == ghost1.y && Math.abs(player.x - ghost1.x) < 10)
            oops = true;
        else if (player.y == ghost2.y && Math.abs(player.x - ghost2.x) < 10)
            oops = true;
        else if (player.y == ghost3.y && Math.abs(player.x - ghost3.x) < 10)
            oops = true;
        else if (player.y == ghost4.y && Math.abs(player.x - ghost4.x) < 10)
            oops = true;

        /* Kill the pacman */
        if (oops && !stopped) {
        	/* 4 frames of death*/
        	dying = 4;
        	dyingScreen.startDying();

            /*Decrement lives, update screen to reflect that.  And set appropriate flags and timers */
            numLives--;
            stopped = true;
            drawLives(g);
        }

        /* Delete the players and ghosts */
        g.setColor(Color.BLACK);
        g.fillRect(player.lastX, player.lastY, Pacman.TILE_SIZE, Pacman.TILE_SIZE);
        g.fillRect(ghost1.lastX, ghost1.lastY, Pacman.TILE_SIZE, Pacman.TILE_SIZE);
        g.fillRect(ghost2.lastX, ghost2.lastY, Pacman.TILE_SIZE, Pacman.TILE_SIZE);
        g.fillRect(ghost3.lastX, ghost3.lastY, Pacman.TILE_SIZE, Pacman.TILE_SIZE);
        g.fillRect(ghost4.lastX, ghost4.lastY, Pacman.TILE_SIZE, Pacman.TILE_SIZE);

        /* Eat pellets */
        if (map.hasPellet(player.pelletX, player.pelletY) && gameFrame != 2 && gameFrame != 3) {
            lastPelletEatenX = player.pelletX;
            lastPelletEatenY = player.pelletY;

            /* Play eating sound */
            sounds.nomNom();

            /* Increment pellets eaten value to track for end game */
            player.eatPellet();

            /* Delete the pellet*/
            map.eatPellet(player.pelletX, player.pelletY);

            /* Increment the score */
            currScore += 50;

            /* Update the screen to reflect the new score */
            drawScore(g);

            /* If this was the last pellet */
            if (player.getPelletsEaten() == 173) {
                /*Demo mode can't get a high score */
                if (!demo) {
                    if (currScore > highScore) {
                        updateScore(currScore);
                    }
                    winScreenB = true;
                } else {
                    titleScreenB = true;
                }
                return;
            }
        }

        /* If we moved to a location without pellets, stop the sounds */
        else if ((player.pelletX != lastPelletEatenX || player.pelletY != lastPelletEatenY) || player.stopped) {
            /* Stop any pacman eating sounds */
            sounds.nomNomStop();
        }


        /* Replace pellets that have been run over by ghosts */
        if (map.hasPellet(ghost1.lastPelletX, ghost1.lastPelletY))
            fillPellet(ghost1.lastPelletX, ghost1.lastPelletY, g);
        if (map.hasPellet(ghost2.lastPelletX, ghost2.lastPelletY))
            fillPellet(ghost2.lastPelletX, ghost2.lastPelletY, g);
        if (map.hasPellet(ghost3.lastPelletX, ghost3.lastPelletY))
            fillPellet(ghost3.lastPelletX, ghost3.lastPelletY, g);
        if (map.hasPellet(ghost4.lastPelletX, ghost4.lastPelletY))
            fillPellet(ghost4.lastPelletX, ghost4.lastPelletY, g);


        /*Draw the ghosts */
        if (ghost1.frameCount < 5) {
            /* Draw first frame of ghosts */
            g.drawImage(GHOST_10_IMAGE, ghost1.x, ghost1.y, Color.BLACK, null);
            g.drawImage(GHOST_20_IMAGE, ghost2.x, ghost2.y, Color.BLACK, null);
            g.drawImage(GHOST_30_IMAGE, ghost3.x, ghost3.y, Color.BLACK, null);
            g.drawImage(GHOST_40_IMAGE, ghost4.x, ghost4.y, Color.BLACK, null);
            ghost1.frameCount++;
        } else {
            /* Draw second frame of ghosts */
            g.drawImage(GHOST_11_IMAGE, ghost1.x, ghost1.y, Color.BLACK, null);
            g.drawImage(GHOST_21_IMAGE, ghost2.x, ghost2.y, Color.BLACK, null);
            g.drawImage(GHOST_31_IMAGE, ghost3.x, ghost3.y, Color.BLACK, null);
            g.drawImage(GHOST_41_IMAGE, ghost4.x, ghost4.y, Color.BLACK, null);
            if (ghost1.frameCount >= 10)
                ghost1.frameCount = 0;
            else
                ghost1.frameCount++;
        }

        /* Draw the pacman */
        if (player.frameCount < 5) {
            /* Draw mouth closed */
            g.drawImage(PACMAN_IMAGE, player.x, player.y, Color.BLACK, null);
        } else {
            /* Draw mouth open in appropriate direction */
            if (player.frameCount >= 10)
                player.frameCount = 0;

            switch (player.currDirection) {
                case 'L':
                    g.drawImage(PACMAN_LEFT_IMAGE, player.x, player.y, Color.BLACK, null);
                    break;
                case 'R':
                    g.drawImage(PACMAN_RIGHT_IMAGE, player.x, player.y, Color.BLACK, null);
                    break;
                case 'U':
                    g.drawImage(PACMAN_UP_IMAGE, player.x, player.y, Color.BLACK, null);
                    break;
                case 'D':
                    g.drawImage(PACMAN_DOWN_IMAGE, player.x, player.y, Color.BLACK, null);
                    break;
            }
        }

        drawBorder(g);
    }

    public void gameOver() {
        if (numLives == -1) {
            /* Demo mode has infinite lives, just give it more lives*/
            if (demo)
                numLives = 2;
            else {
            	if (currScore > highScore) {
            		updateScore(currScore);
            	}
            	overScreenB = true;
            }
        }
	}
}
