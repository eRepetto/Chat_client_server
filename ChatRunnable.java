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
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	
	
	
}
