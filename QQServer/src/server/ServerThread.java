package server;

import bean.Qquser;
import dao.QqUserDao;
import dao.QqUserDaoImpl;
import pub.CommonUse;
import pub.TCPMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Map;

public class ServerThread extends Thread {
    public Socket socket = null;
    public ObjectOutputStream out = null;
    public ObjectInputStream in = null;

    public Qquser fullUser = null;
    public Qquser enduser = null;
    public Map ThreadMap = null;
    private TCPMessage sMessage = null;
//=============================================================================================================

    private List<Qquser> getFriends(String account) {
        List<Qquser> list = null;
        String sql = "select * from qquser where"
                + " account in (select friendAccount from friend where userAccount = '" + account + "')";
        list = new QqUserDaoImpl().findBySql(sql);
        return list;
    }

    private Qquser getFullUser(String account) {
        return new QqUserDaoImpl().findById(account);
    }

    //===============================================================================================================
    private void updateDB(Qquser qquser) {
        QqUserDao dao = new QqUserDaoImpl();
        dao.update(qquser);
    }
//=============================================================================================================

    private boolean checkUser(Qquser qquser) {
        String account = qquser.getAccount();
        String password = qquser.getPassword();

        QqUserDao dao = new QqUserDaoImpl();
        Qquser tempUser = dao.findById(account);
        if (tempUser != null && tempUser.getPassword().equals(password)) {

            return true;
        }
        return false;
    }

    public boolean register(Qquser qquser) {
        boolean flag = false;

        qquser.setState("0");
        qquser.setIp("0");
        qquser.setPort(0);

        QqUserDao qqUserDao = new QqUserDaoImpl();
        if (qqUserDao.save(qquser) > 0) {
            flag = true;
        }
        return flag;
    }

//=======================================================================================================================

    private void online(Qquser qquser) {
        List<Qquser> list = null;
        String sql = "select * from qquser where"
                + " account in (select friendAccount from friend where userAccount " +
                "= '" + qquser.getAccount() + "') and state = '1'";
        sMessage.setHead(CommonUse.ONLINE);
        sMessage.setBody(CommonUse.QQ_USER, qquser);

        list = new QqUserDaoImpl().findBySql(sql);
        try {
            for (Qquser temp : list) {
                ServerThread serverThread = (ServerThread) ThreadMap.get(temp.getName());
                serverThread.out.writeObject(sMessage);
                System.out.println(sMessage);
                serverThread.out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void offline(Qquser qquser) {
        qquser.setIp("0");
        qquser.setPort(0);
        qquser.setState("0");
        List<Qquser> list = null;
        String sql = "select * from qquser where"
                + " account in (select friendAccount from friend where userAccount " +
                "= '" + qquser.getAccount() + "') and state = '1'";
        sMessage.setHead(CommonUse.OFFLINE);
        sMessage.setBody(CommonUse.QQ_USER, qquser);

        list = new QqUserDaoImpl().findBySql(sql);
        try {
            for (Qquser temp : list) {
                ServerThread serverThread = (ServerThread) ThreadMap.get(temp.getName());
                serverThread.out.writeObject(sMessage);
                serverThread.out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendmessage(String name, TCPMessage tcpMessage) {
        List<Qquser> list = null;
        String sql = "select * from qquser where"
                + " account in (select friendAccount from friend where userAccount " +
                "= '" + fullUser.getAccount() + "') and state = '1'";
        list = new QqUserDaoImpl().findBySql(sql);
        try {
            for (Qquser temp : list) {
                if (name.equals(temp.getName())){
                    ServerThread serverThread = (ServerThread) ThreadMap.get(name);
                    serverThread.out.writeObject(tcpMessage);
                    serverThread.out.flush();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void shutSocket() {
        this.offline(enduser);
        this.updateDB(enduser);
        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ServerThread(Socket socket, Map ThreadMap) throws IOException {
        this.ThreadMap = ThreadMap;
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
    }

    @Override
    public void run() {
        try {
            while (true) {
                TCPMessage tcpMessage = (TCPMessage) in.readObject();
                System.out.println(tcpMessage);
                if (CommonUse.REGISTER.equals(tcpMessage.getHead())) {
                    this.sMessage = new TCPMessage();
                    Qquser qquser = (Qquser) tcpMessage.getBody(CommonUse.QQ_USER);
                    if (this.register(qquser)) {
                        sMessage.setHead(CommonUse.SUCCESSFUL);
                    } else {
                        sMessage.setHead(CommonUse.FAILURE);
                    }
                    this.out.writeObject(sMessage);
                    this.out.flush();
                } else if (CommonUse.LOGIN.equals(tcpMessage.getHead())) {
                    this.sMessage = new TCPMessage();
                    Qquser qquser = (Qquser) tcpMessage.getBody(CommonUse.QQ_USER);
                    this.enduser = qquser;
                    if (this.checkUser(qquser)) {
                        //改库
                        qquser.setState("1");
                        this.updateDB(qquser);
                        //得到完整用户
                        this.fullUser = (Qquser) this.getFullUser(qquser.getAccount());
                        sMessage.setBody(CommonUse.QQ_USER, this.fullUser);
                        this.ThreadMap.put(fullUser.getName(), this);
                        //通知上线
                        this.online(this.fullUser);
                        System.out.println("通知上线成功");
                        sMessage.setHead(CommonUse.SUCCESSFUL);
                    } else {
                        sMessage.setHead(CommonUse.FAILURE);
                    }
                    this.out.writeObject(sMessage);
                    this.out.flush();
                } else if (CommonUse.FIND_FRIEND.equals(tcpMessage.getHead())) {
                    this.sMessage = new TCPMessage();
                    sMessage.setHead(CommonUse.FIND_FRIEND);
                    Qquser tempUser = (Qquser) tcpMessage.getBody(CommonUse.QQ_USER);
                    sMessage.setBody(CommonUse.FRIENDS_INFO, this.getFriends(tempUser.getAccount()));
                    this.out.writeObject(sMessage);
                    this.out.flush();
                }else if (CommonUse.MESSAGE.equals(tcpMessage.getHead())){
                    String message = (String)tcpMessage.getBody(CommonUse.MESSAGE);
                    String[] messages = message.split(CommonUse.UDP_PACKET_SYMBOL);
                    String name = messages[2];
                    sendmessage(name,tcpMessage);
                }
            }
        } catch (IOException e) {
            System.out.println(fullUser.getName() + "已下线");
            this.shutSocket();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



}
