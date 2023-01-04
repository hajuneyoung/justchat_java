package justChatClient;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import justChatServer.ServerMain;

public class ChatRoomGUI extends JFrame implements Runnable {

	private JPanel contentPane;
	private JFrame frame;
	JTextArea inChatTxt;
	private JTextField inputTxtx;
	private JTextField filePathTxtx;
	private JList inRoomList;
	private DefaultListModel inListModel;
	
	String roomName, nickname, check;
	Socket socket;
	DataInputStream inStream;
	DataOutputStream outStream;
	ServerMain server;
	ServerSocket receiver; 
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatRoomGUI frame = new ChatRoomGUI(null,null,null,null,null,null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ChatRoomGUI(Socket s, String room, String nick, 
		String id, String pw, String logincheck) throws IOException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 775, 580);
		setTitle("Just Chat!");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setBackground(new Color(180, 205, 230));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		this.roomName = room;
		this.nickname = nick;
		this.check = this.roomName;
		socket = new Socket("127.0.0.1", 5570);
		this.inStream = new DataInputStream(socket.getInputStream());
		this.outStream = new DataOutputStream(socket.getOutputStream());
		outStream.writeUTF("saveRoom|"+roomName);
		outStream.writeUTF("roomResetList|");
		outStream.writeUTF("roomUpdateList|");
		outStream.writeUTF("setChatList|"+nickname+"/"+roomName);
		outStream.writeUTF("chatNameReset|"+roomName);
		outStream.writeUTF("allChatName|"+roomName+"/"+nickname);
		
		JLabel roomLb = new JLabel("ä��â");
		roomLb.setHorizontalAlignment(SwingConstants.CENTER);
		roomLb.setFont(new Font("Dialog", Font.BOLD, 20));
		roomLb.setBounds(38, 29, 76, 29);
		contentPane.add(roomLb);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(38, 70, 376, 342);
		contentPane.add(scrollPane);
		
		// ä��â
		inChatTxt = new JTextArea();
		// allChatTxt.setText("settext");
		scrollPane.setViewportView(inChatTxt);
		inChatTxt.setEditable(false);
		
		JLabel listLb = new JLabel("������ ���");
		listLb.setHorizontalAlignment(SwingConstants.CENTER);
		listLb.setFont(new Font("Dialog", Font.BOLD, 20));
		listLb.setBounds(565, 29, 156, 29);
		contentPane.add(listLb);
		
