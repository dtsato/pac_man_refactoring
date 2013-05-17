import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class WinScreen {
	private static final Image WIN_SCREEN_IMAGE = Toolkit.getDefaultToolkit()
			.getImage(Pacman.class.getResource("img/winScreen.jpg"));

	private Sounds sounds;
	private Board board;

	public WinScreen(Sounds sounds, Board board) {
		this.sounds = sounds;
		this.board = board;
	}

	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 600, 600);
		g.drawImage(WIN_SCREEN_IMAGE, 0, 0, Color.BLACK, null);
		board.gameFrame = 1;
		sounds.nomNomStop();
	}

}
