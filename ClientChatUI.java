import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Builds the client chat user interface. 
 * 
 * @version 1.0
 * @author Gabriel Richard
 * @since 1.8.0_181-b13 
 */
public class ClientChatUI extends JFrame implements Accessible {
	private static final long serialVersionUID = -1426403091025368131L;

	/* Text field for MESSAGE panel */
	private JTextField message;
	
	/* Send button for message in MESSAGE panel */
	private JButton sendButton;
	
	/* Text area for the CHAT DISPLAY panel */
	private JTextArea display;
	
	/* Output stream */
	private ObjectOutputStream outputStream;
	
	/* Socket object */
	private Socket socket;
	
	/* ConnectionWrapper object to establish streams */
	private ConnectionWrapper connection;
	
	/* Connect button for the Port combo box */
	JButton portConnectBtn;
	
	/* Text field for Host selection */
	JTextField hostTextField;
	
	/* Combo box for Port selection */
	JComboBox<String> portComboBox;
	

	/**
	 * ClientChatUI constructor.
	 * 
	 * @param frameTitle Title for main frame.
	 */
	public ClientChatUI(String title) {
		setTitle(title);
		runClient();
	}
	
	/**
	 * Handles the closing of the window. 
	 */
	private class WindowController extends WindowAdapter {
		
		public void windowClosing(WindowEvent we) {
			try {
				outputStream.writeObject(ChatProtocolConstants.CHAT_TERMINATOR);
				outputStream.flush();
			} catch (Exception e) {
				System.exit(0);
			}
			System.exit(0);
		}
	}
	
