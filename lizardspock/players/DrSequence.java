package players;

import java.awt.Color;
import java.awt.Graphics;

import lizardSpock.SLArena;


/**
 * This player repeats a randomly generated sequence of moves
 * @author Sam Scott
 * @version <br>1.0 (March, 2011)
 */
public class DrSequence extends SLPlayer {

	/**
	 * The length of the sequence
	 */
	private final int LENGTH = 100;
	/**
	 * The sequence
	 */
	private int moves[] = new int[LENGTH];
	/**
	 * Current position in move sequence
	 */
	private int nextMove = 0;
	
	/**
	 * Generates the sequence
	 * @param arena the arena
	 */
	public DrSequence(SLArena arena) {
		super(arena);
		for (int i=0; i<LENGTH; i++)
		{
			int move = (int)(Math.random()*5);
			switch (move)
			{
			case 0:
				moves[i] = ROCK;
				break;
			case 1:
				moves[i] = SCISSORS;
				break;
			case 2:
				moves[i] = PAPER;
				break;
			case 3:
				moves[i] = SPOCK;
				break;
			case 4:
				moves[i] = LIZARD;
				break;

			}
		}
	}

	/**
	 * Shows the length of the current sequence
	 */
	public void draw(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0,0,arena.getPlayerScreenWidth(), arena.getPlayerScreenHeight());
		g.setColor(Color.black);
		g.drawString("Length: "+LENGTH,50,50);
		
	}

	/**
	 * Returns the next move in the sequence
	 */
	public int move() {
		if (++nextMove == LENGTH)
			nextMove = 0;
		return moves[nextMove];
	}

	/**
	 * Returns player name
	 */
	public String name() {
		return "Dr. Sequence";
	}

	/**
	 * Unused
	 */
	public void opponentMove(int move) {
		
	}



}
