package players;

import java.awt.Graphics;
import java.applet.Applet;

import lizardSpock.SLArena;

/**
 * My algorithm for Rock Paper Scissors Lizard Spock
 * @author Keith Wong
 * @version December 2, 2013
 */
public class KeithW extends SLPlayer {

	private int[] oppMoves; //size 10 000
	private int[] myMoves; //size 10 000

	private int[][] myPrevMoveAndOppMove; //size 5x5 || i axis is my prev move || j axis is opp move
	private int[][] oppPrevMoveAndOppMove; //size 5x5 || i axis is opp prev move || j axis is opp move

	private int[] oppMoveHz; // size 5 || 0 is Rock || 1 is Paper || 2 is Scissors || 3 is Spock || 4 is Lizard
	private int[] myMoveHz; //size 5 || 0 is Rock || 1 is Paper || 2 is Scissors || 3 is Spock || 4 is Lizard

	private int turnNumber;//ranges from 0 -> 9999
	private int strategy;// ranges from 1 -> 6
	private int lossesWithStrat; //keeps track of losses with the current strategy, reset to 0 when strategy is changed
	private int winsWithStrat; //keeps track of wins withe the current strategy, reset to 0 when strategy is changed
	private int lossesInARow; //reset all 2D arrays when > 10, also change strategies

	private int myMove;
	private int oppMove;

	/**
	 * Gives values to everything that needs them
	 * @param arena The arena
	 */
	public KeithW(SLArena arena) {
		super(arena);
		//initializing arrays
		oppMoves = new int[10000];
		myMoves = new int[10000];

		myPrevMoveAndOppMove = new int[5][5];
		oppPrevMoveAndOppMove = new int[5][5];

		oppMoveHz = new int[5];
		myMoveHz = new int[5];

		//Set all values in arrays that look for trends to 0;
		for (int i = 0; i < 5; i++){
			for (int j = 0; j < 5; j++){
				myPrevMoveAndOppMove[i][j] = 0;
				oppPrevMoveAndOppMove[i][j] = 0;
			}
		}
		for (int i = 0; i < 5; i++){
			oppMoveHz[i] = 0;
			myMoveHz[i] = 0;
		}

		turnNumber = 0;
		strategy = 1; 
		lossesWithStrat = 0;
		winsWithStrat = 0;
		lossesInARow = 0;
	}

	/**
	 * Throws a certain move based on many trends
	 */
	@Override
	public int move() {
		// TODO Auto-generated method stub
		//throws error if it's the first
		if (turnNumber == 0){
			myMove = -1;
		}
		if(turnNumber > 10){
			//System.out.println("Strategy Changed");
			//changes strategies if I've lose more than 10 times in a row or I've lost more than I've won
			if (lossesInARow >= 10 || winsWithStrat <= lossesWithStrat){
				lossesWithStrat = 0;
				winsWithStrat = 0;
				//resets the 2D array myPrevMoveAndOppMove and oppPrevMoveAndOppMove and updates the trends with only the last 10 moves
				for (int i = 0; i < 5; i++){
					for (int j = 0; j < 5; j++){
						myPrevMoveAndOppMove[i][j] = 0;
						oppPrevMoveAndOppMove[i][j] = 0;
					}
				}
				for (int i = 0; i < 10; i++){
					//checks to make sure that all the values are between 0 and 4
					if (myMoves[turnNumber - i - 1] >= 0 && myMoves[turnNumber - i - 1] <= 4 && oppMoves[turnNumber - i -1] >= 0 && oppMoves[turnNumber - i - 1] <= 4 && oppMoves[turnNumber - i] >= 0 && oppMoves[turnNumber - i] <= 4){
						myPrevMoveAndOppMove[myMoves[turnNumber - i - 1]][oppMoves[turnNumber - i]]++;
						oppPrevMoveAndOppMove[oppMoves[turnNumber - i - 1]][oppMoves[turnNumber - i]]++;
					}
				}
				determineBestStrat();
			}
			if(turnNumber != 0){
				if (strategy == 1){
					myMove = strategyOne();
				} else if (strategy == 2){
					myMove = strategyTwo();
				} else if (strategy == 3){
					myMove = strategyThree();
				} else if (strategy == 4){
					myMove = strategyFour();
				} else if (strategy == 5){
					myMove = strategyFive();
				} else if (strategy == 6){
					myMove = strategySix();
				}
			}
		}
		//tracks the moves that are played
		myMoves[turnNumber] = myMove;
		return myMove;
	}

