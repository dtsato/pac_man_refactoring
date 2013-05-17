package com.thoughtworks.pacman.core;

import com.thoughtworks.pacman.ui.Pacman;

public class GameMap {
    private final boolean[][] state;
    private final boolean[][] pellets;

    public GameMap() {
        pellets = new boolean[Pacman.GRID_SIZE][Pacman.GRID_SIZE];
        for (int i = 0; i < Pacman.GRID_SIZE; i++) {
            for (int j = 0; j < Pacman.GRID_SIZE; j++) {
                pellets[i][j] = true;
            }
        }
        state = new boolean[Pacman.GRID_SIZE][Pacman.GRID_SIZE];
        for (int i = 0; i < Pacman.GRID_SIZE; i++) {
            for (int j = 0; j < Pacman.GRID_SIZE; j++) {
                state[i][j] = true;
            }
        }

        fillWall(1, 1);
        fillWall(2, 1);
        fillWall(3, 1);
        fillWall(5, 1);
        fillWall(6, 1);
        fillWall(7, 1);
        fillWall(9, 0);
        fillWall(9, 1);
        fillWall(11, 1);
        fillWall(12, 1);
        fillWall(13, 1);
        fillWall(15, 1);
        fillWall(16, 1);
        fillWall(17, 1);
        fillWall(1, 3);
        fillWall(2, 3);
        fillWall(3, 3);
        fillWall(7, 3);
        fillWall(8, 3);
        fillWall(9, 3);
        fillWall(10, 3);
        fillWall(11, 3);
        fillWall(9, 3);
        fillWall(9, 4);
        fillWall(9, 5);
        fillWall(15, 3);
        fillWall(16, 3);
        fillWall(17, 3);
        fillWall(0, 5);
        fillWall(0, 6);
        fillWall(0, 7);
        fillWall(1, 5);
        fillWall(1, 6);
        fillWall(1, 7);
        fillWall(2, 5);
        fillWall(2, 6);
        fillWall(2, 7);
        fillWall(3, 5);
        fillWall(3, 6);
        fillWall(3, 7);
        fillWall(15, 5);
        fillWall(15, 6);
        fillWall(15, 7);
        fillWall(16, 5);
        fillWall(16, 6);
        fillWall(16, 7);
        fillWall(17, 5);
        fillWall(17, 6);
        fillWall(17, 7);
        fillWall(18, 5);
        fillWall(18, 6);
        fillWall(18, 7);
        fillWall(0, 9);
        fillWall(0, 10);
        fillWall(0, 11);
        fillWall(1, 9);
        fillWall(1, 10);
        fillWall(1, 11);
        fillWall(2, 9);
        fillWall(2, 10);
        fillWall(2, 11);
        fillWall(3, 9);
        fillWall(3, 10);
        fillWall(3, 11);
        fillWall(15, 9);
        fillWall(15, 10);
        fillWall(15, 11);
        fillWall(16, 9);
        fillWall(16, 10);
        fillWall(16, 11);
        fillWall(17, 9);
        fillWall(17, 10);
        fillWall(17, 11);
        fillWall(18, 9);
        fillWall(18, 10);
        fillWall(18, 11);
        fillWall(7, 7);
        fillWall(8, 7);
        fillWall(10, 7);
        fillWall(11, 7);
        fillWall(7, 8);
        fillWall(7, 9);
        fillWall(8, 9);
        fillWall(9, 9);
        fillWall(10, 9);
        fillWall(11, 9);
        fillWall(11, 8);
        fillWall(5, 5);
        fillWall(6, 5);
        fillWall(7, 5);
        fillWall(5, 3);
        fillWall(5, 4);
        fillWall(5, 5);
        fillWall(5, 6);
        fillWall(5, 7);
        fillWall(13, 3);
        fillWall(13, 4);
        fillWall(13, 5);
        fillWall(13, 6);
        fillWall(13, 7);
        fillWall(11, 5);
        fillWall(12, 5);
        fillWall(13, 5);
        fillWall(13, 9);
        fillWall(13, 10);
        fillWall(13, 11);
        fillWall(5, 9);
        fillWall(5, 10);
        fillWall(5, 11);
        fillWall(7, 11);
        fillWall(8, 11);
        fillWall(9, 11);
        fillWall(10, 11);
        fillWall(11, 11);
        fillWall(9, 12);
        fillWall(9, 13);
        fillWall(5, 13);
        fillWall(6, 13);
        fillWall(7, 13);
        fillWall(11, 13);
        fillWall(12, 13);
        fillWall(13, 13);
        fillWall(1, 13);
        fillWall(2, 13);
        fillWall(3, 13);
        fillWall(3, 13);
        fillWall(3, 14);
        fillWall(3, 15);
        fillWall(15, 13);
        fillWall(16, 13);
        fillWall(17, 13);
        fillWall(15, 13);
        fillWall(15, 14);
        fillWall(15, 15);
        fillWall(0, 15);
        fillWall(1, 15);
        fillWall(17, 15);
        fillWall(18, 15);
        fillWall(7, 15);
        fillWall(8, 15);
        fillWall(9, 15);
        fillWall(10, 15);
        fillWall(11, 15);
        fillWall(9, 15);
        fillWall(9, 16);
        fillWall(9, 17);
        fillWall(1, 17);
        fillWall(2, 17);
        fillWall(3, 17);
        fillWall(4, 17);
        fillWall(5, 17);
        fillWall(6, 17);
        fillWall(7, 17);
        fillWall(11, 17);
        fillWall(12, 17);
        fillWall(13, 17);
        fillWall(14, 17);
        fillWall(15, 17);
        fillWall(16, 17);
        fillWall(17, 17);
        fillWall(13, 15);
        fillWall(13, 16);
        fillWall(5, 15);
        fillWall(5, 16);
        fillWall(5, 17);

        /* Handle the weird spots with no pellets*/
        for (int i = 5; i < 14; i++) {
            for (int j = 5; j < 12; j++) {
                pellets[i][j] = false;
            }
        }
        pellets[9][7] = false;
        pellets[8][8] = false;
        pellets[9][8] = false;
        pellets[10][8] = false;
    }

    private void fillWall(int x, int y) {
        state[x][y] = false;
        pellets[x][y] = false;
    }

    public boolean hasPellet(int x, int y) {
        return pellets[x][y];
    }

    public boolean hasWall(int x, int y) {
        return !state[x][y];
    }

    public void eatPellet(int x, int y) {
        pellets[x][y] = false;
    }
}
