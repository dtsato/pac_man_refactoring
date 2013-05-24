package com.thoughtworks.pacman.spike;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Spike {
	public static void main(String[] args) {
		Spike spike = new Spike();
		spike.initialize();
		spike.run();
	}

	private SpikeBoard board;
	private SpikeModel model;

	private void initialize() {
		model = new SpikeModel();

		JFrame container = new JFrame();

		JPanel panel = (JPanel) container.getContentPane();
		panel.setPreferredSize(new Dimension(SpikeBoard.WINDOW_WIDTH,
				SpikeBoard.WINDOW_HEIGHT));
		panel.setLayout(null);

		board = new SpikeBoard(model);
		board.setBounds(0, 0, SpikeBoard.WINDOW_WIDTH, SpikeBoard.WINDOW_HEIGHT);
		panel.add(board);
		board.setIgnoreRepaint(true);

		container.pack();
		container.setResizable(false);
		container.setVisible(true);

		container.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		SpikeController controller = new SpikeController(model);
		container.addKeyListener(controller);

		board.createBufferStrategy(2);
	}

	private void run() {
		long lastLoopTime = System.currentTimeMillis();

		while (model.isRunning()) {
			long delta = System.currentTimeMillis() - lastLoopTime;
			lastLoopTime = System.currentTimeMillis();

			model.move(delta / 1000.0);

			board.redraw();

			try {
				Thread.sleep(10);
			} catch (Exception e) {
			}
		}
	}
}
