import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class OverScreen {
	private static final Image GAME_OVER_IMAGE = Toolkit.getDefaultToolkit()
			.getImage(Pacman.class.getResource("img/gameOver.jpg"));

	private Sounds sounds;
	private Board board;

	public OverScreen(Sounds sounds, Board board) {
		this.sounds = sounds;
		this.board = board;
	}

	public void paing(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 600, 600);
		g.drawImage(GAME_OVER_IMAGE, 0, 0, Color.BLACK, null);
		board.gameFrame = 1;
		sounds.nomNomStop();
	}
}
