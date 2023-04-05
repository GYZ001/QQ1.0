package listener;

import pub.TCPMessage;
import pub.TCPSocket;

public class TCPThread extends Thread {

    private TCPSocket TcpSocket = null;
    private TCPListener listener = null;


    public TCPThread() {
        // TODO Auto-generated constructor stub
    }

    public TCPThread(TCPSocket TcpSocket) {
        this.TcpSocket = TcpSocket;
    }

    public void addTCPListener(TCPListener listener) {
        this.listener = listener;
    }

    @Override
    public void run() {
        while (true) {
            TCPMessage tcpMessage = this.TcpSocket.receive();
            System.out.println("事件源发生了："+tcpMessage);
            this.listener.execute(tcpMessage);
        }
    }


}
