package ui;

import component.ImgPanel;
import pub.TCPSocket;
import bean.Qquser;
import pub.CommonUse;
import pub.TCPMessage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class RegisterFrame3 extends JFrame implements ActionListener {
    private TCPSocket tcpSocket = null;

    private JPanel bodyPanel = null;
    private JPanel centerPanel = null;
    private JPanel buttonPanel = null;

    private JLabel accountLabel = null;
    private JLabel nameLabel = null;
    private JLabel passwordLabel = null;
    private JLabel repasswordLabel = null;
    private JLabel imgLabel = null;

    private JTextField accountTextField = null;
    private JTextField nameTextField = null;
    private JPasswordField passwordField = null;
    private JPasswordField repasswordField = null;
    private JComboBox imgComboBox = null;

    private JButton submitButton = null;
    private JButton cancelButton = null;

    public void createHead() {
        File file = new File("./selectimg");
        File[] files = file.listFiles();
        for (File file1 : files) {
            String name = file1.getName();
            this.imgComboBox.addItem(new ImageIcon("./selectimg/" + name));
        }
    }

    public RegisterFrame3() {
        this.tcpSocket = new TCPSocket(CommonUse.SERVER_IP, CommonUse.SERVER_PORT);
        this.init();
    }

    public void initCenter() {
        this.centerPanel = new ImgPanel("./register.jpg");

        this.accountLabel = new JLabel("账  号：", JLabel.RIGHT);
        this.accountLabel.setPreferredSize(new Dimension(80, 24));
        this.nameLabel = new JLabel("用户名：", JLabel.RIGHT);
        this.nameLabel.setPreferredSize(new Dimension(80, 24));
        this.passwordLabel = new JLabel("口  令：", JLabel.RIGHT);
        this.passwordLabel.setPreferredSize(new Dimension(80, 24));
        this.repasswordLabel = new JLabel("确认口令：", JLabel.RIGHT);
        this.repasswordLabel.setPreferredSize(new Dimension(80, 24));
        this.imgLabel = new JLabel("选择头像：", JLabel.RIGHT);
        this.imgLabel.setPreferredSize(new Dimension(80, 24));

        this.accountTextField = new JTextField();
        this.accountTextField.setPreferredSize(new Dimension(160, 24));
        this.nameTextField = new JTextField();
        this.nameTextField.setPreferredSize(new Dimension(160, 24));
        this.passwordField = new JPasswordField();
        this.passwordField.setPreferredSize(new Dimension(160, 24));
        this.repasswordField = new JPasswordField();
        this.repasswordField.setPreferredSize(new Dimension(160, 24));
        this.imgComboBox = new JComboBox();
        this.imgComboBox.setPreferredSize(new Dimension(160, 24));
        this.createHead();

        Box box0 = Box.createVerticalBox();

        Box box1 = Box.createHorizontalBox();
        Box box2 = Box.createHorizontalBox();
        Box box3 = Box.createHorizontalBox();
        Box box4 = Box.createHorizontalBox();
        Box box5 = Box.createHorizontalBox();

        box1.add(this.accountLabel);
        box1.add(this.accountTextField);

        box2.add(this.nameLabel);
        box2.add(this.nameTextField);

        box3.add(this.passwordLabel);
        box3.add(this.passwordField);

        box4.add(this.repasswordLabel);
        box4.add(this.repasswordField);

        box5.add(this.imgLabel);
        box5.add(this.imgComboBox);

        box0.add(Box.createVerticalStrut(90));
        box0.add(box1);
        box0.add(Box.createVerticalStrut(5));
        box0.add(box2);
        box0.add(Box.createVerticalStrut(5));
        box0.add(box3);
        box0.add(Box.createVerticalStrut(5));
        box0.add(box4);
        box0.add(Box.createVerticalStrut(5));
        box0.add(box5);

        this.centerPanel.add(box0);

    }

    public void initButton() {
        this.buttonPanel = new JPanel();
        this.submitButton = new JButton("注册");
        this.submitButton.addActionListener(this);
        this.cancelButton = new JButton("重置");
        this.cancelButton.addActionListener(this);
        this.buttonPanel.add(this.submitButton);
        this.buttonPanel.add(this.cancelButton);
    }

    public void init() {
        Container content = this.getContentPane();
        content.setLayout(new BorderLayout());
        this.bodyPanel = new JPanel(new BorderLayout());
        content.add(this.bodyPanel, BorderLayout.CENTER);

        this.initCenter();
        this.bodyPanel.add(this.centerPanel, BorderLayout.CENTER);

        this.initButton();
        this.bodyPanel.add(this.buttonPanel, BorderLayout.SOUTH);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("金智qq登录");
    }

    public static void main(String[] args) {
        RegisterFrame3 registerFrame1 = new RegisterFrame3();
        registerFrame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        registerFrame1.setBounds(200, 200, 512, 340);
        registerFrame1.setVisible(true);
    }

    public TCPMessage submit(){
        String account = this.accountTextField.getText();
        String name = this.nameTextField.getText();
        String password = new String(this.passwordField.getPassword());
        String head = this.imgComboBox.getSelectedItem().toString();
        int start = head.lastIndexOf('/');
        int end = head.lastIndexOf('.');
        String img = head.substring((start + 1), end);
        Qquser qquser = new Qquser();
        qquser.setAccount(account);
        qquser.setName(name);
        qquser.setPassword(password);
        qquser.setPic(img);

        TCPMessage tcpMessage = new TCPMessage();
        tcpMessage.setHead(CommonUse.REGISTER);
        tcpMessage.setMap(CommonUse.QQ_USER, qquser);

        return tcpMessage;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.submitButton) {
            TCPMessage tcpMessage1 = tcpSocket.submit(submit());

            if (CommonUse.SUCCESSFUL.equals(tcpMessage1.getHead())) {
                JOptionPane.showMessageDialog(this, "注册成功");
            } else {
                JOptionPane.showMessageDialog(this, "注册失败");
            }
        } else {

        }
    }
}
