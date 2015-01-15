package lizardSpock;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import players.*;

/**
 * <b>Introduction</b>
 * <br><br>
 * This class implements a multi-agent competitive game of Rock-Paper-Scissors-Lizard-Spock. 
 * Players contribute a single Java class that defines a player, and these players battle each 
 * other in a multiple round tournament under the control of this class. For instructions
 * on how to create a player, see the documentation in
 * the class named <i>SLPlayer</i>. For instructions on how to add your players to the arena,
 * see the documentation for the <i>init()</i> method in this class.<br><br>
 *
 * <b>The Game</b><br><br>
 * Spock-Lizard is an extension of Rock-Paper-Scissors, to add two new moves, Spock and Lizard.
 * Each move beats two others and is beaten by two others. The game was popularized on the
 * television show <i>The Big Bang Theory</i> in an episode titled <i>The Spock Lizard Expansion</i>,
 * but was developed long before that and the original inventors were not given credit! You can
 * see their site at http://www.samkass.com/theories/RPSSL.html<br><br>
 * 
 * The rules for determining winners are as follows:
 * <ul><li>SCISSORS cut PAPER</li>
 * <li>PAPER covers ROCK</li>
 * <li>ROCK crushes LIZARD</li>
 * <li>LIZARD poisons SPOCK</li>
 * <li>SPOCK smashes SCISSORS</li>
 * <li>SCISSORS decapitate LIZARD</li>
 * <li>LIZARD eats PAPER</li>
 * <li>PAPER disproves SPOCK</li>
 * <li>SPOCK vaporizes ROCK</li>
 * <li>ROCK crushes SCISSORS</li></ul>
 * 
 * As with Rock Paper Scissors, the mathematically optimal strategy is random play. In practice,
 * human beings are very bad at behaving randomly, and so a predictive strategy can often beat
 * a human player. In contrast, computers are very good at behaving (pseudo-)randomly, so creating
 * a random player is not very interesting - and is prohibited. Rather, this is an AI-type prediction
 * task. The job is to keep track of your opponents' past behaviour over hundreds or thousands of
 * games, and try to predict what they will do next.<br><br>
 * 
 * <b>The Game Engine</b><br><br>
 * The arena plays the selected number of games as fast as it can. In each round of play, the arena:
 *
 *<ol><li>Gets each player's move using <i>player.move()</i></li>
 *
 * 	  <li>Tells each player what the other player did using <i>player.opponentMove(int)</i></li>
 *
 * 	  <li>Processes the moves, and decides a winner for the round - if a player makes an illegal move, it is an automatic win for the other</li></ol>
 *
 * <b>Player CPU Usage</b> <br><br>
 *
 * Processor time is monitored using calls to <i>System.nanoTime()</i> any time
 * a player method is called. This is not perfect, but it does
 * give an approximate estimate of how much CPU each player is consuming. At the
 * moment, this information is displayed but not used in scoring. Players should be
 * able to complete 10000 games in a reasonable amount of time, so this statistic
 * helps determine who is slowing things down if the games are taking too long.<br><br>
 *
 * <b>Buggy Players</b><br><br>
 *
 * When a Player throws an exception or makes a bad move, this is recorded and displayed.
 * Again, this is for information purposes only at the moment. For the purposes of scoring,
 * any legal move beats any bad move. <br><br>
 *
 * <b>Debugging Features</b><br><br>
 *
 * The arena contains a number of features to aid debugging. If DEBUG is set to true, 
 * there will be extensive debugging output dumped to System.out and stack traces for
 * caught exceptions will also be displayed there. If SOUND_TEST is set to true, a little
 * button for testing sounds will appear beneath each player's canvas.<br><br>
 * 
 * The <i>players</i> package contains a few players to test against. <i>MsRandom</i> plays randomly
 * and so should always tie any other player. <i>MrPredictable</i> plays a 5-move sequence over and over
 * again, so should be easily beaten. <i>CopyCat</i> echoes your last move. This is harder to beat. Finally
 * <i>DrSequence</i> repeats a randomly determined sequence of moves, with the length of the sequence determined
 * by a field in the class.<br><br>
 * 
 * There is also a class called <i>SLChallengeArena</i>, which allows you to play single rounds
 * against your player to test how it behaves with various move sequences.<br><br>
 *
 * @version <br>1.0 (November 4, 2007) Initial release as Rock Paper Scissors
 * @version <br>2.0 (March, 2011) Released as Spock-Lizard
 * @version <br>2.1 (August 10, 2011) Converted to an application
 * 
 * @author Sam Scott
 */
