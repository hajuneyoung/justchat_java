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

	String[] roomTitle = {"�� �̸�","�ο���"};
	static List<String> waitClientList = // ���� ������ ����Ʈ
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
		// ===== id/pw�� �г��� �˾ƿ��� =====
		try {
			MainDAO dao = new MainDAO();
			nickname = dao.getNickname(id);
		} catch (Exception e) {} 
		
		// ===== �ʱ� ���� =====
		this.id = id;
		this.pw = pw;
		this.check = login;
		socket = new Socket("127.0.0.1", 5570);
		this.outStream = new DataOutputStream(socket.getOutputStream());
		this.inStream = new DataInputStream(socket.getInputStream());

		outStream.writeUTF("saveRoom|"+"���� �����ڼ�"); // �����ϴ� ��
		outStream.writeUTF("roomResetList|");
		outStream.writeUTF("roomUpdateList|");
		outStream.writeUTF("nameReset|");
		outStream.writeUTF("setNickname|"+nickname); 
		outStream.writeUTF("allWaitName|");
		outStream.writeUTF
		("chatting|"+"���� �����ڼ�"+"/"+"----- [ "+nickname+" ] ----- �� ����"); // ���� ä�� ���� �ȳ�
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 775, 580);
		setTitle("Just Chat!");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setBackground(new Color(180, 205, 230));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.add(scroll);

		// ========== �� ���� Label + TextArea ==========
		JLabel roomLb = new JLabel("\uBC29 \uC815\uBCF4");
		roomLb.setFont(new Font("����", Font.BOLD, 20));
		roomLb.setHorizontalAlignment(SwingConstants.CENTER);
		roomLb.setBounds(12, 7, 76, 29);
		contentPane.add(roomLb);
		
		roomModel = new DefaultTableModel(roomTitle,0);
		table = new JTable(roomModel);
		table.setFont(new Font("����", Font.BOLD, 14));
		table.setBounds(12, 46, 537, 362);
		contentPane.add(table);

		// �� ��� ��� ����
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);

		table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
		
		// ========== ������ ��� Label + TextArea ==========
		JLabel listLb = new JLabel("\uC811\uC18D\uC790 \uBAA9\uB85D");
		listLb.setFont(new Font("����", Font.BOLD, 20));
		listLb.setHorizontalAlignment(SwingConstants.CENTER);
		listLb.setBounds(581, 7, 156, 29);
		contentPane.add(listLb);
		
		model = new DefaultListModel();
		waitList = new JList(model);
		waitList.setBounds(581, 46, 156, 362);
		contentPane.add(waitList);

		// ========== ��ü ä�� Label + TextArea ==========
		JLabel allchatLb = new JLabel("\uB2E8\uCCB4 \uCC44\uD305");
		allchatLb.setHorizontalAlignment(SwingConstants.LEFT);
		allchatLb.setFont(new Font("����", Font.BOLD, 20));
		allchatLb.setBounds(12, 417, 125, 22);
		contentPane.add(allchatLb);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 442, 537, 67);
		contentPane.add(scrollPane);

		allChatTxt = new JTextArea();
		scrollPane.setViewportView(allChatTxt);
		allChatTxt.setEditable(false);

		// ========== ��ü ä�� �Է� Label + TextArea ==========
		JLabel chatinputLb = new JLabel("\u25B6\u25B6");
		chatinputLb.setHorizontalAlignment(SwingConstants.CENTER);
		chatinputLb.setBounds(12, 512, 50, 22);
		contentPane.add(chatinputLb);

		chatinputTxt = new JTextField();
		chatinputTxt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					outStream.writeUTF("chatting|"+"���� �����ڼ�"+"/"+"[ " + nickname + " ] " + chatinputTxt.getText());
				} catch (IOException ee) {}
				chatinputTxt.setText("");
			}
		});
		chatinputTxt.setBounds(61, 512, 488, 22);
		chatinputTxt.setFont(new Font("����", Font.BOLD, 20));
		contentPane.add(chatinputTxt);
		chatinputTxt.setColumns(10);

		// ========== Buttons ==========
		// �� ����� Btn
		// Btn Ŭ�� �� �� ����� ���� ��� �ޱ�(�˾�â)
		JButton newRoomBtn = new JButton("\uBC29 \uB9CC\uB4E4\uAE30");
		newRoomBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JTextField roomName = new JTextField(10);
				JTextField roomOwner = new JTextField(10);
				
				JPanel popup = new JPanel();
				popup.add(new JLabel("�� �̸� : "));
				popup.add(roomName);
