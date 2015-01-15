package players;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import lizardSpock.ApplicationAudioClip;
import lizardSpock.SLArena;


/**
 * Rock-scissors-paper player that always uses the same sequence. 

 * @version <br>1.0 (November 4, 2007)
 * @version <br>2.0 (March, 2011) Updated for the Spock-Lizard expansion
 * @version <br>2.1 (August, 2011) Updated to use ApplicationAudioClip
 * 
 * @author Sam Scott
 */
public class MrPredictable extends SLPlayer implements Runnable {

	/**
	 * Holds the most recent move of this player
	 */
	public int lastMove;
	/**
	 * Controls the cycling rock-scissors-paper animation
	 */
	public int animationParam = 0;

	/**
	 * Loads image and starts animation thread
	 *
	 * @param arena
	 *            The arena applet
	 */
	public MrPredictable(SLArena arena) {
		super(arena);
		victorySound = new ApplicationAudioClip("sounds/ahem_x.wav");
		lastMove = ROCK;
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
		g.setColor(Color.BLUE);
		g.fillRect(0, 0, arena.getPlayerScreenWidth(), arena
				.getPlayerScreenHeight());
		g.setColor(Color.RED);
		g.fillRect(10, 10, arena.getPlayerScreenWidth() - 20, arena
				.getPlayerScreenHeight() - 20);
		g.setColor(Color.BLACK);
		g.setFont(new Font("MonoSpaced", Font.BOLD, 20));
		if (animationParam == 0)
			letter = "Rock";
		else if (animationParam == 1)
			letter = "Scissors";
		else if (animationParam == 2)
			letter = "Paper";
		else if (animationParam == 3)
			letter = "Spock";
		else
			letter = "Lizard";
		g.drawString(letter, 25, arena.getPlayerScreenHeight() / 2 + 10);
	}

	/**
	 * Returns the next move.
	 *
	 * @return the move (ROCK, SCISSORS, or PAPER)
	 */
	@Override
	public int move() {
		if (lastMove == ROCK) {
			lastMove = PAPER;
			return PAPER;
		}
		if (lastMove == PAPER) {
			lastMove = SCISSORS;
			return SCISSORS;
		}
		if (lastMove == SCISSORS) {
			lastMove = SPOCK;
			return SPOCK;
		}
		if (lastMove == SPOCK) {
			lastMove = LIZARD;
			return LIZARD;
		}
		lastMove = ROCK;
		return ROCK;
	}

	/**
	 * Returns the player's name
	 *
	 * @return the name of the player
	 */
	@Override
	public String name() {
		return "Mr. Predictable";
	}

	/**
	 * Unused
	 *
	 * @param result
	 *            What happened last game (win, loss, or tie)
	 */
	@Override
	public void opponentMove(int result) {
	}

	/**
	 * Handles logo animation
	 */
	public void run() {
		while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			animationParam = (animationParam + 1) % 5;
		}
	}

}
