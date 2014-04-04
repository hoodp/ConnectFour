package package1;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**********************************************************************
 * The following class is responsible for displaying the ConnectFour 
 * game. It allows the user to select the board size, choose the number
 * of players, select the number of connections to win and a color of
 * their choice. 
 * 
 * @author Paul Hood
 * @version 10/2/2013
 *********************************************************************/
public class ConnectFourPanel extends JPanel {
	/** JPanel array that displays row and column */
	private JLabel[][] board;

	/** JButton array that allows user to select column */
	private JButton[] selection;

	/** Button responsible for exiting the program */ 
	private JButton exit;

	/** Undoes the last move */ 
	private JButton undo;

	/** JButton for restarting the game completely */ 
	private JButton restart;

	/** ConnectFourGame object that keeps track for game info */
	private ConnectFourGame game;

	/** Static int holds the size of the board */
	private static int BDSIZE;

	/** Static int stores the number of connections to win */
	private static int connections;

	/** JMenuItem that completely restarts the game */
	private JMenuItem newGameItem;

	/** JMenuItem responsible for quitting the game */
	private JMenuItem quitItem;

	/** Top JPanel that displays the users turn and score */
	private JPanel top;

	/** Bottom JPanel that displays the game */
	private JPanel bottom;
	
	/** JPanel for Bottom row of Game */
	private JPanel bottomRow;

	/** int array for storing players and player scores */
	private int[] players;

	/** int of the starting player  */
	private int startPlayer;

	/** JLabel array that displays the scores of the player */
	private JLabel[] scoreboard;

	/** ArrayList that stores the players color */
	private ArrayList<Color> cList;
	
	/** ArrayList that stores the players name */
	private ArrayList<String> names;
	
	/** ButtonListener for button click events */ 
	private ButtonListener listener;

	/******************************************************************
	 * Constructor that sets the board size, connections, number of
	 * players, player colors and starting player by asking the user.
	 * It then creates a new ConnectFourGame based on the user-input 
	 * and displays the game. 
	 *****************************************************************/
	public ConnectFourPanel(JMenuItem quitItem,JMenuItem newGameItem) {
		this.quitItem = quitItem;
		this.newGameItem = newGameItem;
		top = new JPanel();
		bottom = new JPanel();	
		bottom.setBackground(Color.BLACK);
		listener = new ButtonListener();
		quitItem.addActionListener(listener);
		newGameItem.addActionListener(listener);	
		
		// array that stores total players
		players = new int[totalPlayers()];
		
		// asks the user for starting player
		startPlayer = setStarter();
		
		// create display that shows player scores
		scoreboard = new JLabel[players.length];
		
		// set the first player to 0 - Not shown
		players[0] = 0;
		
		// stores players colors in order 
		cList = new ArrayList<Color>(fillColors(players.length));
		
		// stores player name
		names = new ArrayList<String>(addNames(players.length));
		
		// player 0 score that is not displayed
		scoreboard[0] = new JLabel();
		
		// for loop places the number of players and their score in the
		// top JPanel.
		for (int i = 1; i < players.length; i++) {
			scoreboard[i] = new JLabel(scoreDisplay(i));
			top.add(scoreboard[i]);
			
			// Highlights the starting players turn
			if (i == startPlayer) {
				scoreboard[i].setForeground(labelDisplay(i));
			}
		}
		
		// sets the size of the board
		BDSIZE = setBoardSize();
		
		// sets # of connections
		connections = setConnections();	
		game = new ConnectFourGame(BDSIZE, connections,
				players.length - 1, startPlayer);

		bottom.setLayout(new GridLayout( BDSIZE + 1, BDSIZE));
		selection = new JButton[BDSIZE];
		board = new JLabel[BDSIZE][BDSIZE]; 

		// loop that adds selection and listener buttons to board
		for (int b = 0; b < BDSIZE; b++) {
			selection[b] = new JButton("Select");
			selection[b].addActionListener(listener);
			bottom.add(selection[b]);
			selection[b].setBorder(BorderFactory.createLineBorder(
					Color.BLACK, 1));
		}

		// loop that displays board
		for (int row = 0; row < BDSIZE; row++) {
			for (int col = 0; col < BDSIZE; col++) {
				board[row][col] = new JLabel();
				bottom.add(board[row][col]);
				board[row][col].setOpaque(true);
				board[row][col].setBorder(
						BorderFactory.createLineBorder(Color.BLACK,1));
			}
		}

		bottomRow = new JPanel();
		exit = new JButton("Exit");
		restart = new JButton("Restart");
		undo = new JButton("Undo");
		exit.addActionListener(listener);
		restart.addActionListener(listener);
		undo.addActionListener(listener);
		bottomRow.add(restart);
		bottomRow.add(undo);
		bottomRow.add(exit);

		// creates layout of game
		setLayout(new BorderLayout());
		add(BorderLayout.NORTH, top);
		add(BorderLayout.CENTER, bottom);
		add(BorderLayout.SOUTH, bottomRow);
		game.reset();
	}

