package lizardSpock;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import players.CopyCat;
import players.SLPlayer;

/**
 * Implements a rock-scissors-paper human challenge arena. Use this for debugging purposes.
 * Note that this is a sublcass of SLArena and only overrides the methods and fields it 
 * absolutely needs to - for example, debugging constants are still in SLChallengeArena
 *
 * @author Sam Scott
 * @version <br>1.0 (November 4, 2007) Initial release as Rock Paper Scissors
 * @version <br>2.0 (March, 2011) Released as Spock-Lizard
 * @version <br>2.1 (August 10, 2011) Converted to an application
 */
public class SLChallengeArena extends SLArena implements Runnable {

	// METHODS YOU SHOULD CHANGE
	/**
	 * Sets up the arena - change the player name to your class.
	 */
	public SLChallengeArena() {
		player1 = new CopyCat(this); // ROBOT PLAYER
		fanfare2 = new ApplicationAudioClip("sounds/fanfare2.wav");
		click = new ApplicationAudioClip("sounds/click_x.wav");
		JPanel mainPanel = makeGUI();
		setContentPane(mainPanel);
	} 

	// METHODS YOU SHOULD NOT CHANGE
	/**
	 * Main method to create and display the arena 
	 * @param args unused
	 */
	public static void main(String[] args)
	{
		JFrame frame = new SLChallengeArena();
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	/**
	 * Listener for the rock, scissors, and paper buttons
	 */
	private class HumanButtonListener implements ActionListener {
		int p2move;

		/**
		 * Constructor.
		 * @param move Which button this will be (rock, paper, or scissors)
		 */
		public HumanButtonListener(int move) {
			this.p2move = move;
		}

		/**
		 * Checks who won, plays sounds and shows messages
		 */
		public void actionPerformed(ActionEvent arg0) {
			int p1move = player1.move();
			player1.opponentMove(p2move);
			if (	  (p1move == SLPlayer.PAPER & p2move == SLPlayer.ROCK)
					| (p1move == SLPlayer.PAPER & p2move == SLPlayer.SPOCK)
					| (p1move == SLPlayer.SCISSORS & p2move == SLPlayer.PAPER)
					| (p1move == SLPlayer.SCISSORS & p2move == SLPlayer.LIZARD)
					| (p1move == SLPlayer.ROCK & p2move == SLPlayer.SCISSORS)
					| (p1move == SLPlayer.ROCK & p2move == SLPlayer.LIZARD)
					| (p1move == SLPlayer.SPOCK & p2move == SLPlayer.ROCK)
					| (p1move == SLPlayer.SPOCK & p2move == SLPlayer.SCISSORS)
					| (p1move == SLPlayer.LIZARD & p2move == SLPlayer.SPOCK)
					| (p1move == SLPlayer.LIZARD & p2move == SLPlayer.PAPER)) {
				player1Wins++;
				totalNumGames++;
				if (DEBUG)
					System.out.println("player 1 victory sound "
							+ player1.victorySound());
				//if (player1.victorySound() != null)
				//	player1.victorySound().play();
				p1Message.setText(printMove(p1move)+": Win");
				p2Message.setText(printMove(p2move)+": Lose");
				if (DEBUG)
					System.out.println("Player 1 wins");
			} else if (	  (p2move == SLPlayer.PAPER & p1move == SLPlayer.ROCK)
					| (p2move == SLPlayer.PAPER & p1move == SLPlayer.SPOCK)
					| (p2move == SLPlayer.SCISSORS & p1move == SLPlayer.PAPER)
					| (p2move == SLPlayer.SCISSORS & p1move == SLPlayer.LIZARD)
					| (p2move == SLPlayer.ROCK & p1move == SLPlayer.SCISSORS)
					| (p2move == SLPlayer.ROCK & p1move == SLPlayer.LIZARD)
					| (p2move == SLPlayer.SPOCK & p1move == SLPlayer.ROCK)
					| (p2move == SLPlayer.SPOCK & p1move == SLPlayer.SCISSORS)
					| (p2move == SLPlayer.LIZARD & p1move == SLPlayer.SPOCK)
					| (p2move == SLPlayer.LIZARD & p1move == SLPlayer.PAPER)) {
				player2Wins++;
				totalNumGames++;
				// player2.victorySound().play();
				p1Message.setText(printMove(p1move)+": Lose");
				p2Message.setText(printMove(p2move)+": Win");
				//humanWin.play();
				if (DEBUG)
					System.out.println("Player 2 wins");
			} else {
				totalNumGames++;
				p1Message.setText(printMove(p1move));
				p2Message.setText(printMove(p2move));
				if (DEBUG)
					System.out.println("it's a tie!");
			}
			wins1.setText("Wins: " + player1Wins + " ("
					+ (player1Wins * 100 / totalNumGames) + "%)");
			wins2.setText("Wins: " + player2Wins + " ("
					+ (player2Wins * 100 / totalNumGames) + "%)");
			click.play();
		}

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
		JLabel heading = new JLabel("The Rock-Scissors-Paper Challenge Arena");
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
		p1Message = new JLabel(" ");
		p1Message.setFont(new Font("SansSerif", Font.BOLD, 14));
		p1Message.setForeground(highlightColor);
		player1Panel.add(p1Message);

		JPanel player2Panel = new JPanel();
		player2Panel.setOpaque(false);
		player2Panel.setLayout(new BoxLayout(player2Panel, BoxLayout.Y_AXIS));
		JLabel name2 = new JLabel("Human");
		name2.setMaximumSize(new Dimension(120, 18));
		name2.setMinimumSize(new Dimension(120, 18));
		name2.setPreferredSize(new Dimension(120, 18));
		name2.setFont(new Font("SansSerif", Font.BOLD, 16));
		name2.setForeground(highlightColor);
		player2Panel.add(name2);
		JButton rockButton = new JButton("Rock");
		rockButton.addActionListener(new HumanButtonListener(SLPlayer.ROCK));
		rockButton.setPreferredSize(new Dimension(150, 33));
		rockButton.setMinimumSize(new Dimension(150, 33));
		rockButton.setMaximumSize(new Dimension(150, 33));
		JButton scissorsButton = new JButton("Scissors");
		scissorsButton.addActionListener(new HumanButtonListener(
				SLPlayer.SCISSORS));
		scissorsButton.setPreferredSize(new Dimension(150, 33));
		scissorsButton.setMinimumSize(new Dimension(150, 33));
		scissorsButton.setMaximumSize(new Dimension(150, 33));
		JButton paperButton = new JButton("Paper");
		paperButton.addActionListener(new HumanButtonListener(SLPlayer.PAPER));
		paperButton.setPreferredSize(new Dimension(150, 33));
		paperButton.setMinimumSize(new Dimension(150, 33));
		paperButton.setMaximumSize(new Dimension(150, 33));
		JButton spockButton = new JButton("Spock");
		spockButton.addActionListener(new HumanButtonListener(SLPlayer.SPOCK));
		spockButton.setPreferredSize(new Dimension(150, 33));
		spockButton.setMinimumSize(new Dimension(150, 33));
		spockButton.setMaximumSize(new Dimension(150, 33));
		JButton lizardButton = new JButton("Lizard");
		lizardButton.addActionListener(new HumanButtonListener(SLPlayer.LIZARD));
		lizardButton.setPreferredSize(new Dimension(150, 33));
		lizardButton.setMinimumSize(new Dimension(150, 33));
		lizardButton.setMaximumSize(new Dimension(150, 33));
		player2Panel.add(rockButton);
		player2Panel.add(paperButton);
		player2Panel.add(scissorsButton);
		player2Panel.add(spockButton);
		player2Panel.add(lizardButton);

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
		p2Message = new JLabel(" ");
		p2Message.setFont(new Font("SansSerif", Font.BOLD, 14));
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

		// newPanel layout
		newPanel.setLayout(new BoxLayout(newPanel, BoxLayout.Y_AXIS));
		newPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		newPanel.add(headingPanel);
		newPanel.add(Box.createRigidArea(new Dimension(200, 10)));
		newPanel.add(outerBannerPanel);
		newPanel.add(Box.createRigidArea(new Dimension(200, 10)));

		return newPanel;
	}

}
