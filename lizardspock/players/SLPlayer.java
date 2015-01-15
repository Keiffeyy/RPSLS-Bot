package players;
import java.applet.AudioClip;
import java.awt.Graphics;

import lizardSpock.ApplicationAudioClip;
import lizardSpock.SLArena;


/**
 * <b>Introduction</b> <br><br>
 *
 * This is an abstract class for a Spock-Lizard player. All players
 * <i>must</i> extend this class, and <i>must</i> be part of the <i>players</i>
 * package.<br><Br>
 *
 * The players decide their move for each game (one of Rock, Paper, Scissors, Spock, 
 * or Lizard. The arena is the referee and decides who won, then communicates the
 * result to each player. <br><br>
 *
 * <b>Rules</b><ol>
 *
 * <li>You must use the constants defined in this class to communicate your moves
 * to the arena. The values of these constants might change on game day.</li>
 * 
 * <li>You may use other "helper" classes, but spawning threads or timers for any
 * purpose other than animating your logo is not allowed.</li>
 *
 * <li>You must specify a name and a visual for your bot, using the appropriate
 * methods below. Any images used must be stored in the <i>images</i> folder.</li>
 * 
 * <li>You must not use <i>any</i> method of pseudo-random number generation. The aim of
 * this task is to predict and beat your opponent'smove, not to try to be 
 * unpredictable yourself.</li>
 *
 * <b>Rubric</b><br><br>
 *
 * To get an A+, your player should meet all of the following criteria:
 * <ol>
 * <li>Your code must meet all the standards and rules set out in this documentation.</li>
 * <li>Your code must be fully documented to javadoc standards.</li>
 * <li>Your strategy must be completely described in the class header. It should be well thought out
 * and ambitious.</li>
 * <li>Your code must be commented in a way that makes it clear where and how the different
 * parts of your strategy are implemented.</li>
 * <li>Your strategy must make use of array processing techniques to predict your opponent's move.</li>
 * <li>Your code must be as efficient as possible. There should be no superfluous or unnecessary
 * comparison or assignment operations.</li>
 * </ol>
 *
 * <b>More Information</b><br><br>
 *
 * For more general info see the SLArena class. For more info about this class,
 * see the comments on each of its methods.
 *
 * @author Sam Scott *
 * @version <br>1.0 (November 4, 2007) Initial release as Rock Paper Scissors
 * @version <br>2.0 (March, 2011) Released as Spock-Lizard
 */
public abstract class SLPlayer {

	/**
	 * Constant for indicating a ROCK move
	 */
	public static final int ROCK = 0;

	/**
	 * Constant for indicating a PAPER move
	 */
	public static final int PAPER = 1;

	/**
	 * Constant for indicating a SCISSORS move
	 */
	public static final int SCISSORS = 2;

	/**
	 * Constant for indicating a WIN
	 */
	public static final int SPOCK = 3;
	/**
	 * Constant for indicating a LOSS
	 */
	public static final int LIZARD = 4;
	/**
	 * Constant for indicating a bad move
	 */
	public static final int ERROR = -1;
	/**
	 * The arena - use this as an image observer if necessary.
	 */
	public SLArena arena;
	/**
	 * The player's personal victory sound
	 */
	public ApplicationAudioClip victorySound;

	/**
	 * Constructor for setting arena
	 *
	 * @param arena The arena
	 */
	public SLPlayer(SLArena arena) {
		this.arena = arena;
	}

	/**
	 * Called by the arena when it's time to play the player's victory sound.
	 *
	 * @return the victory sound, or null if there is no sound
	 */
	public ApplicationAudioClip victorySound() {
		return victorySound;
	}

	// ABSTRACT METHODS - You must override these in your subclass

	/**
	 * When called, this should return the next move of the current player.
	 * This is called at the beginning of a round.
	 *
	 * @return the move (ROCK, SCISSORS, PAPER, SPOCK, or LIZARD)
	 */
	public abstract int move();

	/**
	 * Called by the arena to communicate the opponents move to the player.
	 * This is called at the end of a round.
	 *
	 * @param move ROCK, SCISSORS, PAPER, SPOCK, or LIZARD
	 */
	public abstract void opponentMove(int move);

	/**
	 * Called by the arena to get the name of the player (15 characters max)
	 *
	 * @return the name of the player
	 */
	public abstract String name();

	/**
	 * Called by the arena once every 10 ms to draw the logo, name, or symbol of
	 * the player.
	 *
	 * @param g the graphics context
	 */
	public abstract void draw(Graphics g);
}
