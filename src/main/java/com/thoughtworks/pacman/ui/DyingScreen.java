package com.thoughtworks.pacman.ui;

import com.thoughtworks.pacman.sound.Sounds;

import java.awt.Color;
import java.awt.Graphics;


public class DyingScreen {
	
	private Sounds sounds;
	private Board board;
	private long timer;

	public DyingScreen(Sounds sounds, Board board) {
		this.sounds = sounds;
		this.board = board;
	}

	public void startDying() {
		sounds.death();
		sounds.nomNomStop();
		timer = System.currentTimeMillis();
	}

	public void paint(Graphics g) {
        sounds.nomNomStop();

        /* Draw the pacman */
        g.drawImage(Board.PACMAN_IMAGE, board.player.x, board.player.y, Color.BLACK, null);
        g.setColor(Color.BLACK);

        /* Kill the pacman */
        if (board.dying == 4)
            g.fillRect(board.player.x, board.player.y, 20, 7);
        else if (board.dying == 3)
            g.fillRect(board.player.x, board.player.y, 20, 14);
        else if (board.dying == 2)
            g.fillRect(board.player.x, board.player.y, 20, 20);
        else if (board.dying == 1)
            g.fillRect(board.player.x, board.player.y, 20, 20);

        /* Take .1 seconds on each frame of death, and then take 2 seconds
for the final frame to allow for the sound effect to end */
        long currTime = System.currentTimeMillis();
        long temp;
        if (board.dying != 1)
            temp = 100;
        else
            temp = 2000;
        /* If it's time to draw a new death frame... */
        if (currTime - timer >= temp) {
        	board.dying--;
            timer = currTime;
            /* If this was the last death frame...*/
            if (board.dying == 0) {
            	board.gameOver();
            }
        }
		
	}
}