		JButton newRoomBtn = new JButton("�ӼӸ�");
		newRoomBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String select = inRoomList.getSelectedValue().toString();
				int result = inRoomList.getSelectedIndex();
				if(select.equals(nickname)) { // �ڱ� �̸��� ������ ��
					JOptionPane.showMessageDialog(null, "�ٸ� ����� �������ּ���.");
				}else {
					try {// �ӼӸ� �Է¹޾� �ش� ������� ������
						String str = JOptionPane.showInputDialog("�ӼӸ� �Է¡��");
						if(!str.equals(null))  // �Է��� ���� ���� ��
							outStream.writeUTF("secretChat|"+roomName+"/"+select
									+"/[ " + nickname+ "(�ӼӸ�) ] " + str);
					} catch (IOException e1) {}
				}
			}
		});
		newRoomBtn.setFont(new Font("Dialog", Font.BOLD, 18));
		newRoomBtn.setBounds(565, 451, 157, 23);
		contentPane.add(newRoomBtn);
		
		// ========== ������ Btn ==========
		JButton returnBtn = new JButton("������"); 
		returnBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showConfirmDialog(null, "�����ðڽ��ϱ�?",
						"������",JOptionPane.YES_NO_OPTION);
				if (result == JOptionPane.YES_OPTION) {
					try { // ----------------- ������ ��ư ------------------
						outStream.writeUTF("portCheck|");
						outStream.writeUTF("roomResetList|");
						outStream.writeUTF("roomUpdateList|");
						outStream.writeUTF("reChatName|"+nickname);
						outStream.writeUTF("chatNameReset|"+roomName);
						outStream.writeUTF("allChatName|"+roomName+"/"+nickname);
						setVisible(false);
						new ClientGUI(id,pw,check).setVisible(true);
					} catch (IOException e1) {}
				}
			}
		});
		returnBtn.setFont(new Font("Dialog", Font.BOLD, 18));
		returnBtn.setBounds(564, 492, 157, 23);
		contentPane.add(returnBtn);
		
		// ä�� �ۼ��ϴ� ��
		inputTxtx = new JTextField();
		inputTxtx.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					outStream.writeUTF("chatting|"+roomName+"/[ " + nickname + " ] "
							+inputTxtx.getText());
				} catch (IOException ee) {}
				inputTxtx.setText("");
			}
		});
		inputTxtx.setFont(new Font("Dialog", Font.BOLD, 20));
		inputTxtx.setColumns(10);
		inputTxtx.setBounds(81, 449, 333, 22);
		contentPane.add(inputTxtx);

		// ������ ���
		inListModel = new DefaultListModel();
		inRoomList = new JList(inListModel);
		inRoomList.setBounds(565, 70, 156, 342);
		contentPane.add(inRoomList);
		
		JLabel chatinputLb = new JLabel("����");
		chatinputLb.setHorizontalAlignment(SwingConstants.CENTER);
		chatinputLb.setBounds(38, 449, 50, 22);
		contentPane.add(chatinputLb);
		
		JButton fileBtn = new JButton("\uCCA8\uBD80");// ----- ���� ������
		fileBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {//*********************����
				String path = filePathTxtx.getText();
				if (path == "���� ��θ� �Է����ּ���") {
					JOptionPane.showMessageDialog(null, "��Ȯ�� ���� ��θ� �Է����ּ���.");
					return;
				}
				String select = inRoomList.getSelectedValue().toString();
				if(select.equals(nickname)) { // �ڱ� �̸��� ������ ��
					JOptionPane.showMessageDialog(null, "�ٸ� ����� �������ּ���.");
				}else {
					try {// �ӼӸ� �Է¹޾� �ش� ������� ������
						receiver = new ServerSocket(9999);
						outStream.writeUTF("fileSend|"+roomName+"/"+select+
								"/"+path+"/"+nickname);
					} catch (IOException e1) {
						//e1.printStackTrace();
					}
				// ��Ÿ �ӼӸ� ����
				}
				filePathTxtx.setText("���� ��θ� �Է����ּ���");//**************��
			}
		});
		fileBtn.setFont(new Font("����", Font.BOLD, 13));
		fileBtn.setBounds(12, 492, 68, 23);
		contentPane.add(fileBtn);
		
		filePathTxtx = new JTextField("���� ��θ� �Է����ּ���");
		filePathTxtx.setFont(new Font("Dialog", Font.BOLD, 20));
		filePathTxtx.setColumns(10);
		filePathTxtx.setBounds(81, 491, 333, 22);
		contentPane.add(filePathTxtx);
		
		Thread th = new Thread(this);
		th.start();
		System.out.println(s);
	}
	
	@Override // �޴� ���� ������� �����ؾ� �Ѵ�. : ������ ������ ���� ������ �� �� �𸣴� ���̹Ƿ� while
	public void run() {
		try {
			while(true) {
				String rcvChatRoomMsgs = inStream.readUTF();
				System.out.println("ä�ù� Ȯ�ο�:"+rcvChatRoomMsgs);
				String[] rcvChatRoomMsg = rcvChatRoomMsgs.split("\\|");
				String number = rcvChatRoomMsg[0]; 
				System.out.println(number);
				switch(number) {
				case "chatNameReset":
					inListModel.removeAllElements();
					break;
				case "allChatName":
					inListModel.addElement(rcvChatRoomMsg[1]);
					break;
				case "chatting": 
					inChatTxt.append(rcvChatRoomMsg[1]+"\n");
					break;
				case "secretChat":
					inChatTxt.append(rcvChatRoomMsg[1]+"\n");
					break;
				case "fileReceive":                             ///************����
					if (rcvChatRoomMsg[1] == "empty")
						break;
					FileOutputStream fOutStream = new FileOutputStream(rcvChatRoomMsg[1]);
					Socket fSocket = receiver.accept();
				    
					InputStream is = fSocket.getInputStream(); 

				    byte[] buffer = new byte[9999];
		            int readBytes;
		            while ((readBytes = is.read(buffer)) != -1) {
		                fOutStream.write(buffer, 0, readBytes);
		                System.out.println("Receving file...");
		            } 
					fOutStream.close();
					is.close();
					fSocket.close();
					receiver.close();
					break;            
				}
			}
		}catch (Exception e) {
			//e.printStackTrace();
		}
	}
}
