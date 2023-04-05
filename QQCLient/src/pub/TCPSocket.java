package pub;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class TCPSocket {
    public Socket socket = null;
    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;

    public TCPSocket(String ip, int port) {
        try {
            this.socket = new Socket();
            SocketAddress socketAddress = new InetSocketAddress(CommonUse.SERVER_IP, CommonUse.SERVER_PORT);
            this.socket.connect(socketAddress);
            this.in = new ObjectInputStream(socket.getInputStream());
            this.out = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public TCPMessage submit(TCPMessage tcpMessage) {
        TCPMessage tcpMessage1 = new TCPMessage();
        try {
            this.out.writeObject(tcpMessage);
            this.out.flush();

            tcpMessage1 = (TCPMessage) in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return tcpMessage1;
    }

    public void send(TCPMessage tcpMessage) {
        try {
            this.out.writeObject(tcpMessage);
            this.out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getIp() {
        return this.socket.getInetAddress().toString();
    }

    public int getPort() {
        return this.socket.getPort();
    }

    public TCPMessage receive() {
        TCPMessage tcpMessage = null;
        try {
            tcpMessage = (TCPMessage) in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            return tcpMessage;
        }
    }
}