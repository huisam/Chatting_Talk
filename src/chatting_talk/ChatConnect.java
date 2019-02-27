package chatting_talk;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ChatConnect {

	String ip;
	int port;
	String name;
	private Socket s;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private ChatClient cl;

	public ChatConnect(String ip, int port, String name) {
		this.ip = ip;
		this.port = port;
		this.name = name;
	}

	public void go() {
		try {
			s = new Socket("localhost", 8888);
			oos = new ObjectOutputStream(s.getOutputStream());
			ois = new ObjectInputStream(s.getInputStream());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void send(String msg) {
		try {
			oos.writeObject(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String listen() {
		String msg = " ";
		try {
			msg = (String) ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}
	public void close() {
		try {
			s.close();
			oos.close();
			ois.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
