package chatting_talk;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class User {
	private Socket s;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
	public User(Socket s, ObjectInputStream ois, ObjectOutputStream oos) {
		this.s = s;
		this.ois = ois;
		this.oos = oos;
	}

	public Socket getS() {
		return s;
	}

	public ObjectInputStream getOis() {
		return ois;
	}

	public ObjectOutputStream getOos() {
		return oos;
	}
	
	
}
