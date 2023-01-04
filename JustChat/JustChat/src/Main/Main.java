package Main;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import accountCheck.AskIdPw;
import accountCheck.CreateClient;
import connectDB.MainDAO;
import justChatClient.ClientGUI;

public class Main extends JFrame {

	private JPanel contentPane;
	private JTextField idTxt;
	private JPasswordField pwTxt;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setTitle("JUST Chat!"); // ���α׷��� ���� ����
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Main() throws UnknownHostException, IOException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 622, 580);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setBackground(new Color(180, 205, 230));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// ========== Title Label(Txt) ==========
		JLabel titleTxtLb = new JLabel("JUST");
		titleTxtLb.setHorizontalAlignment(SwingConstants.CENTER);
		titleTxtLb.setFont(new Font("�ü�", Font.BOLD, 40));
		titleTxtLb.setBounds(106, 75, 120, 56);
		contentPane.add(titleTxtLb);
		
		// ========== Title Label(Icon) ==========
		ImageIcon titleImg 
			= new ImageIcon("D:/Multi/JustChat/src/Images/icon_chat_cloud.png");
		JLabel titleIconLb = new JLabel(null,titleImg,SwingConstants.CENTER);
//		File path = new File(".");
//		System.out.println(path.getAbsolutePath()); // ������ ����� ���� ��� ����
		titleIconLb.setBounds(116, 42, 386, 274);
		contentPane.add(titleIconLb);
		
		// ========== Id Label & Textfield ==========
		JLabel idLb = new JLabel("\uC544\uC774\uB514");
		idLb.setFont(new Font("����", Font.BOLD, 20));
		idLb.setHorizontalAlignment(SwingConstants.CENTER);
		idLb.setBounds(150, 326, 76, 36);
		contentPane.add(idLb);
		
		idTxt = new JTextField();
		idTxt.setBounds(256, 328, 198, 36);
		contentPane.add(idTxt);
		idTxt.setColumns(10);
		
		// ========== Pw Label & Textfield ==========
		JLabel pwLb = new JLabel("\uD328\uC2A4\uC6CC\uB4DC");
		pwLb.setFont(new Font("����", Font.BOLD, 20));
		pwLb.setHorizontalAlignment(SwingConstants.CENTER);
		pwLb.setBounds(119, 372, 107, 36);
		contentPane.add(pwLb);
		
		pwTxt = new JPasswordField();
		pwTxt.setColumns(10);
		pwTxt.setBounds(256, 374, 198, 36);
		contentPane.add(pwTxt);
		
		// ========== Button ==========
		// "�α��� ��ư" : ������ ������ ���ȭ������ �̵�
		JButton loginBtn = new JButton("\uB85C\uADF8\uC778");
		loginBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String ID = idTxt.getText();
				String PWD = pwTxt.getText();
				if(ID.trim().isEmpty()||ID==null|| // ����ִ� ���� �ִٸ�
						PWD.trim().isEmpty()||PWD==null) {
					JOptionPane.showMessageDialog(null, "��ĭ�� �Է��ϼ���");
				}else if(ID.equals("���̵�")||PWD.equals("��й�ȣ")) { // txtfield�� �⺻ txt�� ���� ���
					JOptionPane.showMessageDialog(null, "���̵�� ��� ��� �Է��ϼ���.");
				} else { // ȸ�� ���̵� ã��
					try {
						MainDAO checkDao = new MainDAO();
						boolean check = checkDao.memberCheck(ID, PWD);
						if(check) {
							setVisible(false);
							new ClientGUI(ID, PWD,"�α���").setVisible(true);;
						}else {
							JOptionPane.showMessageDialog(null, "��ġ�ϴ� ���̵�� ����� �����ϴ�.");
						}
					}catch(Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		loginBtn.setFont(new Font("����", Font.BOLD, 25));
		loginBtn.setBounds(150, 424, 304, 39);
		contentPane.add(loginBtn);
		
		// "ȸ������ ��ư"
		JButton newBtn = new JButton("\uD68C\uC6D0\uAC00\uC785");
		newBtn.addActionListener(new ActionListener()  {
			public void actionPerformed(ActionEvent e) {
					setVisible(false);
					new CreateClient().setVisible(true);
			}
		});
		newBtn.setFont(new Font("����", Font.BOLD, 20));
		newBtn.setBounds(150, 471, 148, 36);
		contentPane.add(newBtn);
		
		// "���̵�/��� ã�� ��ư"faa
		JButton searchBtn = new JButton("\uC544\uC774\uB514/\uBE44\uBC88\uCC3E\uAE30");
		searchBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				new AskIdPw().setVisible(true);
			}
		});
		searchBtn.setFont(new Font("����", Font.BOLD, 14));
		searchBtn.setBounds(308, 471, 148, 36);
		contentPane.add(searchBtn);
	}
}
