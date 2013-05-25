package com.thoughtworks.pacman.ui;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class OverScreen {
	private static final Image GAME_OVER_IMAGE = Toolkit.getDefaultToolkit().getImage(Board.class.getResource("gameOver.jpg"));

	private Board board;

	public OverScreen(Board board) {
		this.board = board;
	}

	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 600, 600);
		g.drawImage(GAME_OVER_IMAGE, 0, 0, Color.BLACK, null);
		board.newGame();
	}
}
