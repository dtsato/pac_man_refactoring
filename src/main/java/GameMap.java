public class GameMap {
    private final boolean[][] state;

    public GameMap(boolean[][] state) {
        this.state = state;
    }

    public boolean getState(int x, int y) {
        return state[x][y];
    }
}
