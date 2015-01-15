package players;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import lizardSpock.ApplicationAudioClip;
import lizardSpock.SLArena;


/**
 * Rock-scissors-paper player that always copies the opponent's last move.
 * 
 * @version <br>1.0 (November 4, 2007)
 * @version <br>2.0 (August 10, 2011) Changed to use ApplicationAudioClip
 * @author Sam Scott
 */
public class CopyCat extends SLPlayer implements Runnable {

	/**
	 * The next (or current) move
	 */
	public int myMove;
	
	/**
	 * Controls flashing animation
	 */
	public boolean animationParam = false;

	/**
	 * Constructor loads sound and starts a thread for animation.
	 *
	 * @param arena
	 */
	public CopyCat(SLArena arena) {
		super(arena);
		victorySound = new ApplicationAudioClip("sounds/baby_x.wav");
		myMove = SCISSORS;
		Thread t = new Thread(this);
		t.start();
	}

	/**
	 * Draws the player's logo
	 *
	 * @param g
	 *            the graphics context
	 */
	public void draw(Graphics g) {
		g.setColor(Color.ORANGE);
		g.fillRect(0, 0, arena.getPlayerScreenWidth(), arena
				.getPlayerScreenHeight());
		g.setColor(Color.GREEN);
		g.fillRect(10, 10, arena.getPlayerScreenWidth() - 20, arena
				.getPlayerScreenHeight() - 20);
		g.setColor(Color.BLACK);
		g.setFont(new Font("MonoSpaced", Font.BOLD, 20));
		if (animationParam)
			g.drawString("?", arena.getPlayerScreenWidth() / 2 - 8, arena
					.getPlayerScreenHeight() / 2 + 5);
	}

	/**
	 * Returns the next move
	 *
	 * @return the move 
	 */
	public int move() {
		return myMove;
	}

	/**
	 * Returns the player's name
	 *
	 * @return the name of the player
	 */
	public String name() {
		return "Copy Cat";
	}

	/**
	 * Decides the next move (copies the last player's move)
	 *
	 * @param move the opponent's move
	 */
	public void opponentMove(int move) {
		myMove = move;
	}

	/**
	 * Handles logo animation
	 */
	public void run() {
		while (true) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}
			if (animationParam)
				animationParam = false;
			else
				animationParam = true;
		}
	}

}
