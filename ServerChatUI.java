import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;

public class ServerChatUI extends JFrame implements Accessible {
	private static final long serialVersionUID = 1L;
	
	private JButton sendButton;
	private JTextField message;
	private JTextArea display;
	private ObjectOutputStream outputStream;
	private Socket socket;
	private ConnectionWrapper connection;

	public ServerChatUI(Socket socket) {
		this.socket = socket;
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

		message = new JTextField("Type message");

		sendButton = new JButton("Send");
		sendButton.setMnemonic('s');
		sendButton.setEnabled(true);
		sendButton.addActionListener(controller);
		sendButton.setActionCommand("send");
		sendButton.setPreferredSize(new Dimension(100, 0));
		getRootPane().setDefaultButton(sendButton);

		nestedMessage.add(message, BorderLayout.CENTER);
		nestedMessage.add(sendButton, BorderLayout.EAST);

		messagePane.add(nestedMessage);

		/* chat display panel */
		JPanel chat = new JPanel();
		chat.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE, 10), "CHAT DISPLAY",
				TitledBorder.CENTER, TitledBorder.CENTER));
		chat.setLayout(new BorderLayout(5, 5));

		display = new JTextArea(30, 45);
		// chat.add(textChat, BorderLayout.CENTER);
		display.setEditable(false);

		JScrollPane scroll = new JScrollPane(display);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		chat.add(scroll);
		// chat.add(hbar,BorderLayout.SOUTH);

		panel.add(messagePane, BorderLayout.NORTH);
		panel.add(chat, BorderLayout.CENTER);

		return panel;
	}

	/**
	 * Creates input/output streams and starts a new thread.
	 */
	private void runClient() {
		connection = new ConnectionWrapper(socket);
		try {
			connection.createStreams();
			outputStream = connection.getOutputStream();
			Runnable runnable = new ChatRunnable<ServerChatUI>(ServerChatUI.this, connection);
			Thread thread = new Thread(runnable);
			thread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	final void setFrame(JPanel pane) {

		setMinimumSize(new Dimension(588, 500));
		setContentPane(pane);
		addWindowListener(new WindowsController());
		setResizable(false);
	}

	private class WindowsController extends WindowAdapter {
		@Override
		public void windowClosing(WindowEvent e) {

			System.out.println("ServerUI Window Closing");

				try {
					outputStream.writeObject(ChatProtocolConstants.DISPLACMENT + ChatProtocolConstants.CHAT_TERMINATOR
							+ChatProtocolConstants.LINE_TERMINATOR);
				} catch (IOException e1) {
	
					e1.printStackTrace();
				} finally {

				dispose();
				System.out.println("Closing chat!");
			}
		 
		 try {
		 connection.closeConnection();
		 } catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		 finally {
			 dispose();
			 System.out.println("Chat closed");
		 }
		}

		public void WindowClosed() {

			System.out.println("Server UI Closed");
		}
	}

	private class Controller implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent av) {
			String event = av.getActionCommand();
			if (event.equals("send"))
				send();
		}

		private void send() {
			String sendMessage = message.getText();
			getDisplay().append(sendMessage + ChatProtocolConstants.LINE_TERMINATOR);
			try {
				outputStream.writeObject(ChatProtocolConstants.DISPLACMENT + 
						sendMessage + ChatProtocolConstants.LINE_TERMINATOR);
			} catch (IOException e) {
				getDisplay().setText(e.toString());
			}
		}
	}

	@Override
	public JTextArea getDisplay() {
		return display;
	}

	@Override
	public void closeChat() {

		try {
			socket.close();
			dispose();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
