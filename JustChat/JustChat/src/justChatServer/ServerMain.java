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
	
	static Map<ThreadClass, Integer> threadList = new ConcurrentHashMap<>(); //������/��Ʈ
	static Map<ThreadClass, String> roomList2 = new ConcurrentHashMap<>();//������/���̸�
	static Map<String, String> inRoomList = new ConcurrentHashMap<>(); // �г�/���̸�
	static List<String> nameList = 
			Collections.synchronizedList(new ArrayList<String>()); // ���� ������
	
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
		titleLb.setFont(new Font("�ü�ü", Font.BOLD, 47));
		titleLb.setBounds(154, 46, 297, 65);
		contentPane.add(titleLb);
		
		exitBtn = new JButton("Exit");
		exitBtn.addActionListener(new ActionListener() { // ���α׷� ���� event
			public void actionPerformed(ActionEvent e) { 
				System.exit(0);
			}
		});
		exitBtn.setFont(new Font("����", Font.BOLD, 30));
		exitBtn.setBounds(316, 470, 135, 41);
		contentPane.add(exitBtn);
		
	}// ----- ȭ�� ���� �� -----
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			JTextArea msgTxt;
			@Override
			public void run() {
				try {
					ServerMain frame = new ServerMain();
					msgTxt = new JTextArea();
					msgTxt.setFont(new Font("����", Font.BOLD, 20));
					// msgTxt.setBounds(57, 103, 506, 341);
					msgTxt.setEditable(false);
					// frame.getContentPane().add(msgTxt);
					
//					public void setMsgTxt(String s) {
//						msgTxt.setText(s);
//					}
//					
					/*================ ��ũ�ѹ� �����ϱ� ================
					 * new JScrollPane
						VERTICAL_SCROLLBAR_AS_NEEDED : �ʿ��� ���� ��ũ�� �ٰ� ���̵��� ��
						HORIZONTAL_SCROLLBAR_AS_NEEDED : �ʿ��� ���� ��ũ�� �ٰ� ���̵��� ��
						-----> ������ �����ϸ� ������ �ʴٰ� ������ ���� �þ�鼭
						       ȭ���� ��������� ��ũ�ѹ� ���� */
					JScrollPane scroll = 
							new JScrollPane(msgTxt, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
							JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
					scroll.setBounds(57, 103, 506, 341);
					frame.getContentPane().add(scroll);
					
					JButton btnStart = new JButton("Start");
					btnStart.setFont(new Font("����", Font.BOLD, 30));
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
										msgTxt.append("�����ּ� : "+ServerMain.s.getInetAddress()
								+ " , ������Ʈ : "+ServerMain.s.getPort()+"\n");
										ThreadClass threadServer = new ThreadClass(s);
										threadServer.start();
										threadList.put(threadServer, s.getPort());
										//msgTxt.append
										//("������ �� : "+threadList.size()+" ��\n");
									}
								}
							};
							worker.execute();
						}
					});
					frame.setTitle("����");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
}