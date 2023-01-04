package accountCheck;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import Main.Main;
import connectDB.ChatVO;
import connectDB.chatDAO;
import java.awt.Font;
import javax.swing.SwingConstants;

public class CreateClient extends JFrame  implements ActionListener{

	private JPanel contentPane;
	private JTextField IDtextField;
	private JPasswordField passwordField1;
	private JPasswordField passwordField2;
	private JTextField NAMEtextField;
	private JTextField NICKtextField;
	private JTextField PHONEtextField1;
	private JTextField PHONEtextField2;
	private JTextField PWQtextField;
	private JTextField PWAtextField;
	
	String ID;
	String PWD;
	String PWDck;
	String name;
	String nick;
	String phone;
	String pwq;
	String pwa;
	
	chatDAO dao = new chatDAO();
	Scanner sc=new Scanner(System.in);
	private JTextField PHONEtextField0;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CreateClient frame = new CreateClient();
					frame.setTitle("회원가입 페이지");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	JButton OKBtn = new JButton("확인");
	JButton CCBtn = new JButton("취소");

	public CreateClient() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 580);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setBackground(new Color(180, 205, 230));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel 아이디lNewLabel = new JLabel("\uC544\uC774\uB514");
		아이디lNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		아이디lNewLabel.setFont(new Font("굴림", Font.BOLD, 14));
		아이디lNewLabel.setBounds(59, 37, 58, 51);
		contentPane.add(아이디lNewLabel);
		
		IDtextField = new JTextField();
		IDtextField.setBounds(127, 40, 295, 48);
		contentPane.add(IDtextField);
		IDtextField.setColumns(10);
		
		JLabel 암호NewLabel_1 = new JLabel("\uC554\uD638");
		암호NewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		암호NewLabel_1.setFont(new Font("굴림", Font.BOLD, 14));
		암호NewLabel_1.setBounds(59, 98, 65, 42);
		contentPane.add(암호NewLabel_1);
		
		passwordField1 = new JPasswordField();
		passwordField1.setBounds(127, 98, 295, 47);
		contentPane.add(passwordField1);
		
		JLabel 확인NewLabel_2 = new JLabel("\uC554\uD638 \uD655\uC778");
		확인NewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		확인NewLabel_2.setFont(new Font("굴림", Font.BOLD, 14));
		확인NewLabel_2.setBounds(59, 150, 65, 51);
		contentPane.add(확인NewLabel_2);
		
		passwordField2 = new JPasswordField();
		passwordField2.setBounds(127, 155, 297, 47);
		contentPane.add(passwordField2);
		
		JLabel 이름NewLabel_3 = new JLabel(" \uC774\uB984");
		이름NewLabel_3.setHorizontalAlignment(SwingConstants.CENTER);
		이름NewLabel_3.setFont(new Font("굴림", Font.BOLD, 14));
		이름NewLabel_3.setBounds(59, 229, 52, 42);
		contentPane.add(이름NewLabel_3);
		
		NAMEtextField = new JTextField();
		NAMEtextField.setBounds(127, 230, 295, 42);
		contentPane.add(NAMEtextField);
		NAMEtextField.setColumns(10);
		
		JLabel 닉네임NewLabel_4 = new JLabel("\uB2C9\uB124\uC784");
		닉네임NewLabel_4.setHorizontalAlignment(SwingConstants.CENTER);
		닉네임NewLabel_4.setFont(new Font("굴림", Font.BOLD, 14));
		닉네임NewLabel_4.setBounds(59, 297, 52, 42);
		contentPane.add(닉네임NewLabel_4);
		
		NICKtextField = new JTextField();
		NICKtextField.setBounds(127, 298, 295, 42);
		contentPane.add(NICKtextField);
		NICKtextField.setColumns(10);
		
		JLabel 전화번호NewLabel_5 = new JLabel("\uC804\uD654 \uBC88\uD638");
		전화번호NewLabel_5.setHorizontalAlignment(SwingConstants.CENTER);
		전화번호NewLabel_5.setFont(new Font("굴림", Font.BOLD, 14));
		전화번호NewLabel_5.setBounds(33, 365, 92, 42);
		contentPane.add(전화번호NewLabel_5);
		
		PHONEtextField1 = new JTextField();
		PHONEtextField1.setBounds(222, 364, 92, 45);
		contentPane.add(PHONEtextField1);
		PHONEtextField1.setColumns(10);
		
		PHONEtextField2 = new JTextField();
		PHONEtextField2.setText("");
		PHONEtextField2.setBounds(326, 366, 96, 42);
		contentPane.add(PHONEtextField2);
		PHONEtextField2.setColumns(10);
		
		JButton OKBtn = new JButton("\uD655\uC778");
		OKBtn.setFont(new Font("굴림", Font.BOLD, 18));
		OKBtn.setBounds(127, 449, 142, 57);
		contentPane.add(OKBtn);
		
		JButton CCBtn = new JButton("\uCDE8\uC18C");
		CCBtn.setFont(new Font("굴림", Font.BOLD, 18));
		CCBtn.setBounds(281, 449, 145, 57);
		contentPane.add(CCBtn);
		
		PWQtextField = new JTextField();
		PWQtextField.setBounds(550, 258, 408, 51);
		contentPane.add(PWQtextField);
		PWQtextField.setColumns(10);
		
		PWAtextField = new JTextField();
		PWAtextField.setBounds(550, 365, 408, 42);
		contentPane.add(PWAtextField);
		PWAtextField.setColumns(10);
		
		JLabel 비번질문NewLabel_6 = new JLabel("\uBE44\uBC00\uBC88\uD638 \uBD84\uC2E4\uC2DC \uD655\uC778\uD560 \uC9C8\uBB38");
		비번질문NewLabel_6.setHorizontalAlignment(SwingConstants.CENTER);
		비번질문NewLabel_6.setFont(new Font("굴림", Font.BOLD, 14));
		비번질문NewLabel_6.setBounds(550, 199, 226, 45);
		contentPane.add(비번질문NewLabel_6);
		
		JLabel 비번답NewLabel_7 = new JLabel("\uBE44\uBC00\uBC88\uD638 \uC9C8\uBB38\uC5D0 \uB300\uD55C \uB2F5");
		비번답NewLabel_7.setHorizontalAlignment(SwingConstants.CENTER);
		비번답NewLabel_7.setFont(new Font("굴림", Font.BOLD, 14));
		비번답NewLabel_7.setBounds(550, 319, 226, 36);
		contentPane.add(비번답NewLabel_7);
		
		JButton IDBtn = new JButton("\uC911\uBCF5\uD655\uC778");
		IDBtn.setFont(new Font("굴림", Font.BOLD, 18));
		IDBtn.setBounds(426, 37, 124, 51);
		contentPane.add(IDBtn);
		
		PHONEtextField0 = new JTextField();
		PHONEtextField0.setBounds(127, 364, 83, 45);
		contentPane.add(PHONEtextField0);
		PHONEtextField0.setColumns(10);
		
		OKBtn.addActionListener(this);
		CCBtn.addActionListener(this);
		IDBtn.addActionListener(this);
	}
	//이벤트
	public void actionPerformed(ActionEvent ae) {
		Object obj = ae.getSource();
		if(obj instanceof JButton) {
			String btn = ae.getActionCommand();
			if(btn.equals("확인")) {
				String ID = IDtextField.getText();
				String PWD = passwordField1.getText();
				String PWDCK = passwordField2.getText();
				String nick = NICKtextField.getText();
				String pwq = PWQtextField.getText();
				String pwa = PWAtextField.getText();
				
				if(ID.equals("")) {
					JOptionPane.showMessageDialog(this, "아이디를 입력하셔야 합니다");
				} else if(PWD.equals("")) {
					JOptionPane.showMessageDialog(this, "비밀번호를 입력하셔야 합니다");
				} else if(PWD.length()<4 || PWD.length()>16) {    
					JOptionPane.showMessageDialog(this, "비밀번호는 4자리 이상, 16자리 이하만 가능 합니다.");
				} else if(PWDCK.equals("")) {
					JOptionPane.showMessageDialog(this, "비교할 비밀번호를 입력해 주시기 바랍니다");
				} else if(!PWD.equals(PWDCK)) {
					JOptionPane.showMessageDialog(this, "비밀번호가 일치하지 않습니다");
				}  else if(NAMEtextField.getText().equals("")) {
					JOptionPane.showMessageDialog(this, "이름을 입력하셔야 합니다");
				}  else if (PHONEtextField0.getText().length() < 2) {
					JOptionPane.showMessageDialog(this, "전화번호를 제대로 입력하세요");
				}  else if (PHONEtextField1.getText().length() < 3) {
					JOptionPane.showMessageDialog(this, "전화번호를 제대로 입력하세요");
					PHONEtextField1.requestFocus();
				} else if (PHONEtextField2.getText().length() < 4) {
					JOptionPane.showMessageDialog(this, "전화번호를 제대로 입력하세요");
					PHONEtextField2.requestFocus();
				} else if(nick.equals("")) {
					JOptionPane.showMessageDialog(this, "닉네임을 입력하셔야 합니다");
				}/*else if(!pwq.equals(pwa)) {
					JOptionPane.showMessageDialog(this, "비밀번호 힌트가 일치하지 않습니다.");
				}*/
				else if(pwq.equals("")) {
					JOptionPane.showMessageDialog(this, "질문칸을 채우세요");
				}else if(pwq.equals("")) {
					JOptionPane.showMessageDialog(this, "질문답칸을 채우세요");
				}
				else {
					String phone = PHONEtextField0.getText() + "-" + PHONEtextField1.getText() + "-" + PHONEtextField2.getText();
					ChatVO vo = new ChatVO(IDtextField.getText(),passwordField1.getText(),
							NAMEtextField.getText(),NICKtextField.getText(),
							phone, PWQtextField.getText(),PWAtextField.getText());
									
					int result = dao.SignUpInsert(vo);
					if(result>0) { // 회원등록 성공함
						JOptionPane.showMessageDialog(this, "회원가입에 성공하였습니다");
					} else { //회원등록 실패함
						JOptionPane.showMessageDialog(this, "회원가입에 실패하였습니다");
					}
					try {
						dispose();
						new Main().setVisible(true);
					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}else if(btn.equals("취소")) {
				try {
					dispose();
					new Main().setVisible(true);
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else if(btn.equals("중복확인")) {
				String idSearch = IDtextField.getText();
				System.out.println(idSearch.length());
				if(idSearch.equals("")) {
					JOptionPane.showMessageDialog(this, "아이디를 입력하셔야 합니다");
					//id 특수문자 포함 확인
				}else if(idSearch.length() < 3) {
					JOptionPane.showMessageDialog(this, "아이디를 4자 이상 입력하세요");
				} else {
					List<ChatVO> result = dao.getidCheck(idSearch);
					if(result.size()==0) {
						JOptionPane.showMessageDialog(this, "사용 가능한 아이디 입니다");
						OKBtn.setEnabled(true);
						IDtextField.setEnabled(false);
					} else {
						JOptionPane.showMessageDialog(this, "등록되어 있는 아이디 입니다");
					}
				}
			}
		}
	}
}