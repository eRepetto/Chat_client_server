import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ConnectionWrapper {
	ObjectOutputStream outputStream;
	ObjectInputStream inputStream;
	Socket socket;
	
	public ConnectionWrapper(Socket socket) {
		this.socket = socket;
	}
	
	public Socket getSocket() {
		return this.socket;
	}
	
	public ObjectOutputStream getOutputStream() {
		return this.outputStream;
	}
	
	public ObjectInputStream getInputStream() {
		return this.inputStream;
	}
	
	public ObjectInputStream createObjectIStreams() throws IOException {
		this.inputStream = (ObjectInputStream) socket.getInputStream();
		return this.inputStream;
	}
	
	public ObjectOutputStream createObjectOStreams() throws IOException {
		this.outputStream = (ObjectOutputStream) socket.getOutputStream();
		return this.outputStream;
	}
	
	public void createStreams() throws IOException {
		createObjectIStreams();
		createObjectOStreams();
	}
	
	public void closeConnection() throws IOException {
		if (outputStream != null)
			outputStream.close();
		if (outputStream != null)
			inputStream.close();
		if (!socket.isClosed() && socket != null)
			socket.close();
	}
}
