import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;

public class Client {

	public static void main(String[] args) {

		
		ClientChatUI chatUI = new ClientChatUI("Exequiel's ClientChatUi");		
		
		JFrame frame = new JFrame();
		frame.setMinimumSize(new Dimension(588, 500));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setContentPane(chatUI.getContentPane());
		frame.setVisible(true);
	
		frame.setResizable(false);
		
	}
}