	/**
	 * Takes the opponent's move and then updates the trends
	 * @param move the opponent's move
	 */
	@Override
	public void opponentMove(int move) {
		// TODO Auto-generated method stub
		oppMove = move; //stores the opponent's move into a class variable for future use
		if (oppMove < 0 || oppMove > 4){ //FOR ALSTON, SINCE YOU SOMEHOW FIGURED OUT HOW TO OUTPUT SOMETHING OTHER THAN -1 AS AN ERROR
			oppMove = -1;
		}
		//Keeps track of what the opponent threw each turn
		oppMoves[turnNumber] = oppMove;

		//gets the result (win/loss/tie) of this round
		int result = roundOutcome(myMove, oppMove);
		if (result == 0){
			winsWithStrat++;
			lossesInARow = 0;
		} 
		else if (result == 1){
			lossesWithStrat++;
			lossesInARow++;
		}
		//updates the trends
		if (myMove != -1 && oppMove != -1){
			updateTrends();
		}
		turnNumber++;
	}

	/**
	 * My name
	 */
	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "KeithW";
	}

	/**
	 * My graphic (THERE'S NOTHING HERE, IM TOO LAZY TO PUT ANYTHING IN)
	 */
	@Override
	public void draw(Graphics g) {
		// TODO Auto-generated method stub

	}

	/**
	 * Uses trends in the data to determine which strategy to use.
	 * 
	 */
	private void determineBestStrat(){

		int trendOffMe = 0; //Keeps track of how much the opponent is trending off my moves
		int trendOffThem = 0; //Keeps track of how much the opponent is trending off their moves
		int[] oppAfterMyThrow = new int[5]; //Keeps track of what the opponent tends to throw after my throw
		int[] oppAfterTheirThrow = new int[5];//Keeps track of what the opponent tends to throw after their throw
		
		//checks to see how often the enemy throws a certain move after I throw a certain one
		for (int i = 0; i < 5; i++) {
			if(myMoveHz[i] > 0) {
				int oppAfterThrowI = oppMostLikelyThrow(i, true);
				trendOffMe += myPrevMoveAndOppMove[i][oppAfterThrowI];
				oppAfterMyThrow[i] = oppAfterThrowI;
			}
		}

		//checks to see how often the enemy throws a certain more after he throws a certain one
		for (int i = 0; i < 5; i++){
			if(oppMoveHz[i] > 0){
				int oppAfterThrowI = oppMostLikelyThrow(i, false);
				trendOffThem += myPrevMoveAndOppMove[i][oppAfterThrowI];
				oppAfterTheirThrow[i] = oppAfterThrowI;
			}
		}
		int trend; //keeps track of whether the opponent is losing/winning/ tying against the move
		int numWins = 0; //keeps track of the number of wins of a throw vs the opp's most likely throw after that
		int numLosses = 0;//keeps track of the number of losses of a throw vs the opp's most likely throw after that
		int numTies = 0;//keeps track of the number of tie of a throw vs the opp's most likely throw after that
		
		//sees if they tend to throw more based on my moves or their moves, and counters accordingly
		//opponent is throwing more based on my own moves
		if (trendOffMe > trendOffThem){
			for (int i = 0; i < 5; i++){
				trend = roundOutcome(oppAfterMyThrow[i], i);
				if (trend == 0){
					numWins++;
				} else if (trend == 1){
					numLosses++;
				} else if (trend == 2){
					numTies++;
				}
			}
			if (numWins >= numLosses && numWins >= numTies){
				//opp strat is strategy 1, which loses to strategy 2
				strategy = 2;
			} else if (numLosses >= numWins && numLosses >= numTies){
				//opp strat is strategy 5, which loses to strategy 6
				strategy = 6;
			} else if (numTies >= numWins && numTies >= numLosses)
				//opp strat is strategy 3, which loses to strategy 4
				strategy = 4;
		}
		//opponent is throwing more based on his own moves
		else if (trendOffThem > trendOffMe) {
			for (int i = 0; i < 5; i++){
				trend = roundOutcome(oppAfterTheirThrow[i], i);
				if (trend == 0){
					numWins++;
				} else if (trend == 1){
					numLosses++;
				} else if (trend == 2){
					numTies++;
				}
			}
			if (numWins >= numLosses && numWins >= numTies){
				//opp strat is strategy 4, which loses to strategy 5
				strategy = 5;
			} else if (numLosses >= numWins && numLosses >= numTies){
				//opp strat is strategy 2, which loses to strategy 3
				strategy = 3;
			} else if (numTies >= numWins && numTies >= numLosses)
				//opp strat is strategy 6, which loses to strategy 1
				strategy = 1;
		}
	}

	/**
	 * Determines the opponent's most likely throw based off of trends.
	 * @param theThrow the throw you wish to base trends off of
	 * @param me Whether or not we are checking based off my previous or their previous throws.
	 * @return int the opponent's most likely throw.
	 */
	private int oppMostLikelyThrow(int theThrow, boolean me){
		int mostLikely = 0; //the current most likely move
		int frequency = -1; //how often the most likely move is thrown
		//If we're finding the opponent's most likely throw after my throw
		if (me == true){
			//Loops through 5 times, checks down the row for the largest value, the largest value is the most common throw after theThrow
			for (int i = 0; i < 5; i++){
				if (myPrevMoveAndOppMove[theThrow][i] > frequency){
					mostLikely = i;
					frequency = myPrevMoveAndOppMove[theThrow][i];
				}
			}
		}
		//If we're finding the opponent's most likely throw after their throw
		if (me == false){
			//Loops through 5 times, checks down the row for the largest value, the largest value is the most common throw after theThrow
			for (int i = 0; i < 5; i++){
				if (oppPrevMoveAndOppMove[theThrow][i] > frequency){
					mostLikely = i;
					frequency = oppPrevMoveAndOppMove[theThrow][i];
				}
			}
		}
		return mostLikely;
	}

	/**
	 * Returns the outcome of the previous game. if winner = 1 or 3, opp wins || if winner = 2 or 4, I win || if winner = 0, tie
	 * @param move1 the first move
	 * @param move2 the second move
	 * @return int  BASED OFF OF FIRST MOVE. 0 is a win, 1 is a loss, 2 is a tie
	 */
	private int roundOutcome(int move1, int move2){
		int outcome = 3; //so eclipse doesn't yell at me for possibly returning null
		int mathPart; //holds the output of the magic equation that determines the winner
		if(move1 != -1 && move2 != -1){
			mathPart = (5 + move1 - move2)%5; //1 or 3 = win || 2 or 4 = lose || 0 = tie
			if(mathPart == 1 || mathPart == 3){
				outcome = 0;//move1 wins
			} else if (mathPart == 2 || mathPart == 4){
				outcome = 1;//move2 wins
			} else if (mathPart == 0) {
				outcome = 2;//tie
			}
		} else if (move1 == -1 && move2 == -1){
			outcome = 2; //both threw error, it's a tie
		} else if (move1 != -1){
			outcome = 3; //opponent threw error, I win, but don't count it as a win due to the strategy
		} else if (move2 != -1){
			outcome = 2;//I threw error, I lose
		}
		return outcome;
	}

	/**
	 * Updates the arrays that keep track of trends.
	 */
	private void updateTrends(){
		//only updates if myMove and oppMove are between 0 and 4, in order to avoid arrayIndexOutOfBounds Exceptions
		if (oppMove != -1 && myMove != -1 && myMoves[turnNumber - 1] != -1 && oppMoves[turnNumber - 1] != -1){
			myPrevMoveAndOppMove[myMoves[turnNumber - 1]][oppMove]++;
			oppPrevMoveAndOppMove[oppMoves[turnNumber - 1]][oppMove]++;
			myMoveHz[myMove]++;
			oppMoveHz[oppMove]++;
		}
	}


	/**
	 * My first strategy, beats the opponent's previous throw. Counters opp throwing same as opp previous throw.
	 * Loses to opp throwing what loses to his own throw
	 * @return int my throw
	 */
	private int strategyOne() {
		return((oppMove + 1)%5);
	}

	/**
	 * My second strategy, throws what loses to my prev throw. Counters opp trying to beat my previous throw.
	 * Loses to opp throwing my previous throw.
	 * @return int my throw
	 */
	private int strategyTwo() {
		return ((myMove + 2)%5);
	}

	/**
	 * My third strategy, throws what the opponent previously threw. Counters opp throwing what loses to his prev throw.
	 * Loses to opp throwing what beats his own throw.
	 * @return int my throw
	 */
	private int strategyThree(){
		return oppMove;
	}

	/**
	 * My fourth strategy, throws what beats my prev throw. Counters opp throwing my prev throw.
	 * Loses to opp throwing what loses to my prev throw.
	 * @return int my throw
	 */
	private int strategyFour(){
		return (myMove + 3)%5;
	}

	/**
	 * My fifth strategy, throws what lose to opp prev throw. Counters opp throwing what beats his prev throw.
	 * Loses to opp throwing the same throw as his previous throw.
	 * @return int my throw
	 */
	private int strategyFive(){
		return((oppMove + 4)%5);
	}

	/**
	 * My sixth strategy, throws what I threw last turn. Counters opp throwing what loses to my previous throw.
	 * Loses to opp throwing what beats my previous throw.
	 * @return int my throw
	 */
	private int strategySix(){
		return myMove;
	}
}