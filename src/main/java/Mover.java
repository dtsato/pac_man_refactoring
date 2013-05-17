import java.util.HashSet;
import java.util.Set;

/* Both Player and Ghost inherit Mover.  Has generic functions relevant to both*/
class Mover {
    /* Framecount is used to count animation frames*/
    int frameCount = 0;

    /* gridSize is the size of one square in the game.
max is the height/width of the game.
increment is the speed at which the object moves,
1 increment per move() call */
    protected int gridSize;
    protected int max;
    protected int increment;
    protected final GameMap map;

    int x, y;
    int lastX, lastY;
    protected char direction;

    /* Generic constructor */
    public Mover(int x, int y, GameMap map) {
        this.x = x;
        this.y = y;
        this.map = map;
        this.lastX = x;
        this.lastY = y;
        gridSize = 20;
        increment = 4;
        max = 400;
        direction = 'L';
    }

    /* Determines if a set of coordinates is a valid destination.*/
    protected boolean isValidDest(int x, int y) {
        /* The first statements check that the x and y are inbounds.  The last statement checks the map to
  see if it's a valid location */
        if ((((x) % 20 == 0) || ((y) % 20) == 0) && 20 <= x && x < 400 && 20 <= y && y < 400 && map.getState(x / 20 - 1, y / 20 - 1)) {
            return true;
        }
        return false;
    }

    /* Chooses a new direction randomly for the ghost to move */
    protected char newDirection() {
        int random;
        char backwards = 'U';
        int lookX = x, lookY = y;
        Set<Character> set = new HashSet<Character>();
        switch (direction) {
            case 'L':
                backwards = 'R';
                break;
            case 'R':
                backwards = 'L';
                break;
            case 'U':
                backwards = 'D';
                break;
            case 'D':
                backwards = 'U';
                break;
        }

        char newDirection = backwards;
        /* While we still haven't found a valid direction */
        while (newDirection == backwards || !isValidDest(lookX, lookY)) {
            /* If we've tried every location, turn around and break the loop */
            if (set.size() == 3) {
                newDirection = backwards;
                break;
            }

            lookX = x;
            lookY = y;

            /* Randomly choose a direction */
            random = (int) (Math.random() * 4) + 1;
            if (random == 1) {
                newDirection = 'L';
                lookX -= increment;
            } else if (random == 2) {
                newDirection = 'R';
                lookX += gridSize;
            } else if (random == 3) {
                newDirection = 'U';
                lookY -= increment;
            } else if (random == 4) {
                newDirection = 'D';
                lookY += gridSize;
            }
            if (newDirection != backwards) {
                set.add(newDirection);
            }
        }
        return newDirection;
    }

    /* This function is used for demoMode.  It is copied from the Ghost class.  See that for comments */
    protected boolean isChoiceDest() {
        return x % gridSize == 0 && y % gridSize == 0;
    }
}
