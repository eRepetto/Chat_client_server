import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.BoundedRangeModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;

public class ClientChatUi extends JFrame {

	private String title;
	private static final long serialVersionUID = 1;
	private final String hostName = "<html><u>H</u>ost:</html>";

	public ClientChatUi(String title) {
		this.title = title;
		runClient();

	}

	public JPanel createClientUI() {

		
		ActionListener controller = new Controller();
		
		setTitle(title);

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		JPanel top = new JPanel();
		top.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.RED, 10), "CONNECTION"));
		top.setLayout(new BorderLayout());
		top.setPreferredSize(new Dimension(120, 120));

		/* nested top layout */
		JPanel nestedTop = new JPanel();
		nestedTop.setLayout(new BorderLayout(5, 5));
		nestedTop.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		nestedTop.setPreferredSize(new Dimension(100, 45));

		/* host label */
		JLabel host = new JLabel("<html><u>H</u>ost:</html>");
		host.setDisplayedMnemonic('h');
		host.setPreferredSize(new Dimension(35, 30));

		JTextField text = new JTextField("localhost");		
		Border paddingBorder = BorderFactory.createEmptyBorder(0,5,0,0);		
		text.setBorder(BorderFactory.createCompoundBorder(text.getBorder(),paddingBorder));
		
		text.setCaretPosition(0);
		text.setFocusable(true);
		host.setLabelFor(text);
		
		nestedTop.add(host, BorderLayout.LINE_START);
		nestedTop.add(text, BorderLayout.CENTER);

		JPanel southTop = new JPanel();
		southTop.setLayout(new BorderLayout(5, 5));
		southTop.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		/* port label */
		JLabel port = new JLabel("<html><u>P</u>ort:</html>");
		port.setPreferredSize(new Dimension(35, 30));
		port.setDisplayedMnemonic('p');
		
		southTop.add(port, BorderLayout.WEST);

		/* combo box */
		String num[] = { "", "8089", "65000", "65535" };
		JComboBox<Integer> cb = new JComboBox(num);
		southTop.add(cb, BorderLayout.CENTER);
		cb.setPreferredSize(new Dimension(125, 0));
		cb.setBackground(Color.WHITE);
		cb.setEditable(true);
		cb.addActionListener(controller);
		port.setLabelFor(cb);

		/* connect button */
		JButton connect = new JButton("Connect");
		connect.setPreferredSize(new Dimension(125,0));
		connect.setBackground(Color.RED);
		connect.setMnemonic('c');
		connect.addActionListener(controller);
		southTop.add(connect, BorderLayout.EAST);

		/* message Panel */
		JPanel messagePane = new JPanel();
		messagePane.setBorder(
				BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 10), "MESSAGE"));
		messagePane.setLayout(new BorderLayout());

		JPanel nestedMessage = new JPanel();
		nestedMessage.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		nestedMessage.setLayout(new BorderLayout(5, 5));

		JTextField textMessage = new JTextField("Type message");

		JButton send = new JButton("Send");
		send.setMnemonic('s');
		send.setEnabled(false);
		send.addActionListener(controller);
		send.setPreferredSize(new Dimension(100, 0));

		nestedMessage.add(textMessage, BorderLayout.CENTER);
		nestedMessage.add(send, BorderLayout.EAST);

		/* chat display panel */
		JPanel chat = new JPanel();
		chat.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE, 10), "CHAT DISPLAY",
				TitledBorder.CENTER, TitledBorder.CENTER));
		chat.setLayout(new BorderLayout(5, 5));
		chat.setBackground(Color.WHITE);
		
		JTextArea textChat = new JTextArea(30,45);
		textChat.setEditable(false);
		chat.add(textChat, BorderLayout.CENTER);
		
		JScrollPane scroll = new JScrollPane(textChat);
		scroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
		
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);  
				
		JScrollBar hbar = new JScrollBar(JScrollBar.HORIZONTAL, 20, 200, 20, 400);
		hbar.setVisible(false);
		
		chat.add(scroll);
		//chat.add(hbar,BorderLayout.SOUTH);

		messagePane.add(nestedMessage);
		top.add(nestedTop, BorderLayout.NORTH);
		top.add(southTop, BorderLayout.WEST);

		/* panel holds connection and message panel */
		JPanel bigTop = new JPanel();
		bigTop.setLayout(new BorderLayout());
		bigTop.add(top, BorderLayout.NORTH);
		bigTop.add(messagePane, BorderLayout.SOUTH);

		panel.add(bigTop, BorderLayout.NORTH);
		panel.add(chat, BorderLayout.CENTER);

		return panel;
	}

	private void runClient() {		
		
		EventQueue.invokeLater(new Runnable() {
			@Override
			/**method is separately executing thread*/
			public void run() {
				
				JFrame frame = new JFrame();
				frame.setMinimumSize(new Dimension(588, 500));
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setLocationRelativeTo(null);
				frame.setContentPane(createClientUI());
				frame.setVisible(true);
				frame.setTitle(title);
				frame.setResizable(false);
				frame.addWindowListener(new WindowsController());
				

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