	/******************************************************************
	 * This method asks the user for the total number of players. It 
	 * creates a dropdown list of possible players and also checks for
	 * errors. If cancel is clicked the program exits.
	 * @return The total number of players.
	 *****************************************************************/
	public int totalPlayers() {
		try {
			
			// total possible players
			String[] getPlayers = new String[9];
			for (int i = 0; i < 9; i++) {
				getPlayers[i] = (i + 2) + "";
			}
			
			// asks the user for how many players
			String message = (String) JOptionPane.showInputDialog(
					null,
					"Select the number of players",
					"Connect Four",
					JOptionPane.QUESTION_MESSAGE,
					null,getPlayers,
					getPlayers[0] );
			
			// if cancel is selected
			if (message == null) {
				System.exit(0);
			}
			return Integer.parseInt(message) + 1;
		}
		
		// catches IndexOutOfBounds if 4 is selected
		catch (Exception e) {
			return 3;
		}
	}

	/******************************************************************
	 * This method asks the user for the starting player. If no player
	 * is added the default is player 1. If cancel is selected the game
	 * exits.
	 * @return Integer of starting player.
	 *****************************************************************/
	public int setStarter() {
		try {
			int starter = 0;
			String sPlayer = JOptionPane.showInputDialog(
					null,
					"Enter starting player : 1 - " +
					(players.length - 1), "1");
			if (sPlayer == null) {
				System.exit(0);
			}
			starter = Integer.parseInt(sPlayer);
			
			// if value is less than 1 or greater than 
			// the number of players start at player 1.
			if (starter < 1 || starter > players.length - 1) {
				starter = 1;
			}
			return starter;
		}
		catch (Exception e) {
			return 1;
		}
	}

	/******************************************************************
	 * Method that asks the user for the board size. User can select
	 * from a dropdown list of choices. The minimum size is 4x4 and the
	 *  max is 19x19. The default is 10x10. 
	 * @return Integer of board size.
	 *****************************************************************/
	public int setBoardSize() {
		try {
			
			// possible board sizes
			String[] boardSizes = new String[16];
			for (int i = 0; i < boardSizes.length; i++) {
				int size = i + 4;
				boardSizes[i] = size + " x " + size;
			}
			String message =  (String) JOptionPane.showInputDialog(
					null,
					"Select board size",
					"Connect Four",
					JOptionPane.QUESTION_MESSAGE,
					null,
					boardSizes,boardSizes[6]);
			
			// if cancel is pressed
			if (message == null) {
				System.exit(0);
			}
			return Integer.parseInt(message.substring(0,
					message.indexOf(" ")));
		}
		catch (Exception e) {
			return 10;
		}
	}

	/******************************************************************
	 * This method asks the user for the total number of connections to
	 * win the game. The possible values are between 3 and 18 depending
	 * on the size of the board. If it is a 4x4 board, 3 is returned.
	 * @return Total number of connections to win the game.
	 *****************************************************************/
	public int setConnections() {
		try {
			
			// possible connections based on board size
			String[] totalConnectWin = new String[BDSIZE - 3];
			for (int i = 0; i < BDSIZE - 3; i++) {
				totalConnectWin[i] = (i + 3) + "";
			}
			String message =  (String) JOptionPane.showInputDialog(
					null,
					"Select total connections for win",
					"Connect Four",
					JOptionPane.QUESTION_MESSAGE,
					null,
					totalConnectWin,
					totalConnectWin[1]);
			if (message == null) {
				System.exit(0);
			}
			return Integer.parseInt(message);
		}
		
		// Automatically selected 3 if 4x4 board
		catch (IndexOutOfBoundsException e) {
			JOptionPane.showMessageDialog(null, "Get three connections"
					+ " to win the game.");
			return 3;
		}
	}
	
