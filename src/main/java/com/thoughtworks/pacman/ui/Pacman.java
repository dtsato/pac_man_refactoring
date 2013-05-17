/* Drew Schuster */
package com.thoughtworks.pacman.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.Timer;

/* This class contains the entire game... most of the game logic is in the Board class but this
   creates the gui and captures mouse and keyboard input, as well as controls the game states */
public class Pacman implements MouseListener, KeyListener {

    public static final int WINDOW_WIDTH = 420;
    public static final int WINDOW_HEIGHT = 460;
    public static final int TILE_SIZE = 20;
    public static final int GRID_SIZE = 20;
    public static final int MAX = TILE_SIZE * GRID_SIZE;

    /* These timers are used to kill title, game over, and victory screens after a set idle period (5 seconds)*/
    private long titleTimer = -1;
    private long timer = -1;

    private Board board;
    private Timer frameTimer;

    public Pacman() {
        board = new Board();
    }

	private void initialize() {
        JFrame container = new JFrame();
        container.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        container.add(board, BorderLayout.CENTER);

        board.addMouseListener(this);
        board.addKeyListener(this);

        container.setVisible(true);
        container.setResizable(false);

        /* Set the gameFrame flag to 1 because this is a new game */
        board.gameFrame = 1;
	}

    /* This repaint function repaints only the parts of the screen that may have changed.
       Namely the area around every player ghost and the menu bars
    */
    public void repaint() {
        if (board.player.teleport) {
            board.repaint(board.player.lastX - TILE_SIZE, board.player.lastY - TILE_SIZE, 4*TILE_SIZE, 4*TILE_SIZE);
            board.player.teleport = false;
        }
        board.repaint(0, 0, WINDOW_WIDTH, 20);
        board.repaint(0, 420, WINDOW_WIDTH, 40);
        board.repaint(board.player.x - TILE_SIZE, board.player.y - TILE_SIZE, 4*TILE_SIZE, 4*TILE_SIZE);
        board.repaint(board.ghost1.x - TILE_SIZE, board.ghost1.y - TILE_SIZE, 4*TILE_SIZE, 4*TILE_SIZE);
        board.repaint(board.ghost2.x - TILE_SIZE, board.ghost2.y - TILE_SIZE, 4*TILE_SIZE, 4*TILE_SIZE);
        board.repaint(board.ghost3.x - TILE_SIZE, board.ghost3.y - TILE_SIZE, 4*TILE_SIZE, 4*TILE_SIZE);
        board.repaint(board.ghost4.x - TILE_SIZE, board.ghost4.y - TILE_SIZE, 4*TILE_SIZE, 4*TILE_SIZE);
    }

