package package1;

import java.util.Stack;
/**********************************************************************
 * The following class is responsible for all of the games operations.
 * The game keeps track of the current game status, checks for winners,
 * updates the board and keeps track of the players turn.
 * 
 * @author Paul Hood
 * @version 10/2/2013
 *********************************************************************/
public class ConnectFourGame {
	
	/** int array that stores the users selections */
	private int[][] board;

	/** int representation of the current player */
	private int player;

	/** Current status of the game*/
	private GameStatus status;

	/** Static int of the board size */
	private static int BDSIZE;

	/** Static int of connections to win the game */
	private static int connections;

	/** Stack that stores the board after each selection */
	private Stack<int[][]> saves;

	/** int of the total number of players */
	private int maxPlayers;
	
	/** int of the starting player */
	private int startPlayer;

	/******************************************************************
	 * Constructor that sets the board size, number of connections,
	 * total number of players and the starting player. This
	 * information is received from the ConnectFourPanel class.
	 *****************************************************************/
	public ConnectFourGame(int pBDSIZE, int pConnections, int players,
			int startPlayer) {
		connections = pConnections;
		BDSIZE = pBDSIZE;
		status = GameStatus.InProgress;
		board = new int[BDSIZE][BDSIZE];
		this.startPlayer = startPlayer;
		player = startPlayer;
		saves = new Stack<int[][]>();
		maxPlayers = players;
	}

	/******************************************************************
	 * Method that returns the current player.
	 * @return int that represents the current player.
	 *****************************************************************/
	public int getCurrentPlayer() {
		return player;
	}

	/******************************************************************
	 * This method returns the value of the desired location.
	 * @return Value of location on the board.
	 *****************************************************************/
	public int getValue(int row, int col) {
		return board[row][col];
	}

	/******************************************************************
	 * This method updates the board if the user selected a column with
	 * an opening. The players value is stored inside the board.
	 * @param col Column selected by user.
	 * @return The row if there is an opening. 
	 *****************************************************************/
	public int selectCol(int col) {
		// original is row = 9
		for (int row = BDSIZE - 1; row >= 0; row--) {
			if (board[row][col] == -1) {
				board[row][col] = player;
				return row;
			}
		}
		return -1;
	}

	/******************************************************************
	 * Method that increments the player after a selection. If the last
	 * player makes a selection it goes back to player 1.
	 *****************************************************************/
	public void nextPlayer() {
		if (player == maxPlayers) {
			player = 1;
		}
		else {
			player ++;
		}
	}
	
	/******************************************************************
	 * Method that decrements the current player if the undo button is 
	 * selected. This is responsible for keeping the order of the
	 * players turns.
	 *****************************************************************/
	public void undoPlayer() {
		if (player == 1) {
			player = maxPlayers;
		}
		else {
			player --;
		}
	}

	/******************************************************************
	 * Resets the board after a player has won a game. This method is 
	 * not called to create a new game.
	 *****************************************************************/
	public void reset() {
		
		// clear the saves stack
		saves.clear();
		player = startPlayer;
		
		// reset all values to -1
		for (int row = 0; row < BDSIZE; row++) {
			for (int col = 0; col < BDSIZE; col++) {
				board[row][col] = -1;
			}
		}
		
		// save starting board
		save();
	}
	
