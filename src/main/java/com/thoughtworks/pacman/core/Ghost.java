package com.thoughtworks.pacman.core;

import com.thoughtworks.pacman.ui.Pacman;

/* Ghost class controls the ghost. */
public class Ghost extends Entity {

    /* The pellet the ghost is on top of */
    private int pelletX, pelletY;

    /* The pellet the ghost was last on top of */
    public int lastPelletX, lastPelletY;

    /*Constructor places ghost and updates states*/
    public Ghost(int x, int y, GameMap map) {
        super(x, y, map);
        pelletX = x / Pacman.TILE_SIZE - 1;
        pelletY = x / Pacman.TILE_SIZE - 1;
        lastPelletX = pelletX;
        lastPelletY = pelletY;
    }

    public Ghost(int x, int y) {
        this(x, y, null);
    }

    /* update pellet status */
    public void updatePellet() {
        int tempX, tempY;
        tempX = x / Pacman.TILE_SIZE - 1;
        tempY = y / Pacman.TILE_SIZE - 1;
        if (tempX != pelletX || tempY != pelletY) {
            lastPelletX = pelletX;
            lastPelletY = pelletY;
            pelletX = tempX;
            pelletY = tempY;
        }

    }

    /* Random move function for ghost */
    public void move() {
        lastX = x;
        lastY = y;

        /* If we can make a decision, pick a new direction randomly */
        if (isChoiceDest()) {
            direction = randomDirection();
        }

        /* If that direction is valid, move that way */
        switch (direction) {
            case 'L':
                if (isValidDest(x - INCREMENT, y))
                    x -= INCREMENT;
                break;
            case 'R':
                if (isValidDest(x + Pacman.TILE_SIZE, y))
                    x += INCREMENT;
                break;
            case 'U':
                if (isValidDest(x, y - INCREMENT))
                    y -= INCREMENT;
                break;
            case 'D':
                if (isValidDest(x, y + Pacman.TILE_SIZE))
                    y += INCREMENT;
                break;
        }
    }
}
