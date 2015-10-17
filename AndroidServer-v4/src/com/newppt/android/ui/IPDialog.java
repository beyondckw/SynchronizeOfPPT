package com.newppt.android.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.net.InetAddress;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class IPDialog extends JDialog implements ActionListener {

	JComboBox comboBox;
	JButton ok;
	JButton cancle;
	String ip;

	public IPDialog(JFrame owner, String name, boolean model) {
		super(owner, name, model);
		iniView();
	}

	private void iniView() {
		this.setTitle("选择所在局域网IP");
		this.setBounds(200, 200, 300, 100);
		JPanel contentPane = new JPanel();
		this.setContentPane(contentPane);
		
		this.setUndecorated(true);				//设置对话框未被装饰   设置窗口无边缘
		contentPane.setBackground(Color.lightGray);
		JLabel label = new JLabel("选择IP地址:");
		contentPane.add(label);			
		
		comboBox = new JComboBox();
		
		comboBox.setMaximumSize(new Dimension(300,30));		//固定好下拉菜单的宽度和高度
		comboBox.setMinimumSize(new Dimension(100,30));
		comboBox.setPreferredSize(new Dimension(200,30));
		
		ok = new MyButton("确定");
		ok.setEnabled(true);
		ok.addActionListener(this);
		cancle = new MyButton("取消");
		cancle.addActionListener(this);
		
		ok.setFocusable(false);						//去掉按钮的边框
        ok.setBorderPainted(false);
        cancle.setFocusable(false);
        cancle.setBorderPainted(false);
		
		ok.setLayout(null);
		cancle.setLayout(null);
		ok.setBounds(50, 70, 40, 20);
		cancle.setBounds(200, 70, 40, 20);
		
		String[] ips = getAllLocalHostIP();
		for (int i = 0; i < ips.length; i++) {
			comboBox.addItem(ips[i]);
		}

		ComboBoxEvent(); 
		contentPane.add(comboBox);    
		JPanel jp = new JPanel();
		jp.setLayout(new GridLayout(1, 4));
		jp.setBackground(Color.LIGHT_GRAY);   //设置jp 的背景颜色
		
		jp.add(new JLabel());
		
		jp.add(ok);
		jp.add(new JLabel());
		jp.add(cancle);
		contentPane.add(jp);
		centerWindow();
		
		this.setVisible(true);
	}

	public String getIp() {
		return ip;
	}

	private void ComboBoxEvent() {
		comboBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				ip = ((JComboBox) e.getSource()).getSelectedItem().toString();
				ok.setEnabled(true);
				System.out.println(ip);
			}
		});
	}

	private void centerWindow() {
		int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
		int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
		setLocation((screenWidth - getWidth()) / 2, (screenHeight - getHeight()) / 2);
	}

	private String getLocalHostName() {
		String hostName;
		try {
			InetAddress addr = InetAddress.getLocalHost();
			hostName = addr.getHostName();
		} catch (Exception ex) {
			hostName = "";
		}
		return hostName;
	}

	public String[] getAllLocalHostIP() {
		String[] ret = null;
		try {
			String hostName = getLocalHostName();
			if (hostName.length() > 0) {
				InetAddress[] addrs = InetAddress.getAllByName(hostName);
				if (addrs.length > 0) {
					ret = new String[addrs.length];
					System.out.println(addrs.length);
					for (int i = 0; i < addrs.length; i++) {
						ret[i] = addrs[i].getHostAddress();
						
					}
				}
			}

		} catch (Exception ex) {
			ret = null;
		}
		return ret;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		JButton button = (JButton) e.getSource();
		if (button.getText().toString().equals("确定")) {
			this.dispose();
		}

		else {
			ip = null;
			this.dispose();
		}
	}
}
	
/****
 * 创建椭圆形的按钮	
 * @author Administrator
 *
 */
	
class MyButton extends JButton { 
	private static final long serialVersionUID = 1965063150601339314L;
	 
    public MyButton(String text) {
        super(text);
        setOpaque(false);
        setContentAreaFilled(false); // 这一句非常重要, 否则父类还会绘制按钮的区域.
    }
 
    @Override
    protected void paintComponent(Graphics g) {
        int width = this.getWidth();
        int height = this.getHeight();
 
        Graphics2D g2d = (Graphics2D) g;
 
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
 
        g2d.setColor(Color.GRAY);
        g2d.fillRoundRect(0, 0, width, height, 20, 20);
 
        super.paintComponent(g); // 最后调用这个方法, 让父类绘制在按钮上绘制文字.
    }
}