public class SLArena extends JFrame implements Runnable {
	
	// DEBUGGING CONSTANTS
	/**
	 * Set to true for debugging output
	 */
	public final boolean DEBUG = true;
	/**
	 * Set to true to get sound test buttons
	 */
	public final boolean SOUND_TEST = true;
	
	// SYSTEM CONSTANTS - DO NOT CHANGE
	/**
	 * The color for text highlighting
	 */
	protected final Color highlightColor = new Color(150, 0, 0);
	/**
	 * Duration of pause between player canvas updates
	 */
	protected final int pauseDuration = 10;
	
	// SYSTEM VARIABLES - DO NOT CHANGE
	/**
	 * A player
	 */
	protected SLPlayer player1, player2;
	/**
	 * A player canvas (drawing surfaces)
	 */
	protected PlayerCanvas player1Canvas, player2Canvas;
	/**
	 * The text field for number of games
	 */
	private JTextField numTrialsWindow;
	/**
	 * Number of wins for given player
	 */
	protected int player1Wins = 0, player2Wins = 0;
	/**
	 * Total number of games played so far
	 */
	protected int totalNumGames = 0;
	/**
	 * Label to display number of wins for a player
	 */
	protected JLabel wins1, wins2;
	/**
	 * Label to display CPU usage and exceptions for each player
	 */
	protected JLabel stats1, stats2;
	/**
	 * For decimal formatting
	 */
	protected DecimalFormat df = new DecimalFormat("0.000");
	/**
	 * Label to display message for a player
	 */
	protected JLabel p1Message, p2Message;
	/**
	 * Arena audio
	 */
	protected ApplicationAudioClip cheering, fanfare2, fanfare3, click;
	/**
	 * Number of games to play between click sounds
	 */
	protected int clickDelay = 100;
	/**
	 * Stores CPU time used for a player.
	 */
	protected long cpu1 = 0, cpu2 = 0;
	/**
	 * Stores number of exceptions per player.
	 */
	protected int errors1 = 0, errors2 = 0;
	
	// METHODS YOU SHOULD CHANGE
	/**
	 * Creates the players, sets up the GUI, loads sounds and images, starts thread, etc.
	 * Change this constructor to load the players you want.
	 */
	public SLArena() {
		long time = System.nanoTime();
		try
		{
			player1 = new DrSequence(this); // PUT PLAYER PLAYER 1 HERE 
			cpu1 += System.nanoTime() - time;
		}
		catch (Exception e)
		{
			if (DEBUG) e.printStackTrace();
			errors1++;
		}
		time = System.nanoTime();
		try
		{
			player2 = new KeithW(this); // PUT PLAYER PLAYER 2 HERE 
			cpu2 += System.nanoTime() - time;
		}
		catch (Exception e)
		{
			if (DEBUG) e.printStackTrace();
			errors2++;	
		}

		// Sounds
		cheering = new ApplicationAudioClip("sounds/cheering.wav");
		fanfare2 = new ApplicationAudioClip("sounds/bigbang1.wav");
		fanfare3 = new ApplicationAudioClip("sounds/bigbang2.wav");
		click = new ApplicationAudioClip("sounds/click_x.wav");
		
		// GUI
		JPanel mainPanel = makeGUI();
		setContentPane(mainPanel);
		
		// Thread
		Thread t = new Thread(this);
		t.start();
		
		// Opening fanfare
		fanfare2.play();
	} // init method

