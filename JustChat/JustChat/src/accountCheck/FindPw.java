package accountCheck;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import Main.Main;
import connectDB.FindDAO;

public class FindPw extends JFrame implements ActionListener {

	private JPanel contentPane;
	private JTextField PWAtextField;
	JButton OKbtn = new JButton("확인");
	JButton ccBtn = new JButton("취소");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FindPw frame = new FindPw();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public FindPw() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 513, 408);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("\uBE44\uBC00\uBC88\uD638 \uCC3E\uAE30");
		lblNewLabel.setFont(new Font("굴림", Font.PLAIN, 30));
		lblNewLabel.setBounds(121, 10, 246, 60);
		contentPane.add(lblNewLabel);
		
		PWAtextField = new JTextField();
		PWAtextField.setBounds(64, 191, 358, 47);
		contentPane.add(PWAtextField);
		PWAtextField.setColumns(10);
		
		JButton OKbtn = new JButton("\uD655\uC778");
		OKbtn.setFont(new Font("굴림", Font.PLAIN, 33));
		OKbtn.setBounds(64, 259, 126, 60);
		contentPane.add(OKbtn);
		
		JButton ccBtn = new JButton("\uCDE8\uC18C");
		ccBtn.setFont(new Font("굴림", Font.PLAIN, 30));
		ccBtn.setBounds(270, 259, 137, 60);
		contentPane.add(ccBtn);
		
		JLabel lblNewLabel_1 = new JLabel("\uBE44\uBC00\uBC88\uD638\uC9C8\uBB38 \uB2F5");
		lblNewLabel_1.setFont(new Font("굴림", Font.PLAIN, 20));
		lblNewLabel_1.setBounds(149, 132, 168, 38);
		contentPane.add(lblNewLabel_1);
		
		OKbtn.addActionListener(this);
		ccBtn.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent ae) {
		Object obj = ae.getSource();
		if(obj instanceof JButton) {
			String btn = ae.getActionCommand();
			if(btn.equals("확인")) {
				String inputPWQ= PWAtextField.getText();
				
				if(inputPWQ.trim().isEmpty()||inputPWQ==null) {
					JOptionPane.showMessageDialog(null, "빈칸을 확인하세요.");
				}
				else { // 빈칸이 입력되지 않았을 때
					try {
						FindDAO findDao = new FindDAO();
						String FPwa = findDao.getPwa(inputPWQ);
						System.out.println(FPwa);
						if(FPwa==null) { // 이름, 전화번호가 일치하는 회원이 없는 경우
							JOptionPane.showMessageDialog(null, "질문 답 확인하세요");
						}else { // 둘 다 일치하는 회원이 있는 경우 --> 페이지 이동
							setVisible(false);
							JOptionPane.showMessageDialog(null,"당신의 비밀번호는"+FPwa+"입니다");
							new Main().setVisible(true);
						}
					}catch(Exception e1) {
						e1.printStackTrace();
					}
				}
				
			}else if(btn.equals("취소")) {
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
		}
	}
}
