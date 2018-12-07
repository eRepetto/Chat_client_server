import java.awt.Dimension;

import javax.swing.JFrame;

public class Client {
	public static void main(String[] args) {
		
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				ClientChatUI client = new ClientChatUI("Richard's ClientChatUI");
				client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				client.setLocationByPlatform(true); 
				client.setSize(new Dimension(588, 500));
				client.setResizable(false);
				client.setVisible(true);	
			}
		});
	}
}
