import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Establish the input/output stream connections
 * 
 * @version 1.0
 * @author Gabriel Richard
 * @since 1.8.0_181-b13 
 */
public class ConnectionWrapper {
	ObjectOutputStream outputStream; // Output stream object
	ObjectInputStream inputStream; // Input stream object
	Socket socket; // Socket object
	
	/**
	 * ConnectionWrapper constructor
	 * 
	 * @param socket	The specified socket
	 */
	public ConnectionWrapper(Socket socket) {
		this.socket = socket;
	}
	
	/**
	 * Returns a socket
	 * 
	 * @return	Socket object
	 */
	public Socket getSocket() {
		return this.socket;
	}
	
	/**
	 * Returns output stream
	 * 
	 * @return	ObjectOutputStream object
	 */
	public ObjectOutputStream getOutputStream() {
		return this.outputStream;
	}
	
	/**
	 * Return input stream
	 * 
	 * @return	ObjectInputStream object
	 */
	public ObjectInputStream getInputStream() {
		return this.inputStream;
	}
	
	/**
	 * Creates an input stream
	 * 
	 * @return	ObjectInputStream object
	 * @throws 	IOException
	 */
	public ObjectInputStream createObjectIStreams() throws IOException {
		ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
		inputStream = new ObjectInputStream(ois);
		return inputStream;
	}
	
	/**
	 * Create and output stream
	 * 
	 * @return 	ObjectOutputStream object
	 * @throws	 IOException
	 */
	public ObjectOutputStream createObjectOStreams() throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
		outputStream = new ObjectOutputStream(oos);
		outputStream.flush();
		return outputStream;
	}
	
	/**
	 * Creates input and output stream
	 * 
	 * @throws IOException
	 */
	public void createStreams() throws IOException {
		createObjectOStreams();
		createObjectIStreams();
	}
	
	/**
	 * Closes the input/output streams and socket
	 * 
	 * @throws IOException
	 */
	public void closeConnection() throws IOException {
		if (outputStream != null)
			outputStream.close();
		if (outputStream != null)
			inputStream.close();
		if (!socket.isClosed() && socket != null)
			socket.close();
	}
}