	/******************************************************************
	 * This method asks the user to select a color and stores the color
	 * in an ArrayList. Allows the user to exit if cancel is clicked.
	 * @return ArrayList of the players color choices.
	 *****************************************************************/
	private ArrayList<Color> fillColors(int maxPlayer) {
		ArrayList<Color> colors = new ArrayList<Color>();
		colors.add(Color.RED);
		for (int i = 1; i < maxPlayer; i++) {
			Color color = JColorChooser.showDialog(
					this,
					"Pick a color for Player " + i,
					null);
			if (color == null) {
				System.exit(0);
			}
			colors.add(color);
		}
		return colors;
	}
	
	/******************************************************************
	 * Method that returns an array list of player names.
	 * @param maxPlayer Total number of players.
	 * @return ArrayList of player names.
	 *****************************************************************/
	private ArrayList<String> addNames(int maxPlayer) {
		ArrayList<String> fillNames = new ArrayList<String>();
		fillNames.add("0");
		for (int i = 1; i < maxPlayer; i++) {
			try {
				String input = JOptionPane.showInputDialog(
						null,
						"Enter a name for Player " + i + " :",
						"Player " + i);
				
				// if cancel is pressed
				if (input == null) {
					System.exit(0);
					break;
				}
				input = input.trim();
				
				// entry is greater than 15 characters
				if (input.length() > 15 || input.length() == 0) {
					fillNames.add("Player " + i);
				}
				else {
					fillNames.add(input);
				}
				
				
			}
			catch (Exception e) {
				fillNames.add("Player " + i);
			}
			
		}
		return fillNames;
	}

	/******************************************************************
	 * Method that updates the scoreboard by increasing the players 
	 * value by their position in the array. It then updates the 
	 * scoreboard by calling the scoreDisplay method.
	 * @param winner Winner of the game.
	 *****************************************************************/
	public void updateScore(int winner) {
		players[winner]++;
		scoreboard[winner].setText(scoreDisplay(winner));
	}

	/******************************************************************
	 * This method returns a string of the players score. It is called
	 * after a player wins and in the constructor.
	 * @param winner Winner of the game.
	 * @return String that shows the players score.
	 *****************************************************************/
	public String scoreDisplay(int winner) {
		return names.get(winner) + " : " + players[winner] + "  ";
	}

	/******************************************************************
	 * Method that returns the color that the player choose in the 
	 * beginning of the game. 
	 * @param player Integer of player, matches position in cList.
	 * @return Color of the player.
	 *****************************************************************/
	private Color labelDisplay(int player) {
		return cList.get(player);
	}

	/******************************************************************
	 * Method that resets the game after a win. This method is not
	 * called when the game is restarted. This method sets the board
	 * back to it's original state.
	 *****************************************************************/
	private void reset() {
		for (int row = 0; row < BDSIZE; row++) {
			for (int col = 0; col < BDSIZE; col++) {	
				
				// sets board back to original color
				board[row][col].setBackground(new Color(238,238,238));
			}
		}
		
	}
	
	/******************************************************************
	 * This method is used when the restart or New Game buttons are 
	 * pressed. This method creates a new ConnectFourPanel and sets the
	 * new panels values to the current values based on the users
	 * input.
	 *****************************************************************/
	public void updatePanel() {
		ConnectFourPanel newPanel = new ConnectFourPanel(quitItem,
				newGameItem);
		this.scoreboard = newPanel.scoreboard;
		this.board = newPanel.board;
		this.cList = newPanel.cList;
		this.players = newPanel.players;
		this.game = newPanel.game;
		this.startPlayer = newPanel.startPlayer;
		this.selection = newPanel.selection;
		this.names = newPanel.names;
	}