	// METHODS YOU SHOULD NOT CHANGE
	/**
	 * Main method to create and display the arena 
	 * @param args unused
	 */
	public static void main(String[] args)
	{
		JFrame frame = new SLArena();
		frame.pack();
		frame.setTitle("Lizard Spock Expansion Arena");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	/**
	 * Thread to update player canvases - used to create animations
	 */
	public void run() {
		while (true) {
			repaint();
			try {
				Thread.sleep(pauseDuration);
			} catch (InterruptedException e) {
			}
		}
	}

	/**
	 * This is the action listener for the "Sound Test" button.
	 */
	protected class SoundTestListener implements ActionListener {
		SLPlayer player;

		/**
		 * Constructor.
		 * @param player The player attached to this listener.
		 */
		public SoundTestListener(SLPlayer player) {
			this.player = player;
			if (DEBUG)
				System.out.println(player);
		}

		/**
		 * Plays the sound when button pressed.
		 */
		public void actionPerformed(ActionEvent e) {
			if (player != null && player.victorySound() != null)
				player.victorySound().play();
		}
	}

	/**
	 * This is the action listener for the "Go" button.
	 */
	protected class GoButtonListener implements ActionListener, Runnable {
		private int numTrials;

		/**
		 * Starts a thread to run the selected number of games
		 *
		 * @param e
		 *            the action event
		 */
		public void actionPerformed(ActionEvent e) {
			if (DEBUG)
				System.out.println("Go button pressed");
			try {
				numTrials = Integer.parseInt(numTrialsWindow.getText());
				Thread t = new Thread(this);
				t.start();
			} catch (NumberFormatException nfe) {
				if (DEBUG)
					System.out.println("NumberFormatException");
			}

		}

		/**
		 * Checks if a move made by a player is legal
		 * @param move The move to check
		 * @return true if the move matches one of the defined system constants. False otherwise.
		 */
		protected boolean goodMove(int move)
		{
			return (move == SLPlayer.ROCK || move == SLPlayer.PAPER || move == SLPlayer.SCISSORS || move == SLPlayer.SPOCK || move == SLPlayer.LIZARD);
		}
		
		/**
		 * Runs the selected number of games, processes the results, updates the display
		 */
		public void run() {
			if (DEBUG)
				System.out.println("Running!");
			totalNumGames = 0;
			player1Wins = 0;
			player2Wins = 0;
			p1Message.setText("Get Ready");
			p2Message.setText("Get Ready");
			wins1.setText("Wins: 0 (0%)");
			wins2.setText("Wins: 0 (0%)");
			fanfare3.play();
			try {
				Thread.sleep(1700);
			} catch (InterruptedException ie) {

			}
			p1Message.setText("Go!");
			p2Message.setText("Go!");
			for (int i = numTrials; i > 0; i--) {
				// GET PLAYER MOVES
				int p1move = SLPlayer.ERROR, p2move = SLPlayer.ERROR;
				// PLAYER 1
				long time = System.nanoTime();
				try {
					p1move = player1.move();
					cpu1 += System.nanoTime() - time;
				}
				catch (Exception e)
				{
					cpu1 += System.nanoTime() - time;
					if (DEBUG) e.printStackTrace();
					errors1++;
					p1move = SLPlayer.ERROR;
				}
				// PLAYER 2
				time = System.nanoTime();
				try {
					p2move = player2.move();
					cpu2 += System.nanoTime() - time;
				}
				catch (Exception e)
				{
					cpu2 += System.nanoTime() - time;
					if (DEBUG) e.printStackTrace();
					errors2++;
					p2move = SLPlayer.ERROR;
				}
				// PASS OPPONENT MOVES TO EACH PLAYER
				// PLAYER 1
				time = System.nanoTime();
				try {
					player1.opponentMove(p2move);
					cpu1 += System.nanoTime() - time;
				}
				catch (Exception e)
				{
					cpu1 += System.nanoTime() - time;
					if (DEBUG) e.printStackTrace();
					errors1++;
				}
				// PLAYER 2
				time = System.nanoTime();
				try {
					player2.opponentMove(p1move);
					cpu2 += System.nanoTime() - time;
				}
				catch (Exception e)
				{
					cpu2 += System.nanoTime() - time;
					if (DEBUG) e.printStackTrace();
					errors2++;
				}
				if (!goodMove(p1move)) errors1++;
				if (!goodMove(p2move)) errors2++;
				if (DEBUG)
					System.out.println("Player 1... " + printMove(p1move)
							+ ", Player 2... " + printMove(p2move));
				if (	(p2move == SLPlayer.ERROR && p1move != SLPlayer.ERROR)
						|| (goodMove(p1move) && !goodMove(p2move))
						|| (p1move == SLPlayer.PAPER && p2move == SLPlayer.ROCK)
						|| (p1move == SLPlayer.PAPER && p2move == SLPlayer.SPOCK)
						|| (p1move == SLPlayer.SCISSORS && p2move == SLPlayer.PAPER)
						|| (p1move == SLPlayer.SCISSORS && p2move == SLPlayer.LIZARD)
						|| (p1move == SLPlayer.ROCK && p2move == SLPlayer.SCISSORS)
						|| (p1move == SLPlayer.ROCK && p2move == SLPlayer.LIZARD)
						|| (p1move == SLPlayer.SPOCK && p2move == SLPlayer.ROCK)
						|| (p1move == SLPlayer.SPOCK && p2move == SLPlayer.SCISSORS)
						|| (p1move == SLPlayer.LIZARD && p2move == SLPlayer.SPOCK)
						|| (p1move == SLPlayer.LIZARD && p2move == SLPlayer.PAPER))
				{
					player1Wins++;
					totalNumGames++;
					if (DEBUG)
						System.out.println("Player 1 wins");
				} else 	if (	(p1move == SLPlayer.ERROR && p2move != SLPlayer.ERROR)
						|| (p2move == SLPlayer.PAPER && p1move == SLPlayer.ROCK)
						|| (p2move == SLPlayer.PAPER && p1move == SLPlayer.SPOCK)
						|| (p2move == SLPlayer.SCISSORS && p1move == SLPlayer.PAPER)
						|| (p2move == SLPlayer.SCISSORS && p1move == SLPlayer.LIZARD)
						|| (p2move == SLPlayer.ROCK && p1move == SLPlayer.SCISSORS)
						|| (p2move == SLPlayer.ROCK && p1move == SLPlayer.LIZARD)
						|| (p2move == SLPlayer.SPOCK && p1move == SLPlayer.ROCK)
						|| (p2move == SLPlayer.SPOCK && p1move == SLPlayer.SCISSORS)
						|| (p2move == SLPlayer.LIZARD && p1move == SLPlayer.SPOCK)
						|| (p2move == SLPlayer.LIZARD && p1move == SLPlayer.PAPER))
				{
					player2Wins++;
					totalNumGames++;
					if (DEBUG)
						System.out.println("Player 2 wins");
				} else {
					totalNumGames++;
					if (DEBUG)
						System.out.println("it's a tie!");
				}
				wins1.setText("Wins: " + player1Wins + " ("
						+ (player1Wins * 100 / totalNumGames) + "%)");
				wins2.setText("Wins: " + player2Wins + " ("
						+ (player2Wins * 100 / totalNumGames) + "%)");
				if (i % clickDelay == 0)
					click.play();
				// UPDATE STATS LABELS
				stats1.setText("CPU: "+df.format(cpu1/1000000000.0)+", Errors: "+errors1);
				stats2.setText("CPU: "+df.format(cpu2/1000000000.0)+", Errors: "+errors2);

			}
			cheering.play();
			if (player1Wins > player2Wins) {
				if (DEBUG)
					System.out.println("player 1 victory sound "
							+ player1.victorySound());
				if (player1.victorySound() != null)
					player1.victorySound().play();
				p1Message.setText("Winner!!!");
				p2Message.setText("Loser :-(");
			}
			if (player1Wins < player2Wins) {
				if (DEBUG)
					System.out.println("player 2 victory sound "
							+ player2.victorySound());
				if (player2.victorySound() != null)
					player2.victorySound().play();
				p1Message.setText("Loser :-(");
				p2Message.setText("Winner!!!");
			}
		}
	}

	/**
	 * Returns width of player's screen.
	 */
	public int getPlayerScreenWidth() {
		return player1Canvas.getWidth();
	}

	/**
	 * Returns height of player's screen.
	 */
	public int getPlayerScreenHeight() {
		return player1Canvas.getHeight();
	}

	/**
	 * Debugging method to convert a move to a String.
	 * @param move the move to convert
	 * @return the String equivalent
	 */
	public String printMove(int move)
	{
		if (move == SLPlayer.ROCK)
			return "Rock";
		if (move == SLPlayer.SCISSORS)
			return "Scissors";
		if (move == SLPlayer.PAPER)
			return "Paper";
		if (move == SLPlayer.SPOCK)
			return "Spock";
		if (move == SLPlayer.LIZARD)
			return "Lizard";
		return "Bad move ("+move+")";
	}

	/**
	 * Creates the main GUI layout in a JPanel.
	 *
	 * @return A new JPanel with the layout for the JApplet's GUI.
	 */
	protected JPanel makeGUI() {
		Image backgroundImage = Toolkit.getDefaultToolkit ().getImage (getClass().getClassLoader().getResource("images/spockLizard2.jpg"));
		ImagePanel newPanel = new ImagePanel(backgroundImage, this);

		// HEADING: headingPanel components
		JPanel headingPanel = new JPanel();
		headingPanel.setOpaque(false);
		JLabel heading = new JLabel("The Lizard-Spock Expansion Arena");
		heading.setFont(new Font("SansSerif", Font.BOLD, 24));
		heading.setForeground(highlightColor);
		// headingPanel layout
		headingPanel.setLayout(new BoxLayout(headingPanel, BoxLayout.X_AXIS));
		headingPanel.add(Box.createHorizontalGlue());
		headingPanel.add(Box.createRigidArea(new Dimension(10, 5)));
		headingPanel.add(heading);
		headingPanel.add(Box.createRigidArea(new Dimension(10, 5)));
		headingPanel.add(Box.createHorizontalGlue());

		// BANNERS: bannerPanel components
		JPanel outerBannerPanel = new JPanel();
		outerBannerPanel.setOpaque(false);
		JPanel bannerPanel = new JPanel();
		bannerPanel.setOpaque(false);
		JPanel player1Panel = new JPanel();
		player1Panel.setOpaque(false);
		player1Panel.setLayout(new BoxLayout(player1Panel, BoxLayout.Y_AXIS));
		JLabel name1 = new JLabel(player1.name());
		name1.setMaximumSize(new Dimension(120, 18));
		name1.setMinimumSize(new Dimension(120, 18));
		name1.setPreferredSize(new Dimension(120, 18));
		name1.setFont(new Font("SansSerif", Font.BOLD, 16));
		name1.setForeground(highlightColor);
		player1Panel.add(name1);
		player1Canvas = new PlayerCanvas(player1);
		player1Canvas.setPreferredSize(new Dimension(150, 100));
		player1Canvas.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		player1Panel.add(player1Canvas);
		if (SOUND_TEST) {
			JButton st1Button = new JButton();
			st1Button.setPreferredSize(new Dimension(100, 10));
			st1Button.setMaximumSize(new Dimension(100, 10));
			st1Button.addActionListener(new SoundTestListener(player1));
			player1Panel.add(st1Button);
		}
		wins1 = new JLabel("Wins: 0 (0%)");
		wins1.setFont(new Font("SansSerif", Font.BOLD, 12));
		player1Panel.add(wins1);
		stats1 = new JLabel("CPU: 0.000, Errors: 0");
		stats1.setFont(new Font("SansSerif", Font.PLAIN, 12));
		player1Panel.add(stats1);
		p1Message = new JLabel(" ");
		p1Message.setFont(new Font("SansSerif", Font.BOLD, 24));
		p1Message.setForeground(highlightColor);
		player1Panel.add(p1Message);
		JPanel player2Panel = new JPanel();

		player2Panel.setOpaque(false);
		player2Panel.setLayout(new BoxLayout(player2Panel, BoxLayout.Y_AXIS));
		JLabel name2 = new JLabel(player2.name());
		name2.setMaximumSize(new Dimension(120, 18));
		name2.setMinimumSize(new Dimension(120, 18));
		name2.setPreferredSize(new Dimension(120, 18));
		name2.setFont(new Font("SansSerif", Font.BOLD, 16));
		name2.setForeground(highlightColor);
		player2Panel.add(name2);
		player2Canvas = new PlayerCanvas(player2);
		player2Canvas.setPreferredSize(new Dimension(150, 100));
		player2Canvas.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		player2Panel.add(player2Canvas);
		if (SOUND_TEST) {
			JButton st2Button = new JButton();
			st2Button.setPreferredSize(new Dimension(100, 10));
			st2Button.setMaximumSize(new Dimension(100, 10));
			st2Button.addActionListener(new SoundTestListener(player2));
			player2Panel.add(st2Button);
		}
		wins2 = new JLabel("Wins: 0 (0%)");
		wins2.setFont(new Font("SansSerif", Font.BOLD, 12));
		player2Panel.add(wins2);
		stats2 = new JLabel("CPU: 0.000, Errors: 0");
		stats2.setFont(new Font("SansSerif", Font.PLAIN, 12));
		player2Panel.add(stats2);
		p2Message = new JLabel(" ");
		p2Message.setFont(new Font("SansSerif", Font.BOLD, 24));
		p2Message.setForeground(highlightColor);
		player2Panel.add(p2Message);
		JLabel vsLabel = new JLabel("VS");
		vsLabel.setFont(new Font("SansSerif", Font.ITALIC, 36));
		// bannerPanel layout
		bannerPanel.setLayout(new BoxLayout(bannerPanel, BoxLayout.X_AXIS));
		bannerPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createLineBorder(Color.BLACK), "The Players"));
		bannerPanel.add(Box.createHorizontalGlue());
		bannerPanel.add(player1Panel);
		bannerPanel.add(Box.createRigidArea(new Dimension(60, 100)));
		bannerPanel.add(vsLabel);
		bannerPanel.add(Box.createRigidArea(new Dimension(60, 100)));
		bannerPanel.add(player2Panel);
		bannerPanel.add(Box.createHorizontalGlue());
		outerBannerPanel.setLayout(new BoxLayout(outerBannerPanel,
				BoxLayout.X_AXIS));
		outerBannerPanel.add(Box.createHorizontalGlue());
		outerBannerPanel.add(bannerPanel);
		outerBannerPanel.add(Box.createHorizontalGlue());

