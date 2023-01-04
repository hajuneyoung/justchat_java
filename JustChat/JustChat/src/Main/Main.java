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
					frame.setTitle("JUST Chat!"); // 프로그램의 제목 설정
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
		titleTxtLb.setFont(new Font("궁서", Font.BOLD, 40));
		titleTxtLb.setBounds(106, 75, 120, 56);
		contentPane.add(titleTxtLb);
		
		// ========== Title Label(Icon) ==========
		ImageIcon titleImg 
			= new ImageIcon("D:/Multi/JustChat/src/Images/icon_chat_cloud.png");
		JLabel titleIconLb = new JLabel(null,titleImg,SwingConstants.CENTER);
//		File path = new File(".");
//		System.out.println(path.getAbsolutePath()); // 아이콘 적용시 절대 경로 기입
		titleIconLb.setBounds(116, 42, 386, 274);
		contentPane.add(titleIconLb);
		
		// ========== Id Label & Textfield ==========
		JLabel idLb = new JLabel("\uC544\uC774\uB514");
		idLb.setFont(new Font("굴림", Font.BOLD, 20));
		idLb.setHorizontalAlignment(SwingConstants.CENTER);
		idLb.setBounds(150, 326, 76, 36);
		contentPane.add(idLb);
		
		idTxt = new JTextField();
		idTxt.setBounds(256, 328, 198, 36);
		contentPane.add(idTxt);
		idTxt.setColumns(10);
		
		// ========== Pw Label & Textfield ==========
		JLabel pwLb = new JLabel("\uD328\uC2A4\uC6CC\uB4DC");
		pwLb.setFont(new Font("굴림", Font.BOLD, 20));
		pwLb.setHorizontalAlignment(SwingConstants.CENTER);
		pwLb.setBounds(119, 372, 107, 36);
		contentPane.add(pwLb);
		
		pwTxt = new JPasswordField();
		pwTxt.setColumns(10);
		pwTxt.setBounds(256, 374, 198, 36);
		contentPane.add(pwTxt);
		
		// ========== Button ==========
		// "로그인 버튼" : 조건이 맞으면 대기화면으로 이동
		JButton loginBtn = new JButton("\uB85C\uADF8\uC778");
		loginBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String ID = idTxt.getText();
				String PWD = pwTxt.getText();
				if(ID.trim().isEmpty()||ID==null|| // 비어있는 곳이 있다면
						PWD.trim().isEmpty()||PWD==null) {
					JOptionPane.showMessageDialog(null, "빈칸을 입력하세요");
				}else if(ID.equals("아이디")||PWD.equals("비밀번호")) { // txtfield에 기본 txt가 있을 경우
					JOptionPane.showMessageDialog(null, "아이디와 비번 모두 입력하세요.");
				} else { // 회원 아이디 찾기
					try {
						MainDAO checkDao = new MainDAO();
						boolean check = checkDao.memberCheck(ID, PWD);
						if(check) {
							setVisible(false);
							new ClientGUI(ID, PWD,"로그인").setVisible(true);;
						}else {
							JOptionPane.showMessageDialog(null, "일치하는 아이디와 비번이 없습니다.");
						}
					}catch(Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		loginBtn.setFont(new Font("굴림", Font.BOLD, 25));
		loginBtn.setBounds(150, 424, 304, 39);
		contentPane.add(loginBtn);
		
		// "회원가입 버튼"
		JButton newBtn = new JButton("\uD68C\uC6D0\uAC00\uC785");
		newBtn.addActionListener(new ActionListener()  {
			public void actionPerformed(ActionEvent e) {
					setVisible(false);
					new CreateClient().setVisible(true);
			}
		});
		newBtn.setFont(new Font("굴림", Font.BOLD, 20));
		newBtn.setBounds(150, 471, 148, 36);
		contentPane.add(newBtn);
		
		// "아이디/비번 찾기 버튼"faa
		JButton searchBtn = new JButton("\uC544\uC774\uB514/\uBE44\uBC88\uCC3E\uAE30");
		searchBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				new AskIdPw().setVisible(true);
			}
		});
		searchBtn.setFont(new Font("굴림", Font.BOLD, 14));
		searchBtn.setBounds(308, 471, 148, 36);
		contentPane.add(searchBtn);
	}
}