	/******************************************************************
	 * The following class is a button listener that responds to events
	 * from all the buttons and JMenuItems in the program.
	 * 
	 * @author Paul Hood
	 * @version 10/2/2013
	 *****************************************************************/
	private class ButtonListener implements ActionListener {
		
		/**************************************************************
		 * This method is called after the restart or New Game options 
		 * are selected. The method clears the screen then asks the 
		 * user for new input. It then repaints the screen.
		 *************************************************************/
		private void set() {
			top.removeAll();
			bottom.removeAll();
			top.revalidate();
			bottom.revalidate();
			top.repaint();
			bottom.repaint();
			updatePanel();
			bottom.setLayout(new GridLayout(BDSIZE + 1,BDSIZE));
			
			// update the number of players
			for (int i = 0; i < players.length; i++) {
				top.add(scoreboard[i]);
			}
			
			// create new select buttons
			for (int i = 0; i < BDSIZE; i++) {
				bottom.add(selection[i]);
				selection[i].setBorder(
						BorderFactory.createLineBorder(Color.BLACK,1));
			}
			
			// create a new board
			for (int row = 0; row < BDSIZE; row++) {
				for (int col = 0; col < BDSIZE; col++) {
					bottom.add(board[row][col]);			
					board[row][col].setBorder(BorderFactory.
							createLineBorder(Color.BLACK,1));
				}
			}
			top.revalidate();
			bottom.revalidate();
			top.repaint();
			bottom.repaint();
		}

		/**************************************************************
		 * The following method responds to specific options that are 
		 * selected.
		 *************************************************************/
		public void actionPerformed(ActionEvent event) {
			JComponent comp = (JComponent) event.getSource();
			
			// if New Game or restart are selected
			if (comp == restart || comp == newGameItem) {
				set();
			}
			
			// loop that displays the players choice
			for (int col = 0; col < BDSIZE; col++) {
				if (comp == selection[col]) {
					int row = game.selectCol(col);
					if (row != -1) {
						game.save();
						board[row][col].setBackground(labelDisplay
								(game.getCurrentPlayer()));
						game.nextPlayer();
					}
					else {
						JOptionPane.showMessageDialog(null,
								"Column is full!");
					}

				}
			}
			
			// Changes last selection to white, also keeps track of 
			// last player. 
			if (comp == undo) {
				
				// updates ConnectFourGame to previous board
				game.undo();
				for (int row = 0; row < BDSIZE; row++) {
					for (int col = 0; col < BDSIZE; col++) {
						int value = game.getValue(row, col);
						
						// sets board back to original color
						if (value == -1) {
							board[row][col].setBackground(
									new Color(238,238,238));
						}
					}
				}
			}
			
			// if statement checks for a winner
			if (game.checkWinner() != -1) {
				int winner = game.checkWinner();
				JOptionPane.showMessageDialog(
						null,
						names.get(winner) + " Wins!");
				updateScore(game.checkWinner());
				
				// reset the ConnectFourGame
				game.reset();
				
				// display original board
				reset();
			}
			if (game.getGameStatus() == GameStatus.Cats) {
				JOptionPane.showMessageDialog(null, "Cats Game!");
				game.reset();
				reset();
			}
			
			// if exit or quit buttons are selected
			if (comp == quitItem || comp == exit) {
				
				// confirms that user wants to exit
				int confirm = JOptionPane.showConfirmDialog(
						null,
						"Are you sure? All records will be lost!",
						"Connect Four",
						JOptionPane.YES_NO_OPTION);
				
				if (confirm == 0) {
					System.exit(0);
				}
			}
			
			// highlights the current player if it is their turn
			if (game.getGameStatus() == GameStatus.InProgress) {
				int curPlayer = game.getCurrentPlayer();
				for (int i = 0; i < players.length; i++) {
					
					// players turn
					if (i == curPlayer) {
						scoreboard[i].setForeground(labelDisplay(i));
					}
					
					// not players turn
					else {
						scoreboard[i].setForeground(Color. BLACK);
					}
				}
			}
		}
	}
}
