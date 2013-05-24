package com.thoughtworks.pacman.spike;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferStrategy;

@SuppressWarnings("serial")
public class SpikeBoard extends Canvas {
	static final int WINDOW_WIDTH = 400;
	static final int WINDOW_HEIGHT = 400;

	private SpikeModel model;

	public SpikeBoard(SpikeModel model) {
		this.model = model;
	}

	public void redraw() {
		BufferStrategy strategy = getBufferStrategy();
		Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

		g.setColor(Color.RED);
		Rectangle location = getCurrentLocation();
		g.fillOval(location.x, location.y, location.width, location.height);

		g.dispose();
		strategy.show();
	}

	private Rectangle getCurrentLocation() {
		Point currentPosition = model.getPosition();
		int ballWidth = model.getWidth();
		int ballHeight = model.getHeight();
		int x = currentPosition.x - ballWidth / 2;
		int y = currentPosition.y - ballHeight / 2;
		return new Rectangle(x, y, ballWidth, ballHeight);
	}
}
