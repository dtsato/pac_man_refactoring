package com.thoughtworks.pacman.spike;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class SpikeController implements KeyListener {
	private SpikeModel model;

	public SpikeController(SpikeModel model) {
		this.model = model;
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			model.turnLeft();
			break;
		case KeyEvent.VK_RIGHT:
			model.turnRight();
			break;
		case KeyEvent.VK_UP:
			model.turnUp();
			break;
		case KeyEvent.VK_DOWN:
			model.turnDown();
			break;
		}
	}
}
