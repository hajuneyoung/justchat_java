package justChatClient;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import Main.Main;
import connectDB.MainDAO;

public class ClientGUI extends JFrame implements Runnable {

	Socket socket;
	DataOutputStream outStream;
	DataInputStream inStream;
	String id, pw, check, nickname, createRoom, createOwner;

	String[] roomTitle = {"방 이름","인원수"};
	static List<String> waitClientList = // 대기실 접속자 리스트
			Collections.synchronizedList(new ArrayList<String>());
	
	private JPanel contentPane;
	private JTextArea allChatTxt;
	private JTextField chatinputTxt;
	private JList waitList;
	private JScrollPane scroll = new JScrollPane(chatinputTxt);
	private DefaultListModel model;
	private DefaultTableModel roomModel;
	private JTable table;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientGUI frame = new ClientGUI(null, null,null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ClientGUI(String id, String pw, String login) throws IOException {
		// ===== id/pw로 닉네임 알아오기 =====
		try {
			MainDAO dao = new MainDAO();
			nickname = dao.getNickname(id);
		} catch (Exception e) {} 
		
		// ===== 초기 설정 =====
		this.id = id;
		this.pw = pw;
		this.check = login;
		socket = new Socket("127.0.0.1", 5570);
		this.outStream = new DataOutputStream(socket.getOutputStream());
		this.inStream = new DataInputStream(socket.getInputStream());

		outStream.writeUTF("saveRoom|"+"현재 접속자수"); // 설정하는 것
		outStream.writeUTF("roomResetList|");
		outStream.writeUTF("roomUpdateList|");
		outStream.writeUTF("nameReset|");
		outStream.writeUTF("setNickname|"+nickname); 
		outStream.writeUTF("allWaitName|");
		outStream.writeUTF
		("chatting|"+"현재 접속자수"+"/"+"----- [ "+nickname+" ] ----- 님 입장"); // 대기실 채팅 입장 안내
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 775, 580);
		setTitle("Just Chat!");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setBackground(new Color(180, 205, 230));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.add(scroll);

		// ========== 방 정보 Label + TextArea ==========
		JLabel roomLb = new JLabel("\uBC29 \uC815\uBCF4");
		roomLb.setFont(new Font("굴림", Font.BOLD, 20));
		roomLb.setHorizontalAlignment(SwingConstants.CENTER);
		roomLb.setBounds(12, 7, 76, 29);
		contentPane.add(roomLb);
		
		roomModel = new DefaultTableModel(roomTitle,0);
		table = new JTable(roomModel);
		table.setFont(new Font("굴림", Font.BOLD, 14));
		table.setBounds(12, 46, 537, 362);
		contentPane.add(table);

		// 방 목록 가운데 정렬
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);

		table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
		
		// ========== 접속자 목록 Label + TextArea ==========
		JLabel listLb = new JLabel("\uC811\uC18D\uC790 \uBAA9\uB85D");
		listLb.setFont(new Font("굴림", Font.BOLD, 20));
		listLb.setHorizontalAlignment(SwingConstants.CENTER);
		listLb.setBounds(581, 7, 156, 29);
		contentPane.add(listLb);
		
		model = new DefaultListModel();
		waitList = new JList(model);
		waitList.setBounds(581, 46, 156, 362);
		contentPane.add(waitList);

		// ========== 단체 채팅 Label + TextArea ==========
		JLabel allchatLb = new JLabel("\uB2E8\uCCB4 \uCC44\uD305");
		allchatLb.setHorizontalAlignment(SwingConstants.LEFT);
		allchatLb.setFont(new Font("굴림", Font.BOLD, 20));
		allchatLb.setBounds(12, 417, 125, 22);
		contentPane.add(allchatLb);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 442, 537, 67);
		contentPane.add(scrollPane);

		allChatTxt = new JTextArea();
		scrollPane.setViewportView(allChatTxt);
		allChatTxt.setEditable(false);

		// ========== 단체 채팅 입력 Label + TextArea ==========
		JLabel chatinputLb = new JLabel("\u25B6\u25B6");
		chatinputLb.setHorizontalAlignment(SwingConstants.CENTER);
		chatinputLb.setBounds(12, 512, 50, 22);
		contentPane.add(chatinputLb);

		chatinputTxt = new JTextField();
		chatinputTxt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					outStream.writeUTF("chatting|"+"현재 접속자수"+"/"+"[ " + nickname + " ] " + chatinputTxt.getText());
				} catch (IOException ee) {}
				chatinputTxt.setText("");
			}
		});
		chatinputTxt.setBounds(61, 512, 488, 22);
		chatinputTxt.setFont(new Font("굴림", Font.BOLD, 20));
		contentPane.add(chatinputTxt);
		chatinputTxt.setColumns(10);

		// ========== Buttons ==========
		// 방 만들기 Btn
		// Btn 클릭 → 방 만들기 위한 요소 받기(팝업창)
		JButton newRoomBtn = new JButton("\uBC29 \uB9CC\uB4E4\uAE30");
		newRoomBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JTextField roomName = new JTextField(10);
				JTextField roomOwner = new JTextField(10);
				
				JPanel popup = new JPanel();
				popup.add(new JLabel("방 이름 : "));
				popup.add(roomName);
