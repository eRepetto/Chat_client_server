import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JTextArea;

public class ChatRunnable <T extends JFrame & Accessible> implements Runnable {

	private final T ui;
	private final Socket socket;
	private final ObjectInputStream inputStream;
	private final ObjectOutputStream outputStream;
	private final JTextArea display;	
	
	public ChatRunnable (T ui, ConnectionWrapper connection){
		
		this.ui = ui;
		this.display = ui.getDisplay();
		this.socket = connection.getSocket();
		this.inputStream = connection.getInputStream(); 
		this.outputStream = connection.getOutputStream();
	}
	
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	
	
	
}
