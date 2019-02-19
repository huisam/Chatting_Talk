## Socket(TCP) 통신을 이용한 Chatting Project 만들기
> 특징
>* Server : 사용자가 접속할 때마다 쓰레드를 생성하여 메세지가 오면 BroadCast하는 방식
>* Client : 1. GUI를 awt의 BorderLayer로 설정하여 화면을 구성한다
>* Client : 2. 객체 생성시 파일을 읽고, 각각의 기능 수행시 ActionListener를 설정하여 버튼에 대한 Action을 수행한다.
>* Client : 3. 전송되는 Object는 ObjectOutputStream을 이용하여 전송하고, 받는 것은 ObjectInputStream을 이용하여 수신한다.

### 구현 과정
>* 서버(메인)
```java
public void go() {
    ServerSocket sv = new ServerSocket("ip", port_num); // 1.서버 소켓 생성
	try {
		Socket s;
		System.out.println("Waiting Clients");
		s = sv.accept(); // 2.클라이언트 연결대기
		System.out.println("Connected!!!");
		ObjectOutputStream oos; // 3.쓰는 파이프라인 생성
		ObjectInputStream ois; // 3.받는 파이프라인 생성
		ois = new ObjectInputStream(s.getInputStream());
		oos = new ObjectOutputStream(s.getOutputStream());
		users.add(new User(s, ois, oos)); // 4.사용자 정보저장
		ChatServerThread cs = new ChatServerThread(ois);
		cs.start(); // 5.쓰레드 생성
	} catch (Exception e) {
		e.printStackTrace();
	}
}
```
>* 서버(쓰레드)
```java
class ChatServerThread extends Thread {
	private ObjectInputStream ois;

	public ChatServerThread(ObjectInputStream ois) {
		this.ois = ois;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		while (true) {
			try {
			    String msg = (String) ois.readObject(); // 1. 메세지를 받자
			    broadcast(msg); // 2. 받으면 전부 쏘기
			} catch(EOFException ee) {
				removeClient(ois); // 3. 연결 끊긴 예외 발생시
				System.out.println(getName() + ": bye bye.. ㅠ.ㅠ" );
				stop(); // 4. 쓰레드종료
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}	
}
```
>* 클라이언트(텍스트 전송)
```java
tf.addActionListener(new ActionListener() {
	public void actionPerformed(ActionEvent e) { // 텍스트 필드에서 엔터키 입력시 호출되는 콜백 메서드
		String str = time(); // 1. 시간 입력 같이 해서 모든 텍스트 완성
				
		cc.send(str); // 2. 텍스트 서버로 전송

		tf.setText(""); // 3. 텍스트 글자 지우기
	}
});

```
>* 클라이언트(종료시)
```java
f.addWindowListener(new WindowAdapter() {
	@Override
	public void windowClosing(WindowEvent e) { // 창 닫기 버튼 클릭시 호출되는 콜백 메서드
		progress = false; // 1.퇴장 플래그
		cc.send( "[" + cc.name + "] 님이 퇴장하셨습니다.");
		f.dispose(); // 2.창닫기
		cc.close(); // 3.소켓 끊기
	}
});
```

#### 구현화면[ 시나리오1 ] - 다 같이 대화하자
![캡처](https://user-images.githubusercontent.com/34855745/53022327-50684a80-349e-11e9-9bdf-180bca6f8bf4.JPG)
#### 구현화면[ 시나리오2 ] - 한명이 나갔다
![캡처2](https://user-images.githubusercontent.com/34855745/53022370-62e28400-349e-11e9-89e5-e65f1f5ddb08.JPG)
