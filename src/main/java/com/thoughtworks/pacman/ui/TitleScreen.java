package com.thoughtworks.pacman.ui;

import com.thoughtworks.pacman.sound.Sounds;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class TitleScreen {
	private static final Image TITLE_SCREEN_IMAGE = Toolkit.getDefaultToolkit().getImage(Board.class.getResource("titleScreen.jpg"));

	private Sounds sounds;
	private Board board;

	public TitleScreen(Sounds sounds, Board board) {
		this.sounds = sounds;
		this.board = board;
	}

	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 600, 600);
		g.drawImage(TITLE_SCREEN_IMAGE, 0, 0, Color.BLACK, null);
		sounds.nomNomStop();
		board.gameFrame = 1;
	}

}
