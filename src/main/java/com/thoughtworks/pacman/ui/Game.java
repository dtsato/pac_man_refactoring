package com.thoughtworks.pacman.ui;


public class Game {

	private Board board;

	public Game(Board board) {
		this.board = board;
	}

	public boolean pacmanDied() {
		return !board.stopped && (board.player.collidesWith(board.ghost1) || board.player.collidesWith(board.ghost2) ||
	    		board.player.collidesWith(board.ghost3) || board.player.collidesWith(board.ghost4));
	}

	public void killPacman() {
		board.dying = 4;
		board.dyingScreen.startDying();
	
		board.numLives--;
		board.stopped = true;
	}

}
