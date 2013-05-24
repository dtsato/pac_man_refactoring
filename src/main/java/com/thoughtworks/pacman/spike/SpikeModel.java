package com.thoughtworks.pacman.spike;

import java.awt.Point;

public class SpikeModel {

	private boolean running;
	private double currentX;
	private double currentY;
	private Point speed;

	public SpikeModel() {
		running = true;
		currentX = 200;
		currentY = 200;
		speed = new Point(10, 0);
	}

	public boolean isRunning() {
		return running;
	}

	public Point getPosition() {
		return new Point((int) currentX, (int) currentY);
	}

	public int getWidth() {
		return 20;
	}

	public int getHeight() {
		return 20;
	}

	public void move(double secondsEllapsed) {
		currentX += speed.x * secondsEllapsed;
		currentY += speed.y * secondsEllapsed;
	}

	public void turnLeft() {
		speed = new Point(-10, 0);
	}

	public void turnRight() {
		speed = new Point(10, 0);
	}

	public void turnUp() {
		speed = new Point(0, -10);
	}

	public void turnDown() {
		speed = new Point(0, 10);
	}
}
