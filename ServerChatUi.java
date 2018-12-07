import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;

public class ServerChatUi extends JFrame{

	JButton send;
	
	public ServerChatUi(){		
		
		setFrame(createUI());
		runClient();
	}
	
	 public JPanel createUI() {	
		 
		 ActionListener controller = new Controller();
		 
		 
		 
		 JPanel panel = new JPanel();
		 panel.setLayout(new BorderLayout());
		 
		 
		 
			JPanel messagePane = new JPanel();
			messagePane.setBorder(
					BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 10), "MESSAGE"));
			messagePane.setLayout(new BorderLayout());

			JPanel nestedMessage = new JPanel();
			nestedMessage.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			nestedMessage.setLayout(new BorderLayout(5, 5));

			JTextField textMessage = new JTextField("Type message");
			
			 send = new JButton("Send");
			send.setMnemonic('s');
			send.setEnabled(true);
			send.addActionListener(controller);
			send.setPreferredSize(new Dimension(100, 0));
			getRootPane().setDefaultButton(send);

			nestedMessage.add(textMessage, BorderLayout.CENTER);
			nestedMessage.add(send, BorderLayout.EAST);
			
			messagePane.add(nestedMessage);
		 
			
			/* chat display panel */
			JPanel chat = new JPanel();
			chat.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE, 10), "CHAT DISPLAY",
					TitledBorder.CENTER, TitledBorder.CENTER));
			chat.setLayout(new BorderLayout(5, 5));
			
			JTextArea textChat = new JTextArea(30,45);
			//chat.add(textChat, BorderLayout.CENTER);
			textChat.setEditable(false);

			JScrollPane scroll = new JScrollPane(textChat);
			scroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
			
			scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);  
			
			chat.add(scroll);
			//chat.add(hbar,BorderLayout.SOUTH);

	 
		 panel.add(messagePane,BorderLayout.NORTH);
		 panel.add(chat,BorderLayout.CENTER);
		 
		 return panel;
	}
	
	 
	 private void runClient() {
		 
			
	 }
	 
	 final void setFrame(JPanel pane) {
		 
		 EventQueue.invokeLater(new Runnable() {
				@Override
				/**method is separately executing thread*/
				public void run() {
					
					JFrame frame = new JFrame();
					frame.setMinimumSize(new Dimension(588, 500));
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.setLocationRelativeTo(null);
					frame.setContentPane(pane);
					frame.setVisible(true);
					frame.setTitle("Exequiel's friend ServerChatUI");
					frame.addWindowListener(new WindowsController());
					frame.setResizable(false);
					frame.getRootPane().setDefaultButton(send);
				}
			});
	 }
	
	private class WindowsController extends WindowAdapter {
		@Override
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}

	private class Controller implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
		}
	}
	
}