		// CONTROL: controlPanel Components
		JPanel controlPanel = new JPanel();
		controlPanel.setOpaque(false);
		JPanel innerControlPanel = new JPanel();
		innerControlPanel.setOpaque(false);
		JLabel numTrialsLabel = new JLabel("Number of Trials");
		numTrialsWindow = new JTextField("10000");
		numTrialsWindow.setMaximumSize(new Dimension(70, 20));
		numTrialsWindow.setMinimumSize(new Dimension(70, 20));
		numTrialsWindow.setPreferredSize(new Dimension(70, 20));
		numTrialsWindow.addActionListener(new GoButtonListener());
		JButton goButton = new JButton("Go!");
		goButton.addActionListener(new GoButtonListener());
		innerControlPanel.setLayout(new BoxLayout(innerControlPanel,
				BoxLayout.X_AXIS));
		innerControlPanel.add(Box.createHorizontalGlue());
		innerControlPanel.add(numTrialsLabel);
		innerControlPanel.add(numTrialsWindow);
		innerControlPanel.add(Box.createHorizontalGlue());
		innerControlPanel.add(goButton);
		innerControlPanel.add(Box.createHorizontalGlue());
		// controlPanel layout
		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));
		controlPanel.add(Box.createHorizontalGlue());
		innerControlPanel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.BLACK), "The Controls"));
		controlPanel.add(innerControlPanel);
		controlPanel.add(Box.createHorizontalGlue());

		// newPanel layout
		newPanel.setLayout(new BoxLayout(newPanel, BoxLayout.Y_AXIS));
		newPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		newPanel.add(headingPanel);
		newPanel.add(Box.createRigidArea(new Dimension(200, 10)));
		newPanel.add(outerBannerPanel);
		newPanel.add(Box.createRigidArea(new Dimension(200, 10)));
		newPanel.add(controlPanel);
		newPanel.add(Box.createRigidArea(new Dimension(200, 10)));

		return newPanel;
	}

}
