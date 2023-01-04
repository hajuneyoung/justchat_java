package accountCheck;

import java.awt.Color;
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

public class FindID extends JFrame implements ActionListener{

	private JPanel contentPane;
	private JTextField NAMEtextField;
	private JTextField PHONEtextField1;
	private JTextField PHONEtextField2;
	private JTextField PHONEtextField3;

	/**
	 * Launch the application.
	 */
	
	JButton OKBtn = new JButton("확인");
	JButton CCBtn = new JButton("취소");
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FindID frame = new FindID();
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
	public FindID() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 509, 389);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setBackground(new Color(180, 205, 230));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("\uC544\uC774\uB514 \uCC3E\uAE30");
		lblNewLabel.setFont(new Font("굴림", Font.PLAIN, 40));
		lblNewLabel.setBounds(97, 10, 242, 63);
		contentPane.add(lblNewLabel);
		
		NAMEtextField = new JTextField();
		NAMEtextField.setBounds(123, 86, 263, 42);
		contentPane.add(NAMEtextField);
		NAMEtextField.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("\uC774\uB984 \uC785\uB825");
		lblNewLabel_1.setBounds(29, 85, 82, 42);
		contentPane.add(lblNewLabel_1);
		
		PHONEtextField1 = new JTextField();
		PHONEtextField1.setBounds(123, 146, 76, 34);
		contentPane.add(PHONEtextField1);
		PHONEtextField1.setColumns(10);
		
		PHONEtextField2 = new JTextField();
		PHONEtextField2.setBounds(211, 146, 76, 34);
		contentPane.add(PHONEtextField2);
		PHONEtextField2.setColumns(10);
		
		PHONEtextField3 = new JTextField();
		PHONEtextField3.setBounds(310, 146, 76, 34);
		contentPane.add(PHONEtextField3);
		PHONEtextField3.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("-");
		lblNewLabel_2.setFont(new Font("굴림", Font.PLAIN, 20));
		lblNewLabel_2.setBounds(200, 153, 49, 27);
		contentPane.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("-");
		lblNewLabel_3.setFont(new Font("굴림", Font.PLAIN, 25));
		lblNewLabel_3.setBounds(290, 155, 49, 15);
		contentPane.add(lblNewLabel_3);
		
		JLabel lblNewLabel_4 = new JLabel("\uC804\uD654\uBC88\uD638 \uC785\uB825");
		lblNewLabel_4.setBounds(32, 145, 76, 34);
		contentPane.add(lblNewLabel_4);
		
		JButton OKBtn = new JButton("\uD655\uC778");
		OKBtn.setFont(new Font("굴림", Font.PLAIN, 30));
		OKBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		OKBtn.setBounds(54, 244, 128, 63);
		contentPane.add(OKBtn);
		
		JButton CCBtn = new JButton("\uCDE8\uC18C");
		CCBtn.setFont(new Font("굴림", Font.PLAIN, 30));
		CCBtn.setBounds(261, 244, 128, 63);
		contentPane.add(CCBtn);
		
		OKBtn.addActionListener(this);
		CCBtn.addActionListener(this);
		
	}
	
	//이벤트
		public void actionPerformed(ActionEvent ae) {
			Object obj = ae.getSource();
			if(obj instanceof JButton) {
				String btn = ae.getActionCommand();
				if(btn.equals("확인")) {
					String phone = PHONEtextField1.getText()+"-"+PHONEtextField2.getText()+"-"+PHONEtextField3.getText();
					String inputName = NAMEtextField.getText();
					String inputPhone = phone;
					
					if(inputName.trim().isEmpty()||inputName==null|| // 빈칸이 입력됐을 경우를 방지
							inputPhone.trim().isEmpty()||inputPhone==null) {
						JOptionPane.showMessageDialog(null, "빈칸을 확인하세요.");
					}
					else { // 빈칸이 입력되지 않았을 때
						try {
							FindDAO findDao = new FindDAO();
							String Fid = findDao.getId(inputName, inputPhone);
							
							if(Fid==null) { // 이름, 전화번호가 일치하는 회원이 없는 경우
								JOptionPane.showMessageDialog(null, "이름과 전화번호를 다시 확인하세요.");
							}else { // 둘 다 일치하는 회원이 있는 경우 --> 페이지 이동
								setVisible(false);
								JOptionPane.showMessageDialog(null,"당신의 아이디는"+Fid+"입니다");
								new FindPw().setVisible(true);
							}
						}catch(Exception e1) {
							e1.printStackTrace();
						}
					}
					
				}else if(btn.equals("취소")) {
					dispose();
					try {
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
