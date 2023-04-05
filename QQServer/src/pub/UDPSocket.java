package pub;

import java.io.IOException;
import java.net.*;

public class UDPSocket {
    private DatagramPacket datagramPacket = null;
    private DatagramSocket datagramSocket = null;

    public UDPSocket() {
        try {
            this.datagramSocket = new DatagramSocket();

            byte[] pool = new byte[1024];
            this.datagramPacket = new DatagramPacket(pool, 0, pool.length);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public UDPSocket(String ip, int port) {
        try {
            this.datagramSocket = new DatagramSocket();
            byte[] pool = new byte[1024];
            this.datagramPacket = new DatagramPacket(pool, 0, pool.length, InetAddress.getByName(ip), port);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }

    public UDPSocket(Socket socket) {
        System.out.println(socket.getInetAddress());
        System.out.println(socket.getPort());
        try {
            this.datagramSocket = new DatagramSocket();
            byte[] pool = new byte[1024];
            this.datagramPacket = new DatagramPacket(pool, 0, pool.length, socket.getInetAddress(),socket.getPort());
        } catch (SocketException e) {
            e.printStackTrace();
        }

    }

    public String recieve() {
        String info = null;
        try {
            this.datagramSocket.receive(this.datagramPacket);
            info = new String(this.datagramPacket.getData());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return info;
    }

    public void send(String info) {
        this.datagramPacket.setData(info.getBytes());
        try {
            this.datagramSocket.send(this.datagramPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getPort(){
        return  this.datagramSocket.getPort();
    }
}