	/**
	 * Handles events sent by the connect and send buttons. 
	 */
	private class Controller implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent av) {
			boolean connected = false;
			String host = "";
			int port;
			
			String event = av.getActionCommand();
			
			// Check if Connect button was pressed
			if (event.equals("connect")) {
				host = hostTextField.getText();
				port = Integer.valueOf((String) portComboBox.getSelectedItem());
				
				connected = connect(host, port);
				
				// Check if connection was successful
				if (connected) {
					portConnectBtn.setEnabled(false);
					portConnectBtn.setBackground(Color.BLUE);
					sendButton.setEnabled(true);
					message.requestFocus();
					
					Runnable runnable = new ChatRunnable<ClientChatUI>(ClientChatUI.this, connection);
					
					try {
						Thread thread = new Thread(runnable);
						thread.start();
					} catch (Exception e) {
						getDisplay().setText(e.toString());
						return;
					}
				} // End inner if statement
				else
					return;
			} // End outer if statement
			
			if (event.equals("send"))
				send();
		}
		
		/**
		 * Connects to an output stream. 
		 * 
		 * @param host		The specified host
		 * @param port		The specified port
		 * @return			True if the connection is successful; false otherwise
		 */
		private boolean connect(String host, int port) {
			Socket s = new Socket();
			
			try {
				s.connect(new InetSocketAddress(InetAddress.getByName(host), port), 10000);
				
				if (s.isConnected()) {
					socket = s;
					
					if (socket.getSoLinger() != -1)
						socket.setSoLinger(true, 5);
					if (!socket.getTcpNoDelay())
						socket.setTcpNoDelay(true);
					
					getDisplay().append(socket.toString() + ChatProtocolConstants.LINE_TERMINATOR);
					
					ConnectionWrapper cw = new ConnectionWrapper(socket);
					connection = cw;
					connection.createStreams();
					outputStream = connection.getOutputStream();
					
					return true;
				}
			} catch (UnknownHostException e) {
				getDisplay().setText(e.toString());
			} catch (IOException e) {
				getDisplay().setText(e.toString());
			} 
			
			return false;
		}
		
		/**
		 * Send message through the output stream
		 */
		private void send() {
			String sendMessage = message.getText();
			getDisplay().append(sendMessage + ChatProtocolConstants.LINE_TERMINATOR);
			try {
				outputStream.writeObject(ChatProtocolConstants.DISPLACMENT + 
						sendMessage + ChatProtocolConstants.LINE_TERMINATOR);
				outputStream.flush();
			} catch (IOException e) {
				enableConnectButton();
				getDisplay().setText(e.toString());
			}
		}
	}
	
	/**
	 * Builds the GUI responsible for the client user interface. 
	 */
	public JPanel createClientUI() {
		/* Handler object for buttons */
		Controller controller = new Controller();
		
		/* Main container for client chat UI */
		JPanel clientPanel = new JPanel(new BorderLayout());
		
		/* Container for the CONNECTION panel */
		JPanel connectionPanel = new JPanel(new BorderLayout());
		
		/* Container for the MESSAGE panel */
		JPanel messagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		/* Container for CHAT DISPLAY panel */
		JPanel chatDisplayPanel = new JPanel(new BorderLayout());
		
		/* Container for the Host label and text field */
		JPanel hostPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		/* Container for portGridPanel */
		JPanel portPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		/* Titled border for the CONNECTION panel */
		TitledBorder connectionBorder;
		
		/* Titled border for the MESSAGE panel */
		CompoundBorder messageBorder;
		
		/* Titled border for the CHAT DISPLAY panel */
		TitledBorder chatDisplayBorder;
		
		/* Red line border for CONNECTION panel */
		Border redLine;
		
		/* Black line border for MESSAGE panel */
		Border blackLine;
		
		/* Blue line border for CHAT DISPLAY panel */
		Border blueLine;
		
		/* Label for Host selection text field */
		JLabel hostLabel;
		
		/* Label for Port combo box */
		JLabel portLabel;
		
		/* Dropdown options for Port combo box*/
		String[] portOptions = {
				"",
				"8089",
				"65000",
				"65535"
		};
		
		/* Scroll bar for chatTextArea */
		JScrollPane scroll;
		
		/* CONNECTION components */
		hostTextField = new JTextField("localhost", 45);
		hostTextField.setBackground(Color.WHITE);
		hostTextField.requestFocus();
		hostTextField.setCaretPosition(0);
		hostTextField.setMargin(new Insets(0, 5, 0, 0));
		
		hostLabel = new JLabel("Host:", JLabel.CENTER);
		hostLabel.setPreferredSize(new Dimension(35, 30));
		hostLabel.setDisplayedMnemonic('H');
		hostLabel.setLabelFor(hostTextField);
		hostLabel.setBorder(new EmptyBorder(0, 2, 0, 0));
		
		hostPanel.add(hostLabel);
		hostPanel.add(hostTextField);
		
		portComboBox = new JComboBox<>(portOptions);
		portComboBox.setBackground(Color.WHITE);
		portComboBox.setEditable(true);
		portComboBox.addActionListener(controller);
		
		portLabel = new JLabel("Port:", JLabel.CENTER);
		portLabel.setPreferredSize(new Dimension(35, 30));
		portLabel.setDisplayedMnemonic('P');
		portLabel.setLabelFor(portComboBox);
		portLabel.setBorder(new EmptyBorder(0, 2, 0, 0));
		
		portConnectBtn = new JButton("Connect");
		portConnectBtn.setBackground(Color.RED);
		portConnectBtn.setMnemonic('C');
		portConnectBtn.setPreferredSize(new Dimension(129, 25));
		portConnectBtn.addActionListener(controller);
		portConnectBtn.setActionCommand("connect");
		
		portPanel.add(portLabel);
		portPanel.add(portComboBox);
		portPanel.add(portConnectBtn);
		
		
		/* MESSAGE components */
		message = new JTextField("Type message", 40);
		message.setBackground(Color.WHITE);
		
		sendButton = new JButton("Send");
		sendButton.setMnemonic('S');
		sendButton.setEnabled(false);
		sendButton.addActionListener(controller);
		sendButton.setPreferredSize(new Dimension(90, 19));
		sendButton.setActionCommand("send");
		
		messagePanel.add(message);
		messagePanel.add(sendButton);
		
		
		/* CHAT DISPLAY components */
		display = new JTextArea(30, 45);
		display.setEditable(false);
		scroll = new JScrollPane(display, 
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		/* Create borders for panels */
		redLine = BorderFactory.createLineBorder(Color.RED, 10);
		blackLine = BorderFactory.createLineBorder(Color.BLACK, 10);
		blueLine = BorderFactory.createLineBorder(Color.BLUE, 10);
		
		connectionBorder = BorderFactory.createTitledBorder(redLine, 
				"CONNECTION");
		
		messageBorder = BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(blackLine, "MESSAGE"),
				BorderFactory.createEmptyBorder(3, 4, 3, 1));
		
		chatDisplayBorder = BorderFactory.createTitledBorder(blueLine, 
				"CHAT DISPLAY", TitledBorder.CENTER, 
				TitledBorder.DEFAULT_POSITION);
		
		connectionPanel.setBorder(connectionBorder); 
		connectionPanel.add(hostPanel, BorderLayout.NORTH);
		connectionPanel.add(portPanel, BorderLayout.SOUTH);
		
		messagePanel.setBorder(messageBorder);
		
		chatDisplayPanel.setBorder(chatDisplayBorder);
		chatDisplayPanel.add(scroll);
		chatDisplayPanel.setPreferredSize(new Dimension(
				chatDisplayPanel.getWidth(), 280));
		
		clientPanel.add(connectionPanel, BorderLayout.NORTH);
		clientPanel.add(messagePanel, BorderLayout.CENTER);
		clientPanel.add(chatDisplayPanel, BorderLayout.SOUTH);
		return clientPanel;
	}
	
	/**
	 * Sets the content pane with the JPanel create by the createClientUI
	 * method and adds a window listener to the frame. 
	 * 
	 * @see createClientUI 
	 */
	private void runClient() {
		WindowController windowController = new WindowController();
		setContentPane(createClientUI());
		addWindowListener(windowController);
	}

	/**
	 * Return the client display
	 * 
	 * @return The client display
	 */
	@Override
	public JTextArea getDisplay() {
		return display;
	}

	/**
	 * Close the connection and terminate the application
	 */
	@Override
	public void closeChat() {
		if (!socket.isClosed()) {
			try {
				connection.closeConnection();
				enableConnectButton();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Enable the connect button and disable the send button.
	 */
	private void enableConnectButton() {
		portConnectBtn.setEnabled(true);
		portConnectBtn.setBackground(Color.RED);
		sendButton.setEnabled(false);
		hostTextField.requestFocus();
	}
}