//				popup.add(Box.createHorizontalStrut(15));
//				popup.add(new JLabel("������ : "));
//				popup.add(roomOwner);
				
				int result = JOptionPane.showConfirmDialog(null, popup,
						"ä�ù� �����",JOptionPane.CLOSED_OPTION); // Ȯ�� ��ư�� �ִ� �˾�â
				// ���� ��Ҹ� ����
				createRoom = roomName.getText();
				createOwner = nickname;
				
				// �� ĭ�� �� ok Btn�� ������ error msg ���.
				if(result == JOptionPane.OK_OPTION) { 
					if(createRoom.trim().isEmpty()|| createOwner.trim().isEmpty()) {
						JOptionPane.showMessageDialog(null, "��ĭ�� �Է��ϼ���");
					}
					else {// ���ǿ��� ä�ù����� ����--------------------------
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
		newRoomBtn.setFont(new Font("����", Font.BOLD, 18));
		newRoomBtn.setBounds(580, 442, 157, 23);
		contentPane.add(newRoomBtn);

		// �� ���� Btn
		JButton inRoomBtn = new JButton("\uBC29 \uB4E4\uC5B4\uAC00\uAE30");
		inRoomBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int column = 0;
				int row = table.getSelectedRow(); // ���̺��� ���õ� ���� index
				if(table.getSelectedRowCount() == 0) {
					JOptionPane.showMessageDialog(null, "���� �������ּ���.");
				}
				else {
					if(table.getValueAt(row, column).equals("���� �����ڼ�")) {
						JOptionPane.showMessageDialog(null, "���� �����Դϴ�.");
					}else {// ������ �̵�
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
				// row = 0 == ���� ���� �ȳ�
			}
		});
		inRoomBtn.setFont(new Font("����", Font.BOLD, 18));
		inRoomBtn.setBounds(580, 477, 157, 23);
		contentPane.add(inRoomBtn);

		// �α׾ƿ� Btn
		JButton logoutBtn = new JButton("\uB85C\uADF8\uC544\uC6C3");
		logoutBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				try {
					new Main().setVisible(true);
				} catch (Exception e1) {}
			}
		});
		logoutBtn.setFont(new Font("����", Font.BOLD, 18));
		logoutBtn.setBounds(580, 510, 157, 23);
		contentPane.add(logoutBtn);
		
		Thread th = new Thread(this);
		th.start();
	}

	@Override // ========== �������� �Ѿ���� ������ �޴� �κ� ==========
	public void run() {
		try {
			while (inStream != null) {
				String rcvMsg = inStream.readUTF();
				String[] rcvMsgs = rcvMsg.split("\\|"); //number/��
				String number = rcvMsgs[0];
				switch(number) {
				case "nameReset":
					model.removeAllElements();
					break;
				case "allWaitName":
					model.addElement(rcvMsgs[1]);
					break;
				case "setRoom": //*********** �ӽõ�
					System.out.println(rcvMsgs[1]);
					String[] roomInfo = rcvMsgs[1].split("\\/"); // ���̸�/�ο���
					String[] createRoom = {roomInfo[0],roomInfo[1]};
					roomModel.addRow(createRoom);
					break;
				case "roomUpdateList":
					String[] roomInfo1 = rcvMsgs[1].split("\\/"); // ���̸�/�ο���
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
