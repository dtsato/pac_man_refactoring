package com.thoughtworks.pacman.core;

import com.thoughtworks.pacman.ui.Pacman;

/* This is the pacman object */
public class Player extends Entity {
    public char currDirection;
    public char desiredDirection;

    private int pelletsEaten;

    /* Which pellet the pacman is on top of */
    public int pelletX, pelletY;

    /* teleport is true when travelling through the teleport tunnels*/
    public boolean teleport;

    /* Stopped is set when the pacman is not moving or has been killed */
    public boolean stopped = false;

    /* Constructor places pacman in initial location and orientation */
    public Player(int x, int y, GameMap map) {
        super(x, y, map);
        teleport = false;
        pelletsEaten = 0;
        pelletX = x / Pacman.TILE_SIZE - 1;
        pelletY = y / Pacman.TILE_SIZE - 1;
        currDirection = 'L';
        desiredDirection = 'L';
    }

    public Player(int x, int y) {
        this(x, y, null);
    }

    /* This function is used for demoMode.  It is copied from the Ghost class.  See that for comments */
    public void demoMove() {
        lastX = x;
        lastY = y;
        if (isChoiceDest()) {
            direction = randomDirection();
        }
        switch (direction) {
            case 'L':
                if (isValidDest(x - INCREMENT, y)) {
                    x -= INCREMENT;
                } else if (y == 9 * Pacman.TILE_SIZE && x < 2 * Pacman.TILE_SIZE) {
                    x = Pacman.MAX - Pacman.TILE_SIZE;
                    teleport = true;
                }
                break;
            case 'R':
                if (isValidDest(x + Pacman.TILE_SIZE, y)) {
                    x += INCREMENT;
                } else if (y == 9 * Pacman.TILE_SIZE && x > Pacman.MAX - Pacman.TILE_SIZE * 2) {
                    x = Pacman.TILE_SIZE;
                    teleport = true;
                }
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
        currDirection = direction;
        frameCount++;
    }

    /* The move function moves the pacman for one frame in non demo mode */
    public void move() {
        lastX = x;
        lastY = y;

        /* Try to turn in the direction input by the user */
        /*Can only turn if we're in center of a grid*/
        if (isChoiceDest() ||
                /* Or if we're reversing*/
                (desiredDirection == 'L' && currDirection == 'R') ||
                (desiredDirection == 'R' && currDirection == 'L') ||
                (desiredDirection == 'U' && currDirection == 'D') ||
                (desiredDirection == 'D' && currDirection == 'U')
                ) {
            switch (desiredDirection) {
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
        /* If we haven't moved, then move in the direction the pacman was headed anyway */
        if (lastX == x && lastY == y) {
            switch (currDirection) {
                case 'L':
                    if (isValidDest(x - INCREMENT, y))
                        x -= INCREMENT;
                    else if (y == 9 * Pacman.TILE_SIZE && x < 2 * Pacman.TILE_SIZE) {
                        x = Pacman.MAX - Pacman.TILE_SIZE;
                        teleport = true;
                    }
                    break;
                case 'R':
                    if (isValidDest(x + Pacman.TILE_SIZE, y))
                        x += INCREMENT;
                    else if (y == 9 * Pacman.TILE_SIZE && x > Pacman.MAX - Pacman.TILE_SIZE * 2) {
                        x = Pacman.TILE_SIZE;
                        teleport = true;
                    }
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

        /* If we did change direction, update currDirection to reflect that */
        else {
            currDirection = desiredDirection;
        }

        /* If we didn't move at all, set the stopped flag */
        if (lastX == x && lastY == y)
            stopped = true;

            /* Otherwise, clear the stopped flag and increment the frameCount for animation purposes*/
        else {
            stopped = false;
            frameCount++;
        }
    }

    /* Update what pellet the pacman is on top of */
    public void updatePellet() {
        if (isChoiceDest()) {
            pelletX = x / Pacman.TILE_SIZE - 1;
            pelletY = y / Pacman.TILE_SIZE - 1;
        }
    }

    public void eatPellet() {
        pelletsEaten++;
        map.eatPellet(pelletX, pelletY);
    }

    public int getPelletsEaten() {
        return pelletsEaten;
    }
}
