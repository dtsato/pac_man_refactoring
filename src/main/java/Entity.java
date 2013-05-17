import java.util.HashSet;
import java.util.Set;

/* Both Player and Ghost inherit Entity.  Has generic functions relevant to both*/
class Entity {
    /* Framecount is used to count animation frames*/
    int frameCount = 0;

    protected final static int MAX = Pacman.TILE_SIZE * 20;
    protected final static int INCREMENT = 4;

    protected final GameMap map;

    int x, y;
    int lastX, lastY;
    protected char direction;

    /* Generic constructor */
    public Entity(int x, int y, GameMap map) {
        this.x = x;
        this.y = y;
        this.map = map;
        this.lastX = x;
        this.lastY = y;
        this.direction = 'L';
    }

    /* Determines if a set of coordinates is a valid destination.*/
    protected boolean isValidDest(int x, int y) {
        /* The first statements check that the x and y are inbounds.  The last statement checks the map to
  see if it's a valid location */
        if (((x % Pacman.TILE_SIZE == 0) || (y % Pacman.TILE_SIZE == 0)) && Pacman.TILE_SIZE <= x && x < MAX && Pacman.TILE_SIZE <= y && y < MAX && map.getState(x / Pacman.TILE_SIZE - 1, y / Pacman.TILE_SIZE - 1)) {
            return true;
        }
        return false;
    }

    /* Chooses a new direction randomly for the ghost to move */
    protected char randomDirection() {
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
                lookX -= INCREMENT;
            } else if (random == 2) {
                newDirection = 'R';
                lookX += Pacman.TILE_SIZE;
            } else if (random == 3) {
                newDirection = 'U';
                lookY -= INCREMENT;
            } else if (random == 4) {
                newDirection = 'D';
                lookY += Pacman.TILE_SIZE;
            }
            if (newDirection != backwards) {
                set.add(newDirection);
            }
        }
        return newDirection;
    }

    /* This function is used for demoMode.  It is copied from the Ghost class.  See that for comments */
    protected boolean isChoiceDest() {
        return x % Pacman.TILE_SIZE == 0 && y % Pacman.TILE_SIZE == 0;
    }
}
