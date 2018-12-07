import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ConnectionWrapper {

	private ObjectOutputStream outputStream;
	private ObjectInputStream inputStream;
	private Socket socket;

	ConnectionWrapper(Socket socket) {
		this.socket = socket;
	}

	public Socket getSocket() {

		return socket;
	}

	public ObjectOutputStream getOutputStream() {

		return outputStream;
	}

	public ObjectInputStream getInputStream() {

		return inputStream;
	}

}
