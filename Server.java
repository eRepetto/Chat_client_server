import java.awt.Dimension;
import java.awt.EventQueue;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;

import javax.swing.JFrame;

//import ServerChatUi.WindowsController;

public class Server {

	//private static ServerSocket server;

	public static void main(String[] args) {
		int friend = 0;
		int port = 65535;
		ServerSocket server;
		
		if(args.length != 0)
			port = Integer.parseInt(args[0]);	
		else
			System.out.println("Using default port: " + port);
			
		try {
			server = new ServerSocket(port);
			while (true) {
				Socket socket = server.accept();
				if (socket.getSoLinger() != -1)
					socket.setSoLinger(true, 5);
				if (!socket.getTcpNoDelay())
					socket.setTcpNoDelay(true);
				System.out.println("Connected to " + socket);
				friend++;
				final String title = "YourName's Friend " + friend;
				launchClient(socket, title);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void launchClient(Socket socket, String title) {

		EventQueue.invokeLater(new Runnable() {
			@Override
			/** method is separately executing thread */
			public void run() {

				ServerChatUI serverChat = new ServerChatUI(socket);
				JFrame frame = new JFrame();
				frame.setMinimumSize(new Dimension(588, 500));
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setLocationRelativeTo(null);
				frame.setContentPane(serverChat.getContentPane());
				frame.setVisible(true);
				frame.setTitle(title);
				frame.setResizable(false);

			}
		});

	}

}
