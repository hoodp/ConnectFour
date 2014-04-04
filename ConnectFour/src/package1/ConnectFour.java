package package1;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**********************************************************************
 * This class is responsible for creating a JFrame to display the 
 * ConnectFourGame. It also creates the File Menu.
 * 
 * @author Paul Hood
 * @version 10/2/2013
 *********************************************************************/
public class ConnectFour {
	
	/******************************************************************
	 * Static void main method that creates a new ConnectFourGame.
	 *****************************************************************/
	public static void main(String args[]) {
		JMenuBar menus;
		JMenu fileMenu;
		JMenuItem quitItem;
		JMenuItem newGameItem;
		fileMenu = new JMenu("File");
		quitItem = new JMenuItem("Quit");
		newGameItem = new JMenuItem("New Game");
		JFrame frame = new JFrame("Connect Four");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fileMenu.add(newGameItem);
		fileMenu.add(quitItem);
		menus = new JMenuBar();
		menus.add(fileMenu);
		frame.setJMenuBar(menus);
		ConnectFourPanel panel = new ConnectFourPanel(quitItem,
				newGameItem);
		frame.getContentPane().add(panel);
		frame.setSize(500, 500);
		frame.setVisible(true);
	}
}