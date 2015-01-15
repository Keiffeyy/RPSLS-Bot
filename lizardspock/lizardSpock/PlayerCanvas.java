package lizardSpock;
import java.awt.Graphics;
import javax.swing.JPanel;

import players.SLPlayer;

/**
 * Implements the drawing surface for the players' symbols
 * 
 * @author Sam Scott
 * @version 1.0 (November 4, 2007)
 */
public class PlayerCanvas extends JPanel {

	/**
	 * The player to be drawn on this canvas
	 */
	private SLPlayer player;

	/**
	 * Loads the player so it can call the draw method.
	 *
	 * @param player The player that uses this canvas
	 */
	PlayerCanvas(SLPlayer player) {
		this.player = player;
	}

	/**
	 * Draws the player on the given graphics context.
	 *
	 * @param g The graphics context
	 */
	public void paintComponent(Graphics g) {
		player.draw(g);
	}
}
