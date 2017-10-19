package finalassignment;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class Login {

	JFrame frame;
	JPanel main;
	JLabel label[];
	JPasswordField pwf,ensure;
	JTextField tf;
	JPanel pn[];
	JButton register,submit1,back;
	int currentstatus = 0;//不同的currentstatus下显示不同的界面
	
	private void clear(){
		tf.setText("");
		pwf.setText("");
	}
	
	private void changetop0(){
		
		clear();
		ensure.setText("");
		pn[0].add(label[0]);
		pn[0].add(tf);
		pn[0].add(label[1]);
		pn[0].add(pwf);
		pn[0].add(submit1);
		pn[0].add(register);
		main.add(pn[0]);
	}
	
	private void changetop1(){
		
		clear();
		ensure.setText("");
		pn[1].add(label[2]);
		pn[1].add(tf);
		pn[1].add(label[3]);
		pn[1].add(pwf);
		pn[1].add(submit1);
		pn[1].add(label[4]);
		pn[1].add(ensure);
		pn[1].add(back);
		main.add(pn[1]);
	}
	
	Login(){
		
		frame = new JFrame("欢迎使用旅游行程安排系统，请登陆");
		frame.setSize(500, 300);
		main = new JPanel();
		main.setLayout(null);
		main.setBounds(0,0, 500, 300);
		main.setBackground(new Color(230,239,182));
		
		submit1 = new JButton("确定");
		submit1.setBounds(240, 80, 100, 30);
		register = new JButton("注册");
		register.setBounds(240, 150, 100, 30);
		back = new JButton("返回");
		back.setBounds(240, 150, 100, 30);
		
		pn = new JPanel[2];
		label = new JLabel[5];
	
		pn[0] = new JPanel();
		pn[0].setLayout(null);
		pn[0].setBounds(40, 20, 400, 250);
		pn[0].setBackground(new Color(230,239,182));
		pn[1] = new JPanel();
		pn[1].setLayout(null);
		pn[1].setBounds(40, 20, 400, 250);
		pn[1].setBackground(new Color(230,239,182));
		
		pwf = new JPasswordField();
		pwf.setBounds(13, 120, 200, 30);
		ensure = new JPasswordField();
		ensure.setBounds(13, 185, 200, 30);
		tf = new JTextField();
		tf.setBounds(13, 50, 200, 30);
		
		label[0] = new JLabel("用户名");
		label[0].setBounds(13, 14,150, 30);
		label[1] = new JLabel("密码");
		label[1].setBounds(13, 80,150, 30);
		label[2] = new JLabel("请输入一个新的用户名");
		label[2].setBounds(13, 14,150, 30);
		label[3] = new JLabel("请输入您的密码");
		label[3].setBounds(13, 80,150, 30);
		label[4] = new JLabel("请您确认密码密码");
		label[4].setBounds(13, 150,150, 30);
		
		changetop0();
		
		frame.add(main);
		frame.setLayout(null);
		frame.setVisible(true);
		
		submit1.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				if(currentstatus==0){
					String users = tf.getText();
					char pw[] = pwf.getPassword();
					if(Manager.Login(users,pw)){
						frame.dispose();
						new Main(users,new String(pw));
					}
					else{
						JOptionPane.showMessageDialog(frame, "账号与密码不匹配！请检查"
								+ "","提示",JOptionPane.INFORMATION_MESSAGE);
					}
				}
				if(currentstatus==1){
					String users = tf.getText();
					char pw[] = pwf.getPassword();
					char pw2[] = ensure.getPassword();
					if(!(Arrays.equals(pw, pw2))){
						JOptionPane.showMessageDialog(frame, "两次密码不匹配！！"
								+ "","提示",JOptionPane.INFORMATION_MESSAGE);
					}
					else{
						if(Manager.Register(users, new String(pw))){
							JOptionPane.showMessageDialog(frame, "注册成功！请用新的账号密码"
									+ "登录","提示",JOptionPane.INFORMATION_MESSAGE);
						}
						else{
							JOptionPane.showMessageDialog(frame, "用户名已经存在！"
									+ "","提示",JOptionPane.INFORMATION_MESSAGE);
						}
					}
				}
			}	
		});
		
		register.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				currentstatus = 1;
				main.removeAll();
				changetop1();
				main.repaint();
			}	
		});
		
		back.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				currentstatus = 0;
				main.removeAll();
				changetop0();
				main.repaint();
			}	
		});
	}
}