package server;

import pub.CommonUse;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {

	public static void main(String[] args) {
		Socket socket = null;
		ServerSocket serverSocket = null;
		System.out.println("服务器启动");
		Map<String, ServerThread> ThreadMap = new HashMap<>();

		try {
			serverSocket = new ServerSocket(CommonUse.SERVER_PORT);
			while(true) {
				socket = serverSocket.accept();
				ServerThread thread5 = new ServerThread(socket,ThreadMap);
				thread5.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
		System.out.println("服务器关闭");
	}
}
