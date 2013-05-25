package com.thoughtworks.pacman.ui;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class TitleScreen {
	private static final Image TITLE_SCREEN_IMAGE = Toolkit.getDefaultToolkit().getImage(Board.class.getResource("titleScreen.jpg"));

	private Board board;

	public TitleScreen(Board board) {
		this.board = board;
	}

	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 600, 600);
		g.drawImage(TITLE_SCREEN_IMAGE, 0, 0, Color.BLACK, null);
		board.newGame();
	}

}
