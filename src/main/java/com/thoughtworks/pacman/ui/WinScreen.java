package com.thoughtworks.pacman.ui;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class WinScreen {
	private static final Image WIN_SCREEN_IMAGE = Toolkit.getDefaultToolkit().getImage(Board.class.getResource("winScreen.jpg"));

	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 600, 600);
		g.drawImage(WIN_SCREEN_IMAGE, 0, 0, Color.BLACK, null);
	}

}
