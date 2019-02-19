package chatting_talk;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ChatClient extends Thread {

	private TextArea ta;
	private TextField tf;
	private Frame f;
	private Button b1;
	private Button b2;
	private Panel p;
	private ChatConnect cc;
	private boolean progress = true;

	public ChatClient(String ip, int port, String name) {
		cc = new ChatConnect(ip, port, name);
		createGUI();
		cc.go();
		cc.send("[" + cc.name + "] 님이 입장하셨습니다.");
	}
	
	@SuppressWarnings("deprecation")
	public void run() {
		while(progress) {
			show();
		}
		stop();
	}

	public void createGUI() {
		f = new Frame(cc.name + "_카카오톡");
		f.setBounds(600, 100, 300, 400);
		f.setLayout(new BorderLayout());

		ta = new TextArea();
		f.add(ta, BorderLayout.CENTER);
		ta.setEditable(false);

		tf = new TextField();
		f.add(tf, BorderLayout.SOUTH);

		p = new Panel();
		p.setLayout(new BorderLayout());

		b1 = new Button("Send");
		b2 = new Button("Exit");
		p.add(b1, BorderLayout.SOUTH);
		p.add(b2, BorderLayout.NORTH);

		f.add(p, BorderLayout.EAST);

		f.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) { // 창 닫기 버튼 클릭시 호출되는 콜백 메서드
				progress = false;
				cc.send( "[" + cc.name + "] 님이 퇴장하셨습니다.");
				f.dispose();
				cc.close();
				
			}
		});
		b2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				progress = false;
				cc.send( "[" + cc.name + "]" + " 님이 퇴장하셨습니다.");
				f.dispose();
				cc.close();
			}
		});
		tf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { // 텍스트 필드에서 엔터키 입력시 호출되는 콜백 메서드
				String str = time();
				
				cc.send(str); // 서버 전송

				tf.setText(""); // 글자 지우기
			}
		});

		b1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String str = time();
				
				cc.send(str); // 서버 전송
				
				tf.setText(""); // 글자 지우기
			}
		});
		f.setVisible(true); // 보통 가장 마지막에 한다
	}

	public void show() {
		String msg = cc.listen();
		ta.append(msg + "\n"); // 받으면 텍스트 에어리어에 추가하기
	}

	public String time() {
		int minute = LocalTime.now().getMinute();
		int hour = LocalTime.now().getHour();
		String str_hour = "";
		String str_min = "";
		String af = "";
		if (hour >= 12) af = "pm";
		else af = "am";
		if (hour > 12) hour -= 12;
		
		if (hour < 10) str_hour = "0" + Integer.toString(hour);
		else str_hour = Integer.toString(hour);	
		if (minute < 10) str_min = "0" + minute;
		else str_min = Integer.toString(minute);
		
		String str = cc.name + " : " + tf.getText() + " (" + str_hour+ ":"  + str_min + af + ")"; // 문자열 읽어옴
		
		return str;
		
	}
	
	public void actionPerformed(ActionEvent ae) {
		
	}

	public static void main(String[] args) {
		ChatClient client = new ChatClient("localhost", 8080, "선생님");
		ChatClient client2 = new ChatClient("localhost", 8080, "반장");
		ChatClient client3 = new ChatClient("localhost", 8080, "학생");
		client.start();
		client2.start();
		client3.start();
		
	}

}
