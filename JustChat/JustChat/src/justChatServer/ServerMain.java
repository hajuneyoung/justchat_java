package justChatServer;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

public class ServerMain extends JFrame{
	private static Socket s = null;
	private static ServerSocket ss = null;
	
	static Map<ThreadClass, Integer> threadList = new ConcurrentHashMap<>(); //스레드/포트
	static Map<ThreadClass, String> roomList2 = new ConcurrentHashMap<>();//스레드/방이름
	static Map<String, String> inRoomList = new ConcurrentHashMap<>(); // 닉넴/방이름
	static List<String> nameList = 
			Collections.synchronizedList(new ArrayList<String>()); // 대기실 접속자
	
	private JPanel contentPane;
	private JButton exitBtn;

	public ServerMain() throws IOException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 622, 580);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// ========== Server Label ==========
		JLabel titleLb = new JLabel("Server");
		titleLb.setHorizontalAlignment(SwingConstants.CENTER);
		titleLb.setFont(new Font("궁서체", Font.BOLD, 47));
		titleLb.setBounds(154, 46, 297, 65);
		contentPane.add(titleLb);
		
		exitBtn = new JButton("Exit");
		exitBtn.addActionListener(new ActionListener() { // 프로그램 종료 event
			public void actionPerformed(ActionEvent e) { 
				System.exit(0);
			}
		});
		exitBtn.setFont(new Font("굴림", Font.BOLD, 30));
		exitBtn.setBounds(316, 470, 135, 41);
		contentPane.add(exitBtn);
		
	}// ----- 화면 설계 끝 -----
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			JTextArea msgTxt;
			@Override
			public void run() {
				try {
					ServerMain frame = new ServerMain();
					msgTxt = new JTextArea();
					msgTxt.setFont(new Font("굴림", Font.BOLD, 20));
					// msgTxt.setBounds(57, 103, 506, 341);
					msgTxt.setEditable(false);
					// frame.getContentPane().add(msgTxt);
					
//					public void setMsgTxt(String s) {
//						msgTxt.setText(s);
//					}
//					
					/*================ 스크롤바 생성하기 ================
					 * new JScrollPane
						VERTICAL_SCROLLBAR_AS_NEEDED : 필요할 때만 스크롤 바가 보이도록 함
						HORIZONTAL_SCROLLBAR_AS_NEEDED : 필요할 때만 스크롤 바가 보이도록 함
						-----> 서버를 실행하면 보이지 않다가 접속자 수가 늘어나면서
						       화면이 길어질수록 스크롤바 생성 */
					JScrollPane scroll = 
							new JScrollPane(msgTxt, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
							JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
					scroll.setBounds(57, 103, 506, 341);
					frame.getContentPane().add(scroll);
					
					JButton btnStart = new JButton("Start");
					btnStart.setFont(new Font("굴림", Font.BOLD, 30));
					btnStart.setBounds(129, 470, 135, 41);
					frame.getContentPane().add(btnStart);
					btnStart.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){
								@Override
								protected Void doInBackground() throws Exception {
									//Socket socket = null;
									ss = new ServerSocket(5570);
									msgTxt.append("------------- Start Server--------------\n");
									while(true) {
										ServerMain.s = ss.accept();
										msgTxt.append("접속주소 : "+ServerMain.s.getInetAddress()
								+ " , 접속포트 : "+ServerMain.s.getPort()+"\n");
										ThreadClass threadServer = new ThreadClass(s);
										threadServer.start();
										threadList.put(threadServer, s.getPort());
										//msgTxt.append
										//("접속자 수 : "+threadList.size()+" 명\n");
									}
								}
							};
							worker.execute();
						}
					});
					frame.setTitle("서버");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
}