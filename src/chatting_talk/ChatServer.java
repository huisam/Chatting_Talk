package chatting_talk;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer {

	class ChatServerThread extends Thread {
		private ObjectInputStream ois;
		private Socket s;

		public ChatServerThread(ObjectInputStream ois, Socket s) {
			this.ois = ois;
			this.s = s;
		}

		@SuppressWarnings("deprecation")
		@Override
		public void run() {
			while (true) {
				try {
					String msg = (String) ois.readObject();
					broadcast(msg);
				} catch (EOFException ee) {
					removeClient(ois);
					System.out.println(getName() + ": bye bye.. ㅠ.ㅠ");
					try {
						s.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					stop();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
	}

	private ArrayList<User> users = new ArrayList<>();
	private int port;
	private String ip;
	private ServerSocket sv;

	public ChatServer(int port, String ip) {
		this.port = port;
		this.ip = ip;
		try {
			sv = new ServerSocket(this.port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void go() {
		try {
			Socket s;
			System.out.println("Waiting Clients");
			s = sv.accept();
			System.out.println("Connected!!!");
			ObjectOutputStream oos;
			ObjectInputStream ois;
			ois = new ObjectInputStream(s.getInputStream());
			oos = new ObjectOutputStream(s.getOutputStream());
			users.add(new User(s, ois, oos)); // 사용자 정보저장
			ChatServerThread cs = new ChatServerThread(ois, s);
			cs.start(); // 쓰레드 생성

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void broadcast(String msg) {
		if (users.isEmpty()) {
			System.out.println("No Connected User");
			return;
		}
		for (User user : users) {
			try {
				user.getOos().writeObject(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void removeClient(ObjectInputStream ois) {
		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).getOis() == ois)
				users.remove(users.get(i));
		}
	}

	public static void main(String[] args) {
		ChatServer server = new ChatServer(8888, "localhost");
		while (true) {
			server.go();
		}
	}

}