//				popup.add(Box.createHorizontalStrut(15));
//				popup.add(new JLabel("개설자 : "));
//				popup.add(roomOwner);
				
				int result = JOptionPane.showConfirmDialog(null, popup,
						"채팅방 만들기",JOptionPane.CLOSED_OPTION); // 확인 버튼만 있는 팝업창
				// 받은 요소를 저장
				createRoom = roomName.getText();
				createOwner = nickname;
				
				// 빈 칸일 때 ok Btn을 누르면 error msg 뜬다.
				if(result == JOptionPane.OK_OPTION) { 
					if(createRoom.trim().isEmpty()|| createOwner.trim().isEmpty()) {
						JOptionPane.showMessageDialog(null, "빈칸을 입력하세요");
					}
					else {// 대기실에서 채팅방으로 들어가기--------------------------
						try {
							outStream.writeUTF("portCheck|");
							outStream.writeUTF("nameReset|");
							outStream.writeUTF("deleteWaitName|"+nickname);
							outStream.writeUTF("allWaitName|");
							setVisible(false);
							new ChatRoomGUI(socket,createRoom,nickname,id,pw,
									check).setVisible(true);
						} catch (IOException e1) {}
					}
				}
			}
		});
		newRoomBtn.setFont(new Font("굴림", Font.BOLD, 18));
		newRoomBtn.setBounds(580, 442, 157, 23);
		contentPane.add(newRoomBtn);

		// 방 들어가기 Btn
		JButton inRoomBtn = new JButton("\uBC29 \uB4E4\uC5B4\uAC00\uAE30");
		inRoomBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int column = 0;
				int row = table.getSelectedRow(); // 테이블에서 선택된 방의 index
				if(table.getSelectedRowCount() == 0) {
					JOptionPane.showMessageDialog(null, "방을 선택해주세요.");
				}
				else {
					if(table.getValueAt(row, column).equals("현재 접속자수")) {
						JOptionPane.showMessageDialog(null, "현재 대기실입니다.");
					}else {// 방으로 이동
						createRoom = (String) table.getValueAt(row, column);
						setVisible(false);
						try {
							outStream.writeUTF("portCheck|"+nickname+"/"+createRoom);
							outStream.writeUTF("nameReset|");
							outStream.writeUTF("deleteWaitName|"+nickname);
							outStream.writeUTF("allWaitName|");
							new ChatRoomGUI(socket,createRoom,nickname,id,pw,
									check).setVisible(true);
						} catch (IOException e1) {
							//e1.printStackTrace();
						}
					}
				}
				// row = 0 == 현재 대기실 안내
			}
		});
		inRoomBtn.setFont(new Font("굴림", Font.BOLD, 18));
		inRoomBtn.setBounds(580, 477, 157, 23);
		contentPane.add(inRoomBtn);

		// 로그아웃 Btn
		JButton logoutBtn = new JButton("\uB85C\uADF8\uC544\uC6C3");
		logoutBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				try {
					new Main().setVisible(true);
				} catch (Exception e1) {}
			}
		});
		logoutBtn.setFont(new Font("굴림", Font.BOLD, 18));
		logoutBtn.setBounds(580, 510, 157, 23);
		contentPane.add(logoutBtn);
		
		Thread th = new Thread(this);
		th.start();
	}

	@Override // ========== 서버에서 넘어오는 내용을 받는 부분 ==========
	public void run() {
		try {
			while (inStream != null) {
				String rcvMsg = inStream.readUTF();
				String[] rcvMsgs = rcvMsg.split("\\|"); //number/값
				String number = rcvMsgs[0];
				switch(number) {
				case "nameReset":
					model.removeAllElements();
					break;
				case "allWaitName":
					model.addElement(rcvMsgs[1]);
					break;
				case "setRoom": //*********** 머시따
					System.out.println(rcvMsgs[1]);
					String[] roomInfo = rcvMsgs[1].split("\\/"); // 방이름/인원수
					String[] createRoom = {roomInfo[0],roomInfo[1]};
					roomModel.addRow(createRoom);
					break;
				case "roomUpdateList":
					String[] roomInfo1 = rcvMsgs[1].split("\\/"); // 방이름/인원수
					String[] createRoom1 = {roomInfo1[0],roomInfo1[1]};
					roomModel.addRow(createRoom1);
					break;
				case "roomResetList":
					roomModel.setRowCount(0);
					break;
				case "chatting":
					allChatTxt.append(rcvMsgs[1]+"\n");
					break;
				}
			}
		} catch (Exception eee) {}
	}
}
