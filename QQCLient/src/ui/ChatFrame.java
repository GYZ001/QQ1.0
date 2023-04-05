package ui;

import pub.CommonUse;
import pub.TCPMessage;
import pub.TCPSocket;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map;

public class ChatFrame extends JFrame {
    private TCPSocket tcpSocket = null;
    private String name = null;
    private String myname = null;
    private String message = null;
    public Map chatmap = null;

    private JTextArea messageArea; // 消息显示区域
    private JTextField inputField; // 输入框
    private JButton sendButton; // 发送按钮

    private void initUI() {
        // 消息显示区域
        messageArea = new JTextArea();
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(messageArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // 输入框和发送按钮
        inputField = new JTextField();
        sendButton = new JButton("发送");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage(inputField);
            }
        });

        // 布局
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(inputField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    //发送消息
    private void sendMessage(JTextField messsage) {
        String info = messsage.getText().trim();
        if (!info.isEmpty()) {
            String message = this.myname + CommonUse.UDP_PACKET_SYMBOL +
                    info + CommonUse.UDP_PACKET_SYMBOL + this.name + CommonUse.UDP_PACKET_SYMBOL;
            TCPMessage tcpMessage = new TCPMessage();
            tcpMessage.setHead(CommonUse.MESSAGE);
            tcpMessage.setBody(CommonUse.MESSAGE, message);
            tcpSocket.send(tcpMessage);
            messageArea.append("我：" + info + "\n");
            inputField.setText("");
        }
    }

    //收到消息
    public void reMessage(String rmessage) {
        if (!rmessage.isEmpty()) {
            messageArea.append(this.name + "：" + rmessage + "\n");
        }
    }

    private void closeChat() {
        // 关闭聊天窗口
        dispose();
    }

    public ChatFrame(String name, String myname, Map<String, ChatFrame> chatmap, TCPSocket tcpSocket) {
        this.tcpSocket = tcpSocket;
        this.name = name;
        this.myname = myname;
        this.chatmap = chatmap;

        setTitle("Chat with " + name);
        setSize(480, 300);
        //setBounds(300, 150, 400, 300);
        setVisible(true);
        setLocationRelativeTo(null); // 窗口居中
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // 设置窗口关闭事件
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // 从map中删除键值对
                chatmap.remove(name);
                dispose();
            }
        });
        initUI();
    }

//    public ChatFrame(String name, String message,  Map<String, ChatFrame> chatmap) {
//        this.name = name;
//        this.message = message;
//        this.chatmap = chatmap;
//
//        setTitle("Chat with " + name);
//        setSize(480, 300);
//        setLocationRelativeTo(null); // 窗口居中
//        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//
//        // 设置窗口关闭事件
//        addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowClosing(WindowEvent e) {
//                // 从map中删除键值对
//                chatmap.remove(name);
//                dispose();
//            }
//        });
//        initUI();
//    }
}
