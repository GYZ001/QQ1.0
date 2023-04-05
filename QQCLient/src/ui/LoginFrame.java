package ui;

import component.ImgPanel;
import pub.CommonUse;
import pub.TCPMessage;
import pub.TCPSocket;
import bean.Qquser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class LoginFrame extends JFrame implements ActionListener {

    private TCPSocket tcpSocket = null;

    private JPanel bodyPanel = null;
    private JPanel centerPanel = null;
    private JPanel bottomPanel = null;

    private JLabel accountLabel = null;
    private JTextField accountTextField = null;
    private JLabel passowrdLabel = null;
    private JPasswordField passwordField = null;

    private JButton loginButton = null;
    private JButton registerButton = null;

    public void initcenter() {
        this.centerPanel = new ImgPanel("./logon.jpg");

        this.accountLabel = new JLabel("用 户 名：", JLabel.RIGHT);
        this.accountLabel.setPreferredSize(new Dimension(60, 24));
        this.passowrdLabel = new JLabel("口    令：", JLabel.RIGHT);
        this.passowrdLabel.setPreferredSize(new Dimension(60, 24));
        this.accountTextField = new JTextField(16);
        this.passwordField = new JPasswordField(16);

        Box box0 = Box.createVerticalBox();
        Box box1 = Box.createHorizontalBox();
        Box box2 = Box.createHorizontalBox();

        box1.add(this.accountLabel);
        box1.add(this.accountTextField);
        box2.add(this.passowrdLabel);
        box2.add(this.passwordField);

        box0.add(Box.createVerticalStrut(90));
        box0.add(box1);
        box0.add(Box.createVerticalStrut(10));
        box0.add(box2);
        this.centerPanel.add(box0);
    }

    public void initbottom() {
        this.bottomPanel = new JPanel();
        this.loginButton = new JButton("登陆");
        this.loginButton.addActionListener(this);
        this.bottomPanel.add(this.loginButton);
        this.registerButton = new JButton("注册");
        this.registerButton.addActionListener(this);
        this.bottomPanel.add(this.registerButton);
    }

    public void init() {
        this.bodyPanel = (JPanel) this.getContentPane();
        this.bodyPanel.setLayout(new BorderLayout());

        this.initcenter();
        this.bodyPanel.add(this.centerPanel, BorderLayout.CENTER);

        this.initbottom();
        this.bodyPanel.add(this.bottomPanel, BorderLayout.SOUTH);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("金智QQ登陆");
    }

    public LoginFrame() {
        this.tcpSocket = new TCPSocket(CommonUse.SERVER_IP, CommonUse.SERVER_PORT);
        this.init();
    }

    public static void main(String[] args) {
        LoginFrame loginFrame = new LoginFrame();
        loginFrame.setBounds(100, 100, 414, 307);
        loginFrame.setVisible(true);

    }

    public void setconfig(){
        String ip = null;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new URL("http://checkip.amazonaws.com/").openStream()));
            ip = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.loginButton) {
            String account = this.accountTextField.getText();
            String password = new String(this.passwordField.getPassword());

            Qquser qquser = new Qquser();//客户端登录的qquser
            qquser.setAccount(account);
            qquser.setPassword(password);
            qquser.setIp(this.tcpSocket.getIp());
            qquser.setPort(this.tcpSocket.getPort());

            TCPMessage sMessage = new TCPMessage();//发送报文
            sMessage.setHead(CommonUse.LOGIN);
            sMessage.setBody(CommonUse.QQ_USER, qquser);
            TCPMessage rMessage = this.tcpSocket.submit(sMessage);//接收报文

            String head = rMessage.getHead();
            if (CommonUse.SUCCESSFUL.equals(head)) {
                JOptionPane.showMessageDialog(this, "登录成功");
                Qquser fullUser = (Qquser) rMessage.getBody(CommonUse.QQ_USER);
                MainFrame mainFrame1 = new MainFrame(this.tcpSocket, fullUser);
                mainFrame1.setBounds(100, 20, 320, 600);
                mainFrame1.setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "登录失败");
            }
        }
    }

}