    /* Steps the screen forward one frame */
    public void stepFrame(boolean firstFrame) {
        /* If we aren't on a special screen than the timers can be set to -1 to disable them */
        if (!board.titleScreenB && !board.winScreenB && !board.overScreenB) {
            timer = -1;
            titleTimer = -1;
        }

        /* If we are playing the dying animation, keep advancing frames until the animation is complete */
        if (board.dying > 0) {
            board.repaint();
            return;
        }

        /* gameFrame can either be specified by the gameFrame parameter in stepFrame function call or by the state
  of board.gameFrame.  Update gameFrame accordingly */
        firstFrame = firstFrame || (board.gameFrame != 0);

        /* If this is the title screen, make sure to only stay on the title screen for 5 seconds.
If after 5 seconds the user hasn't started a game, start up demo mode */
        if (board.titleScreenB) {
            if (titleTimer == -1) {
                titleTimer = System.currentTimeMillis();
            }

            long currTime = System.currentTimeMillis();
            if (currTime - titleTimer >= 5000) {
                board.titleScreenB = false;
                board.demo = true;
                titleTimer = -1;
            }
            board.repaint();
            return;
        }

        /* If this is the win screen or game over screen, make sure to only stay on the screen for 5 seconds.
If after 5 seconds the user hasn't pressed a key, go to title screen */
        else if (board.winScreenB || board.overScreenB) {
            if (timer == -1) {
                timer = System.currentTimeMillis();
            }

            long currTime = System.currentTimeMillis();
            if (currTime - timer >= 5000) {
                board.winScreenB = false;
                board.overScreenB = false;
                board.titleScreenB = true;
                timer = -1;
            }
            board.repaint();
            return;
        }


        /* If we have a normal game state, move all pieces and update pellet status */
        if (!firstFrame) {
            /* The pacman player has two functions, demoMove if we're in demo mode and move if we're in
   user playable mode.  Call the appropriate one here */
            if (board.demo) {
                board.player.demoMove();
            } else {
                board.player.move();
            }

            /* Also move the ghosts, and update the pellet states */
            board.ghost1.move();
            board.ghost2.move();
            board.ghost3.move();
            board.ghost4.move();
            board.player.updatePellet();
            board.ghost1.updatePellet();
            board.ghost2.updatePellet();
            board.ghost3.updatePellet();
            board.ghost4.updatePellet();
        }

        /* We either have a new game or the user has died, either way we have to reset the board */
        if (board.stopped || firstFrame) {
            /*Temporarily stop advancing frames */
            frameTimer.stop();

            /* If user is dying ... */
            while (board.dying > 0) {
                /* Play dying animation. */
                stepFrame(false);
            }

            /* Move all game elements back to starting positions and orientations */
            board.player.currDirection = 'L';
            board.player.direction = 'L';
            board.player.desiredDirection = 'L';
            board.player.x = 200;
            board.player.y = 300;
            board.ghost1.x = 180;
            board.ghost1.y = 180;
            board.ghost2.x = 200;
            board.ghost2.y = 180;
            board.ghost3.x = 220;
            board.ghost3.y = 180;
            board.ghost4.x = 220;
            board.ghost4.y = 180;

            /* Advance a frame to display main state*/
            board.repaint(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

            /*Start advancing frames once again*/
            board.stopped = false;
            frameTimer.start();
        }
        /* Otherwise we're in a normal state, advance one frame*/
        else {
            repaint();
        }
    }

    /* Handles user key presses*/
    public void keyPressed(KeyEvent e) {
        /* Pressing a key in the title screen starts a game */
        if (board.titleScreenB) {
            board.titleScreenB = false;
            return;
        }
        /* Pressing a key in the win screen or game over screen goes to the title screen */
        else if (board.winScreenB || board.overScreenB) {
            board.titleScreenB = true;
            board.winScreenB = false;
            board.overScreenB = false;
            return;
        }
        /* Pressing a key during a demo kills the demo mode and starts a new game */
        else if (board.demo) {
            board.demo = false;
            /* Stop any pacman eating sounds */
            board.sounds.nomNomStop();
            board.gameFrame = 1;
            return;
        }

        /* Otherwise, key presses control the player! */
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                board.player.desiredDirection = 'L';
                break;
            case KeyEvent.VK_RIGHT:
                board.player.desiredDirection = 'R';
                break;
            case KeyEvent.VK_UP:
                board.player.desiredDirection = 'U';
                break;
            case KeyEvent.VK_DOWN:
                board.player.desiredDirection = 'D';
                break;
        }

        repaint();
    }

    /* This function detects user clicks on the menu items on the bottom of the screen */
    public void mousePressed(MouseEvent e) {
        if (board.titleScreenB || board.winScreenB || board.overScreenB) {
            /* If we aren't in the game where a menu is showing, ignore clicks */
            return;
        }

        /* Get coordinates of click */
        int x = e.getX();
        int y = e.getY();
        if (400 <= y && y <= 460) {
            if (100 <= x && x <= 150) {
                /* New game has been clicked */
                board.gameFrame = 1;
            } else if (180 <= x && x <= 300) {
                /* Clear high scores has been clicked */
                board.clearHighScores();
            } else if (350 <= x && x <= 420) {
                /* Exit has been clicked */
                System.exit(0);
            }
        }
    }


    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public void play() {
        initialize();

        stepFrame(true);

        frameTimer = new javax.swing.Timer(30, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stepFrame(false);
            }
        });
        frameTimer.start();

        board.requestFocus();
    }
}
