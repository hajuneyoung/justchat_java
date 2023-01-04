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
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Main.Main;

public class AskIdPw extends JFrame implements ActionListener{

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	
	JButton FindIDBtn = new JButton("아이디 찾으러 가기");
	JButton FindIDPWBtn = new JButton("아이디/비밀번호 찾으러 가기");
	JButton CCBtn = new JButton("취소");
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AskIdPw frame = new AskIdPw();
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
	public AskIdPw() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 502, 375);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setBackground(new Color(180, 205, 230));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton FindIDBtn = new JButton("\uC544\uC774\uB514 \uCC3E\uC73C\uB7EC \uAC00\uAE30");
		FindIDBtn.setBounds(22, 80, 155, 78);
		contentPane.add(FindIDBtn);
		
		JButton FindIDPWBtn = new JButton("\uC544\uC774\uB514/\uBE44\uBC00\uBC88\uD638 \uCC3E\uC73C\uB7EC \uAC00\uAE30");
		FindIDPWBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				new FindID().setVisible(true);
			}
		});
		FindIDPWBtn.setBounds(262, 80, 197, 78);
		contentPane.add(FindIDPWBtn);
		
		JLabel lblNewLabel = new JLabel("\uACC4\uC815 \uCC3E\uAE30");
		lblNewLabel.setFont(new Font("굴림", Font.PLAIN, 30));
		lblNewLabel.setBounds(148, 13, 230, 57);
		contentPane.add(lblNewLabel);
		
		JButton CCBtn = new JButton("\uCDE8\uC18C");
		CCBtn.setFont(new Font("굴림", Font.PLAIN, 30));
		CCBtn.setBounds(148, 233, 185, 93);
		contentPane.add(CCBtn);
		
		FindIDBtn.addActionListener(this);
		FindIDPWBtn.addActionListener(this);
		CCBtn.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent ae) {
		Object obj = ae.getSource();
		if(obj instanceof JButton) {
			String btn = ae.getActionCommand();
			if(btn.equals("아이디 찾으러 가기")) {
				dispose();
				new OnlyFindID().setVisible(true);
			
				
				
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
			}else if(btn.equals("아이디/비밀번호 찾으러 가기")) {
				dispose();
				new FindID().setVisible(true);
			}
		}
	}
}
