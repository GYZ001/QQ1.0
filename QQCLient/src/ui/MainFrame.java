package ui;

import component.ClinetImgCell;
import listener.TCPListener;
import listener.TCPThread;
import pub.TCPMessage;
import pub.TCPSocket;
import bean.Qquser;
import pub.CommonUse;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainFrame extends JFrame implements TCPListener {
    private TCPSocket tcpSocket = null;

    private Qquser FullUser = null;

    private JPanel bodyPanel = null;
    private List<Qquser> friends = null;

    private JLabel nameLabel = null;//看得见
    private JList<Qquser> friendList = null;
    private DefaultListModel<Qquser> listModel = null;


    private Map<String, ChatFrame> chatmap = new HashMap<>();

    private List<Qquser> getFriends(Qquser qquser) {
        List<Qquser> friends = null;

        TCPMessage sMessage = new TCPMessage();//发送报文
        sMessage.setHead(CommonUse.FIND_FRIEND);
        sMessage.setBody(CommonUse.QQ_USER, qquser);
        TCPMessage rMessage = this.tcpSocket.submit(sMessage);//接收报文
        friends = (List<Qquser>) rMessage.getBody(CommonUse.FRIENDS_INFO);//报文体就是链表形式的对象

        return friends;

    }

    public void refreshlist() {
        List<Qquser> before = this.friends;//乱序
        List<Qquser> after = new ArrayList();//排序

        for (Qquser temp : before) {
            if ("1".equals(temp.getState())) {
                after.add(temp); //在线的先加入到after这个链表中
            }
        }
        for (Qquser temp : before) {
            if ("0".equals(temp.getState())) {
                after.add(temp);
            }
        }

        for (Qquser qquser : after) {
            if ("1".equals(qquser.getState())) {
                qquser.setPlace1("./onimg/" + qquser.getPic() + ".png");
            }
            if ("0".equals(qquser.getState())) {
                qquser.setPlace1("./outimg/" + qquser.getPic() + ".png");
            }
        }

        this.friends = after;//看不见的好友集合

        this.listModel = new DefaultListModel<Qquser>();//存储和管理元素集合的Swing组件
        for (Qquser qquser : friends) {
            this.listModel.addElement(qquser);
        }
        this.friendList.setModel(this.listModel);
    }

    private void createFriendList() {
        List<Qquser> before = this.getFriends(this.FullUser);//乱序
        List<Qquser> after = new ArrayList();//排序

        for (Qquser temp : before) {
            if ("1".equals(temp.getState())) {
                after.add(temp); //在线的先加入到after这个链表中
            }
        }
        for (Qquser temp : before) {
            if ("0".equals(temp.getState())) {
                after.add(temp);
            }
        }

        for (Qquser qquser : after) {
            if ("1".equals(qquser.getState())) {
                qquser.setPlace1("./onimg/" + qquser.getPic() + ".png");
            }
            if ("0".equals(qquser.getState())) {
                qquser.setPlace1("./outimg/" + qquser.getPic() + ".png");
            }
        }

        this.friends = after;//看不见的好友集合

        this.friendList = new JList<Qquser>();//看得见的显示集合的Swing组件
        this.friendList.setCellRenderer(new ClinetImgCell());
        this.listModel = new DefaultListModel<Qquser>();//存储和管理元素集合的Swing组件
        for (Qquser qquser : friends) {
            this.listModel.addElement(qquser);
        }
        this.friendList.setModel(this.listModel);
        //鼠标监听事件
        this.friendList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == 1 && e.getClickCount() == 2) {
                    Qquser qquser = friendList.getSelectedValue();
                    // 打开聊天界面
                    if (chatmap.get(qquser.getName()) == null) {
                        ChatFrame chatFrame = new ChatFrame(qquser.getName(), FullUser.getName(), chatmap, tcpSocket);
                        chatmap.put(qquser.getName(), chatFrame);
                    }
                }
            }
        });
    }


    private void init() {
        this.bodyPanel = (JPanel) this.getContentPane();
        this.bodyPanel.setLayout(new BorderLayout());

        this.nameLabel = new JLabel(this.FullUser.getName());
        this.bodyPanel.add(this.nameLabel, BorderLayout.NORTH);

        this.createFriendList();//生成好友列表
        this.bodyPanel.add(this.friendList, BorderLayout.CENTER);

        TCPThread udpThread = new TCPThread(this.tcpSocket);
        udpThread.addTCPListener(this);
        udpThread.start();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle(this.FullUser.getAccount() + "的主界面");
    }

    public MainFrame(TCPSocket tcpSocket, Qquser fullUser) {
        this.tcpSocket = tcpSocket;
        FullUser = fullUser;
        this.init();
    }


    @Override
    public void execute(TCPMessage tcpMessage) {
        String head = tcpMessage.getHead();
        System.out.println("报文头：" + head);
        if (CommonUse.ONLINE.equals(head)) {
            Qquser qquser = (Qquser) tcpMessage.getBody(CommonUse.QQ_USER);
            System.out.println(qquser);
            String account = qquser.getAccount();
            for (Qquser temp : this.friends) {
                if (account.equals(temp.getAccount())) {
                    temp.setState("1");
                    break;
                }
            }
            this.refreshlist();

        } else if (CommonUse.OFFLINE.equals(head)) {
            Qquser qquser = (Qquser) tcpMessage.getBody(CommonUse.QQ_USER);
            String account = qquser.getAccount();

            for (Qquser temp : this.friends) {
                if (account.equals(temp.getAccount())) {
                    temp.setState("0");
                    break;
                }
            }
            this.refreshlist();
        } else if (CommonUse.MESSAGE.equals(head)) {
            TCPMessage sMessage = new TCPMessage();
            String message = (String) tcpMessage.getBody(CommonUse.MESSAGE);
            String[] messages = message.split(CommonUse.UDP_PACKET_SYMBOL);
            ChatFrame chatFrame = null;
            if ((chatFrame = chatmap.get(messages[0])) != null) {
                chatFrame.reMessage(messages[1]);
            } else {
                chatFrame = new ChatFrame(messages[0], this.FullUser.getName(), chatmap, this.tcpSocket);
                chatmap.put(messages[0], chatFrame);
                chatFrame.reMessage(messages[1]);
            }
        }
    }


}
