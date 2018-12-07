import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JTextArea;

public class ChatRunnable <T extends JFrame & Accessible> implements Runnable {

	final T ui;
	final Socket socket;
	final ObjectInputStream inputStream;
	final ObjectOutputStream outputStream;
	final JTextArea display;	
	
	public ChatRunnable (T ui, ConnectionWrapper connection){
		
		this.ui = ui;
		this.socket = connection.getSocket();
		this.inputStream = connection.getInputStream(); // i am not sure about this yet
		this.outputStream = connection.getOutputStream();
	}
	
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	
	
	
}
