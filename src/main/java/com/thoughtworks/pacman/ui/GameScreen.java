package com.thoughtworks.pacman.ui;

import java.awt.Color;
import java.awt.Graphics;

import com.thoughtworks.pacman.sound.Sounds;

public class GameScreen {

    private Sounds sounds;
    private Board board;
    private Game game;
    private long timer;

    public GameScreen(Sounds sounds, Board board) {
        this.sounds = sounds;
        this.board = board;
        this.game = new Game(board);
    }

    private void drawScore(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Pacman.WINDOW_WIDTH, 20);
        g.setColor(Color.YELLOW);
        g.setFont(Board.FONT);
        if (board.demo)
            g.drawString("DEMO MODE PRESS ANY KEY TO START A GAME\t High Score: " + board.highScore, 20, 10);
        else
            g.drawString("Score: " + board.currScore + "\t High Score: " + board.highScore, 20, 10);
    }

    private void drawBorder(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawRect(19, 19, 382, 382);
    }

    private void drawWalls(Graphics g) {
        for (int i = 0; i < Pacman.GRID_SIZE; i++) {
            for (int j = 0; j < Pacman.GRID_SIZE; j++) {
                if (board.map.hasWall(i, j)) {
                    g.setColor(Color.BLUE);
                    g.fillRect((i + 1) * Pacman.TILE_SIZE, (j + 1) * Pacman.TILE_SIZE, Pacman.TILE_SIZE,
                            Pacman.TILE_SIZE);
                }
            }
        }
    }

    private void drawLives(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, Pacman.MAX + 5, Pacman.WINDOW_WIDTH, Pacman.TILE_SIZE);

        g.setColor(Color.YELLOW);
        g.setFont(Board.FONT);
        for (int i = 0; i < board.numLives; i++) {
            g.fillOval(Pacman.TILE_SIZE * (i + 1), Pacman.MAX + 5, Pacman.TILE_SIZE, Pacman.TILE_SIZE);
        }
        g.drawString("Reset", 100, Pacman.MAX + 5 + Pacman.TILE_SIZE);
        g.drawString("Clear High Scores", 180, Pacman.MAX + 5 + Pacman.TILE_SIZE);
        g.drawString("Exit", 350, Pacman.MAX + 5 + Pacman.TILE_SIZE);
    }

    private void fillBackground(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Pacman.WINDOW_WIDTH, Pacman.WINDOW_HEIGHT);
    }

    private void fillPellet(int x, int y, Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillOval((x + 1) * Pacman.TILE_SIZE + 8, (y + 1) * Pacman.TILE_SIZE + 8, 4, 4);
    }

    private void drawPellets(Graphics g) {
        for (int i = 0; i < Pacman.GRID_SIZE - 1; i++) {
            for (int j = 0; j < Pacman.GRID_SIZE - 1; j++) {
                if (board.map.hasPellet(i, j))
                    fillPellet(i, j, g);
            }
        }
    }

    public void paint(Graphics g) {
        fillBackground(g);
        drawWalls(g);
        drawLives(g);
        drawPellets(g);
        drawScore(g);
        drawBorder(g);

        if (board.gameFrame != 0) {
            handleGameStartFrames();
            return;
        }

        if (game.pacmanDied()) {
            game.killPacman();
        }

        /* Eat pellets */
        if (board.map.hasPellet(board.player.pelletX, board.player.pelletY)) {
            board.lastPelletEatenX = board.player.pelletX;
            board.lastPelletEatenY = board.player.pelletY;

            /* Play eating sound */
            board.sounds.nomNom();

            /* Increment pellets eaten value to track for end game */
            board.player.eatPellet();

            /* Increment the score */
            board.currScore += 50;

            /* Update the screen to reflect the new score */
            drawScore(g);

            /* If this was the last pellet */
            if (board.player.getPelletsEaten() == 173) {
                /* Demo mode can't get a high score */
                if (!board.demo) {
                    if (board.currScore > board.highScore) {
                        board.updateScore(board.currScore);
                    }
                    board.winScreenB = true;
                }
                else {
                    board.titleScreenB = true;
                }
                return;
            }
        }

        /* If we moved to a location without pellets, stop the sounds */
        else if ((board.player.pelletX != board.lastPelletEatenX || board.player.pelletY != board.lastPelletEatenY)
                || board.player.stopped) {
            /* Stop any pacman eating sounds */
            sounds.nomNomStop();
        }

        /* Draw the ghosts */
        if (board.ghost1.frameCount < 5) {
            /* Draw first frame of ghosts */
            g.drawImage(Board.GHOST_10_IMAGE, board.ghost1.x, board.ghost1.y, Color.BLACK, null);
            g.drawImage(Board.GHOST_20_IMAGE, board.ghost2.x, board.ghost2.y, Color.BLACK, null);
            g.drawImage(Board.GHOST_30_IMAGE, board.ghost3.x, board.ghost3.y, Color.BLACK, null);
            g.drawImage(Board.GHOST_40_IMAGE, board.ghost4.x, board.ghost4.y, Color.BLACK, null);
            board.ghost1.frameCount++;
        }
        else {
            /* Draw second frame of ghosts */
            g.drawImage(Board.GHOST_11_IMAGE, board.ghost1.x, board.ghost1.y, Color.BLACK, null);
            g.drawImage(Board.GHOST_21_IMAGE, board.ghost2.x, board.ghost2.y, Color.BLACK, null);
            g.drawImage(Board.GHOST_31_IMAGE, board.ghost3.x, board.ghost3.y, Color.BLACK, null);
            g.drawImage(Board.GHOST_41_IMAGE, board.ghost4.x, board.ghost4.y, Color.BLACK, null);
            if (board.ghost1.frameCount >= 10)
                board.ghost1.frameCount = 0;
            else
                board.ghost1.frameCount++;
        }

        /* Draw the pacman */
        if (board.player.frameCount < 5) {
            /* Draw mouth closed */
            g.drawImage(Board.PACMAN_IMAGE, board.player.x, board.player.y, Color.BLACK, null);
        }
        else {
            /* Draw mouth open in appropriate direction */
            if (board.player.frameCount >= 10)
                board.player.frameCount = 0;

            switch (board.player.currDirection) {
            case 'L':
                g.drawImage(Board.PACMAN_LEFT_IMAGE, board.player.x, board.player.y, Color.BLACK, null);
                break;
            case 'R':
                g.drawImage(Board.PACMAN_RIGHT_IMAGE, board.player.x, board.player.y, Color.BLACK, null);
                break;
            case 'U':
                g.drawImage(Board.PACMAN_UP_IMAGE, board.player.x, board.player.y, Color.BLACK, null);
                break;
            case 'D':
                g.drawImage(Board.PACMAN_DOWN_IMAGE, board.player.x, board.player.y, Color.BLACK, null);
                break;
            }
        }

        drawBorder(g);
    }

    private void handleGameStartFrames() {
        if (board.gameFrame == 1) {
            board.gameFrame++;
        }
        else if (board.gameFrame == 2) {
            board.gameFrame++;
        }
        else if (board.gameFrame == 3) {
            board.gameFrame++;
            /* Play the newGame sound effect */
            sounds.newGame();
            timer = System.currentTimeMillis();
        }
        /* Fourth frame of new game */
        else if (board.gameFrame == 4) {
            /* Stay in this state until the sound effect is over */
            long currTime = System.currentTimeMillis();
            if (currTime - timer >= 5000) {
                board.gameFrame = 0;
            }
        }
    }

}
