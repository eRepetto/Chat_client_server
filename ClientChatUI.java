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

public class ClientChatUI extends JFrame implements Accessible {
	private static final long serialVersionUID = 1L;
	
	JTextField message;
	JButton sendButton;
	JTextArea display;
	ObjectOutputStream outputStream;
	Socket socket;
	ConnectionWrapper connection;
	
	/* Connect button for the Port combo box */
	JButton portConnectBtn;
	
	/* Send button for message in MESSAGE panel */
	JButton sendMessageBtn;
	
	/* Text field for Host selection */
	JTextField hostTextField;
	
	/* Text field for MESSAGE panel */
	JTextField messageTextField;
	
	/* Combo box for Port selection */
	JComboBox<String> portComboBox;
	
	/* Text area for the CHAT DISPLAY panel */
	JTextArea chatTextArea;
	

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
	 * No implementation yet. 
	 * 
	 * @author Gabriel Richard
	 */
	private class WindowController extends WindowAdapter {
		
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}
	
	/**
	 * No implementation yet. 
	 * 
	 * @author Gabriel Richard
	 */
	private class Controller implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent av) {
			boolean connected = false;
			String host = "";
			int port;
			
			String event = av.getActionCommand();
			if (event.equals("connect")) 
				host = hostTextField.getText();;
			
			
			port = (int) portComboBox.getSelectedItem();
			connected = connect(host, port);
			if (connected) {
				portConnectBtn.setEnabled(false);
				portConnectBtn.setBackground(Color.BLUE);
				sendMessageBtn.setEnabled(true);
				messageTextField.requestFocus();
				
				Runnable runnable = new ChatRunnable<ClientChatUI>(ClientChatUI.this, connection);
				
				try {
					Thread thread = new Thread(runnable);
					thread.start();
				} catch (Exception e) {
					chatTextArea.setText(e.toString());
					return;
				}
			} 
			else
				return;
			
			if (event.equals("send"))
				send();
		}
		
		private boolean connect(String host, int port) {
			Socket s = new Socket();
			
			try {
				s.connect(new InetSocketAddress(InetAddress.getByName(host), port), 10000);
				
				if (socket != null) {
					socket = s;
					
					if (socket.getSoLinger() != -1)
						socket.setSoLinger(true, 5);
					if (!socket.getTcpNoDelay())
						socket.setTcpNoDelay(true);
					
					chatTextArea.append(socket.toString());
					connection = new ConnectionWrapper(socket);
					connection.createStreams();
					outputStream = connection.getOutputStream();
					
					return true;
				}
			} catch (UnknownHostException e) {
				chatTextArea.setText(e.toString());
			} catch (IOException e) {
				chatTextArea.setText(e.toString());
			} 
			
			return false;
		}
		
		private void send() {
			String sendMessage = messageTextField.getText();
			chatTextArea.append(sendMessage + ChatProtocolConstants.LINE_TERMINATOR);
			try {
				outputStream.writeObject(ChatProtocolConstants.DISPLACEMENT + 
						sendMessage + ChatProtocolConstants.LINE_TERMINATOR);
			} catch (IOException e) {
				enableConnectButton();
				chatTextArea.setText(e.toString());
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
		messageTextField = new JTextField("Type message", 40);
		messageTextField.setBackground(Color.WHITE);
		
		sendMessageBtn = new JButton("Send");
		sendMessageBtn.setMnemonic('S');
		sendMessageBtn.setEnabled(false);
		sendMessageBtn.addActionListener(controller);
		sendMessageBtn.setPreferredSize(new Dimension(90, 19));
		sendMessageBtn.setActionCommand("send");
		
		messagePanel.add(messageTextField);
		messagePanel.add(sendMessageBtn);
		
		
		/* CHAT DISPLAY components */
		chatTextArea = new JTextArea(30, 45);
		chatTextArea.setEditable(false);
		scroll = new JScrollPane(chatTextArea, 
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

	@Override
	public JTextArea getDisplay() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void closeChat() {
		if (!socket.isClosed()) {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		enableConnectButton();
	}
	
	private void enableConnectButton() {
		portConnectBtn.setEnabled(true);
		portConnectBtn.setBackground(Color.RED);
		sendMessageBtn.setEnabled(false);
		hostTextField.requestFocus();
	}
}
