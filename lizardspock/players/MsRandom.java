package players;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import lizardSpock.ApplicationAudioClip;
import lizardSpock.SLArena;


/**
 * Rock-scissors-paper player that plays randomly. 
 * 
 * @version <br>1.0 (November 4, 2007)
 * @version <br>2.0 (March, 2011) Updated for Spock-Lizard expansion
 * @version <br>2.1 (August, 2011) Updated to use ApplicationAudioClip
 * @author Sam Scott
 */
public class MsRandom extends SLPlayer implements Runnable {

	/**
	 * Determins which word (rock, scissors, paper) to display next
	 */
	public int animationParam = 0;
	/**
	 * Holds randomly determined location for word (rock, scissors, paper)
	 */
	public int drawX = 20, drawY = 20;

	/**
	 * Loads sound and starts a thread for animation.
	 *
	 * @param arena
	 */
	public MsRandom(SLArena arena) {
		super(arena);
		victorySound = new ApplicationAudioClip("sounds/boing3.wav");
		Thread t = new Thread(this);
		t.start();
	}

	/**
	 * Draws the player's logo
	 *
	 * @param g
	 *            the graphics context
	 */
	@Override
	public void draw(Graphics g) {
		String letter;
		g.setColor(Color.CYAN);
		g.fillRect(0, 0, arena.getPlayerScreenWidth(), arena
				.getPlayerScreenHeight());
		g.setColor(Color.MAGENTA);
		g.fillRect(10, 10, arena.getPlayerScreenWidth() - 20, arena
				.getPlayerScreenHeight() - 20);
		g.setColor(Color.BLACK);
		g.setFont(new Font("MonoSpaced", Font.BOLD, 20));
		if (animationParam == 0)
			letter = "Rock";
		else if (animationParam == 1)
			letter = "Scissors";
		else
			letter = "Paper";
		g.drawString(letter, drawX, drawY);
	}

	/**
	 * Returns the next move .
	 *
	 * @return the move (ROCK, SCISSORS, or PAPER)
	 */
	@Override
	public int move() {
		int move = (int) (Math.random() * 5);
		if (move == 0)
			return ROCK;
		if (move == 1)
			return PAPER;
		if (move == 2)
			return SCISSORS;
		if (move == 3)
			return SPOCK;
		return LIZARD;
	}

	/**
	 * Returns the player's name
	 *
	 * @return the name of the player
	 */
	@Override
	public String name() {
		return "Ms. Random";
	}

	/**
	 * Unused
	 *
	 * @param move the opponent's move
	 */
	@Override
	public void opponentMove(int move) {
	}

	/**
	 * Handles logo animation
	 */
	public void run() {
		while (true) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
			int oldAP = animationParam;
			do
				animationParam = (int) (Math.random() * 3);
			while (animationParam == oldAP);
			drawX = (int) (Math.random() * (arena.getPlayerScreenWidth() - 120) + 10);
			drawY = (int) (Math.random() * (arena.getPlayerScreenHeight() - 50) + 32);
		}
	}
}
