/* Drew Schuster */
package com.thoughtworks.pacman.ui;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.JPanel;

import com.thoughtworks.pacman.core.GameMap;
import com.thoughtworks.pacman.core.Ghost;
import com.thoughtworks.pacman.core.Player;
import com.thoughtworks.pacman.sound.GameSounds;
import com.thoughtworks.pacman.sound.NoSounds;
import com.thoughtworks.pacman.sound.Sounds;

/*This board class contains the player, ghosts, pellets, and most of the game logic.*/
@SuppressWarnings("serial")
public class Board extends JPanel {
    static final Image PACMAN_IMAGE = Toolkit.getDefaultToolkit().getImage(Board.class.getResource("pacman.jpg"));
    static final Image PACMAN_UP_IMAGE = Toolkit.getDefaultToolkit().getImage(Board.class.getResource("pacmanup.jpg"));
    static final Image PACMAN_DOWN_IMAGE = Toolkit.getDefaultToolkit().getImage(Board.class.getResource("pacmandown.jpg"));
    static final Image PACMAN_LEFT_IMAGE = Toolkit.getDefaultToolkit().getImage(Board.class.getResource("pacmanleft.jpg"));
    static final Image PACMAN_RIGHT_IMAGE = Toolkit.getDefaultToolkit().getImage(Board.class.getResource("pacmanright.jpg"));
    static final Image GHOST_10_IMAGE = Toolkit.getDefaultToolkit().getImage(Board.class.getResource("ghost10.jpg"));
    static final Image GHOST_20_IMAGE = Toolkit.getDefaultToolkit().getImage(Board.class.getResource("ghost20.jpg"));
    static final Image GHOST_30_IMAGE = Toolkit.getDefaultToolkit().getImage(Board.class.getResource("ghost30.jpg"));
    static final Image GHOST_40_IMAGE = Toolkit.getDefaultToolkit().getImage(Board.class.getResource("ghost40.jpg"));
    static final Image GHOST_11_IMAGE = Toolkit.getDefaultToolkit().getImage(Board.class.getResource("ghost11.jpg"));
    static final Image GHOST_21_IMAGE = Toolkit.getDefaultToolkit().getImage(Board.class.getResource("ghost21.jpg"));
    static final Image GHOST_41_IMAGE = Toolkit.getDefaultToolkit().getImage(Board.class.getResource("ghost41.jpg"));
    static final Image GHOST_31_IMAGE = Toolkit.getDefaultToolkit().getImage(Board.class.getResource("ghost31.jpg"));

    static final Font FONT = new Font("Monospaced", Font.BOLD, 12);

    /* Initialize the player and ghosts */
    Player player = new Player(200, 300);
    Ghost ghost1 = new Ghost(180, 180);
    Ghost ghost2 = new Ghost(200, 180);
    Ghost ghost3 = new Ghost(220, 180);
    Ghost ghost4 = new Ghost(220, 180);

    /* Dying is used to count frames in the dying animation.  If it's non-zero, pacman is in the process of dying */
    int dying = 0;

    /* Score information */
    int currScore;
    int highScore;

    /* if the high scores have been cleared, we have to update the top of the screen to reflect that */
    boolean clearHighScores = false;

    int numLives = 2;

    GameMap map;

    /* State flags*/
    boolean stopped;

    boolean titleScreenB = true;
    boolean winScreenB = false;
    boolean overScreenB = false;
    boolean demo = false;

    private TitleScreen titleScreen;
    private WinScreen winScreen;
    private OverScreen overScreen;
    DyingScreen dyingScreen;
    private GameScreen gameScreen;
    int gameFrame;

    Sounds sounds;

    int lastPelletEatenX = 0;
    int lastPelletEatenY = 0;

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
        titleScreen = new TitleScreen(this);
        winScreen = new WinScreen();
        overScreen = new OverScreen();
        dyingScreen = new DyingScreen(sounds, this);
        gameScreen = new GameScreen(sounds, this);
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

    void reset() {
        numLives = 2;
        currScore = 0;
        map = new GameMap();
    }

    void resetEntities() {
        player = new Player(200, 300, map);
        ghost1 = new Ghost(180, 180, map);
        ghost2 = new Ghost(200, 180, map);
        ghost3 = new Ghost(220, 180, map);
        ghost4 = new Ghost(220, 180, map);
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

        gameScreen.paint(g);
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

	public void newGame() {
		gameFrame = 1;
        reset();
        resetEntities();
	}
}