	/******************************************************************
	 * This method is responsible for checking for a winner. It
	 * vertically, horizontally and diagonally for wins.
	 * @param pPlayer Player to check for win.
	 * @return True if player win, false if not.
	 *****************************************************************/
	private boolean checkPlayerWin(int pPlayer) {

		// check for vertical win
		for (int row = 0; row < BDSIZE; row++) {
			for (int col = 0; col < BDSIZE - connections + 1; col++) {
				
				// stores the number of consecutive connections
				int win = 0;
				
				// i in this loop represents the number of connections
				// needed to make a win. win must be equal to the #
				// of connections in order to win.
				for (int i = 0; i < connections; i++) {
					if (board[row][col + i] == pPlayer) {
						win++;
						
						// player wins
						if (win == connections) {
							return true;
						}					
					}
					else {
						win = 0;
					}
				}
			}
		}

		// check for horizontal win
		for (int row = 0; row < BDSIZE - connections + 1; row++) {
			for (int col = 0; col < BDSIZE; col++) {
				int win = 0;
				for (int i = 0; i < connections; i++) {
					if (board[row + i][col] == pPlayer) {
						win++;
						if (win == connections) {
							return true;
						}
					}
					else {
						win = 0;
					}
				}
			}
		}

		// check for diagonal win
		for (int row = 0; row < BDSIZE - connections + 1; row++) {
			for (int col = 0; col < BDSIZE - connections + 1; col++) {
				int win = 0;
				
				// checks for connections to the right diagonally
				for (int i = 0; i < connections; i++) {
					if (board[row + i][col + i] == pPlayer) {
						win++;
						if (win == connections) {
							return true;
						}
					}
					else {
						win = 0;
					}
				}
			}
		}

		// check for diagonal win
		for (int row = 0; row < BDSIZE - connections + 1; row++) {
			for (int col = connections - 1; col < BDSIZE; col++) {
				int win = 0;
				for (int i = 0; i < connections; i++) {
					
					// checks for connections to the left diagonally
					if (board[row+i][col - i] == pPlayer) {
						win++;
						if (win == connections) {
							return true;
						}
					}
					else {
						win = 0;
					}
				}
			}
		}
		
		// no winner
		return false;
	}
	
	/******************************************************************
	 * This method is responsible for setting the starting player to
	 * the winner of the last game. If it is a cats game then it is not 
	 * called.
	 * @param winner Player that won the game.
	 *****************************************************************/
	public void updateStarter(int winner) {
		startPlayer = winner;
	}
	
	/******************************************************************
	 * Calls the checkPlayerWin() method to search for a winner based
	 * on the number of players.
	 * @return Winner if checkPlayerWin is true, false if not.
	 *****************************************************************/
	public int checkWinner() {
		for (int i = 1; i <= maxPlayers; i++) {
			if (checkPlayerWin(i)) {
				updateStarter(i);
				return i;
			}
		}
		
		// no winner
		return -1;
	}

	/******************************************************************
	 * This method returns the GameStatus if there is no winner.
	 * @return GameStatus if there is no winner
	 *****************************************************************/
	public GameStatus getGameStatus() {
		if (!(checkCats())) {

			return GameStatus.Cats;
		}
		else {
			return GameStatus.InProgress;
		}
	}

	/******************************************************************
	 * Method responsible for checking for a cats game.
	 * @return True if cats game, false if not.
	 *****************************************************************/
	private Boolean checkCats() {
		Boolean catStatus = false;
		for (int row = 0; row < BDSIZE; row++) {
			for (int col = 0; col < BDSIZE; col++) {
				if (board[row][col] == -1) {
					catStatus = true;
					break;
				}
			}
		}
		return catStatus;
	}

	/******************************************************************
	 * Used to push the last board onto the stack in order to use the
	 * undo method. 
	 *****************************************************************/
	public void save() {
		int[][] saveBoard = new int[BDSIZE][BDSIZE];
		
		// loops creates copy of the current board
		for (int row = 0; row < BDSIZE; row++) {
			for (int col = 0; col < BDSIZE; col++) {
				saveBoard[row][col] = board[row][col];
			}
		}
		
		// push the current board onto the stack
		saves.push(saveBoard);
	}

	/******************************************************************
	 * Undo method is responsible for changing the board back to it's
	 * previous selections. If the size of the board is one then there
	 * is no selections and it cannot be undone any further.
	 *****************************************************************/
	public void undo() {
		
		// move current board off the stack
		if (saves.size() > 1) {
			saves.pop();
			undoPlayer();
		}
		
		// previous board
		int[][] undoBoard = saves.peek();
		
		// update the board to previous selections
		for (int row = 0; row < BDSIZE; row++) {
			for (int col = 0; col < BDSIZE; col++) {
				board[row][col] = undoBoard[row][col];
			}
		}
	}
}
