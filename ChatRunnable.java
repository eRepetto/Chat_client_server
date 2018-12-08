import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JFrame;
import javax.swing.JTextArea;

public class ChatRunnable<T extends JFrame & Accessible> implements Runnable {

	private final T ui;
	private final Socket socket;
	private final ObjectInputStream inputStream;
	private final ObjectOutputStream outputStream;
	private final JTextArea display;

	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, hh:mm a");
	LocalDateTime localTime;

	public ChatRunnable(T ui, ConnectionWrapper connection) {

		this.ui = ui;
		this.display = ui.getDisplay();
		this.socket = connection.getSocket();
		this.inputStream = connection.getInputStream();
		this.outputStream = connection.getOutputStream();
	}

	@Override
	public void run() {

		String strin = "";

		while (true) {

			if (!socket.isClosed())
				/* not sure about this try-catch but got ride of error */
				try {
					strin = (String) inputStream.readObject();
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
				}
			else
				break;

			if (strin.trim().equals(ChatProtocolConstants.CHAT_TERMINATOR)) {
				final String terminate;
				try {
					localTime = LocalDateTime.now();/* gets the current time */
					terminate = ChatProtocolConstants.DISPLACMENT + 
							formatter.format(localTime) + 
							ChatProtocolConstants.LINE_TERMINATOR
							+ strin;
					display.append(terminate);
					break;
				} catch (Exception e) {
					break;
				}
			} else {
				localTime = LocalDateTime.now();/* gets the current time */
				final String append;
				append = ChatProtocolConstants.DISPLACMENT + 
						formatter.format(localTime) + 
						ChatProtocolConstants.LINE_TERMINATOR + strin;
				display.append(append);
			}

			if (!socket.isClosed())
				/*not sure about this*/
				try {
					outputStream.writeObject(
							ChatProtocolConstants.DISPLACMENT + 
							ChatProtocolConstants.CHAT_TERMINATOR + 
							ChatProtocolConstants.LINE_TERMINATOR);
				} catch (IOException e) {

					e.printStackTrace();
				}
		}
		ui.closeChat();
	}
}
