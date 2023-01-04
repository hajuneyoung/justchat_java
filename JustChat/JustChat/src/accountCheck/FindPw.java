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
	JButton OKbtn = new JButton("Ȯ��");
	JButton ccBtn = new JButton("���");

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
		lblNewLabel.setFont(new Font("����", Font.PLAIN, 30));
		lblNewLabel.setBounds(121, 10, 246, 60);
		contentPane.add(lblNewLabel);
		
		PWAtextField = new JTextField();
		PWAtextField.setBounds(64, 191, 358, 47);
		contentPane.add(PWAtextField);
		PWAtextField.setColumns(10);
		
		JButton OKbtn = new JButton("\uD655\uC778");
		OKbtn.setFont(new Font("����", Font.PLAIN, 33));
		OKbtn.setBounds(64, 259, 126, 60);
		contentPane.add(OKbtn);
		
		JButton ccBtn = new JButton("\uCDE8\uC18C");
		ccBtn.setFont(new Font("����", Font.PLAIN, 30));
		ccBtn.setBounds(270, 259, 137, 60);
		contentPane.add(ccBtn);
		
		JLabel lblNewLabel_1 = new JLabel("\uBE44\uBC00\uBC88\uD638\uC9C8\uBB38 \uB2F5");
		lblNewLabel_1.setFont(new Font("����", Font.PLAIN, 20));
		lblNewLabel_1.setBounds(149, 132, 168, 38);
		contentPane.add(lblNewLabel_1);
		
		OKbtn.addActionListener(this);
		ccBtn.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent ae) {
		Object obj = ae.getSource();
		if(obj instanceof JButton) {
			String btn = ae.getActionCommand();
			if(btn.equals("Ȯ��")) {
				String inputPWQ= PWAtextField.getText();
				
				if(inputPWQ.trim().isEmpty()||inputPWQ==null) {
					JOptionPane.showMessageDialog(null, "��ĭ�� Ȯ���ϼ���.");
				}
				else { // ��ĭ�� �Էµ��� �ʾ��� ��
					try {
						FindDAO findDao = new FindDAO();
						String FPwa = findDao.getPwa(inputPWQ);
						System.out.println(FPwa);
						if(FPwa==null) { // �̸�, ��ȭ��ȣ�� ��ġ�ϴ� ȸ���� ���� ���
							JOptionPane.showMessageDialog(null, "���� �� Ȯ���ϼ���");
						}else { // �� �� ��ġ�ϴ� ȸ���� �ִ� ��� --> ������ �̵�
							setVisible(false);
							JOptionPane.showMessageDialog(null,"����� ��й�ȣ��"+FPwa+"�Դϴ�");
							new Main().setVisible(true);
						}
					}catch(Exception e1) {
						e1.printStackTrace();
					}
				}
				
			}else if(btn.equals("���")) {
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
